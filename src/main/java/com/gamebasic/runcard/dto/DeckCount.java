package com.gamebasic.runcard.dto;

import lombok.Getter;

@Getter
public class DeckCount {
    private final Long gameId;
    private final long cardCount;

    public DeckCount(Long gameId, long cardCount) {
        this.gameId = gameId;
        this.cardCount = cardCount;
    }
}
