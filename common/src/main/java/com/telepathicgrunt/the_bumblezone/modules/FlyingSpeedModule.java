package com.telepathicgrunt.the_bumblezone.modules;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modules.base.Module;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class FlyingSpeedModule implements Module<FlyingSpeedModule> {

    public static final ModuleSerializer<FlyingSpeedModule> SERIALIZER = new Serializer();

    private float originalSpeed = 0.2f;

    public void setOriginalFlyingSpeed(float originalSpeed) {
        this.originalSpeed = originalSpeed;
    }

    public float getOriginalFlyingSpeed() {
        return originalSpeed;
    }

    @Override
    public ModuleSerializer<FlyingSpeedModule> serializer() {
        return SERIALIZER;
    }

    private static final class Serializer implements ModuleSerializer<FlyingSpeedModule> {

        @Override
        public Class<FlyingSpeedModule> moduleClass() {
            return FlyingSpeedModule.class;
        }

        @Override
        public ResourceLocation id() {
            return new ResourceLocation(Bumblezone.MODID, "original_flying_speed");
        }

        @Override
        public void read(FlyingSpeedModule module, CompoundTag tag) {
            module.setOriginalFlyingSpeed(tag.getFloat("original_flying_speed"));
        }

        @Override
        public void write(CompoundTag tag, FlyingSpeedModule module) {
            tag.putFloat("original_flying_speed", module.getOriginalFlyingSpeed());
        }

    }
}
