package com.telepathicgrunt.the_bumblezone.modules;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzDimensionConfigs;
import com.telepathicgrunt.the_bumblezone.modules.base.Module;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class EntityPosAndDimModule implements Module<EntityPosAndDimModule> {
    public static final Codec<EntityPosAndDimModule> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            ResourceLocation.CODEC.fieldOf("nonBZDimension").orElse(new ResourceLocation(BzDimensionConfigs.defaultDimension)).forGetter(module -> module.nonBZDimension),
            Vec3.CODEC.optionalFieldOf("nonBZPosition").forGetter(module -> module.nonBZPosition)
    ).apply(instance, EntityPosAndDimModule::new));

    public static final ResourceLocation ID = new ResourceLocation(Bumblezone.MODID, "entity_pos_and_dim");
    private ResourceLocation nonBZDimension;
    private Optional<Vec3> nonBZPosition;

    public EntityPosAndDimModule(ResourceLocation nonBZDimension, Optional<Vec3> nonBZPosition) {
        this.nonBZDimension = nonBZDimension;
        this.nonBZPosition = nonBZPosition;
    }

    public EntityPosAndDimModule() {
        this.nonBZDimension = new ResourceLocation(BzDimensionConfigs.defaultDimension);
        this.nonBZPosition = Optional.empty();
    }

    public void setNonBZDim(ResourceLocation incomingDim) {
        if (incomingDim.equals(Bumblezone.MOD_DIMENSION_ID)) {
            this.nonBZDimension = new ResourceLocation(BzDimensionConfigs.defaultDimension);
            Bumblezone.LOGGER.error("Error: The non-bz dimension passed in to be stored was bz dimension. Please contact mod creator to let them know of this issue.");
        }
        else {
            nonBZDimension = incomingDim;
        }
    }

    public ResourceLocation getNonBZDim() {
        return nonBZDimension;
    }

    public void setNonBZPos(Optional<Vec3> incomingPos) {
        nonBZPosition = incomingPos;
    }

    public Optional<Vec3> getNonBZPos() {
        return nonBZPosition;
    }

    public boolean hasPos() {
        return nonBZPosition != null;
    }

    @Override
    public Codec<EntityPosAndDimModule> codec() {
        return CODEC;
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
