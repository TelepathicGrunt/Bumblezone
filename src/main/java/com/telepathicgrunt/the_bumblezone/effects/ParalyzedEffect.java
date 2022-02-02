package com.telepathicgrunt.the_bumblezone.effects;

import com.telepathicgrunt.the_bumblezone.configs.BzBeeAggressionConfigs;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.util.List;
import java.util.UUID;

public class ParalyzedEffect extends MobEffect {
    public ParalyzedEffect(MobEffectCategory type, int potionColor) {
        super(type, potionColor);
    }

    /**
     * Returns true if the potion has an instant effect instead of a continuous one (eg Harming)
     */
    @Override
    public boolean isInstantenous() {
        return false;
    }

    /**
     * checks if Potion effect is ready to be applied this tick.
     */
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration >= 1;
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int level) {
        // TODO: make jitter without phasing through blocks
        if(!livingEntity.isDeadOrDying()) {
//            livingEntity.setPos(
//                    livingEntity.getX() + (livingEntity.level.getGameTime() % 2 * 2 - 1) * 0.05D,
//                    livingEntity.getY(),
//                    livingEntity.getZ() + (livingEntity.level.getGameTime() % 2 * 2 - 1) * 0.05D);
        }
    }

    public static boolean isParalyzed(LivingEntity livingEntity) {
        return livingEntity.hasEffect(BzEffects.PARALYZED.get());
    }
}
