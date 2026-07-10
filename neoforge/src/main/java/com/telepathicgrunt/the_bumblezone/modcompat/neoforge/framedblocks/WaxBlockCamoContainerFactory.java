package com.telepathicgrunt.the_bumblezone.modcompat.neoforge.framedblocks;

import com.mojang.serialization.MapCodec;
import com.telepathicgrunt.the_bumblezone.blocks.AncientWax;
import com.telepathicgrunt.the_bumblezone.blocks.CarvableWax;
import com.telepathicgrunt.the_bumblezone.blocks.LuminescentWaxBase;
import com.telepathicgrunt.the_bumblezone.items.BzBlockItem;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import io.github.xfacthd.framedblocks.api.camo.TriggerRegistrar;
import io.github.xfacthd.framedblocks.api.camo.block.AbstractBlockCamoContainerFactory;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import org.jetbrains.annotations.Nullable;

/**
 * Factory for creating {@link WaxBlockCamoContainer}s when a {@link CarvableWax} block is applied as a camo
 * to a framed block. This is necessary as carvable wax places blocks with a blockstate property that depends on the
 * item being used, which FramedBlocks cannot deal with by default and instead defaults to the default state of the block
 */
final class WaxBlockCamoContainerFactory extends AbstractBlockCamoContainerFactory<WaxBlockCamoContainer> {

    private static final MapCodec<WaxBlockCamoContainer> CODEC = BlockState.CODEC
            .xmap(WaxBlockCamoContainer::new, WaxBlockCamoContainer::getState).fieldOf("state");
    private static final StreamCodec<ByteBuf, WaxBlockCamoContainer> STREAM_CODEC = ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY)
            .map(WaxBlockCamoContainer::new, WaxBlockCamoContainer::getState);

    /**
     * Compute the {@linkplain BlockState camo state} associated with the {@link ItemStack} used for applying the camo
     *
     * @param level The level of the framed block the camo is being applied to
     * @param pos The position of the framed block the camo is being applied to
     * @param player The player applying the camo
     * @param itemAccess The item access holding the stack with which the camo is being applied
     * @return the {@linkplain BlockState camo state} associated with the {@link ItemStack} used for applying the camo
     */
    @Override
    @Nullable
    protected BlockState getStateFromItemStack(Level level, BlockPos pos, Player player, ItemAccess itemAccess) {
        if (itemAccess.getResource().getItem() instanceof BzBlockItem item && item.getBlock() instanceof CarvableWax) {
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
     * @param itemAccess The item access holding the stack with which the camo is being applied
     * @return the camo container resulting from the computed {@linkplain BlockState camo state} and additional context
     */
    @Override
    protected WaxBlockCamoContainer createContainer(BlockState camoState, Level level, BlockPos pos, Player player, ItemAccess itemAccess) {
        return new WaxBlockCamoContainer(camoState);
    }

    /**
     * Create a copy of the given camo container with the camo state replaced by the given new {@linkplain BlockState camo state}
     *
     * @param original The original camo container
     * @param newCamoState The new camo state to store in the copied container
     * @return the copied camo container with the new camo state
     */
    @Override
    protected WaxBlockCamoContainer copyContainerWithState(WaxBlockCamoContainer original, BlockState newCamoState) {
        return new WaxBlockCamoContainer(newCamoState);
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
        return camoState.getBlock() instanceof CarvableWax || camoState.getBlock() instanceof LuminescentWaxBase || camoState.getBlock() instanceof AncientWax;
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
    public ItemStack dropCamo(WaxBlockCamoContainer container) {
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
     * @param itemAccess The item access holding the stack with which the camo is being removed
     * @param container The camo container being removed
     * @return The {@link ItemStack} to return to the player
     */
    @Override
    protected ItemStack createItemStack(Level level, BlockPos pos, Player player, ItemAccess itemAccess, WaxBlockCamoContainer container) {
        return dropCamo(container);
    }

    /// Handle interactions with the given camo in the provided context. If the interaction changes the camo data,
    /// then a new camo container with the new data must be returned, otherwise the given camo should be returned.
    ///
    /// @param level  The level the framed block holding the camo is in
    /// @param pos    The position of the framed block holding the camo
    /// @param player The player interacting with the framed block
    /// @param camo   The camo container the player is interacting with
    /// @param stack  The stack used to interact with the framed block
    /// @param hand   The hand holding the stack used to interact with the framed block
    /// @return a new camo container if the camo data changes from this interaction, otherwise the given one
    @Override
    public WaxBlockCamoContainer handleInteraction(
            Level level,
            BlockPos pos,
            Player player,
            WaxBlockCamoContainer camo,
            ItemStack stack,
            InteractionHand hand
    ) {
        BlockState camoState = camo.getContent().getState();
        BlockState newCamoState = switch (camoState.getBlock()) {
            case CarvableWax carvableWax -> carvableWax.tryCarve(stack, camoState, level, pos, player, hand);
            case LuminescentWaxBase lumiWax -> lumiWax.tryRotate(stack, camoState, level, pos, player, hand);
            case AncientWax ancientWax -> ancientWax.trySwap(stack, camoState, level, pos, player, hand);
            default -> camoState;
        };
        if (newCamoState != null && newCamoState != camoState) {
            return camo.copyWithState(newCamoState);
        }
        return camo;
    }

    /**
     * Write the relevant data of the given camo container to the given NBT tag for syncing to the client
     * @param output The value output to write the relevant data to
     * @param container The camo container to be synced
     */
    @Override
    protected void writeToNetwork(ValueOutput output, WaxBlockCamoContainer container) {
        output.putInt("state", Block.getId(container.getState()));
    }

    /**
     * Reconstruct a camo container from the given network-synced NBT tag
     * @param input The value input received over the network
     * @return The reconstructed camo container
     */
    @Override
    protected WaxBlockCamoContainer readFromNetwork(ValueInput input) {
        BlockState state = Block.stateById(input.getIntOr("state", 0));
        return new WaxBlockCamoContainer(state);
    }

    /**
     * {@return a {@link MapCodec} for reading and writing the camo container from and to disk}
     */
    @Override
    public MapCodec<WaxBlockCamoContainer> codec() {
        return CODEC;
    }

    /**
     * {@return a {@link StreamCodec} for reading and writing the camo container from and to network packets}
     */
    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, WaxBlockCamoContainer> streamCodec() {
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

        registrar.registerApplicationItem(BzItems.ANCIENT_WAX_BRICKS.get());
        registrar.registerApplicationItem(BzItems.ANCIENT_WAX_DIAMOND.get());
        registrar.registerApplicationItem(BzItems.ANCIENT_WAX_COMPOUND_EYES.get());
        registrar.registerApplicationItem(BzItems.ANCIENT_WAX_BRICKS_STAIRS.get());
        registrar.registerApplicationItem(BzItems.ANCIENT_WAX_DIAMOND_STAIRS.get());
        registrar.registerApplicationItem(BzItems.ANCIENT_WAX_COMPOUND_EYES_STAIRS.get());
        registrar.registerApplicationItem(BzItems.ANCIENT_WAX_BRICKS_SLAB.get());
        registrar.registerApplicationItem(BzItems.ANCIENT_WAX_DIAMOND_SLAB.get());
        registrar.registerApplicationItem(BzItems.ANCIENT_WAX_COMPOUND_EYES_SLAB.get());

        registrar.registerApplicationItem(BzItems.LUMINESCENT_WAX_CHANNEL.get());
        registrar.registerApplicationItem(BzItems.LUMINESCENT_WAX_CHANNEL_RED.get());
        registrar.registerApplicationItem(BzItems.LUMINESCENT_WAX_CHANNEL_PURPLE.get());
        registrar.registerApplicationItem(BzItems.LUMINESCENT_WAX_CHANNEL_BLUE.get());
        registrar.registerApplicationItem(BzItems.LUMINESCENT_WAX_CHANNEL_GREEN.get());
        registrar.registerApplicationItem(BzItems.LUMINESCENT_WAX_CHANNEL_YELLOW.get());
        registrar.registerApplicationItem(BzItems.LUMINESCENT_WAX_CHANNEL_WHITE.get());
        registrar.registerApplicationItem(BzItems.LUMINESCENT_WAX_CORNER.get());
        registrar.registerApplicationItem(BzItems.LUMINESCENT_WAX_CORNER_RED.get());
        registrar.registerApplicationItem(BzItems.LUMINESCENT_WAX_CORNER_PURPLE.get());
        registrar.registerApplicationItem(BzItems.LUMINESCENT_WAX_CORNER_BLUE.get());
        registrar.registerApplicationItem(BzItems.LUMINESCENT_WAX_CORNER_GREEN.get());
        registrar.registerApplicationItem(BzItems.LUMINESCENT_WAX_CORNER_YELLOW.get());
        registrar.registerApplicationItem(BzItems.LUMINESCENT_WAX_CORNER_WHITE.get());
        registrar.registerApplicationItem(BzItems.LUMINESCENT_WAX_NODE.get());
        registrar.registerApplicationItem(BzItems.LUMINESCENT_WAX_NODE_RED.get());
        registrar.registerApplicationItem(BzItems.LUMINESCENT_WAX_NODE_PURPLE.get());
        registrar.registerApplicationItem(BzItems.LUMINESCENT_WAX_NODE_BLUE.get());
        registrar.registerApplicationItem(BzItems.LUMINESCENT_WAX_NODE_GREEN.get());
        registrar.registerApplicationItem(BzItems.LUMINESCENT_WAX_NODE_YELLOW.get());
        registrar.registerApplicationItem(BzItems.LUMINESCENT_WAX_NODE_WHITE.get());

        registrar.registerRemovalPredicate(TriggerRegistrar.DEFAULT_REMOVAL);
    }
}
