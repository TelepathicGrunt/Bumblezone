package com.telepathicgrunt.the_bumblezone.blocks.blockentities;

import com.telepathicgrunt.the_bumblezone.modinit.BzBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CrystallineFlowerBlockEntity extends BlockEntity {
    public static String TIER_TAG = "tier";
    public static String XP_TAG = "xp";
    public static String GUID_TAG = "guid";
    private int xpTier = 1;
    private int currentXp = 0;
    private String guid = java.util.UUID.randomUUID().toString();

    protected CrystallineFlowerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public CrystallineFlowerBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(BzBlockEntities.CRYSTALLINE_FLOWER.get(), blockPos, blockState);
    }

    public int getXpTier() {
        return this.xpTier;
    }

    public void setXpTier(int xpTier) {
        this.xpTier = xpTier;
    }

    public int getCurrentXp() {
        return this.currentXp;
    }

    public void setCurrentXp(int currentXp) {
        this.currentXp = currentXp;
    }

    public String getGUID() {
        return this.guid;
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        this.xpTier = compoundTag.getInt(TIER_TAG);
        this.currentXp = compoundTag.getInt(XP_TAG);
        this.guid = compoundTag.getString(GUID_TAG);
        if (this.guid.isEmpty()) {
            this.guid = java.util.UUID.randomUUID().toString();
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        saveFieldsToTag(compoundTag);
    }

    private void saveFieldsToTag(CompoundTag compoundTag) {
        compoundTag.putInt(TIER_TAG, this.xpTier);
        compoundTag.putInt(XP_TAG, this.currentXp);
        compoundTag.putString(GUID_TAG, this.guid);
    }

    @Override
    public void saveToItem(ItemStack stack) {
        CompoundTag compoundTag = new CompoundTag();
        this.saveAdditional(compoundTag);
        BlockItem.setBlockEntityData(stack, this.getType(), compoundTag);
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

    public void addXpAndTier(int xpChange) {
        currentXp += xpChange;

        while (currentXp >= getMaxXpForTier(xpTier) && !isMaxTier()) {
            xpTier++;
            currentXp -= getMaxXpForTier(xpTier);
        }

        while (currentXp < 0 && !isMinTier()) {
            xpTier--;
            currentXp += getMaxXpForTier(xpTier);
        }

        if (isMaxTier()) {
            currentXp = 0;
        }

        if (currentXp >= getMaxXpForTier(xpTier)) {
            currentXp = getMaxXpForTier(xpTier);
        }
        else if (currentXp < 0) {
            currentXp = 0;
        }
        this.setChanged();
    }

    public void increaseTier(int tierIncrease) {
        if (!isMaxTier()) {
            xpTier += tierIncrease;
        }
        else {
            currentXp = getMaxXpForTier(xpTier);
        }
        this.setChanged();
    }

    public void decreaseTier(int tierDecrease) {
        if (!isMinTier()) {
            xpTier -= tierDecrease;
        }
        this.setChanged();
    }

    public boolean isMaxXP() {
        return currentXp == getMaxXpForTier(xpTier);
    }

    public boolean isMinXP() {
        return currentXp == 0;
    }

    public boolean isMaxTier() {
        return xpTier == 7;
    }

    public boolean isMinTier() {
        return xpTier == 0;
    }

    public int getMaxXpForTier(int tier) {
        return 100;
    }

    public int getXpForNextTiers(int nextTiersToCalculate) {
        int totalXpNeeded = 0;
        for (int i = 0; i < nextTiersToCalculate; i++) {
            if (i == 0) {
                totalXpNeeded += getMaxXpForTier(xpTier) - currentXp;
            }
            else if (xpTier + i <= 7) {
                totalXpNeeded += getMaxXpForTier(xpTier + i);
            }
        }
        return totalXpNeeded;
    }
}
