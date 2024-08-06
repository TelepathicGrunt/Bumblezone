package com.telepathicgrunt.the_bumblezone.modcompat.neoforge.framedblocks;

import com.mojang.serialization.MapCodec;
import com.telepathicgrunt.the_bumblezone.blocks.CarvableWax;
import com.telepathicgrunt.the_bumblezone.items.BzBlockItem;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.camo.TriggerRegistrar;
import xfacthd.framedblocks.api.camo.block.AbstractBlockCamoContainerFactory;
import xfacthd.framedblocks.api.util.Utils;

/**
 * Factory for creating {@link CarvableWaxBlockCamoContainer}s when a {@link CarvableWax} block is applied as a camo
 * to a framed block. This is necessary as carvable wax places blocks with a blockstate property that depends on the
 * item being used, which FramedBlocks cannot deal with by default and instead defaults to the default state of the block
 */
final class CarvableWaxBlockCamoContainerFactory extends AbstractBlockCamoContainerFactory<CarvableWaxBlockCamoContainer> {

    private static final MapCodec<CarvableWaxBlockCamoContainer> CODEC = BlockState.CODEC
            .xmap(CarvableWaxBlockCamoContainer::new, CarvableWaxBlockCamoContainer::getState).fieldOf("state");
    private static final StreamCodec<ByteBuf, CarvableWaxBlockCamoContainer> STREAM_CODEC = ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY)
            .map(CarvableWaxBlockCamoContainer::new, CarvableWaxBlockCamoContainer::getState);

    /**
     * Compute the {@linkplain BlockState camo state} associated with the {@link ItemStack} used for applying the camo
     *
     * @param level The level of the framed block the camo is being applied to
     * @param pos The position of the framed block the camo is being applied to
     * @param player The player applying the camo
     * @param stack The stack with which the camo is being applied
     * @return the {@linkplain BlockState camo state} associated with the {@link ItemStack} used for applying the camo
     */
    @Override
    @Nullable
    protected BlockState getStateFromItemStack(Level level, BlockPos pos, Player player, ItemStack stack) {
        if (stack.getItem() instanceof BzBlockItem item && item.getBlock() instanceof CarvableWax) {
            return item.getBlockState();
        }
        return null;
    }

    /**
     * Create the camo container resulting from the computed {@linkplain BlockState camo state} and additional context when applying the camo
     *
     * @param level The level of the framed block the camo is being applied to
     * @param pos The position of the framed block the camo is being applied to
     * @param player The player applying the camo
     * @param stack The stack with which the camo is being applied
     * @return the camo container resulting from the computed {@linkplain BlockState camo state} and additional context
     */
    @Override
    protected CarvableWaxBlockCamoContainer createContainer(BlockState camoState, Level level, BlockPos pos, Player player, ItemStack stack) {
        return new CarvableWaxBlockCamoContainer(camoState);
    }

    /**
     * Create a copy of the given camo container with the camo state replaced by the given new {@linkplain BlockState camo state}
     *
     * @param original The original camo container
     * @param newCamoState The new camo state to store in the copied container
     * @return the copied camo container with the new camo state
     */
    @Override
    protected CarvableWaxBlockCamoContainer copyContainerWithState(CarvableWaxBlockCamoContainer original, BlockState newCamoState) {
        return new CarvableWaxBlockCamoContainer(newCamoState);
    }

    /**
     * Check whether the given {@linkplain BlockState camo state} is a valid camo in the given context when applied
     * by a player or loaded from disk
     *
     * @param camoState The camo state to check
     * @param level The level of the framed block the camo is being applied to or loaded on
     * @param pos The position of the framed block the camo is being applied to or loaded on
     * @param player The player applying the camo, if available
     * @return whether the given camo is valid in the given context
     */
    @Override
    protected boolean isValidBlock(BlockState camoState, BlockGetter level, BlockPos pos, @Nullable Player player) {
        return camoState.getBlock() instanceof CarvableWax;
    }

    /**
     * {@return whether a camo container produced by this factory can be trivially converted to an {@link ItemStack}
     * without any additional context or resource consumption}
     */
    @Override
    public boolean canTriviallyConvertToItemStack() {
        return true;
    }

    /**
     * {@return the {@link ItemStack} to drop when a framed block with the given camo container applied is broken}
     */
    @Override
    public ItemStack dropCamo(CarvableWaxBlockCamoContainer container) {
        if (container.getState().getBlock() instanceof CarvableWax carvableWax) {
            return carvableWax.toItemStack(container.getState());
        }
        return new ItemStack(container.getState().getBlock());
    }

    /**
     * Create the {@link ItemStack} to return to the player when removing the given camo container with a valid removal tool
     *
     * @param level The level of the framed block the camo is being removed from
     * @param pos The position of the framed block the camo is being removed from
     * @param player The player removing the camo
     * @param stack The stack with which the camo is being removed
     * @param container The camo container being removed
     * @return The {@link ItemStack} to return to the player
     */
    @Override
    protected ItemStack createItemStack(Level level, BlockPos pos, Player player, ItemStack stack, CarvableWaxBlockCamoContainer container) {
        return dropCamo(container);
    }

    /**
     * Write the relevant data of the given camo container to the given NBT tag for syncing to the client
     * @param tag The NBT tag to write the relevant data to
     * @param container The camo container to be synced
     */
    @Override
    protected void writeToNetwork(CompoundTag tag, CarvableWaxBlockCamoContainer container) {
        tag.putInt("state", Block.getId(container.getState()));
    }

    /**
     * Reconstruct a camo container from the given network-synced NBT tag
     * @param tag The NBT tag received over the network
     * @return The reconstructed camo container
     */
    @Override
    protected CarvableWaxBlockCamoContainer readFromNetwork(CompoundTag tag) {
        BlockState state = Block.stateById(tag.getInt("state"));
        return new CarvableWaxBlockCamoContainer(state);
    }

    /**
     * {@return a {@link MapCodec} for reading and writing the camo container from and to disk}
     */
    @Override
    public MapCodec<CarvableWaxBlockCamoContainer> codec() {
        return CODEC;
    }

    /**
     * {@return a {@link StreamCodec} for reading and writing the camo container from and to network packets}
     */
    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, CarvableWaxBlockCamoContainer> streamCodec() {
        return STREAM_CODEC;
    }

    /**
     * Register valid items for applying and removing camo containers made by this factory to and from a framed block
     */
    @Override
    public void registerTriggerItems(TriggerRegistrar registrar) {
        registrar.registerApplicationItem(BzItems.CARVABLE_WAX.get());
        registrar.registerApplicationItem(BzItems.CARVABLE_WAX_WAVY.get());
        registrar.registerApplicationItem(BzItems.CARVABLE_WAX_FLOWER.get());
        registrar.registerApplicationItem(BzItems.CARVABLE_WAX_CHISELED.get());
        registrar.registerApplicationItem(BzItems.CARVABLE_WAX_DIAMOND.get());
        registrar.registerApplicationItem(BzItems.CARVABLE_WAX_BRICKS.get());
        registrar.registerApplicationItem(BzItems.CARVABLE_WAX_CHAINS.get());
        registrar.registerApplicationItem(BzItems.CARVABLE_WAX_MUSIC.get());
        registrar.registerApplicationItem(BzItems.CARVABLE_WAX_GRATE.get());

        registrar.registerRemovalItem(Utils.FRAMED_HAMMER.value());
    }
}
