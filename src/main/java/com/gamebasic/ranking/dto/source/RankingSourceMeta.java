package com.gamebasic.ranking.dto.source;

import lombok.Getter;

@Getter
public class RankingSourceMeta {
    private RankingSourceSeason season;
    private String generatedAt;
    private int schemaVersion;
    private int totalRecords;
}
