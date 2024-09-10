package com.telepathicgrunt.the_bumblezone.entities;

public interface TemporaryPlayerData {

    default int bumblezonePlayerTickOffGroundInHeavyAir() { return 0; }

    default boolean bumblezonePlayerInHeavyAir() { return false; }
}
