package com.telepathicgrunt.the_bumblezone.blocks.blockentities;

import com.telepathicgrunt.the_bumblezone.blocks.SuperCandleBase;
import com.telepathicgrunt.the_bumblezone.blocks.SuperCandleWick;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlockEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class SuperIncenseCandleBlockEntity extends BlockEntity {
    public static String COLOR_TAG = "color";
    public static String STATUS_EFFECT_TAG = "status";
    public static String AMPLIFIER_TAG = "amplifier";
    public static String MAX_DURATION_TAG = "max_duration";
    public static String CURRENT_DURATION_TAG = "current_duration";
    public static String INFINITE_TAG = "infinite";
    private int color = 16777215;
    private MobEffect mobEffect = null;
    private int amplifier = 0;
    private int maxDuration = 12000;
    private int currentDuration = 0;
    private boolean infinite = false;

    protected SuperIncenseCandleBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public SuperIncenseCandleBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(BzBlockEntities.SUPER_INCENSE_CANDLE.get(), blockPos, blockState);
    }

    public int getColor() {
        return this.color;
    }

    public MobEffect getMobEffect() {
        return this.mobEffect;
    }

    public int getMaxDuration() {
        return this.maxDuration;
    }

    public int getAmplifier() {
        return this.amplifier;
    }

    public int getCurrentDuration() {
        return this.currentDuration;
    }

    public boolean isInfinite() {
        return this.infinite;
    }

    public void resetCurrentDuration() {
        this.currentDuration = 0;
    }

    public void increaseCurrentDuration() {
        this.currentDuration += 1;
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        this.color = compoundTag.contains(COLOR_TAG) ? compoundTag.getInt(COLOR_TAG) : 16777215;
        this.mobEffect = compoundTag.contains(STATUS_EFFECT_TAG) ? Registry.MOB_EFFECT.get(new ResourceLocation(compoundTag.getString(STATUS_EFFECT_TAG))) : null;
        this.amplifier = compoundTag.contains(AMPLIFIER_TAG) ? compoundTag.getInt(AMPLIFIER_TAG) : 0;
        this.maxDuration = compoundTag.contains(MAX_DURATION_TAG) ? compoundTag.getInt(MAX_DURATION_TAG) : 12000;
        this.currentDuration = compoundTag.contains(CURRENT_DURATION_TAG) ? compoundTag.getInt(CURRENT_DURATION_TAG) : 0;
        this.infinite = compoundTag.contains(INFINITE_TAG) && compoundTag.getBoolean(INFINITE_TAG);

        if (this.level instanceof ClientLevel) {
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 8);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        saveFieldsToTag(compoundTag);
    }

    private void saveFieldsToTag(CompoundTag compoundTag) {
        compoundTag.putInt(COLOR_TAG, this.color);
        if (this.mobEffect != null) {
            compoundTag.putString(STATUS_EFFECT_TAG, Registry.MOB_EFFECT.getKey(this.mobEffect).toString());
        }
        compoundTag.putInt(AMPLIFIER_TAG, this.amplifier);
        compoundTag.putInt(MAX_DURATION_TAG, this.maxDuration);
        compoundTag.putInt(CURRENT_DURATION_TAG, this.currentDuration);
        compoundTag.putBoolean(INFINITE_TAG, this.infinite);
    }

    @Override
    public void saveToItem(ItemStack stack) {
        CompoundTag compoundTag = new CompoundTag();
        super.saveAdditional(compoundTag);
        compoundTag.putInt(COLOR_TAG, this.color);
        if (this.mobEffect != null) {
            compoundTag.putString(STATUS_EFFECT_TAG, Registry.MOB_EFFECT.getKey(this.mobEffect).toString());
        }
        compoundTag.putInt(MAX_DURATION_TAG, this.maxDuration);
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

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity) {
        if (level.getGameTime() % 10 == 0 && blockEntity instanceof SuperIncenseCandleBlockEntity superIncenseCandleBlockEntity) {
            if (blockState.hasProperty(SuperCandleBase.LIT) && blockState.getValue(SuperCandleBase.LIT)) {
                if (!superIncenseCandleBlockEntity.isInfinite() && superIncenseCandleBlockEntity.getCurrentDuration() >= superIncenseCandleBlockEntity.getMaxDuration()) {
                    SuperCandleWick.extinguish(null, level.getBlockState(blockPos.above()), level, blockPos.above());
                    superIncenseCandleBlockEntity.resetCurrentDuration();
                }
                else if (superIncenseCandleBlockEntity.getMobEffect() != null) {
                    if (superIncenseCandleBlockEntity.getMobEffect().isInstantenous() && level.getGameTime() % 200 != 0) {
                        return;
                    }

                    List<LivingEntity> livingEntities = level.getEntitiesOfClass(
                        LivingEntity.class,
                        AABB.ofSize(new Vec3(
                            blockPos.getX() + 0.5d,
                            blockPos.getY() + 0.5d,
                            blockPos.getZ() + 0.5d),
                            10,
                            10,
                            10
                        ),
                        (e) -> true);

                    for (LivingEntity livingEntity : livingEntities) {
                        MobEffectInstance mobEffectInstance = new MobEffectInstance(
                            superIncenseCandleBlockEntity.getMobEffect(),
                            superIncenseCandleBlockEntity.getMobEffect() == MobEffects.NIGHT_VISION ? 240 : 60,
                            superIncenseCandleBlockEntity.getAmplifier(),
                            true,
                            true,
                            true);

                        livingEntity.addEffect(mobEffectInstance);
                    }

                    superIncenseCandleBlockEntity.increaseCurrentDuration();
                }
            }
        }
    }
}
