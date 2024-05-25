package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.blocks.CrystallineFlower;
import com.telepathicgrunt.the_bumblezone.datacomponents.CrystallineFlowerData;
import com.telepathicgrunt.the_bumblezone.modinit.BzDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class CrystallineFlowerBlockItem extends BzBlockItem {
    public CrystallineFlowerBlockItem(Block block, Properties properties, boolean fitInContainers, boolean useBlockName) {
        super(block, properties.component(BzDataComponents.CRYSTALLINE_FLOWER_DATA.get(), new CrystallineFlowerData()), fitInContainers, useBlockName);
    }

    @Override
    public void verifyComponentsAfterLoad(ItemStack itemStack) {
        if (itemStack.get(BzDataComponents.CRYSTALLINE_FLOWER_DATA.get()) == null) {
            itemStack.set(BzDataComponents.CRYSTALLINE_FLOWER_DATA.get(), new CrystallineFlowerData());
        }
    }

    @Override
    protected BlockState getPlacementState(BlockPlaceContext blockPlaceContext) {
        BlockState blockState = this.getBlock().getStateForPlacement(blockPlaceContext);
        BlockState resultantState = blockState != null && this.canPlace(blockPlaceContext, blockState) ? blockState : null;

        if (blockPlaceContext.getItemInHand().has(BzDataComponents.CRYSTALLINE_FLOWER_DATA.get())) {
            CrystallineFlowerData crystallineFlowerData = blockPlaceContext.getItemInHand().get(BzDataComponents.CRYSTALLINE_FLOWER_DATA.get());
            if (resultantState != null && crystallineFlowerData != null) {
                int tiers = crystallineFlowerData.tier();
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

        return resultantState;
    }
}
