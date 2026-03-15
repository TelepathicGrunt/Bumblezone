package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.blocks.CrystallineFlower;
import com.telepathicgrunt.the_bumblezone.items.datacomponents.CrystallineFlowerData;
import com.telepathicgrunt.the_bumblezone.modinit.BzDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.function.Consumer;

public class CrystallineFlowerBlockItem extends BzBlockItem {
    public CrystallineFlowerBlockItem(Block block, Properties properties, boolean fitInContainers) {
        super(block,
            properties
                .stacksTo(1)
                .component(BzDataComponents.CRYSTALLINE_FLOWER_DATA.get(), new CrystallineFlowerData())
                .rarity(Rarity.UNCOMMON),
            fitInContainers
        );
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
                        serverPlayer.sendOverlayMessage(message);
                    }

                    return null;
                }
            }
        }

        return resultantState;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
        CrystallineFlowerData flowerData = itemStack.getOrDefault(BzDataComponents.CRYSTALLINE_FLOWER_DATA.get(), new CrystallineFlowerData());
        if (flowerData.uuid().compareTo(CrystallineFlowerData.DEFAULT_UUID) != 0 && flowerData.tier() != 0) {
            builder.accept(Component.translatable("item.the_bumblezone.crystalline_flower_info_1", flowerData.tier()).withStyle(ChatFormatting.LIGHT_PURPLE).withStyle(ChatFormatting.ITALIC));
            builder.accept(Component.translatable("item.the_bumblezone.crystalline_flower_info_2", flowerData.experience()).withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC));
        }
    }
}
