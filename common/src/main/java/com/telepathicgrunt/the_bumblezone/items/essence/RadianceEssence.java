package com.telepathicgrunt.the_bumblezone.items.essence;

import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.datacomponents.AbilityEssenceActivityData;
import com.telepathicgrunt.the_bumblezone.datacomponents.AbilityEssenceCooldownData;
import com.telepathicgrunt.the_bumblezone.modinit.BzDataComponents;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
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
import net.minecraft.world.entity.player.Player;
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
    public int getColor() {
        return 0xEAC027;
    }

    @Override
    void addDescriptionComponents(List<Component> components) {
        components.add(Component.translatable("item.the_bumblezone.essence_radiance_description_1").withStyle(ChatFormatting.YELLOW).withStyle(ChatFormatting.ITALIC));
        components.add(Component.translatable("item.the_bumblezone.essence_radiance_description_2").withStyle(ChatFormatting.YELLOW).withStyle(ChatFormatting.ITALIC));
    }


    @Override
    public void applyAbilityEffects(ItemStack itemStack, Level level, ServerPlayer serverPlayer) {
        AbilityEssenceActivityData abilityEssenceActivityData = itemStack.get(BzDataComponents.ABILITY_ESSENCE_ACTIVITY_DATA.get());
        if (abilityEssenceActivityData.isActive() && level.getBrightness(LightLayer.SKY, serverPlayer.blockPosition()) >= 13 && level.isDay()) {
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
                        if (decrementAbilityUseRemaining(itemStack, serverPlayer, serverPlayer.isSprinting() ? 3 : 1)) {
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
                        if (decrementAbilityUseRemaining(itemStack, serverPlayer, 1)) {
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
                        if (decrementAbilityUseRemaining(itemStack, serverPlayer, serverPlayer.getHealth() < serverPlayer.getMaxHealth() ? 5 : 1)) {
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
                        if (decrementAbilityUseRemaining(itemStack, serverPlayer, 1)) {
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
                        if (decrementAbilityUseRemaining(itemStack, serverPlayer, serverPlayer.getFoodData().needsFood() ? 3 : 1)) {
                            return;
                        }
                    }
                    else {
                        serverPlayer.addEffect(new MobEffectInstance(
                                effectHolder,
                                120,
                                0,
                                false,
                                false));
                        if (decrementAbilityUseRemaining(itemStack, serverPlayer, 1)) {
                            return;
                        }
                    }
                }

                for (ItemStack armorItem : serverPlayer.getArmorSlots()) {
                    if (armorItem.isDamageableItem() && armorItem.isDamaged() && !armorItem.is(BzTags.RADIANCE_CANNOT_REPAIR)) {
                        armorItem.setDamageValue(armorItem.getDamageValue() - 1);
                        if (decrementAbilityUseRemaining(itemStack, serverPlayer, 10)) {
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

    public static boolean IsRadianceEssenceActive(Player player) {
        if (player != null) {
            ItemStack offHandItem = player.getOffhandItem();
            return offHandItem.is(BzItems.ESSENCE_RADIANCE.get()) && offHandItem.get(BzDataComponents.ABILITY_ESSENCE_ACTIVITY_DATA.get()).isActive();
        }
        return false;
    }
}