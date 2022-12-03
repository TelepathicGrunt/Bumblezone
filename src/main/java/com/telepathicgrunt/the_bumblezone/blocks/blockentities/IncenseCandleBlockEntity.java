package com.telepathicgrunt.the_bumblezone.blocks.blockentities;

import com.telepathicgrunt.the_bumblezone.blocks.SuperCandleBase;
import com.telepathicgrunt.the_bumblezone.blocks.SuperCandleWick;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlockEntities;
import net.minecraft.client.multiplayer.ClientLevel;
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

public class IncenseCandleBlockEntity extends BlockEntity {
    public static final int DEFAULT_COLOR = 16777215;
    public static final int DEFAULT_MAX_DURATION = 12000;
    public static final int DEFAULT_RANGE = 3;
    public static final int DEFAULT_LINGER_TIME = 60;
    public static final int DEFAULT_NIGHT_VISION_LINGER_TIME = 240;
    public static String COLOR_TAG = "color";
    public static String STATUS_EFFECT_TAG = "status";
    public static String AMPLIFIER_TAG = "amplifier";
    public static String MAX_DURATION_TAG = "max_duration";
    public static String CURRENT_DURATION_TAG = "current_duration";
    public static String INSTANT_START_TIME_TAG = "instant_start_time";
    public static String INFINITE_TAG = "infinite";
    public static String RANGE_TAG = "range";
    public static String LINGER_TIME_TAG = "linger_time";
    private int color = DEFAULT_COLOR;
    private MobEffect mobEffect = null;
    private int amplifier = 0;
    private int maxDuration = DEFAULT_MAX_DURATION;
    private int currentDuration = 0;
    private long instantStartTime = 0;
    private boolean infinite = false;
    private int range = DEFAULT_RANGE;
    private int lingerTime = DEFAULT_LINGER_TIME;

    protected IncenseCandleBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public IncenseCandleBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(BzBlockEntities.INCENSE_CANDLE, blockPos, blockState);
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

    public int getAmplifier() {
        return this.amplifier;
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
        this.amplifier = compoundTag.contains(AMPLIFIER_TAG) ? compoundTag.getInt(AMPLIFIER_TAG) : 0;
        this.maxDuration = compoundTag.contains(MAX_DURATION_TAG) ? compoundTag.getInt(MAX_DURATION_TAG) : DEFAULT_MAX_DURATION;
        this.currentDuration = compoundTag.contains(CURRENT_DURATION_TAG) ? compoundTag.getInt(CURRENT_DURATION_TAG) : 0;
        this.instantStartTime = compoundTag.contains(INSTANT_START_TIME_TAG) ? compoundTag.getLong(INSTANT_START_TIME_TAG) : 0;
        this.infinite = this.mobEffect == null || (compoundTag.contains(INFINITE_TAG) && compoundTag.getBoolean(INFINITE_TAG));
        this.range = compoundTag.contains(RANGE_TAG) ? compoundTag.getInt(RANGE_TAG) : DEFAULT_RANGE;
        this.lingerTime = compoundTag.contains(LINGER_TIME_TAG) ? compoundTag.getInt(LINGER_TIME_TAG) : DEFAULT_LINGER_TIME;

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
        compoundTag.putInt(AMPLIFIER_TAG, this.amplifier);
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
        if (blockEntity instanceof IncenseCandleBlockEntity incenseCandleBlockEntity) {
            boolean isInstant = incenseCandleBlockEntity.getMobEffect() != null && incenseCandleBlockEntity.getMobEffect().isInstantenous();
            boolean instantPotionTime = isInstantEffectApplyTime(level, incenseCandleBlockEntity);

            if (blockState.hasProperty(SuperCandleBase.LIT) && blockState.getValue(SuperCandleBase.LIT)) {
                if ((isInstant && instantPotionTime) || level.getGameTime() % 10 == 0) {
                    if (!incenseCandleBlockEntity.isInfinite() && incenseCandleBlockEntity.getCurrentDuration() >= incenseCandleBlockEntity.getMaxDuration()) {
                        SuperCandleWick.extinguish(null, level.getBlockState(blockPos.above()), level, blockPos.above());
                        incenseCandleBlockEntity.resetCurrentDuration();
                        incenseCandleBlockEntity.resetInstantStartTime();
                    }
                    else if (incenseCandleBlockEntity.getMobEffect() != null) {
                        if (!isInstant || instantPotionTime) {
                            int diameter = (incenseCandleBlockEntity.getRange() * 2) + 1;

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

                            int lingeringTime = isInstant ? 1 : incenseCandleBlockEntity.getLingerTime();
                            for (LivingEntity livingEntity : livingEntities) {
                                MobEffectInstance mobEffectInstance = new MobEffectInstance(
                                        incenseCandleBlockEntity.getMobEffect(),
                                        lingeringTime,
                                        incenseCandleBlockEntity.getAmplifier(),
                                        true,
                                        true,
                                        !incenseCandleBlockEntity.getMobEffect().isInstantenous());

                                livingEntity.addEffect(mobEffectInstance);
                            }

                            if (isInstant && level instanceof ServerLevel serverLevel) {
                                spawnEffectParticles(serverLevel, blockPos, incenseCandleBlockEntity.getMobEffect().isBeneficial(), incenseCandleBlockEntity.getRange());
                            }
                        }
                    }
                }
                incenseCandleBlockEntity.increaseCurrentDuration();
            }
        }
    }

    public static boolean isInstantEffectApplyTime(Level world, IncenseCandleBlockEntity incenseCandleBlockEntity) {
        long trueTimePassed = world.getGameTime() - incenseCandleBlockEntity.getInstantStartTime();
        long thresholdTime = getInstantEffectThresholdTime(incenseCandleBlockEntity.getAmplifier());
        return trueTimePassed % thresholdTime == 0;
    }

    public static long getInstantEffectThresholdTime(int amplifier) {
        return 200L + (150L * (long)(amplifier * amplifier * 0.6f));
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
