package com.telepathicgrunt.the_bumblezone.entities.queentrades;

import com.google.gson.annotations.Expose;

import java.util.List;

public class QueenTradesCollectionObj {
    @Expose
    public boolean is_color_randomizer_trade;

    @Expose
    public List<TradeEntryObj> wants;

    @Expose
    public List<TradeEntryObj> possible_rewards;

    public QueenTradesCollectionObj(boolean is_color_randomizer_trade, List<TradeEntryObj> wants, List<TradeEntryObj> possible_rewards) {
        this.is_color_randomizer_trade = is_color_randomizer_trade;
        this.wants = wants;
        this.possible_rewards = possible_rewards;
    }

    public QueenTradesCollectionObj(List<TradeEntryObj> wants, List<TradeEntryObj> possible_rewards) {
        this.is_color_randomizer_trade = false;
        this.wants = wants;
        this.possible_rewards = possible_rewards;
    }
}
