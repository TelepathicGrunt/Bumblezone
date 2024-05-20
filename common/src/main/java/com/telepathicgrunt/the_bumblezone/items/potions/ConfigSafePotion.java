package com.telepathicgrunt.the_bumblezone.items.potions;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class ConfigSafePotion extends Potion {
    private final Supplier<List<MobEffectInstance>> effectListCreator;
    private List<MobEffectInstance> cachedEffectList = null;

    public ConfigSafePotion(Supplier<List<MobEffectInstance>> mobEffectInstancesSupplier) {
        this(null, mobEffectInstancesSupplier);
    }

    public ConfigSafePotion(@Nullable String string, Supplier<List<MobEffectInstance>> mobEffectInstancesSupplier) {
        super(string);
        this.effectListCreator = mobEffectInstancesSupplier;
    }

    @Override
    public List<MobEffectInstance> getEffects() {
        return getCachedEffectList();
    }

    @Override
    public boolean hasInstantEffects() {
        if (!getCachedEffectList().isEmpty()) {
            for (MobEffectInstance mobEffectInstance : getCachedEffectList()) {
                if (!mobEffectInstance.getEffect().value().isInstantenous()) {
                    continue;
                }
                return true;
            }
        }
        return false;
    }

    public List<MobEffectInstance> getCachedEffectList() {
        if (cachedEffectList == null) {
            cachedEffectList = effectListCreator.get();
        }
        return cachedEffectList;
    }

    public void clearCachedEffectList() {
        cachedEffectList = null;
    }
}
