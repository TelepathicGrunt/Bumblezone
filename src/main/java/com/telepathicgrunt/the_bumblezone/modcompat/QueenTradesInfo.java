package com.telepathicgrunt.the_bumblezone.modcompat;

import net.minecraft.world.item.ItemStack;

public record QueenTradesInfo (ItemStack wantItem) {

    public record RewardInfo(ItemStack itemStack, int xp, int weight) { }


}
