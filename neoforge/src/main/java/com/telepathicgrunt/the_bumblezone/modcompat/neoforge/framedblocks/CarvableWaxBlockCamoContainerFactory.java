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

final class CarvableWaxBlockCamoContainerFactory extends AbstractBlockCamoContainerFactory<CarvableWaxBlockCamoContainer> {

    private static final MapCodec<CarvableWaxBlockCamoContainer> CODEC = BlockState.CODEC
            .xmap(CarvableWaxBlockCamoContainer::new, CarvableWaxBlockCamoContainer::getState).fieldOf("state");
    private static final StreamCodec<ByteBuf, CarvableWaxBlockCamoContainer> STREAM_CODEC = ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY)
            .map(CarvableWaxBlockCamoContainer::new, CarvableWaxBlockCamoContainer::getState);

    @Override
    @Nullable
    protected BlockState getStateFromItemStack(Level level, BlockPos pos, Player player, ItemStack stack) {
        if (stack.getItem() instanceof BzBlockItem item && item.getBlock() instanceof CarvableWax) {
            return item.getBlockState();
        }
        return null;
    }

    @Override
    protected CarvableWaxBlockCamoContainer createContainer(BlockState camoState, Level level, BlockPos pos, Player player, ItemStack stack) {
        return new CarvableWaxBlockCamoContainer(camoState);
    }

    @Override
    protected CarvableWaxBlockCamoContainer copyContainerWithState(CarvableWaxBlockCamoContainer original, BlockState newCamoState) {
        return new CarvableWaxBlockCamoContainer(newCamoState);
    }

    @Override
    protected boolean isValidBlock(BlockState camoState, BlockGetter level, BlockPos pos, @Nullable Player player) {
        return camoState.getBlock() instanceof CarvableWax;
    }

    @Override
    public boolean canTriviallyConvertToItemStack() {
        return true;
    }

    @Override
    public ItemStack dropCamo(CarvableWaxBlockCamoContainer container) {
        if (container.getState().getBlock() instanceof CarvableWax carvableWax) {
            return carvableWax.toItemStack(container.getState());
        }
        return new ItemStack(container.getState().getBlock());
    }

    @Override
    protected ItemStack createItemStack(Level level, BlockPos pos, Player player, ItemStack stack, CarvableWaxBlockCamoContainer container) {
        return dropCamo(container);
    }

    @Override
    protected void writeToNetwork(CompoundTag tag, CarvableWaxBlockCamoContainer container) {
        tag.putInt("state", Block.getId(container.getState()));
    }

    @Override
    protected CarvableWaxBlockCamoContainer readFromNetwork(CompoundTag tag) {
        BlockState state = Block.stateById(tag.getInt("state"));
        return new CarvableWaxBlockCamoContainer(state);
    }

    @Override
    public MapCodec<CarvableWaxBlockCamoContainer> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, CarvableWaxBlockCamoContainer> streamCodec() {
        return STREAM_CODEC;
    }

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
