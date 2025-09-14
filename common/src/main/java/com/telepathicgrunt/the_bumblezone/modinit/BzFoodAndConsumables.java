package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import net.minecraft.core.HolderSet;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.RemoveStatusEffectsConsumeEffect;

import java.util.List;

public class BzFoodAndConsumables {
    public static final FoodProperties HONEY_CRYSTAL_SHARDS_FOOD = new FoodProperties.Builder().nutrition(2).saturationModifier(0.15F).build();

    public static final FoodProperties SUGAR_WATER_FOOD = new FoodProperties.Builder().nutrition(1).saturationModifier(0.05F).build();
    public static final Consumable SUGAR_WATER_CONSUME = Consumable.builder()
            .animation(ItemUseAnimation.DRINK)
            .hasConsumeParticles(false)
            .consumeSeconds(1.6F)
            .sound(BzSounds.SUGAR_WATER_DRINK.holder())
            .soundAfterConsume(BzSounds.SUGAR_WATER_DRINK.holder())
            .onConsume(new ApplyStatusEffectsConsumeEffect(List.of(
                        new MobEffectInstance(MobEffects.HASTE, 600, 0)
            )))
            .build();

    public static final FoodProperties ROYAL_JELLY_BOTTLE_FOOD = new FoodProperties.Builder().nutrition(12).saturationModifier(1.0F).build();
    public static final Consumable ROYAL_JELLY_BOTTLE_CONSUME = Consumable.builder()
            .animation(ItemUseAnimation.DRINK)
            .hasConsumeParticles(false)
            .consumeSeconds(1.6F)
            .sound(BzSounds.ROYAL_JELLY_DRINK.holder())
            .soundAfterConsume(BzSounds.ROYAL_JELLY_DRINK.holder())
            .onConsume(new ApplyStatusEffectsConsumeEffect(List.of(
                new MobEffectInstance(MobEffects.SLOW_FALLING, 9600, 0),
                new MobEffectInstance(MobEffects.JUMP_BOOST, 9600, 3),
                new MobEffectInstance(MobEffects.SPEED, 9600, 1),
                new MobEffectInstance(BzEffects.BEENERGIZED.holder(), 9600, 2)
            )))
            .onConsume(new RemoveStatusEffectsConsumeEffect(HolderSet.direct(
                MobEffects.POISON,
                MobEffects.SLOWNESS,
                MobEffects.WEAKNESS
            )))
            .build();

    public static final FoodProperties BEE_BREAD_FOOD = new FoodProperties.Builder().nutrition(6).saturationModifier(0.1F).alwaysEdible().build();
    public static final Consumable BEE_BREAD_CONSUME = Consumable.builder()
            .animation(ItemUseAnimation.EAT)
            .hasConsumeParticles(false)
            .consumeSeconds(1.6F)
            .sound(BzSounds.ROYAL_JELLY_DRINK.holder())
            .soundAfterConsume(BzSounds.ROYAL_JELLY_DRINK.holder())
            .onConsume(new ApplyStatusEffectsConsumeEffect(List.of(
                new MobEffectInstance(MobEffects.NAUSEA, 120, 1),
                new MobEffectInstance(BzEffects.BEENERGIZED.holder(), 6000, 0)
            )))
            .build();

    public static final FoodProperties BEE_SOUP_FOOD = new FoodProperties.Builder().nutrition(12).saturationModifier(1.6F).alwaysEdible().build();
    public static final Consumable BEE_SOUP_CONSUME = Consumable.builder()
            .animation(ItemUseAnimation.EAT)
            .hasConsumeParticles(false)
            .consumeSeconds(1.6F)
            .sound(BzSounds.ROYAL_JELLY_DRINK.holder())
            .soundAfterConsume(BzSounds.ROYAL_JELLY_DRINK.holder())
            .onConsume(new ApplyStatusEffectsConsumeEffect(List.of(
                new MobEffectInstance(BzEffects.BEENERGIZED.holder(), 12000, 1)
            )))
            .onConsume(new ApplyStatusEffectsConsumeEffect(
                List.of(
                    new MobEffectInstance(MobEffects.LEVITATION, 800, 0)
                ),
                0.2F
            ))
            .onConsume(new ApplyStatusEffectsConsumeEffect(
                List.of(
                    new MobEffectInstance(MobEffects.POISON, 800, 0)
                ),
                0.2F
            ))
            .onConsume(new ApplyStatusEffectsConsumeEffect(
                List.of(
                    new MobEffectInstance(MobEffects.SLOW_FALLING, 800, 0)
                ),
                0.2F
            ))
            .onConsume(new ApplyStatusEffectsConsumeEffect(
                List.of(
                    new MobEffectInstance(MobEffects.LUCK, 18000, 0)
                ),
                0.2F
            ))
            .onConsume(new ApplyStatusEffectsConsumeEffect(
                List.of(
                    new MobEffectInstance(BzEffects.PARALYZED.holder(), Math.min(BzGeneralConfigs.paralyzedMaxTickDuration, 200), 0)
                ),
                0.2F
            ))
            .build();

    public static final Consumable ESSENCE_OF_THE_BEES_CONSUME = Consumable.builder()
            .animation(ItemUseAnimation.DRINK)
            .hasConsumeParticles(false)
            .consumeSeconds(3.5F)
            .sound(BzSounds.BEE_ESSENCE_CONSUMING.holder())
            .soundAfterConsume(BzSounds.BEE_ESSENCE_CONSUMED.holder())
            .build();
}
