package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record QueenTradesInfo (ItemStack wantItem) {

    public record RewardInfo(ItemStack itemStack, int xp, int weight) { }


}
