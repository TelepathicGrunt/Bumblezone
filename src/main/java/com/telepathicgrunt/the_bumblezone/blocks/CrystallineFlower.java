package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.CrystallineFlowerBlockEntity;
import com.telepathicgrunt.the_bumblezone.modinit.BzStats;
import com.telepathicgrunt.the_bumblezone.screens.CrystallineFlowerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;


public class CrystallineFlower extends Block {
    private static final Component CONTAINER_TITLE = Component.translatable(Bumblezone.MODID + ".container.crystalline_flower");

    public CrystallineFlower() {
        super(Properties.of(Material.REPLACEABLE_PLANT, MaterialColor.COLOR_ORANGE).strength(0.4F, 0.01F));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        else {
            player.openMenu(state.getMenuProvider(level, pos));
            player.awardStat(BzStats.INTERACT_WITH_CRYSTALLINE_FLOWER_RL.get());
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        int searchLevel = 0;
        int searchTreasure = 0;
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CrystallineFlowerBlockEntity crystallineFlowerBlockEntity) {
            searchLevel = (crystallineFlowerBlockEntity.getXpTier() * 2) + 1;
            if (crystallineFlowerBlockEntity.getXpTier() > 5) {
                searchTreasure = 1;
            }
        }

        int finalSearchTreasure = searchTreasure;
        int finalSearchLevel = searchLevel;
        return new SimpleMenuProvider(
                (containerId, inventory, player) -> new CrystallineFlowerMenu(
                        containerId,
                        inventory,
                        ContainerLevelAccess.create(level, pos),
                        finalSearchLevel,
                        finalSearchTreasure
                ), CONTAINER_TITLE);
    }
}
