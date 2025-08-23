package com.telepathicgrunt.the_bumblezone.blocks.blockentities;

import com.telepathicgrunt.the_bumblezone.blocks.SuperCandleBase;
import com.telepathicgrunt.the_bumblezone.blocks.SuperCandleWick;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class PotionCandleBlockEntity extends BlockEntity {
    public static final int DEFAULT_COLOR = 16777215;
    public static final int DEFAULT_MAX_DURATION = 12000;
    public static final int DEFAULT_RANGE = 3;
    public static final int DEFAULT_LINGER_TIME = 60;
    public static final int DEFAULT_NIGHT_VISION_LINGER_TIME = 240;
    public static final int DEFAULT_CALCULATED_EFFECT_APPLY_INTERVAL_TIME = 10;
    public static final int MAX_CALCULATED_EFFECT_APPLY_INTERVAL_TIME = 200;
    public static final String COLOR_TAG = "color";
    public static final String STATUS_EFFECT_TAG = "status";
    public static final String EFFECT_LEVEL_TAG = "amplifier";
    public static final String MAX_DURATION_TAG = "max_duration";
    public static final String CURRENT_DURATION_TAG = "current_duration";
    public static final String INSTANT_START_TIME_TAG = "instant_start_time";
    public static final String INFINITE_TAG = "infinite";
    public static final String RANGE_TAG = "range";
    public static final String LINGER_TIME_TAG = "linger_time";
    public static final String CALCULATED_EFFECT_APPLY_INTERVAL_TAG = "calculated_effect_apply_interval";
    private int color = DEFAULT_COLOR;
    private MobEffect mobEffect = null;
    private int effectLevel = 0;
    private int maxDuration = DEFAULT_MAX_DURATION;
    private int currentDuration = 0;
    private long instantStartTime = 0;
    private boolean infinite = false;
    private int range = DEFAULT_RANGE;
    private int lingerTime = DEFAULT_LINGER_TIME;
    private int calculatedEffectApplyInterval = DEFAULT_CALCULATED_EFFECT_APPLY_INTERVAL_TIME;

    protected PotionCandleBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public PotionCandleBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(BzBlockEntities.POTION_CANDLE.get(), blockPos, blockState);
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int colorIn) {
        this.color = colorIn;
    }

    public MobEffect getMobEffect() {
        return this.mobEffect;
    }

    public void setMobEffect(MobEffect mobEffect) {
        this.mobEffect = mobEffect;
    }

    public int getMaxDuration() {
        return this.maxDuration;
    }

    public int getEffectLevel() {
        return this.effectLevel;
    }

    public int getCurrentDuration() {
        return this.currentDuration;
    }

    public void resetCurrentDuration() {
        this.currentDuration = 0;
    }

    public void increaseCurrentDuration() {
        this.currentDuration += 1;
    }

    public long getInstantStartTime() {
        return this.instantStartTime;
    }

    public void resetInstantStartTime() {
        this.instantStartTime = 0;
    }

    public void setInstantStartTime(long startTime) {
        this.instantStartTime = startTime;
    }

    public boolean isInfinite() {
        return this.infinite;
    }

    public int getRange() {
        return this.range;
    }

    public int getLingerTime() {
        return this.lingerTime;
    }

    public int getCalculatedEffectApplyInterval() {
        return this.calculatedEffectApplyInterval;
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        this.color = compoundTag.contains(COLOR_TAG) ? compoundTag.getInt(COLOR_TAG) : DEFAULT_COLOR;
        if (compoundTag.contains(STATUS_EFFECT_TAG) && !compoundTag.getString(STATUS_EFFECT_TAG).trim().equals("")) {
            this.mobEffect = BuiltInRegistries.MOB_EFFECT.getOptional(new ResourceLocation(compoundTag.getString(STATUS_EFFECT_TAG))).orElse(null);
        }
        else {
            this.mobEffect = null;
        }
        this.effectLevel = compoundTag.contains(EFFECT_LEVEL_TAG) ? compoundTag.getInt(EFFECT_LEVEL_TAG) : 0;
        this.maxDuration = compoundTag.contains(MAX_DURATION_TAG) ? compoundTag.getInt(MAX_DURATION_TAG) : DEFAULT_MAX_DURATION;
        this.currentDuration = compoundTag.contains(CURRENT_DURATION_TAG) ? compoundTag.getInt(CURRENT_DURATION_TAG) : 0;
        this.instantStartTime = compoundTag.contains(INSTANT_START_TIME_TAG) ? compoundTag.getLong(INSTANT_START_TIME_TAG) : 0;
        this.infinite = this.mobEffect == null || (compoundTag.contains(INFINITE_TAG) && compoundTag.getBoolean(INFINITE_TAG));
        this.range = compoundTag.contains(RANGE_TAG) ? compoundTag.getInt(RANGE_TAG) : DEFAULT_RANGE;
        this.lingerTime = compoundTag.contains(LINGER_TIME_TAG) ? compoundTag.getInt(LINGER_TIME_TAG) : DEFAULT_LINGER_TIME;

        if (compoundTag.contains(CALCULATED_EFFECT_APPLY_INTERVAL_TAG)) {
            this.calculatedEffectApplyInterval = compoundTag.getInt(CALCULATED_EFFECT_APPLY_INTERVAL_TAG);
        }
        else {
            this.calculatedEffectApplyInterval = createIntervalTimeForEffectApply(this.mobEffect, this.effectLevel, this.lingerTime);
            boolean isInstant = this.mobEffect != null && this.mobEffect.isInstantenous();
            if (!isInstant) {
                this.lingerTime += this.calculatedEffectApplyInterval;
            }
        }

        if (this.level != null && this.level.isClientSide()) {
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
            compoundTag.putString(STATUS_EFFECT_TAG, BuiltInRegistries.MOB_EFFECT.getKey(this.mobEffect).toString());
        }
        compoundTag.putInt(EFFECT_LEVEL_TAG, this.effectLevel);
        compoundTag.putInt(MAX_DURATION_TAG, this.maxDuration);
        compoundTag.putInt(CURRENT_DURATION_TAG, this.currentDuration);
        compoundTag.putLong(INSTANT_START_TIME_TAG, this.instantStartTime);
        compoundTag.putBoolean(INFINITE_TAG, this.mobEffect == null || this.infinite);
        compoundTag.putInt(RANGE_TAG, this.range);
        compoundTag.putInt(LINGER_TIME_TAG, this.lingerTime);
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

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity) {
        if (blockEntity instanceof PotionCandleBlockEntity potionCandleBlockEntity) {
            boolean isInstant = potionCandleBlockEntity.getMobEffect() != null && potionCandleBlockEntity.getMobEffect().isInstantenous();
            boolean instantPotionTime = isInstantEffectApplyTime(level, potionCandleBlockEntity);

            if (blockState.hasProperty(SuperCandleBase.LIT) && blockState.getValue(SuperCandleBase.LIT)) {
                if ((isInstant && instantPotionTime) || level.getGameTime() % potionCandleBlockEntity.getCalculatedEffectApplyInterval() == 0) {
                    if (!potionCandleBlockEntity.isInfinite() && potionCandleBlockEntity.getCurrentDuration() >= potionCandleBlockEntity.getMaxDuration()) {
                        SuperCandleWick.extinguish(null, level.getBlockState(blockPos.above()), level, blockPos.above());
                        potionCandleBlockEntity.resetCurrentDuration();
                        potionCandleBlockEntity.resetInstantStartTime();
                    }
                    else if (potionCandleBlockEntity.getMobEffect() != null) {
                        if (!isInstant || instantPotionTime) {
                            int diameter = (potionCandleBlockEntity.getRange() * 2) + 1;

                            List<LivingEntity> livingEntities = level.getEntitiesOfClass(
                                    LivingEntity.class,
                                    AABB.ofSize(new Vec3(
                                            blockPos.getX() + 0.5d,
                                            blockPos.getY() + 0.5d,
                                            blockPos.getZ() + 0.5d),
                                            diameter,
                                            diameter,
                                            diameter
                                    ),
                                    (e) -> true);

                            int lingeringTime = isInstant ? 1 : potionCandleBlockEntity.getLingerTime();
                            for (LivingEntity livingEntity : livingEntities) {
                                boolean shouldApply = true;
                                if (!isInstant && livingEntity.hasEffect(potionCandleBlockEntity.getMobEffect())) {
                                    MobEffectInstance mobEffectInstance = livingEntity.getEffect(potionCandleBlockEntity.getMobEffect());
                                    int currentEffectTick = mobEffectInstance.getDuration();
                                    shouldApply = currentEffectTick < lingeringTime - 1;
                                }

                                if (!shouldApply) {
                                    continue;
                                }

                                MobEffectInstance mobEffectInstance = new MobEffectInstance(
                                        potionCandleBlockEntity.getMobEffect(),
                                        isInstant ? lingeringTime : lingeringTime + potionCandleBlockEntity.getCalculatedEffectApplyInterval() - 1,
                                        Math.max(0, potionCandleBlockEntity.getEffectLevel() - 1),
                                        true,
                                        true,
                                        !potionCandleBlockEntity.getMobEffect().isInstantenous());

                                livingEntity.addEffect(mobEffectInstance);
                            }

                            if (isInstant && level instanceof ServerLevel serverLevel) {
                                spawnEffectParticles(serverLevel, blockPos, potionCandleBlockEntity.getMobEffect().isBeneficial(), potionCandleBlockEntity.getRange());
                            }
                        }
                    }
                }
                potionCandleBlockEntity.increaseCurrentDuration();
            }
        }
    }

    public static boolean isInstantEffectApplyTime(Level world, PotionCandleBlockEntity potionCandleBlockEntity) {
        long trueTimePassed = world.getGameTime() - potionCandleBlockEntity.getInstantStartTime();
        long thresholdTime = getInstantEffectThresholdTime(potionCandleBlockEntity.getEffectLevel());
        return trueTimePassed % thresholdTime == 0;
    }

    public static long getInstantEffectThresholdTime(int amplifier) {
        return 200L + (150L * (long)(amplifier * amplifier * 0.6f));
    }

    public static int createIntervalTimeForEffectApply(MobEffect mobEffect, int effectLevel, int durationStartingPoint) {
        if (mobEffect == null) {
            return DEFAULT_CALCULATED_EFFECT_APPLY_INTERVAL_TIME;
        }

        int interval = DEFAULT_CALCULATED_EFFECT_APPLY_INTERVAL_TIME;
        while (interval < MAX_CALCULATED_EFFECT_APPLY_INTERVAL_TIME) {
            if (mobEffect.isDurationEffectTick(interval + durationStartingPoint, effectLevel - 1)) {
                return interval;
            }
            interval++;
        }
        return DEFAULT_CALCULATED_EFFECT_APPLY_INTERVAL_TIME;
    }

    public static Vec3 convertIntegerColorToRGB(int color) {
        Vec3 colorRGB;
        double red = (double)(color >> 16 & 255) / 255.0D;
        double green = (double)(color >> 8 & 255) / 255.0D;
        double blue = (double)(color & 255) / 255.0D;
        colorRGB = new Vec3(red, green, blue);
        return colorRGB;
    }

    private static void spawnEffectParticles(ServerLevel world, BlockPos position, boolean beneficial, int range) {
        world.sendParticles(beneficial ? ParticleTypes.GLOW : ParticleTypes.WITCH,
                position.getX() + 0.5d,
                position.getY() + 0.7d,
                position.getZ() + 0.5d,
                range * 10,
                (float)(range / 2),
                (float)(range / 2),
                (float)(range / 2),
                0.1d);
    }
}
