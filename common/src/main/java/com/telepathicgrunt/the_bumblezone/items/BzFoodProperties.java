package com.telepathicgrunt.the_bumblezone.items;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectFloatImmutablePair;
import it.unimi.dsi.fastutil.objects.ObjectFloatPair;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class BzFoodProperties extends FoodProperties {

    private final List<ObjectFloatPair<Supplier<MobEffectInstance>>> effects;

    public BzFoodProperties(int i, float f, boolean bl, boolean bl2, boolean bl3, List<ObjectFloatPair<Supplier<MobEffectInstance>>> effects) {
        super(i, f, bl, bl2, bl3, new ArrayList<>());
        this.effects = effects;
    }

    @Override
    public List<Pair<MobEffectInstance, Float>> getEffects() {
        List<Pair<MobEffectInstance, Float>> effects = new ArrayList<>(super.getEffects());
        for (var effect : this.effects) {
            effects.add(new Pair<>(effect.first().get(), effect.secondFloat()));
        }
        return effects;
    }

    public static Builder builder(int hunger, float saturation) {
        return new Builder(hunger, saturation);
    }

    public static class Builder {
        private final List<ObjectFloatPair<Supplier<MobEffectInstance>>> effects = new ArrayList<>();
        private final int hunger;
        private final float saturation;
        private boolean meat;
        private boolean fast;
        private boolean alwaysEat;

        public Builder(int hunger, float saturation) {
            this.hunger = hunger;
            this.saturation = saturation;
        }

        public Builder meat() {
            this.meat = true;
            return this;
        }

        public Builder fast() {
            this.fast = true;
            return this;
        }

        public Builder alwaysEat() {
            this.alwaysEat = true;
            return this;
        }

        public Builder effect(Supplier<MobEffectInstance> effect, float chance) {
            this.effects.add(new ObjectFloatImmutablePair<>(effect, chance));
            return this;
        }

        public BzFoodProperties build() {
            return new BzFoodProperties(hunger, saturation, meat, fast, alwaysEat, effects);
        }
    }
}
