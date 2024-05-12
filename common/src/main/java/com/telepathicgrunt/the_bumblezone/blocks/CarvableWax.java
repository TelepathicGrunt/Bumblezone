package com.telepathicgrunt.the_bumblezone.blocks;

import com.mojang.serialization.MapCodec;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.utils.PlatformHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
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
        CHAINS("chains"),
        MUSIC("music"),
        GRATE("grate");

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

    public static final MapCodec<CarvableWax> CODEC = Block.simpleCodec(CarvableWax::new);

    private Item item;

    public CarvableWax() {
        this(Properties.of()
                .mapColor(MapColor.SAND)
                .instrument(NoteBlockInstrument.FLUTE)
                .strength(0.28F, 0.28F)
                .sound(SoundType.WOOD));
    }


    public CarvableWax(Properties properties) {
        super(properties);

        this.registerDefaultState(this.stateDefinition.any().setValue(CARVING, Carving.UNCARVED));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
    }

    @Override
    public MapCodec<? extends CarvableWax> codec() {
        return CODEC;
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
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
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
                case MUSIC:
                    return BzItems.CARVABLE_WAX_MUSIC.get().getDefaultInstance();
                case GRATE:
                    return BzItems.CARVABLE_WAX_GRATE.get().getDefaultInstance();
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
    public ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos position, Player playerEntity, InteractionHand playerHand, BlockHitResult raytraceResult) {
        if (blockState.hasProperty(CARVING) &&
            (PlatformHooks.isToolAction(itemStack, ShearsItem.class, "shears_carve") ||
            PlatformHooks.isToolAction(itemStack, SwordItem.class, "sword_dig")))
        {
            level.setBlock(position, BzBlocks.CARVABLE_WAX.get().defaultBlockState().setValue(CARVING, blockState.getValue(CARVING).next()), 3);
            this.spawnDestroyParticles(level, playerEntity, position,blockState);

            playerEntity.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
            if (playerEntity instanceof ServerPlayer serverPlayer) {
                BzCriterias.CARVE_WAX_TRIGGER.get().trigger(serverPlayer, position);

                if (!serverPlayer.getAbilities().instabuild) {
                    itemStack.hurtAndBreak(1, serverPlayer, playerHand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
                }
            }

            return ItemInteractionResult.SUCCESS;
        }

        return super.useItemOn(itemStack, blockState, level, position, playerEntity, playerHand, raytraceResult);
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
