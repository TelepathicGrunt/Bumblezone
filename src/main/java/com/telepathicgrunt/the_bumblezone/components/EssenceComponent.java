package com.telepathicgrunt.the_bumblezone.components;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.nbt.CompoundTag;

public class EssenceComponent implements Component {

    public boolean isBeeEssenced = false;

    @Override
    public void writeToNbt(CompoundTag nbt) {
        nbt.putBoolean("is_bee_essenced", this.isBeeEssenced);
    }

    @Override
    public void readFromNbt(CompoundTag nbtTag) {
        this.isBeeEssenced = nbtTag.getBoolean("is_bee_essenced");
    }
}