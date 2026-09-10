package com.gamebasic.ranking.dto.source;

import lombok.Getter;

import java.util.List;

@Getter
public class RankingSourceBossFight {
    private List<RankingSourcePhase> phases;
    private String finishingCard;
    private int totalTurns;
}
