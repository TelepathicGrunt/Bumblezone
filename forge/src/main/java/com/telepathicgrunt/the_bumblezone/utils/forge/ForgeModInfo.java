package com.telepathicgrunt.the_bumblezone.utils.forge;

import com.telepathicgrunt.the_bumblezone.platform.ModInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

public class ForgeModInfo implements ModInfo {

    private final IModInfo info;
    private final ArtifactVersion version;

    public ForgeModInfo(IModInfo info, boolean qualifierIsVersion) {
        this.info = info;
        // some people do 1.16.4-0.5.0.5 and we have to parse the second half instead.
        // if someone does 0.5.0.5-1.16.4, well, we are screwed lmao. WE HAVE STANDARDS FOR A REASON PEOPLE! lmao
        this.version = qualifierIsVersion && info.getVersion().getQualifier() != null ?
                new DefaultArtifactVersion(info.getVersion().getQualifier()) :
                info.getVersion();
    }

    @Override
    public String displayName() {
        return this.info.getDisplayName();
    }

    @Override
    public String modid() {
        return this.info.getModId();
    }

    @Override
    public String version() {
        return this.version.toString();
    }

    @Override
    public int compare(String version) {
        return this.version.compareTo(new DefaultArtifactVersion(version));
    }
}
