package com.telepathicgrunt.the_bumblezone.platform;

public interface ModInfo {

    String displayName();

    String modid();

    String version();

    int compare(String version);
}
