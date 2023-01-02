package com.telepathicgrunt.the_bumblezone.modcompat.recipecategories;

import net.minecraft.world.item.ItemStack;

public record QueenTradesInfo (ItemStack wantItem) {

    public record RewardInfo(ItemStack itemStack, int xp, int weight) { }


}
