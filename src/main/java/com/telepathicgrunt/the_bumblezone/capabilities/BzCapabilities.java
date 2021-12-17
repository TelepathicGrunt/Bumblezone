package com.telepathicgrunt.the_bumblezone.capabilities;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public final class BzCapabilities {
    public static final Capability<IFlyingSpeed> ORIGINAL_FLYING_SPEED_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<IEntityPosAndDim> ENTITY_POS_AND_DIM_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    private BzCapabilities() {}

    public static void setupCapabilities() {
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(BzCapabilities::registerCaps);
        forgeBus.addGenericListener(Entity.class, AttacherFlyingSpeed::attach);
        forgeBus.addGenericListener(Entity.class, AttacherEntityPositionAndDimension::attach);
        forgeBus.addListener(BzCapabilities::copyOverCaps);
    }

    public static void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(IFlyingSpeed.class);
        event.register(IEntityPosAndDim.class);
    }

    public static void copyOverCaps(PlayerEvent.Clone event) {
        IEntityPosAndDim originalEPADCapability = event.getOriginal().getCapability(BzCapabilities.ENTITY_POS_AND_DIM_CAPABILITY).orElseThrow(RuntimeException::new);
        IEntityPosAndDim newEPADCapability = event.getPlayer().getCapability(BzCapabilities.ENTITY_POS_AND_DIM_CAPABILITY).orElseThrow(RuntimeException::new);
        newEPADCapability.setNonBZDim(originalEPADCapability.getNonBZDim());
        newEPADCapability.setNonBZPos(originalEPADCapability.getNonBZPos());

        IFlyingSpeed originalFSCapability = event.getOriginal().getCapability(BzCapabilities.ORIGINAL_FLYING_SPEED_CAPABILITY).orElseThrow(RuntimeException::new);
        IFlyingSpeed newFSCapability = event.getPlayer().getCapability(BzCapabilities.ORIGINAL_FLYING_SPEED_CAPABILITY).orElseThrow(RuntimeException::new);
        newFSCapability.setOriginalFlyingSpeed(originalFSCapability.getOriginalFlyingSpeed());
    }
}
