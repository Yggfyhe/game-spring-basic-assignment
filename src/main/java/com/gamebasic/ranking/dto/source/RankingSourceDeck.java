package com.gamebasic.ranking.dto.source;

import lombok.Getter;

import java.util.List;

@Getter
public class RankingSourceDeck {
    private int size;
    private List<RankingSourceDeckCard> cards;
}
