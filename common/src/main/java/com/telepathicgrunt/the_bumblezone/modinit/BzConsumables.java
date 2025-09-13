package com.telepathicgrunt.the_bumblezone.modinit;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.RemoveStatusEffectsConsumeEffect;

public class BzConsumables {
    public static final Consumable ESSENCE_OF_THE_BEES = Consumable.builder()
            .animation(ItemUseAnimation.DRINK)
            .hasConsumeParticles(false)
            .consumeSeconds(3.75F)
            .sound(BzSounds.BEE_ESSENCE_CONSUMING.get())
            .soundAfterConsume(BzSounds.BEE_ESSENCE_CONSUMED.get())
            .build();
}
