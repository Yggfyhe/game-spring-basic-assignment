package com.gamebasic.ranking.service;

import com.gamebasic.ranking.CardType;
import com.gamebasic.ranking.client.RankingClient;
import com.gamebasic.ranking.dto.RankingEntryResponse;
import com.gamebasic.ranking.dto.RankingResponse;
import com.gamebasic.ranking.dto.source.RankingSource;
import com.gamebasic.ranking.dto.source.RankingSourceBossFight;
import com.gamebasic.ranking.dto.source.RankingSourceDeck;
import com.gamebasic.ranking.dto.source.RankingSourceDeckCard;
import com.gamebasic.ranking.dto.source.RankingSourcePhase;
import com.gamebasic.ranking.dto.source.RankingSourceRecord;
import com.gamebasic.ranking.dto.source.RankingSourceRun;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankingService {

    private static final List<String> BOSS_PHASE_ORDER = List.of("THRONE", "UNBOUND", "ECLIPSE");
    private static final Set<String> CARD_TYPES = Arrays.stream(CardType.values())
        .map(Enum::name)
        .collect(Collectors.toUnmodifiableSet());

    private final RankingClient rankingClient;

    public RankingResponse getRankings() {
        RankingSource source = rankingClient.fetch();
        List<RankingSourceRecord> records = source.getRecords();

        List<RankingSourceRecord> validRecords = new ArrayList<>();
        int excludedCount = 0;
        for (RankingSourceRecord record : records) {
            if (!isRankingTarget(record)) {
                continue;
            }
            if (isValidRecord(record)) {
                validRecords.add(record);
            } else {
                excludedCount++;
            }
        }

        validRecords.sort(
            Comparator.comparingInt((RankingSourceRecord r) -> r.getRun().getDurationSeconds())
                .thenComparing(Comparator.comparingInt((RankingSourceRecord r) -> r.getRun().getFinalHp()).reversed())
                .thenComparingLong(RankingSourceRecord::getId)
        );

        List<RankingSourceRecord> bestPerPlayer = keepBestPerPlayer(validRecords);

        List<RankingEntryResponse> entries = new ArrayList<>();
        int rank = 1;
        for (RankingSourceRecord record : bestPerPlayer) {
            RankingSourceRun run = record.getRun();
            entries.add(new RankingEntryResponse(
                rank++,
                record.getPlayer().getName(),
                run.getDurationSeconds(),
                run.getFinalHp(),
                record.getBossFight().getTotalTurns(),
                record.getDeck().getCards().size()
            ));
        }

        return new RankingResponse(
            source.getMeta().getSeason().getId(),
            records.size(),
            excludedCount,
            entries
        );
    }

    private List<RankingSourceRecord> keepBestPerPlayer(List<RankingSourceRecord> sortedRecords) {
        Map<String, RankingSourceRecord> bestByPlayer = new LinkedHashMap<>();
        for (RankingSourceRecord record : sortedRecords) {
            bestByPlayer.putIfAbsent(record.getPlayer().getId(), record);
        }
        return new ArrayList<>(bestByPlayer.values());
    }

    private boolean isRankingTarget(RankingSourceRecord record) {
        RankingSourceRun run = record.getRun();
        return run != null && "CLEARED".equals(run.getStatus()) && run.getClearedFloor() == 10;
    }

    private boolean isValidRecord(RankingSourceRecord record) {
        return isValidRun(record.getRun())
            && isValidDeck(record.getDeck())
            && isValidBossFight(record.getBossFight())
            && isFinishingCardInDeck(record.getBossFight(), record.getDeck());
    }

    private boolean isValidRun(RankingSourceRun run) {
        if (run.getDurationSeconds() < run.getClearedFloor() * 30) {
            return false;
        }
        return run.getFinalHp() >= 1 && run.getFinalHp() <= 99;
    }

    private boolean isValidDeck(RankingSourceDeck deck) {
        if (deck == null || deck.getCards() == null) {
            return false;
        }
        List<RankingSourceDeckCard> cards = deck.getCards();
        if (cards.size() < 9 || cards.size() > 20) {
            return false;
        }
        if (deck.getSize() != cards.size()) {
            return false;
        }
        for (RankingSourceDeckCard card : cards) {
            if (!CARD_TYPES.contains(card.getCardType())) {
                return false;
            }
            if (card.getAcquiredFloor() < 0 || card.getAcquiredFloor() > 9) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidBossFight(RankingSourceBossFight bossFight) {
        if (bossFight == null || bossFight.getPhases() == null || bossFight.getPhases().size() != 3) {
            return false;
        }
        List<RankingSourcePhase> phases = bossFight.getPhases();
        int turnSum = 0;
        for (int i = 0; i < 3; i++) {
            RankingSourcePhase phase = phases.get(i);
            if (!BOSS_PHASE_ORDER.get(i).equals(phase.getPhase())) {
                return false;
            }
            if (phase.getTurns() < 1) {
                return false;
            }
            turnSum += phase.getTurns();
        }
        return bossFight.getTotalTurns() == turnSum;
    }

    private boolean isFinishingCardInDeck(RankingSourceBossFight bossFight, RankingSourceDeck deck) {
        String finishingCard = bossFight.getFinishingCard();
        return deck.getCards().stream()
            .map(RankingSourceDeckCard::getCardType)
            .collect(Collectors.toSet())
            .contains(finishingCard);
    }
}
