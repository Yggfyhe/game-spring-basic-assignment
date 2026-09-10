package com.gamebasic.ranking.dto.source;

import lombok.Getter;

import java.util.List;

@Getter
public class RankingSourcePlayer {
    private String id;
    private String name;
    private String region;
    private List<String> tags;
}
