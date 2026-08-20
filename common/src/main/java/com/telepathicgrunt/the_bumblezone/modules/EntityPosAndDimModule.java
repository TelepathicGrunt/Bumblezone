package com.telepathicgrunt.the_bumblezone.modules;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzDimensionConfigs;
import com.telepathicgrunt.the_bumblezone.modules.base.Module;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class EntityPosAndDimModule implements Module<EntityPosAndDimModule> {
    public static final MapCodec<EntityPosAndDimModule> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            Identifier.CODEC.fieldOf("nonBZDimension").orElse(Identifier.tryParse(BzDimensionConfigs.defaultDimension)).forGetter(module -> module.nonBZDimension),
            Vec3.CODEC.optionalFieldOf("nonBZPosition").forGetter(module -> module.nonBZPosition)
    ).apply(instance, EntityPosAndDimModule::new));

    public static final Identifier ID = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "entity_pos_and_dim");
    private Identifier nonBZDimension;
    private Optional<Vec3> nonBZPosition;

    public EntityPosAndDimModule(Identifier nonBZDimension, Optional<Vec3> nonBZPosition) {
        this.nonBZDimension = nonBZDimension;
        this.nonBZPosition = nonBZPosition;
    }

    public EntityPosAndDimModule() {
        this.nonBZDimension = Identifier.tryParse(BzDimensionConfigs.defaultDimension);
        this.nonBZPosition = Optional.empty();
    }

    public void setNonBZDim(Identifier incomingDim) {
        if (incomingDim.equals(Bumblezone.MOD_DIMENSION_ID)) {
            this.nonBZDimension = Identifier.tryParse(BzDimensionConfigs.defaultDimension);
            Bumblezone.LOGGER.error("Error: The non-bz dimension passed in to be stored was bz dimension. Please contact mod creator to let them know of this issue.");
        }
        else {
            nonBZDimension = incomingDim;
        }
    }

    public Identifier getNonBZDim() {
        return nonBZDimension;
    }

    public void setNonBZPos(Optional<Vec3> incomingPos) {
        nonBZPosition = incomingPos;
    }

    public Optional<Vec3> getNonBZPos() {
        return nonBZPosition;
    }

    public boolean hasPos() {
        return nonBZPosition.isPresent();
    }

    @Override
    public MapCodec<EntityPosAndDimModule> codec() {
        return CODEC;
    }

    @Override
    public Identifier id() {
        return ID;
    }
}
