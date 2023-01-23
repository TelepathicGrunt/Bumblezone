package com.telepathicgrunt.the_bumblezone.utils.quilt;

import com.telepathicgrunt.the_bumblezone.platform.ModInfo;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.metadata.ModMetadata;

public class QuiltModInfo implements ModInfo {

    private final ModMetadata info;

    public QuiltModInfo(ModMetadata info) {
        this.info = info;
    }

    @Override
    public String displayName() {
        return info.getName();
    }

    @Override
    public String modid() {
        return info.getId();
    }

    @Override
    public String version() {
        return info.getVersion().getFriendlyString();
    }

    @Override
    public int compare(String version) {
        try {
            return info.getVersion().compareTo(Version.parse(version));
        } catch (Exception e) {
            return 0;
        }
    }
}
