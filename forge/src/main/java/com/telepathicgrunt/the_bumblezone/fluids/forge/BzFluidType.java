package com.telepathicgrunt.the_bumblezone.fluids.forge;

import com.telepathicgrunt.the_bumblezone.fluids.base.FluidProperties;
import net.minecraft.Util;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundAction;
import net.minecraftforge.fluids.FluidType;

import java.util.function.Consumer;

public class BzFluidType extends FluidType {

    private final FluidProperties fluidProperties;

    private BzFluidType(FluidProperties fluidProperties, Properties properties) {
        super(properties);
        this.fluidProperties = fluidProperties;
    }

    public static BzFluidType of(FluidProperties fluidProperties) {
        var properties = Properties.create();
        properties.descriptionId(Util.makeDescriptionId("fluid_type", fluidProperties.id()));
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
        properties.supportsBoating(fluidProperties.supportsBoating());
        properties.pathType(fluidProperties.pathType());
        properties.rarity(fluidProperties.rarity());
        properties.temperature(fluidProperties.temperature());
        properties.viscosity(fluidProperties.viscosity());
        fluidProperties.sounds().forEach((name, sound) -> properties.sound(SoundAction.get(name), sound.get()));
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