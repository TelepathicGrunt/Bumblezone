package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.blocks.CrystallineFlower;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class CrystallineFlowerBlockItem extends BzBlockItem {
    public CrystallineFlowerBlockItem(Block block, Properties properties, boolean fitInContainers, boolean useBlockName) {
        super(block, properties, fitInContainers, useBlockName);
    }

    @Override
    protected BlockState getPlacementState(BlockPlaceContext blockPlaceContext) {
        BlockState blockState = this.getBlock().getStateForPlacement(blockPlaceContext);
        BlockState resultantState = blockState != null && this.canPlace(blockPlaceContext, blockState) ? blockState : null;

        if (blockPlaceContext.getItemInHand().has(DataComponents.BLOCK_ENTITY_DATA)) {
            CustomData customData = blockPlaceContext.getItemInHand().get(DataComponents.BLOCK_ENTITY_DATA);
            if (resultantState != null && customData != null && !customData.isEmpty()) {
                CompoundTag innerTag = customData.copyTag();
                if (innerTag.contains("tier")) {
                    int tiers = innerTag.getInt("tier");
                    List<Boolean> obstructions = CrystallineFlower.getObstructions(tiers, blockPlaceContext.getLevel(), blockPlaceContext.getClickedPos());
                    if (obstructions.stream().anyMatch(b -> b)) {
                        if (blockPlaceContext.getPlayer() instanceof ServerPlayer serverPlayer) {
                            Component message = Component.translatable("item.the_bumblezone.crystalline_flower_cannot_place").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.RED);
                            serverPlayer.displayClientMessage(message, true);
                        }

                        return null;
                    }
                }
            }
        }

        return resultantState;
    }
}
