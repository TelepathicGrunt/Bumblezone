package com.telepathicgrunt.the_bumblezone.modules;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modules.base.Module;
import net.minecraft.resources.Identifier;

public class FlyingSpeedModule implements Module<FlyingSpeedModule> {
    public static final MapCodec<FlyingSpeedModule> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            Codec.FLOAT.fieldOf("originalFlyingSpeed").orElse(0.2F).forGetter(module -> module.originalFlyingSpeed)
    ).apply(instance, FlyingSpeedModule::new));

    public static final Identifier ID = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "original_flying_speed");
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
    public MapCodec<FlyingSpeedModule> codec() {
        return CODEC;
    }

    @Override
    public Identifier id() {
        return ID;
    }
}
