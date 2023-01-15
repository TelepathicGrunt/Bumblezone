package com.telepathicgrunt.the_bumblezone.modules;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modules.base.Module;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class NeurotoxinsMissedCounterModule implements Module<NeurotoxinsMissedCounterModule> {

    public static final ModuleSerializer<NeurotoxinsMissedCounterModule> SERIALIZER = new Serializer();

    private int missedParalysis = 0;

    public int getMissedParalysis() {
        return missedParalysis;
    }

    public void setMissedParalysis(int missedParalysis) {
        this.missedParalysis = missedParalysis;
    }

    @Override
    public ModuleSerializer<NeurotoxinsMissedCounterModule> serializer() {
        return SERIALIZER;
    }

    private static final class Serializer implements ModuleSerializer<NeurotoxinsMissedCounterModule> {

        @Override
        public Class<NeurotoxinsMissedCounterModule> moduleClass() {
            return NeurotoxinsMissedCounterModule.class;
        }

        @Override
        public ResourceLocation id() {
            return new ResourceLocation(Bumblezone.MODID, "neurotoxins_missed_counter");
        }

        @Override
        public void read(NeurotoxinsMissedCounterModule module, CompoundTag tag) {
            module.setMissedParalysis(tag.getInt("missed_paralysis"));
        }

        @Override
        public void write(CompoundTag tag, NeurotoxinsMissedCounterModule module) {
            tag.putInt("missed_paralysis", module.getMissedParalysis());
        }
    }
}
