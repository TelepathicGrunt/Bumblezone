package com.telepathicgrunt.the_bumblezone.items.essence;

import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Supplier;

public class RadianceEssence extends AbilityEssenceItem {

    private static final Supplier<Integer> cooldownLengthInTicks = () -> BzGeneralConfigs.radianceEssenceCooldown;
    private static final Supplier<Integer> abilityUseAmount = () -> BzGeneralConfigs.radianceEssenceAbilityUse;

    public RadianceEssence(Properties properties) {
        super(properties, cooldownLengthInTicks, abilityUseAmount);
    }

    @Override
    void addDescriptionComponents(List<Component> components) {
        components.add(Component.translatable("item.the_bumblezone.essence_green_description_1").withStyle(ChatFormatting.GREEN).withStyle(ChatFormatting.ITALIC));
        components.add(Component.translatable("item.the_bumblezone.essence_green_description_2").withStyle(ChatFormatting.GREEN).withStyle(ChatFormatting.ITALIC));
    }

    public void decrementAbilityUseRemaining(ItemStack stack, ServerPlayer serverPlayer, int decreaseAmount) {
        int getRemainingUse = Math.max(getAbilityUseRemaining(stack) - decreaseAmount, 0);
        setAbilityUseRemaining(stack, getRemainingUse);
        if (getRemainingUse == 0) {
            setDepleted(stack, serverPlayer, false);
        }
    }

    @Override
    public void applyAbilityEffects(ItemStack stack, Level level, ServerPlayer serverPlayer) {
        if (getIsActive(stack) && level.getBrightness(LightLayer.SKY, serverPlayer.blockPosition()) >= 13 && level.isDay()) {
            if (((long)serverPlayer.tickCount + serverPlayer.getUUID().getLeastSignificantBits()) % (serverPlayer.isSprinting() ? 2L : 12L) == 0) {
                spawnParticles(serverPlayer.serverLevel(), serverPlayer.position(), serverPlayer.getRandom());
            }

            if (((long)serverPlayer.tickCount + serverPlayer.getUUID().getLeastSignificantBits()) % 25L == 0) {

                List<Holder<MobEffect>> radianceEffects = BuiltInRegistries.MOB_EFFECT.getTag(BzTags.RADIANCE_SUN_EFFECTS)
                        .stream()
                        .flatMap(HolderSet.ListBacked::stream)
                        .filter(Holder::isBound)
                        .toList();

                for (Holder<MobEffect> effectHolder : radianceEffects) {
                    if (effectHolder.value() == MobEffects.MOVEMENT_SPEED) {
                        serverPlayer.addEffect(new MobEffectInstance(
                                MobEffects.MOVEMENT_SPEED,
                                120,
                                0,
                                false,
                                false));
                        decrementAbilityUseRemaining(stack, serverPlayer, serverPlayer.isSprinting() ? 3 : 1);
                        if (getForcedCooldown(stack)) {
                            return;
                        }
                    }
                    else if (effectHolder.value() == MobEffects.DAMAGE_RESISTANCE) {
                        serverPlayer.addEffect(new MobEffectInstance(
                                MobEffects.DAMAGE_RESISTANCE,
                                120,
                                1,
                                false,
                                false));
                        decrementAbilityUseRemaining(stack, serverPlayer, 1);
                        if (getForcedCooldown(stack)) {
                            return;
                        }
                    }
                    else if (effectHolder.value() == MobEffects.REGENERATION) {
                        serverPlayer.addEffect(new MobEffectInstance(
                                MobEffects.REGENERATION,
                                120,
                                0,
                                false,
                                false));
                        decrementAbilityUseRemaining(stack, serverPlayer, serverPlayer.getHealth() < serverPlayer.getMaxHealth() ? 5 : 1);
                        if (getForcedCooldown(stack)) {
                            return;
                        }
                    }
                    else if (effectHolder.value() == MobEffects.DIG_SPEED) {
                        serverPlayer.addEffect(new MobEffectInstance(
                                MobEffects.DIG_SPEED,
                                120,
                                1,
                                false,
                                false));
                        decrementAbilityUseRemaining(stack, serverPlayer, 1);
                        if (getForcedCooldown(stack)) {
                            return;
                        }
                    }
                    else if (effectHolder.value() == MobEffects.SATURATION) {
                        serverPlayer.addEffect(new MobEffectInstance(
                                MobEffects.SATURATION,
                                120,
                                0,
                                false,
                                false));
                        decrementAbilityUseRemaining(stack, serverPlayer, serverPlayer.getFoodData().needsFood() ? 3 : 1);
                        if (getForcedCooldown(stack)) {
                            return;
                        }
                    }
                    else {
                        serverPlayer.addEffect(new MobEffectInstance(
                                effectHolder.value(),
                                120,
                                0,
                                false,
                                false));
                        decrementAbilityUseRemaining(stack, serverPlayer, 1);
                        if (getForcedCooldown(stack)) {
                            return;
                        }
                    }
                }




                for (ItemStack armorItem : serverPlayer.getArmorSlots()) {
                    if (armorItem.isDamageableItem() && armorItem.isDamaged()) {
                        armorItem.setDamageValue(armorItem.getDamageValue() - 1);
                        decrementAbilityUseRemaining(stack, serverPlayer, 10);

                        if (getForcedCooldown(stack)) {
                            return;
                        }
                    }
                }
            }
        }
    }

    public static void spawnParticles(ServerLevel world, Vec3 location, RandomSource random) {
        world.sendParticles(
                ParticleTypes.CHERRY_LEAVES,
                location.x(),
                location.y() + 1,
                location.z(),
                1,
                random.nextGaussian() * 0.15D,
                (random.nextGaussian() * 0.2D) + 0.1,
                random.nextGaussian() * 0.15D,
                0.0D);
    }
}