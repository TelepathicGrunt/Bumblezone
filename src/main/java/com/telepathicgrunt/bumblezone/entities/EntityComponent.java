package com.telepathicgrunt.bumblezone.entities;

import com.telepathicgrunt.bumblezone.Bumblezone;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.Level;

public class EntityComponent implements IEntityComponent {
    private boolean teleporting = false;
    private ResourceLocation nonBZDimensionType = net.minecraft.world.level.Level.OVERWORLD.location();
    public Vec3 nonBZPosition = null;

    @Override
    public boolean getIsTeleporting() {
        return this.teleporting;
    }
    @Override
    public void setIsTeleporting(boolean isTeleporting) {
        this.teleporting = isTeleporting;
    }


    @Override
    public void setNonBZPos(Vec3 incomingPos)
    {
        nonBZPosition = incomingPos;
    }
    @Override
    public Vec3 getNonBZPos()
    {
        return nonBZPosition;
    }


    @Override
    public ResourceLocation getNonBZDimension() {
        return this.nonBZDimensionType;
    }
    @Override
    public void setNonBZDimension(ResourceLocation nonBZDimension) {
        if (nonBZDimension.equals(Bumblezone.MOD_DIMENSION_ID)) {
            this.nonBZDimensionType = net.minecraft.world.level.Level.OVERWORLD.location();
            Bumblezone.LOGGER.log(Level.ERROR, "Error: The non-bz dimension passed in to be stored was bz dimension. Please contact mod creator to let them know of this issue.");
        } else {
            this.nonBZDimensionType = nonBZDimension;
        }

    }


    @Override
    public void readFromNbt(CompoundTag tag) {
        this.teleporting = tag.getBoolean("teleporting");
        this.nonBZDimensionType = new ResourceLocation(tag.getString("non_bz_dimensiontype_namespace"), tag.getString("non_bz_dmensiontype_path"));
    }
    @Override
    public void writeToNbt(CompoundTag tag) {
        tag.putBoolean("teleporting", this.teleporting);
        tag.putString("non_bz_dimensiontype_namespace", nonBZDimensionType.getNamespace());
        tag.putString("non_bz_dmensiontype_path", nonBZDimensionType.getPath());
    }

}