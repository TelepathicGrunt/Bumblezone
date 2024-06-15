package com.telepathicgrunt.the_bumblezone.modules;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modules.base.Module;
import net.minecraft.resources.ResourceLocation;

public class LivingEntityDataModule implements Module<LivingEntityDataModule> {
    public static final Codec<LivingEntityDataModule> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.INT.fieldOf("missedParalysis").forGetter(module -> module.missedParalysis)
    ).apply(instance, LivingEntityDataModule::new));

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "living_entity_data");
    private int missedParalysis;

    public LivingEntityDataModule(int missedParalysis) {
        this.missedParalysis = missedParalysis;
    }

    public LivingEntityDataModule() {
        this.missedParalysis = 0;
    }

    public int getMissedParalysis() {
        return missedParalysis;
    }

    public void setMissedParalysis(int missedParalysis) {
        this.missedParalysis = missedParalysis;
    }

    @Override
    public Codec<LivingEntityDataModule> codec() {
        return CODEC;
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
