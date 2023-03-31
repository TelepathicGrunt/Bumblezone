package com.telepathicgrunt.the_bumblezone.world.features;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.world.features.configs.ItemFrameConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;


public class ItemFrameWithRandomItem extends Feature<ItemFrameConfig> {

    public ItemFrameWithRandomItem(Codec<ItemFrameConfig> code) {
        super(code);
    }

    @Override
    public boolean place(FeaturePlaceContext<ItemFrameConfig> context) {

        ItemFrameConfig config = context.config();
        BlockPos targetSpot = context.origin();
        Direction direction = null;
        if (config.attachment == ItemFrameConfig.Attachment.FLOOR) {
            direction = Direction.UP;
        }
        else if (config.attachment == ItemFrameConfig.Attachment.CEILING) {
            direction = Direction.DOWN;
        }
        else {
            for (Direction side : Direction.Plane.HORIZONTAL) {
                BlockPos sidePos = targetSpot.relative(side);
                BlockState sideBlock = context.level().getBlockState(sidePos);

                if (sideBlock.isFaceSturdy(context.level(), sidePos, side)) {
                    direction = side.getOpposite();
                    break;
                }
            }

            if (direction == null) {
                return false;
            }
        }

        ItemFrame itemFrame = new ItemFrame(context.level().getLevel(), targetSpot, direction);

        HolderSet<Item> items = config.itemToPickFrom;
        if (items.size() > 0) {
            itemFrame.setItem(items.getRandomElement(context.random()).get().value().getDefaultInstance(), false);
        }

        context.level().addFreshEntityWithPassengers(itemFrame);
        return true;
    }
}