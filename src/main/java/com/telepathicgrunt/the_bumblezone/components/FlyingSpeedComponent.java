package com.telepathicgrunt.the_bumblezone.components;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.nbt.CompoundTag;

public class FlyingSpeedComponent implements Component {
    private float originalFlyingSpeed = 0.02f;

    public void setOriginalFlyingSpeed(float originalFlyingSpeed) {
        this.originalFlyingSpeed = originalFlyingSpeed;
    }

    public float getOriginalFlyingSpeed() {
        return originalFlyingSpeed;
    }


    public void readFromNbt(CompoundTag tag) {
        this.setOriginalFlyingSpeed(tag.getFloat("original_flying_speed"));
    }

    public void writeToNbt(CompoundTag tag) {
        tag.putFloat("original_flying_speed", this.getOriginalFlyingSpeed());
    }
}