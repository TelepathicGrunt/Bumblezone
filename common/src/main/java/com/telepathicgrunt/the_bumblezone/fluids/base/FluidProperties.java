package com.telepathicgrunt.the_bumblezone.fluids.base;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public record FluidProperties(
        ResourceLocation id,
        double motionScale,
        boolean canPushEntity,
        boolean canSwim,
        boolean canDrown,
        float fallDistanceModifier,
        boolean canExtinguish,
        boolean canConvertToSource,
        boolean supportsBoating,
        BlockPathTypes pathType,
        BlockPathTypes adjacentPathType,
        boolean canHydrate,
        int lightLevel,
        int density,
        int temperature,
        int viscosity,
        Rarity rarity,
        Map<String, Supplier<SoundEvent>> sounds,
        int tickDelay,
        int slopeFindDistance,
        int dropOff,
        float explosionResistance
) {


    public static class Builder {
        final String id;
        double motionScale = 0.014;
        boolean canPushEntity = true;
        boolean canSwim = false;
        boolean canDrown = true;
        float fallDistanceModifier = 0.5f;
        boolean canExtinguish = false;
        boolean canConvertToSource = true;
        boolean supportsBoating = false;
        BlockPathTypes pathType = BlockPathTypes.WATER;
        BlockPathTypes adjacentPathType = BlockPathTypes.WATER_BORDER;
        boolean canHydrate = true;
        int lightLevel = 0;
        int density = 1000;
        int temperature = 300;
        int viscosity = 1000;
        Rarity rarity = Rarity.COMMON;
        final Map<String, Supplier<SoundEvent>> sounds = new HashMap<>();
        int tickDelay = 5;
        int slopeFindDistance = 4;
        int dropOff = 1;
        float explosionResistance = 100.0f;

        public Builder(String id) {
            this.id = id;
        }

        public Builder motionScale(double motionScale) {
            this.motionScale = motionScale;
            return this;
        }

        public Builder canPushEntity(boolean canPushEntity) {
            this.canPushEntity = canPushEntity;
            return this;
        }

        public Builder canSwim(boolean canSwim) {
            this.canSwim = canSwim;
            return this;
        }

        public Builder canDrown(boolean canDrown) {
            this.canDrown = canDrown;
            return this;
        }

        public Builder fallDistanceModifier(float fallDistanceModifier) {
            this.fallDistanceModifier = fallDistanceModifier;
            return this;
        }

        public Builder canExtinguish(boolean canExtinguish) {
            this.canExtinguish = canExtinguish;
            return this;
        }

        public Builder canConvertToSource(boolean canConvertToSource) {
            this.canConvertToSource = canConvertToSource;
            return this;
        }

        public Builder supportsBoating(boolean supportsBoating) {
            this.supportsBoating = supportsBoating;
            return this;
        }

        public Builder pathType(BlockPathTypes pathType) {
            this.pathType = pathType;
            return this;
        }

        public Builder adjacentPathType(BlockPathTypes adjacentPathType) {
            this.adjacentPathType = adjacentPathType;
            return this;
        }

        public Builder canHydrate(boolean canHydrate) {
            this.canHydrate = canHydrate;
            return this;
        }

        public Builder lightLevel(int lightLevel) {
            this.lightLevel = lightLevel;
            return this;
        }

        public Builder density(int density) {
            this.density = density;
            return this;
        }

        public Builder temperature(int temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder viscosity(int viscosity) {
            this.viscosity = viscosity;
            return this;
        }

        public Builder rarity(Rarity rarity) {
            this.rarity = rarity;
            return this;
        }

        public Builder sound(String name, Supplier<SoundEvent> sound) {
            this.sounds.put(name, sound);
            return this;
        }

        public Builder tickDelay(int tickDelay) {
            this.tickDelay = tickDelay;
            return this;
        }

        public Builder slopeFindDistance(int slopeFindDistance) {
            this.slopeFindDistance = slopeFindDistance;
            return this;
        }

        public Builder dropOff(int dropOff) {
            this.dropOff = dropOff;
            return this;
        }

        public Builder explosionResistance(float explosionResistance) {
            this.explosionResistance = explosionResistance;
            return this;
        }

        @ApiStatus.Internal
        public FluidProperties build(String modid) {
            return new FluidProperties(
                    new ResourceLocation(modid, id),
                    motionScale,
                    canPushEntity,
                    canSwim,
                    canDrown,
                    fallDistanceModifier,
                    canExtinguish,
                    canConvertToSource,
                    supportsBoating,
                    pathType,
                    adjacentPathType,
                    canHydrate,
                    lightLevel,
                    density,
                    temperature,
                    viscosity,
                    rarity,
                    sounds,
                    tickDelay,
                    slopeFindDistance,
                    dropOff,
                    explosionResistance
            );
        }
    }
}