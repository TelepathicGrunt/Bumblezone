package com.telepathicgrunt.the_bumblezone.capabilities;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;

public final class AttacherEntityMisc {

    // Every entity will hold their own instance of FSCapabilityProvider.
    // Their instances will hold a lazy that holds the cap.
    private static class FSCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

        public static final ResourceLocation IDENTIFIER = new ResourceLocation(Bumblezone.MODID, "entity_misc");
        private final EntityMisc backend = new EntityMisc();
        private final LazyOptional<EntityMisc> optionalData = LazyOptional.of(() -> backend);

        @Override
        public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
            return BzCapabilities.ENTITY_MISC.orEmpty(cap, this.optionalData);
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

    // attach only to server players
    public static void attach(final AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof ServerPlayer) {
            final FSCapabilityProvider provider = new FSCapabilityProvider();
            event.addCapability(FSCapabilityProvider.IDENTIFIER, provider);
        }
    }
}
