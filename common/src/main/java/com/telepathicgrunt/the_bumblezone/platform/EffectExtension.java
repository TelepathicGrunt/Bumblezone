package com.telepathicgrunt.the_bumblezone.platform;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public interface EffectExtension {

    default List<ItemStack> bz$getCurativeItems() {
        ArrayList<ItemStack> ret = new ArrayList<>();
        ret.add(new ItemStack(Items.MILK_BUCKET));
        return ret;
    }
}
