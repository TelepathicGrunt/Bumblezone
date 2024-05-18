package com.telepathicgrunt.the_bumblezone.entities;

import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.utils.PlatformHooks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

import java.util.Optional;

public class SwapItemFrameContents {
    private static String checkedFluidConfig = "";
    private static Item cachedBucketItem = null;

    public static void onItemFrameSpawn(ItemFrame itemFrame) {
        if (!BzModCompatibilityConfigs.alternativeFluidToReplaceHoneyFluid.isEmpty() && itemFrame.getItem().is(BzItems.HONEY_BUCKET.get())) {
            if (!BzModCompatibilityConfigs.alternativeFluidToReplaceHoneyFluid.equals(checkedFluidConfig)) {

                checkedFluidConfig = BzModCompatibilityConfigs.alternativeFluidToReplaceHoneyFluid;
                Optional<Fluid> fluid = BuiltInRegistries.FLUID.getOptional(new ResourceLocation(BzModCompatibilityConfigs.alternativeFluidToReplaceHoneyFluid));
                if (fluid.isPresent()) {
                    Optional<Item> itemOptional = BuiltInRegistries.ITEM.stream()
                            .filter(item -> item instanceof BucketItem bucketItem && PlatformHooks.getBucketItemFluid(bucketItem) == fluid.get())
                            .findFirst();

                    if (itemOptional.isPresent()) {
                        itemOptional.ifPresent(item -> cachedBucketItem = item);
                    }
                    else {
                        return;
                    }
                }
                else {
                    return;
                }
            }
            itemFrame.setItem(cachedBucketItem.getDefaultInstance(), false);
        }
    }
}
