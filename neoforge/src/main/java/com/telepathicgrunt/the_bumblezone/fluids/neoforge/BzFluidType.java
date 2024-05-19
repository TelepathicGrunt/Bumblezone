package com.telepathicgrunt.the_bumblezone.fluids.neoforge;

import com.teamresourceful.resourcefullib.common.fluid.data.FluidProperties;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.common.SoundAction;
import net.neoforged.neoforge.fluids.FluidType;

import java.util.function.Consumer;

public class BzFluidType extends FluidType {

    private final FluidProperties fluidProperties;

    private BzFluidType(FluidProperties fluidProperties, Properties properties) {
        super(properties);
        this.fluidProperties = fluidProperties;
    }

    public static BzFluidType of(FluidProperties fluidProperties) {
        var properties = Properties.create();
        properties.adjacentPathType(fluidProperties.adjacentPathType());
        properties.canConvertToSource(fluidProperties.canConvertToSource());
        properties.canDrown(fluidProperties.canDrown());
        properties.canExtinguish(fluidProperties.canExtinguish());
        properties.canHydrate(fluidProperties.canHydrate());
        properties.canPushEntity(fluidProperties.canPushEntity());
        properties.canSwim(fluidProperties.canSwim());
        properties.density(fluidProperties.density());
        properties.fallDistanceModifier(fluidProperties.fallDistanceModifier());
        properties.lightLevel(fluidProperties.lightLevel());
        properties.motionScale(fluidProperties.motionScale());
        properties.supportsBoating(fluidProperties.supportsBloating());
        properties.pathType(fluidProperties.pathType());
        properties.rarity(fluidProperties.rarity());
        properties.temperature(fluidProperties.temperature());
        properties.viscosity(fluidProperties.viscosity());
        fluidProperties.sounds().sounds().forEach((name, sound) -> properties.sound(SoundAction.get(name), sound));
        return new BzFluidType(fluidProperties, properties);
    }

    public FluidProperties properties() {
        return fluidProperties;
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new BzClientFluidExtension(this));
    }
}