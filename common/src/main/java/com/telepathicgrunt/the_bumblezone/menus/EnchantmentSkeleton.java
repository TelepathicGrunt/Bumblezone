package com.telepathicgrunt.the_bumblezone.menus;

import java.io.Serializable;

public class EnchantmentSkeleton implements Serializable {
    public final String path;
    public final String namespace;
    public final int level;
    public final int minCost;
    public final boolean isMaxLevel;
    public final boolean isCurse;
    public final boolean isTreasure;

    public EnchantmentSkeleton(String path,
                               String namespace,
                               int level,
                               int minCost,
                               boolean isMaxLevel,
                               boolean isCurse,
                               boolean isTreasure)
    {
        this.path = path;
        this.namespace = namespace;
        this.level = level;
        this.minCost = minCost;
        this.isMaxLevel = isMaxLevel;
        this.isCurse = isCurse;
        this.isTreasure = isTreasure;
    }
}