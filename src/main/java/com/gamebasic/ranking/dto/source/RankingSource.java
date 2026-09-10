package com.gamebasic.ranking.dto.source;

import lombok.Getter;

import java.util.List;

@Getter
public class RankingSource {
    private RankingSourceMeta meta;
    private List<RankingSourceRecord> records;
}
