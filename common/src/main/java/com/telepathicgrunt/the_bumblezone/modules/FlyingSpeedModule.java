package com.telepathicgrunt.the_bumblezone.modules;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modules.base.Module;
import net.minecraft.resources.ResourceLocation;

public class FlyingSpeedModule implements Module<FlyingSpeedModule> {
    public static final Codec<FlyingSpeedModule> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.FLOAT.fieldOf("originalFlyingSpeed").orElse(0.2F).forGetter(module -> module.originalFlyingSpeed)
    ).apply(instance, FlyingSpeedModule::new));

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "original_flying_speed");
    private float originalFlyingSpeed;

    public FlyingSpeedModule(float originalFlyingSpeed) {
        this.originalFlyingSpeed = originalFlyingSpeed;
    }

    public FlyingSpeedModule() {
        this.originalFlyingSpeed = 0.2F;
    }

    public void setOriginalFlyingSpeed(float originalSpeed) {
        this.originalFlyingSpeed = originalSpeed;
    }

    public float getOriginalFlyingSpeed() {
        return originalFlyingSpeed;
    }

    @Override
    public Codec<FlyingSpeedModule> codec() {
        return CODEC;
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
