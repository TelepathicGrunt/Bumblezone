package com.telepathicgrunt.the_bumblezone.modules;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modules.base.Module;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class LivingEntityDataModule implements Module<LivingEntityDataModule> {

    public static final ModuleSerializer<LivingEntityDataModule> SERIALIZER = new Serializer();

    private int missedParalysis = 0;

    public int getMissedParalysis() {
        return missedParalysis;
    }

    public void setMissedParalysis(int missedParalysis) {
        this.missedParalysis = missedParalysis;
    }

    @Override
    public ModuleSerializer<LivingEntityDataModule> serializer() {
        return SERIALIZER;
    }

    private static final class Serializer implements ModuleSerializer<LivingEntityDataModule> {

        @Override
        public Class<LivingEntityDataModule> moduleClass() {
            return LivingEntityDataModule.class;
        }

        @Override
        public ResourceLocation id() {
            return new ResourceLocation(Bumblezone.MODID, "living_entity_data");
        }

        @Override
        public void read(LivingEntityDataModule module, CompoundTag tag) {
            module.setMissedParalysis(tag.getInt("missed_paralysis"));
        }

        @Override
        public void write(CompoundTag tag, LivingEntityDataModule module) {
            tag.putInt("missed_paralysis", module.getMissedParalysis());
        }
    }
}
