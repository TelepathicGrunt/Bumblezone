package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;


public class CarvableWax extends Block {
    public static final EnumProperty<Carving> CARVING = EnumProperty.create("carving", Carving.class);

    public enum Carving implements StringRepresentable {
        UNCARVED("uncarved"),
        WAVY("wavy"),
        FLOWER("flower"),
        CHISELED("chiseled"),
        DIAMOND("diamond"),
        BRICKS("bricks");

        private final String name;
        private static final Carving[] values = values();

        Carving(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        public String getSerializedName() {
            return this.name;
        }

        public Carving next() {
            return values[(this.ordinal()+1) % values.length];
        }
    }

    public CarvableWax() {
        super(Properties.of(Material.WOOD, MaterialColor.COLOR_ORANGE).strength(0.28F, 0.28F).sound(SoundType.WOOD));
        this.registerDefaultState(this.stateDefinition.any().setValue(CARVING, Carving.UNCARVED));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add().add(CARVING);
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState blockState, Level world, BlockPos position, Player playerEntity, InteractionHand playerHand, BlockHitResult raytraceResult) {
        ItemStack itemstack = playerEntity.getItemInHand(playerHand);

        if (blockState.hasProperty(CARVING) && (itemstack.getItem() instanceof ShearsItem || itemstack.getItem() instanceof SwordItem)) {
            world.setBlock(position, BzBlocks.CARVABLE_WAX.defaultBlockState().setValue(CARVING, blockState.getValue(CARVING).next()), 3);
            this.spawnDestroyParticles(world, playerEntity, position,blockState);

            if (playerEntity instanceof ServerPlayer serverPlayer) {
                BzCriterias.CARVE_CARVABLE_WAX_TRIGGER.trigger(serverPlayer, position);

                if (!serverPlayer.getAbilities().instabuild) {
                    itemstack.hurt(1, playerEntity.getRandom(), serverPlayer);
                }
            }

            return InteractionResult.SUCCESS;
        }

        return super.use(blockState, world, position, playerEntity, playerHand, raytraceResult);
    }

    /**
     * tell redstone that this can be use with comparator
     */
    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    /**
     * the power fed into comparator
     */
    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
        return Math.min(blockState.getValue(CARVING).ordinal(), 15);
    }
}
