package com.telepathicgrunt.the_bumblezone.mixin.fabric.client;

import com.telepathicgrunt.the_bumblezone.client.rendering.cosmiccrystal.CosmicCrystalRenderer;
import net.minecraft.client.Camera;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin implements CosmicCrystalRenderer.CameraOrientation {

    @Shadow
    private @Nullable Level level;

    @Shadow
    protected abstract void setRotation(float yRot, float xRot);

    @Inject(method = "alignWithEntity", at = @At("RETURN"))
    private void setupCameraState(float partialTicks, CallbackInfo ci) {
        CosmicCrystalRenderer.laserScreenShake(level, (Camera)(Object) this, partialTicks, this);
    }

    @Invoker("yRot")
    @Override
    public abstract float bz$getYaw();

    @Override
    public void bz$setYaw(float yaw) {
        this.setRotation(yaw, bz$getPitch());
    }

    @Invoker("xRot")
    @Override
    public abstract float bz$getPitch();

    @Override
    public void bz$setPitch(float pitch) {
        this.setRotation(bz$getYaw(), pitch);
    }
}
