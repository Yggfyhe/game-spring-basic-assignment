package com.gamebasic.ranking.dto.source;

import lombok.Getter;

import java.util.List;

@Getter
public class RankingSourceFloor {
    private int floor;
    private String enemy;
    private int turns;
    private int hpAfter;
    private List<RankingSourceReward> rewards;
}
