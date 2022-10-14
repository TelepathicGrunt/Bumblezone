package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.CrystallineFlowerBlockEntity;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlockEntities;
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
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;


public class CrystallineFlower extends BaseEntityBlock {
    private static final Component CONTAINER_TITLE = Component.translatable(Bumblezone.MODID + ".container.crystalline_flower");

    public CrystallineFlower() {
        super(Properties.of(Material.REPLACEABLE_PLANT, MaterialColor.COLOR_ORANGE).strength(0.4F, 0.01F));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return BzBlockEntities.CRYSTALLINE_FLOWER.get().create(blockPos, blockState);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CrystallineFlowerBlockEntity crystallineFlowerBlockEntity) {
            if (level.players().stream().noneMatch(p ->
                p.containerMenu instanceof CrystallineFlowerMenu crystallineFlowerMenu &&
                crystallineFlowerMenu.crystallineFlowerBlockEntity.getGUID().equals(crystallineFlowerBlockEntity.getGUID())))
            {
                if (level.isClientSide) {
                    return InteractionResult.SUCCESS;
                }

                player.openMenu(state.getMenuProvider(level, pos));
                player.awardStat(BzStats.INTERACT_WITH_CRYSTALLINE_FLOWER_RL.get());
                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.PASS;
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
        CrystallineFlowerBlockEntity finalCrystallineFlowerBlockEntity = blockEntity instanceof CrystallineFlowerBlockEntity ? (CrystallineFlowerBlockEntity) blockEntity : null;
        return new SimpleMenuProvider(
                (containerId, inventory, player) -> new CrystallineFlowerMenu(
                        containerId,
                        inventory,
                        ContainerLevelAccess.create(level, pos),
                        finalSearchLevel,
                        finalSearchTreasure,
                        finalCrystallineFlowerBlockEntity
                ), CONTAINER_TITLE);
    }
}
