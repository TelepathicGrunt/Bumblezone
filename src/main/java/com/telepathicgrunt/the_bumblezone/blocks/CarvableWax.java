package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;


public class CarvableWax extends ProperFacingBlock {
    public static final EnumProperty<Carving> CARVING = EnumProperty.create("carving", Carving.class);

    public enum Carving implements StringRepresentable {
        UNCARVED("uncarved"),
        WAVY("wavy"),
        FLOWER("flower"),
        CHISELED("chiseled"),
        DIAMOND("diamond"),
        BRICKS("bricks"),
        CHAINS("chains");

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

    private Item item;

    public CarvableWax() {
        super(Properties.of(Material.CLAY, MaterialColor.SAND).strength(0.28F, 0.28F).sound(SoundType.WOOD));
        this.registerDefaultState(this.stateDefinition.any().setValue(CARVING, Carving.UNCARVED));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add().add(CARVING);
        builder.add().add(FACING);
    }

    @Override
    public Item asItem() {
        if (this.item == null) {
            this.item = BzItems.CARVABLE_WAX.get();
        }

        return this.item;
    }

    /**
     * Return correct blockitem for creative middle click (pick block)
     */
    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        if (state.hasProperty(CARVING)) {
            Carving pattern = state.getValue(CARVING);

            switch(pattern) {
                case UNCARVED:
                    return BzItems.CARVABLE_WAX.get().getDefaultInstance();
                case BRICKS:
                    return BzItems.CARVABLE_WAX_BRICKS.get().getDefaultInstance();
                case CHAINS:
                    return BzItems.CARVABLE_WAX_CHAINS.get().getDefaultInstance();
                case DIAMOND:
                    return BzItems.CARVABLE_WAX_DIAMOND.get().getDefaultInstance();
                case CHISELED:
                    return BzItems.CARVABLE_WAX_CHISELED.get().getDefaultInstance();
                case FLOWER:
                    return BzItems.CARVABLE_WAX_FLOWER.get().getDefaultInstance();
                case WAVY:
                    return BzItems.CARVABLE_WAX_WAVY.get().getDefaultInstance();
                default:
            }
        }

        return new ItemStack(this);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }

    public static BlockState getFacingStateForPlacement(BlockState carvableWaxBlockState, BlockPlaceContext context) {
        return carvableWaxBlockState.setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState blockState, Level world, BlockPos position, Player playerEntity, InteractionHand playerHand, BlockHitResult raytraceResult) {
        ItemStack itemstack = playerEntity.getItemInHand(playerHand);

        if (blockState.hasProperty(CARVING) && (itemstack.getItem() instanceof ShearsItem || itemstack.getItem() instanceof SwordItem)) {
            world.setBlock(position, BzBlocks.CARVABLE_WAX.get().defaultBlockState().setValue(CARVING, blockState.getValue(CARVING).next()), 3);
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
