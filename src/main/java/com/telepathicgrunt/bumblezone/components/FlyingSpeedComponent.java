package com.telepathicgrunt.bumblezone.components;

import net.minecraft.nbt.CompoundTag;

public class FlyingSpeedComponent implements IFlyingSpeedComponent {
    private float originalFlyingSpeed = 0.02f;

    @Override
    public void setOriginalFlyingSpeed(float originalFlyingSpeed) {
        this.originalFlyingSpeed = originalFlyingSpeed;
    }

    @Override
    public float getOriginalFlyingSpeed() {
        return originalFlyingSpeed;
    }


    @Override
    public void readFromNbt(CompoundTag tag) {
        this.setOriginalFlyingSpeed(tag.getFloat("original_flying_speed"));
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        tag.putFloat("original_flying_speed", this.getOriginalFlyingSpeed());
    }
}