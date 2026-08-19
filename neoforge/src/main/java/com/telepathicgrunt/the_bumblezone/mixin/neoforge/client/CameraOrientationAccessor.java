package com.telepathicgrunt.the_bumblezone.mixin.neoforge.client;

import com.telepathicgrunt.the_bumblezone.client.rendering.cosmiccrystal.CosmicCrystalRenderer;
import net.neoforged.neoforge.client.event.ViewportEvent;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ViewportEvent.ComputeCameraAngles.class)
public interface CameraOrientationAccessor extends CosmicCrystalRenderer.CameraOrientation {
}
