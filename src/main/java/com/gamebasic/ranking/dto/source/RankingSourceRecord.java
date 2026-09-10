package com.gamebasic.ranking.dto.source;

import lombok.Getter;

@Getter
public class RankingSourceRecord {
    private long id;
    private String submittedAt;
    private RankingSourceClientInfo client;
    private RankingSourcePlayer player;
    private RankingSourceRun run;
    private RankingSourceBossFight bossFight;
    private RankingSourceDeck deck;
}
