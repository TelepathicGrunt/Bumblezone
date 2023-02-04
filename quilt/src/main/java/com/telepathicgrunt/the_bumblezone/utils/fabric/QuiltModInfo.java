package com.telepathicgrunt.the_bumblezone.utils.fabric;

import com.telepathicgrunt.the_bumblezone.platform.ModInfo;
import org.quiltmc.loader.api.ModMetadata;
import org.quiltmc.loader.api.Version;

public class QuiltModInfo implements ModInfo {

    private final ModMetadata info;

    public QuiltModInfo(ModMetadata info) {
        this.info = info;
    }

    @Override
    public String displayName() {
        return info.name();
    }

    @Override
    public String modid() {
        return info.id();
    }

    @Override
    public String version() {
        return info.version().raw();
    }

    @Override
    public int compare(String version) {
        try {
            return info.version().semantic().compareTo(Version.of(version).semantic());
        } catch (Exception e) {
            return 0;
        }
    }
}
