package com.telepathicgrunt.bumblezone.modcompat;

import net.minecraft.block.BlockState;

import java.util.Random;

/**
 * This class is used so java wont load BeeBetterCompat class and crash
 * if the mod isn't on. This is because java will load classes if their method
 * is present even though it isn't called when going through some code. However,
 * java won't load classes referenced in the method ahead of time so the redirection
 * works to keep BeeBetterCompat unloaded by "nesting" the method dependant on the mod.
 */
public class BeeBetterRedirection
{
    public static BlockState getCandle(Random random){
        return BeeBetterCompat.getCandle(random);
    }

    public static BlockState getBeeDungeonBlock(Random random){
        return BeeBetterCompat.getBeeDungeonBlock(random);
    }

    public static BlockState getSpiderDungeonBlock(Random random){
        return BeeBetterCompat.getSpiderDungeonBlock(random);
    }

    public static BlockState getBeeswaxBlock(){
        return BeeBetterCompat.getBeeswaxBlock();
    }
}
