package com.telepathicgrunt.the_bumblezone.blocks.blockentities;

import com.telepathicgrunt.the_bumblezone.blocks.CrystallineFlower;
import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlockEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
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
    private Boolean isBreaking = false;

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

    public boolean getIsBreaking() {
        return this.isBreaking;
    }

    public void setIsBreaking(boolean isBreaking) {
        this.isBreaking = isBreaking;
    }

    public String getGUID() {
        return this.guid;
    }

    public void setGUID(String guid) {
        this.guid = guid;
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        this.xpTier = compoundTag.getInt(TIER_TAG);
        this.currentXp = Math.min(compoundTag.getInt(XP_TAG), getMaxXpForTier(this.xpTier));
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
        int tierChange = 0;

        while (currentXp >= getMaxXpForTier(xpTier) && !isMaxTier()) {
            currentXp -= getMaxXpForTier(xpTier);
            tierChange++;
            xpTier++;
        }

        while (currentXp < 0 && !isMinTier()) {
            currentXp += getMaxXpForTier(xpTier);
            tierChange--;
            xpTier--;
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
        setPillar(tierChange);
    }

    public void increaseTier(int tierIncrease) {
        int tierChange = Math.min(7 - xpTier, tierIncrease);
        if (!isMaxTier()) {
            xpTier += tierIncrease;
        }
        else {
            currentXp = getMaxXpForTier(xpTier);
        }

        if (currentXp >= getMaxXpForTier(xpTier) && !isMaxTier()) {
            currentXp = getMaxXpForTier(xpTier) - 1;
        }

        this.setChanged();
        setPillar(tierChange);
    }

    public void decreaseTier(int tierDecrease) {
        int tierChange = Math.min(xpTier - 1, tierDecrease);
        if (!isMinTier()) {
            xpTier -= tierDecrease;
        }

        if (currentXp >= getMaxXpForTier(xpTier) && !isMaxTier()) {
            currentXp = getMaxXpForTier(xpTier) - 1;
        }

        this.setChanged();
        setPillar(-tierChange);
    }

    public void setPillar(int tierChange) {
        if (this.level != null && tierChange != 0) {
            int bottomHeight = CrystallineFlower.flowerHeightBelow(this.level, this.getBlockPos());
            BlockPos operatingPos = this.getBlockPos().below(bottomHeight);
            int topHeight = CrystallineFlower.flowerHeightAbove(this.level, operatingPos);

            BlockEntity blockEntity = level.getBlockEntity(operatingPos);
            if (blockEntity instanceof CrystallineFlowerBlockEntity crystallineFlowerBlockEntity) {

                if (bottomHeight != 0) {
                    BlockEntity targetBlockEntity = level.getBlockEntity(this.getBlockPos().below(bottomHeight));
                    if (targetBlockEntity instanceof CrystallineFlowerBlockEntity) {
                        targetBlockEntity.load(crystallineFlowerBlockEntity.getUpdateTag());
                    }
                }

                for (int i = 0; i < topHeight + 1; i++) {
                    BlockEntity blockEntity2 = level.getBlockEntity(operatingPos.above(i));
                    if (blockEntity2 instanceof CrystallineFlowerBlockEntity crystallineFlowerBlockEntity2) {
                        crystallineFlowerBlockEntity2.setIsBreaking(true);
                    }
                }

                boolean upward = tierChange > 0;
                for (int i = 0; i < (upward ? this.xpTier : topHeight + 1); i++) {
                    boolean placePlant = upward || i < this.xpTier;

                    level.setBlock(
                            operatingPos.above(i),
                            placePlant ? BzBlocks.CRYSTALLINE_FLOWER.get().defaultBlockState() : Blocks.AIR.defaultBlockState(),
                            3);

                    level.updateNeighborsAt(operatingPos.above(i), placePlant ? BzBlocks.CRYSTALLINE_FLOWER.get() : Blocks.AIR);

                    if (placePlant) {
                        BlockEntity blockEntity2 = level.getBlockEntity(operatingPos.above(i));
                        if (blockEntity2 instanceof CrystallineFlowerBlockEntity crystallineFlowerBlockEntity2) {
                            crystallineFlowerBlockEntity2.setGUID(crystallineFlowerBlockEntity.getGUID());
                            crystallineFlowerBlockEntity2.setIsBreaking(false);
                        }
                    }
                    else if (this.level instanceof ServerLevel serverLevel) {
                        for (int itemsToDrop = 0; itemsToDrop < 2 + (i / 1.5); itemsToDrop++) {
                            ItemStack stack = BzItems.HONEY_CRYSTAL_SHARDS.get().getDefaultInstance();
                            stack.setCount(1);
                            GeneralUtils.spawnItemEntity(
                                    serverLevel,
                                    operatingPos.above(i),
                                    stack,
                                    0.05D,
                                    0.2D);
                        }
                    }
                }

                operatingPos = operatingPos.above(upward ? this.xpTier - 1 : topHeight + tierChange);

                level.setBlock(
                        operatingPos,
                        BzBlocks.CRYSTALLINE_FLOWER.get().defaultBlockState().setValue(CrystallineFlower.FLOWER, true),
                        3);

                BlockEntity blockEntity2 = level.getBlockEntity(operatingPos);
                if (blockEntity2 instanceof CrystallineFlowerBlockEntity crystallineFlowerBlockEntity2) {
                    crystallineFlowerBlockEntity2.setGUID(crystallineFlowerBlockEntity.getGUID());
                    crystallineFlowerBlockEntity2.setIsBreaking(false);
                }
            }
        }
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
        return Math.max(1, (45 + (tier * tier * 5)) + BzGeneralConfigs.crystallineFlowerExtraXpNeededForTiers.get());
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
