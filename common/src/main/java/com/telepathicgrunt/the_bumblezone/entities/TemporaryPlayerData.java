package com.telepathicgrunt.the_bumblezone.entities;

public interface TemporaryPlayerData {

    default int theBumblezone$PlayerTickOffGroundInHeavyAir() { return 0; }

    default boolean theBumblezone$PlayerInHeavyAir() { return false; }
}
