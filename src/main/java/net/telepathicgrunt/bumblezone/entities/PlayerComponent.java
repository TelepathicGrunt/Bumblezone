package net.telepathicgrunt.bumblezone.entities;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensionType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.dimension.DimensionType;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.dimension.BzDimensionType;
import org.apache.logging.log4j.Level;

public class PlayerComponent implements IPlayerComponent {
    private boolean teleporting = false;
    private DimensionType nonBZDimensionType = DimensionType.OVERWORLD;
    public Vec3d nonBZPosition = null;

    @Override
    public boolean getIsTeleporting() {
        return this.teleporting;
    }
    @Override
    public void setIsTeleporting(boolean isTeleporting) {
        this.teleporting = isTeleporting;
    }


    @Override
    public void setNonBZPos(Vec3d incomingPos)
    {
        nonBZPosition = incomingPos;
    }
    @Override
    public Vec3d getNonBZPos()
    {
        return nonBZPosition;
    }


    @Override
    public DimensionType getNonBZDimension() {
        return this.nonBZDimensionType;
    }
    @Override
    public void setNonBZDimension(DimensionType nonBZDimension) {
        if (nonBZDimension == BzDimensionType.BUMBLEZONE_TYPE) {
            this.nonBZDimensionType = DimensionType.OVERWORLD;
            Bumblezone.LOGGER.log(Level.ERROR, "Error: The non-bz dimension passed in to be stored was bz dimension. Please contact mod creator to let them know of this issue.");
        } else {
            this.nonBZDimensionType = nonBZDimension;
        }

    }


    @Override
    public void fromTag(CompoundTag tag) {
        this.teleporting = tag.getBoolean("teleporting");
        this.nonBZDimensionType = FabricDimensionType.byId(
                new Identifier(tag.getString("non_bz_dimensiontype_namespace"), tag.getString("non_bz_dmensiontype_path")));
    }
    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag.putBoolean("teleporting", this.teleporting);
        tag.putString("non_bz_dimensiontype_namespace", FabricDimensionType.getId(nonBZDimensionType).getNamespace());
        tag.putString("non_bz_dmensiontype_path", FabricDimensionType.getId(nonBZDimensionType).getPath());

        return tag;
    }

}