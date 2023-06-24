package com.telepathicgrunt.the_bumblezone.blocks.blockentities;

import com.telepathicgrunt.the_bumblezone.modinit.BzBlockEntities;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class InfinityBarrierBlockEntity extends BlockEntity {
    public static final int DEFAULT_COLOR_1 = 16777215;
    public static final int DEFAULT_COLOR_2 = 16770000;
    public static String PRIMARY_COLOR_TAG = "primaryColor";
    public static String SECONDARY_COLOR_TAG = "secondaryColor";
    private int primaryColor = 0;
    private int secondaryColor = 0;

    protected InfinityBarrierBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public InfinityBarrierBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(BzBlockEntities.INFINITY_BARRIER.get(), blockPos, blockState);
    }

    public int getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(int primaryColor) {
        this.primaryColor = primaryColor;
    }

    public int getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(int secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        if (this.level instanceof ClientLevel) {
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 8);
        }

        this.primaryColor = compoundTag.contains(PRIMARY_COLOR_TAG) ? compoundTag.getInt(PRIMARY_COLOR_TAG) : DEFAULT_COLOR_1;
        this.secondaryColor = compoundTag.contains(SECONDARY_COLOR_TAG) ? compoundTag.getInt(SECONDARY_COLOR_TAG) : DEFAULT_COLOR_2;
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        saveFieldsToTag(compoundTag);
    }

    private void saveFieldsToTag(CompoundTag compoundTag) {
        compoundTag.putInt(PRIMARY_COLOR_TAG, this.primaryColor);
        compoundTag.putInt(SECONDARY_COLOR_TAG, this.secondaryColor);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveFieldsToTag(tag);
        return tag;
    }

    public boolean shouldDrawSide(Direction direction) {
        return Block.shouldRenderFace(this.getBlockState(), this.getLevel(), this.getBlockPos(), direction, this.getBlockPos().relative(direction));
    }

    public static int getBlockColor(BlockAndTintGetter world, BlockPos pos, int tintIndex) {
        if (world != null) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof InfinityBarrierBlockEntity infinityBarrierBlockEntity) {
                if (tintIndex == 0) {
                    return infinityBarrierBlockEntity.getPrimaryColor();
                }
                else if (tintIndex == 1) {
                    return infinityBarrierBlockEntity.getSecondaryColor();
                }
            }
        }
        return tintIndex;
    }
}
