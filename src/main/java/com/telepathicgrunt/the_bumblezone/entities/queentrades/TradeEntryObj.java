package com.telepathicgrunt.the_bumblezone.entities.queentrades;

import com.google.gson.annotations.Expose;

public class TradeEntryObj {
    @Expose
    public final String id;

    @Expose
    private Boolean required;

    @Expose
    private Integer xp_reward;

    @Expose
    private Integer weight;

    public TradeEntryObj(String id, Boolean required, Integer xp_reward, Integer weight) {
        this.id = id;
        this.required = required;
        this.xp_reward = xp_reward;
        this.weight = weight;
    }

    public boolean isRequired() {
        return required == null || required;
    }

    public Integer getXpReward() {
        if (xp_reward == null) {
            return 0;
        }

        if (xp_reward <= 0) {
            throw new RuntimeException("Bumblezone: xp_reward cannot be negative. Id entry: " + id);
        }

        return xp_reward;
    }

    public Integer getWeight() {
        if (weight == null) {
            return 1;
        }

        if (weight <= 0) {
            throw new RuntimeException("Bumblezone: weight cannot be 0 or negative. Id entry: " + id);
        }

        return weight;
    }
}
