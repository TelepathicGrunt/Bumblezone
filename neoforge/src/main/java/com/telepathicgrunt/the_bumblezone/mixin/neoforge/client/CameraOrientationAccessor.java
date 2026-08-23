package com.telepathicgrunt.the_bumblezone.mixin.neoforge.client;

import com.telepathicgrunt.the_bumblezone.client.rendering.cosmiccrystal.CosmicCrystalRenderer;
import net.neoforged.neoforge.client.event.ViewportEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ViewportEvent.ComputeCameraAngles.class)
public abstract class CameraOrientationAccessor implements CosmicCrystalRenderer.CameraOrientation {
    @Invoker("getYaw")
    @Override
    public abstract float bz$getYaw();

    @Invoker("setYaw")
    @Override
    public abstract void bz$setYaw(float yaw);

    @Invoker("getPitch")
    @Override
    public abstract float bz$getPitch();

    @Invoker("setPitch")
    @Override
    public abstract void bz$setPitch(float pitch);
}
