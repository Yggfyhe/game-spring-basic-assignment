package com.gamebasic.ranking.dto.source;

import lombok.Getter;

import java.util.List;

@Getter
public class RankingSourceRun {
    private String seed;
    private String status;
    private int clearedFloor;
    private int durationSeconds;
    private int finalHp;
    private List<RankingSourceFloor> floors;
}
