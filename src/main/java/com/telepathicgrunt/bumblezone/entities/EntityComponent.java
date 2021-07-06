package com.telepathicgrunt.bumblezone.entities;

import com.telepathicgrunt.bumblezone.Bumblezone;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

public class EntityComponent implements IEntityComponent {
    private boolean teleporting = false;
    private Identifier nonBZDimensionType = World.OVERWORLD.getValue();
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
    public Identifier getNonBZDimension() {
        return this.nonBZDimensionType;
    }
    @Override
    public void setNonBZDimension(Identifier nonBZDimension) {
        if (nonBZDimension.equals(Bumblezone.MOD_DIMENSION_ID)) {
            this.nonBZDimensionType = World.OVERWORLD.getValue();
            Bumblezone.LOGGER.log(Level.ERROR, "Error: The non-bz dimension passed in to be stored was bz dimension. Please contact mod creator to let them know of this issue.");
        } else {
            this.nonBZDimensionType = nonBZDimension;
        }

    }


    @Override
    public void readFromNbt(NbtCompound tag) {
        this.teleporting = tag.getBoolean("teleporting");
        this.nonBZDimensionType = new Identifier(tag.getString("non_bz_dimensiontype_namespace"), tag.getString("non_bz_dmensiontype_path"));
    }
    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putBoolean("teleporting", this.teleporting);
        tag.putString("non_bz_dimensiontype_namespace", nonBZDimensionType.getNamespace());
        tag.putString("non_bz_dmensiontype_path", nonBZDimensionType.getPath());
    }

}