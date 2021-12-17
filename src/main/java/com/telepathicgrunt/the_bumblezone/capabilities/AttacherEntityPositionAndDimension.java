package com.telepathicgrunt.the_bumblezone.capabilities;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;

public final class AttacherEntityPositionAndDimension {

    private static class EPADCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

        public static final ResourceLocation IDENTIFIER = new ResourceLocation(Bumblezone.MODID, "entity_position_and_dimension");

        private final IEntityPosAndDim backend = new EntityPositionAndDimension();
        private final LazyOptional<IEntityPosAndDim> optionalData = LazyOptional.of(() -> backend);

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            return BzCapabilities.ENTITY_POS_AND_DIM_CAPABILITY.orEmpty(cap, this.optionalData);
        }

        void invalidate() {
            this.optionalData.invalidate();
        }

        @Override
        public CompoundTag serializeNBT() {
            return this.backend.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            this.backend.deserializeNBT(nbt);
        }
    }

    public static void attach(final AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof LivingEntity) {
            final EPADCapabilityProvider provider = new EPADCapabilityProvider();
            event.addCapability(EPADCapabilityProvider.IDENTIFIER, provider);
            event.addListener(provider::invalidate);
        }
    }
}
