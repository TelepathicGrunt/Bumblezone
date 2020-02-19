package net.telepathicgrunt.bumblezone.entities;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensionType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.world.dimension.DimensionType;

public class PlayerComponent implements IPlayerComponent  {
    private boolean teleporting = false;
    private DimensionType nonBZDimensionType = DimensionType.OVERWORLD;

    @Override
    public boolean getIsTeleporting() {
        return this.teleporting;
    }

    @Override
    public void setIsTeleporting(boolean isTeleporting) {
        this.teleporting = isTeleporting;
    }

    @Override
    public DimensionType getNonBZDimension() {
        return this.nonBZDimensionType;
    }

    @Override
    public void setNonBZDimension(DimensionType nonBZDimension) {
        this.nonBZDimensionType = nonBZDimension;
    }

    @Override public void fromTag(CompoundTag tag)
    {
        this.teleporting = tag.getBoolean("teleporting");
        this.nonBZDimensionType = FabricDimensionType.byId(
                new Identifier(tag.getString("non_bz_dimensiontype_namespace"), tag.getString("non_bz_dmensiontype_path")));
    }

    @Override public CompoundTag toTag(CompoundTag tag)
    {
        tag.putBoolean("teleporting", this.teleporting);
        tag.putString("non_bz_dimensiontype_namespace", FabricDimensionType.getId(nonBZDimensionType).getNamespace());
        tag.putString("non_bz_dmensiontype_path", FabricDimensionType.getId(nonBZDimensionType).getPath());

        return tag;
    }

}