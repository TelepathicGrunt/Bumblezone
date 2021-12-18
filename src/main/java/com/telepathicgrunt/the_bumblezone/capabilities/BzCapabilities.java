package com.telepathicgrunt.the_bumblezone.capabilities;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public final class BzCapabilities {
    public static final Capability<IFlyingSpeed> ORIGINAL_FLYING_SPEED_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<IEntityPosAndDim> ENTITY_POS_AND_DIM_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    private BzCapabilities() {}

    public static void setupCapabilities() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(BzCapabilities::registerCaps);

        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addGenericListener(Entity.class, AttacherFlyingSpeed::attach);
        forgeBus.addGenericListener(Entity.class, AttacherEntityPositionAndDimension::attach);
        forgeBus.addListener(BzCapabilities::copyOverCaps);
    }

    public static void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(IFlyingSpeed.class);
        event.register(IEntityPosAndDim.class);
    }

    public static void copyOverCaps(PlayerEvent.Clone event) {
        event.getOriginal().getCapability(BzCapabilities.ENTITY_POS_AND_DIM_CAPABILITY).invalidate();
        event.getOriginal().getCapability(BzCapabilities.ORIGINAL_FLYING_SPEED_CAPABILITY).invalidate();
    }
}
