package com.telepathicgrunt.the_bumblezone.blocks.blockentities;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.CrystallineFlower;
import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.items.datacomponents.CrystallineFlowerData;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlockEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzDataComponents;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.UUID;

public class CrystallineFlowerBlockEntity extends BlockEntity {
    public static final String TIER_TAG = "tier";
    public static final String XP_TAG = "xp";
    public static final String UUID_TAG = "guid";
    private int xpTier = 1;
    private int currentXp = 0;
    private UUID uuid = java.util.UUID.randomUUID();

    public static final String BOOK_SLOT_ITEMS = "bookItems";
    public static final String CONSUME_SLOT_ITEMS = "consumeItems";
    private ItemStack bookSlotItems = ItemStack.EMPTY;
    private ItemStack consumeSlotItems = ItemStack.EMPTY;

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

    public UUID getUUID() {
        return this.uuid;
    }

    public void setGUID(UUID uuid) {
        this.uuid = uuid;
    }

    public ItemStack getBookSlotItems() {
        return this.bookSlotItems;
    }

    public void setBookSlotItems(ItemStack bookSlotItems) {
        this.bookSlotItems = bookSlotItems;
    }

    public ItemStack getConsumeSlotItems() {
        return this.consumeSlotItems;
    }

    public void setConsumeSlotItems(ItemStack consumeSlotItems) {
        this.consumeSlotItems = consumeSlotItems;
        setPillar(0);
    }

    public void syncPillar() {
        setPillar(0);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
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
        if (this.level != null) {
            int bottomHeight = CrystallineFlower.flowerHeightBelow(this.level, this.getBlockPos());
            BlockPos operatingPos = this.getBlockPos().below(bottomHeight);
            int topHeight = CrystallineFlower.flowerHeightAbove(this.level, operatingPos);
            BlockEntity blockEntity = level.getBlockEntity(operatingPos);
            if (blockEntity instanceof CrystallineFlowerBlockEntity crystallineFlowerBlockEntity) {

                if (tierChange != 0) {
                    if (bottomHeight != 0) {
                        BlockEntity targetBlockEntity = level.getBlockEntity(this.getBlockPos().below(bottomHeight));
                        if (targetBlockEntity instanceof CrystallineFlowerBlockEntity) {
                            targetBlockEntity.loadWithComponents(
                                    TagValueInput.create(
                                            new ProblemReporter.ScopedCollector(Bumblezone.LOGGER),
                                            level.registryAccess(),
                                            crystallineFlowerBlockEntity.getUpdateTag(level.registryAccess())
                                    )
                            );
                        }
                    }

                    boolean upward = tierChange > 0;
                    for (int i = 0; i < (upward ? this.xpTier : topHeight + 1); i++) {
                        boolean placePlant = upward || i < this.xpTier;

                        level.setBlock(
                                operatingPos.above(i),
                                placePlant ? BzBlocks.CRYSTALLINE_FLOWER.get().defaultBlockState() : Blocks.AIR.defaultBlockState(),
                                2);

                        if (this.level instanceof ServerLevel serverLevel && !placePlant) {
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
                            2);

                    BlockEntity blockEntity2 = level.getBlockEntity(operatingPos);
                    if (blockEntity2 instanceof CrystallineFlowerBlockEntity crystallineFlowerBlockEntity2) {
                        crystallineFlowerBlockEntity2.loadWithComponents(
                                TagValueInput.create(
                                        new ProblemReporter.ScopedCollector(Bumblezone.LOGGER),
                                        level.registryAccess(),
                                        crystallineFlowerBlockEntity.getUpdateTag(level.registryAccess())
                                )
                        );
                        blockEntity2.setChanged();
                    }
                }

                for (int i = 0; i <= topHeight; i++) {
                    BlockPos updatePos = operatingPos.above(i);
                    BlockState state = level.getBlockState(updatePos);
                    level.updateNeighborsAt(updatePos, state.getBlock());

                    if (i != 0) {
                        BlockEntity blockEntity2 = level.getBlockEntity(updatePos);
                        if (blockEntity2 instanceof CrystallineFlowerBlockEntity crystallineFlowerBlockEntity2) {
                            crystallineFlowerBlockEntity2.loadWithComponents(
                                    TagValueInput.create(
                                            new ProblemReporter.ScopedCollector(Bumblezone.LOGGER),
                                            level.registryAccess(),
                                            crystallineFlowerBlockEntity.getUpdateTag(level.registryAccess())
                                    )
                            );
                        }
                    }
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
        return Math.max(1, (90 + (tier * tier * 15)) + BzGeneralConfigs.crystallineFlowerExtraXpNeededForTiers);
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

    @Override
    protected void loadAdditional(ValueInput valueInput) {
        super.loadAdditional(valueInput);
        this.xpTier = valueInput.getIntOr(TIER_TAG, 0);
        this.currentXp = Math.min(valueInput.getIntOr(XP_TAG, 0), getMaxXpForTier(this.xpTier));

        this.uuid = valueInput.read(UUID_TAG, UUIDUtil.CODEC).orElse(java.util.UUID.randomUUID());
        if (this.uuid.compareTo(CrystallineFlowerData.DEFAULT_UUID) == 0) {
            this.uuid = java.util.UUID.randomUUID();
        }

        this.bookSlotItems = valueInput.read(BOOK_SLOT_ITEMS, ItemStack.CODEC).orElse(ItemStack.EMPTY);
        this.consumeSlotItems = valueInput.read(CONSUME_SLOT_ITEMS, ItemStack.CODEC).orElse(ItemStack.EMPTY);
    }

    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);
        saveFieldsToTag(valueOutput);
    }

    private void saveFieldsToTag(ValueOutput valueOutput) {
        valueOutput.putInt(TIER_TAG, this.xpTier);
        valueOutput.putInt(XP_TAG, this.currentXp);
        valueOutput.store(UUID_TAG, UUIDUtil.CODEC, this.uuid);

        if (!this.bookSlotItems.isEmpty()) {
            valueOutput.store(BOOK_SLOT_ITEMS, ItemStack.CODEC, this.bookSlotItems);
        }
        if (!this.consumeSlotItems.isEmpty()) {
            valueOutput.store(CONSUME_SLOT_ITEMS, ItemStack.CODEC, this.consumeSlotItems);
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        TagValueOutput tagvalueoutput = TagValueOutput.createWithContext(new ProblemReporter.ScopedCollector(Bumblezone.LOGGER), provider);
        saveFieldsToTag(tagvalueoutput);
        return tagvalueoutput.buildResult();
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter dataComponentInput) {
        super.applyImplicitComponents(dataComponentInput);
        CrystallineFlowerData crystallineFlowerData = dataComponentInput.getOrDefault(BzDataComponents.CRYSTALLINE_FLOWER_DATA.get(), new CrystallineFlowerData());

        this.xpTier = crystallineFlowerData.tier();
        this.currentXp = crystallineFlowerData.experience();
        if (crystallineFlowerData.uuid().compareTo(CrystallineFlowerData.DEFAULT_UUID) != 0) {
            this.uuid = crystallineFlowerData.uuid();
        }
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder builder) {
        super.collectImplicitComponents(builder);
        CrystallineFlowerData crystallineFlowerData = new CrystallineFlowerData(this.xpTier, this.currentXp, this.uuid);
        builder.set(BzDataComponents.CRYSTALLINE_FLOWER_DATA.get(), crystallineFlowerData);
    }

    @Override
    public void removeComponentsFromTag(ValueOutput valueOutput) {
        valueOutput.discard(TIER_TAG);
        valueOutput.discard(XP_TAG);
        valueOutput.discard(UUID_TAG);
        super.removeComponentsFromTag(valueOutput);
    }
}
