package com.telepathicgrunt.the_bumblezone.entities.queentrades;

import com.google.gson.annotations.Expose;

import java.util.List;
import java.util.Map;

public class QueenTradesCollectionObj {
    @Expose
    public List<TradeEntryObj> wants;

    @Expose
    public List<TradeEntryObj> possible_rewards;

    public QueenTradesCollectionObj(List<TradeEntryObj> wants, List<TradeEntryObj> possible_rewards) {
        this.wants = wants;
        this.possible_rewards = possible_rewards;
    }
}
