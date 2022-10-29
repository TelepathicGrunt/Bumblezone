package com.telepathicgrunt.the_bumblezone.components;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzConfig;
import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.Level;

public class EntityComponent implements Component {
    private boolean teleporting = false;
    private ResourceLocation nonBZDimensionType = new ResourceLocation(BzConfig.defaultDimension);
    public Vec3 nonBZPosition = null;

    public void setNonBZPos(Vec3 incomingPos) {
        nonBZPosition = incomingPos;
    }

    public Vec3 getNonBZPos() {
        return nonBZPosition;
    }

    public ResourceLocation getNonBZDimension() {
        return this.nonBZDimensionType;
    }

    public void setNonBZDimension(ResourceLocation nonBZDimension) {
        if (nonBZDimension.equals(Bumblezone.MOD_DIMENSION_ID)) {
            this.nonBZDimensionType = new ResourceLocation(BzConfig.defaultDimension);
            this.nonBZPosition = null;
            Bumblezone.LOGGER.log(Level.ERROR, "Error: The non-bz dimension passed in to be stored was bz dimension. Please contact mod creator to let them know of this issue.");
        }
        else {
            this.nonBZDimensionType = nonBZDimension;
        }
    }

    public void readFromNbt(CompoundTag tag) {
        this.teleporting = tag.getBoolean("teleporting");
        this.nonBZDimensionType = new ResourceLocation(tag.getString("non_bz_dimensiontype_namespace"), tag.getString("non_bz_dmensiontype_path"));
        if (tag.contains("non_bz_position_x") &&
                tag.contains("non_bz_position_y") &&
                tag.contains("non_bz_position_z"))
        {
            this.nonBZPosition = new Vec3(
                    tag.getDouble("non_bz_position_x"),
                    tag.getDouble("non_bz_position_y"),
                    tag.getDouble("non_bz_position_z")
            );
        }
        else {
            this.nonBZPosition = null;
        }
    }

    public void writeToNbt(CompoundTag tag) {
        tag.putBoolean("teleporting", this.teleporting);
        tag.putString("non_bz_dimensiontype_namespace", nonBZDimensionType.getNamespace());
        tag.putString("non_bz_dmensiontype_path", nonBZDimensionType.getPath());
        if (this.nonBZPosition != null) {
            tag.putDouble("non_bz_position_x", this.nonBZPosition.x());
            tag.putDouble("non_bz_position_y", this.nonBZPosition.y());
            tag.putDouble("non_bz_position_z", this.nonBZPosition.z());
        }
    }
}