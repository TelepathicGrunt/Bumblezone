package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.blocks.blockentities.EssenceBlockEntity;
import com.telepathicgrunt.the_bumblezone.items.EssenceOfTheBees;
import com.telepathicgrunt.the_bumblezone.modinit.BzParticles;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;


public class EssenceBlock extends BaseEntityBlock {
    public EssenceBlock() {
        super(Properties.of()
                .mapColor(MapColor.SNOW)
                .strength(-1.0f, 3600000.8f)
                .lightLevel((blockState) -> 15)
                .noCollission()
                .noLootTable()
                .noOcclusion()
                .forceSolidOn()
                .pushReaction(PushReaction.BLOCK));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new EssenceBlockEntity(blockPos, blockState);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return true;
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        if (context instanceof EntityCollisionContext ctx) {
            Entity entity = ctx.getEntity();
            if (entity == null) {
                return Shapes.empty();
            }

            if (!(entity instanceof ServerPlayer serverPlayer) || !EssenceOfTheBees.hasEssence(serverPlayer)) {
                return Shapes.block();
            }
        }
        return Shapes.empty();
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        if (entity instanceof Player player) {
        }
    }

    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        if (randomSource.nextFloat() < 0.1f) {
            level.addParticle(
                    BzParticles.SPARKLE_PARTICLE.get(),
                    (double)blockPos.getX() + (randomSource.nextDouble() * 1.5D) - 0.25D,
                    (double)blockPos.getY() + (randomSource.nextDouble() * 1.5D) - 0.25D,
                    (double)blockPos.getZ() + (randomSource.nextDouble() * 1.5D) - 0.25D,
                    randomSource.nextGaussian() * 0.003d,
                    randomSource.nextGaussian() * 0.003d,
                    randomSource.nextGaussian() * 0.003d);
        }
    }
}
