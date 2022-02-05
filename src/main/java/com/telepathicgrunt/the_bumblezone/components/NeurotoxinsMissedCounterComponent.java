package com.telepathicgrunt.the_bumblezone.components;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.nbt.CompoundTag;

public class NeurotoxinsMissedCounterComponent implements Component {
    private int missedParalysis = 0;

    public void setMissedCounter(int missedParalysis ) {
        this.missedParalysis  = missedParalysis ;
    }

    public int getMissedCounter() {
        return missedParalysis ;
    }


    public void readFromNbt(CompoundTag tag) {
        this.setMissedCounter(tag.getInt("missed_paralysis"));
    }

    public void writeToNbt(CompoundTag tag) {
        tag.putInt("missed_paralysis", this.getMissedCounter());
    }
}