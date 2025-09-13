package com.telepathicgrunt.the_bumblezone.blocks.blockentities;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class InfinityBarrierBlockEntity extends BlockEntity {
    public static final int DEFAULT_COLOR_1 = 16777215;
    public static final int DEFAULT_COLOR_2 = 16770000;
    public static final String PRIMARY_COLOR_TAG = "primaryColor";
    public static final String SECONDARY_COLOR_TAG = "secondaryColor";
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
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
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

    @Override
    public void loadAdditional(ValueInput valueInput) {
        super.loadAdditional(valueInput);

        if (this.level != null && this.level.isClientSide()) {
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 8);
        }

        this.primaryColor = valueInput.getIntOr(PRIMARY_COLOR_TAG, DEFAULT_COLOR_1);
        this.secondaryColor = valueInput.getIntOr(SECONDARY_COLOR_TAG, DEFAULT_COLOR_2);
    }

    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);
        saveFieldsToTag(valueOutput);
    }

    private void saveFieldsToTag(ValueOutput valueOutput) {
        valueOutput.putInt(PRIMARY_COLOR_TAG, this.primaryColor);
        valueOutput.putInt(SECONDARY_COLOR_TAG, this.secondaryColor);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        TagValueOutput tagvalueoutput = TagValueOutput.createWithContext(new ProblemReporter.ScopedCollector(Bumblezone.LOGGER), provider);
        saveFieldsToTag(tagvalueoutput);
        return tagvalueoutput.buildResult();
    }
}
