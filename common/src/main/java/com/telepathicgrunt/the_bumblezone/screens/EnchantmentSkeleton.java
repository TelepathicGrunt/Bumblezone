package com.telepathicgrunt.the_bumblezone.screens;

import java.io.Serializable;

public class EnchantmentSkeleton implements Serializable {
    public String path;
    public String namespace;
    public int level;
    public int minCost;
    public boolean isMaxLevel;
    public boolean isCurse;
    public boolean isTreasure;

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