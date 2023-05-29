package com.telepathicgrunt.the_bumblezone.modules;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzDimensionConfigs;
import com.telepathicgrunt.the_bumblezone.modules.base.Module;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public class EntityPosAndDimModule implements Module<EntityPosAndDimModule> {

    public static final ModuleSerializer<EntityPosAndDimModule> SERIALIZER = new Serializer();

    private ResourceLocation nonBZDimension = new ResourceLocation("overworld");
    private Vec3 nonBZPosition = null;

    public void setNonBZDim(ResourceLocation incomingDim) {
        if (incomingDim.equals(Bumblezone.MOD_DIMENSION_ID)) {
            this.nonBZDimension = new ResourceLocation(BzDimensionConfigs.defaultDimension);
            Bumblezone.LOGGER.error("Error: The non-bz dimension passed in to be stored was bz dimension. Please contact mod creator to let them know of this issue.");
        }
        else {
            nonBZDimension = incomingDim;
        }
    }

    public ResourceLocation getNonBZDim() {
        return nonBZDimension;
    }

    public void setNonBZPos(Vec3 incomingPos) {
        nonBZPosition = incomingPos;
    }

    public Vec3 getNonBZPos() {
        return nonBZPosition;
    }

    public boolean hasPos() {
        return nonBZPosition != null;
    }

    @Override
    public ModuleSerializer<EntityPosAndDimModule> serializer() {
        return SERIALIZER;
    }

    private static final class Serializer implements ModuleSerializer<EntityPosAndDimModule> {

        @Override
        public Class<EntityPosAndDimModule> moduleClass() {
            return EntityPosAndDimModule.class;
        }

        @Override
        public ResourceLocation id() {
            return new ResourceLocation(Bumblezone.MODID, "entity_dim_component");
        }

        @Override
        public void read(EntityPosAndDimModule module, CompoundTag tag) {
            module.setNonBZDim(new ResourceLocation(tag.getString("non_bz_dimensiontype_namespace"), tag.getString("non_bz_dmensiontype_path")));
            if (tag.contains("non_bz_position_x") && tag.contains("non_bz_position_y") && tag.contains("non_bz_position_z")) {
                module.setNonBZPos(new Vec3(tag.getDouble("non_bz_position_x"), tag.getDouble("non_bz_position_y"), tag.getDouble("non_bz_position_z")));
            }
            else {
                module.setNonBZPos(null);
            }
        }

        @Override
        public void write(CompoundTag tag, EntityPosAndDimModule module) {
            tag.putString("non_bz_dimensiontype_namespace", module.getNonBZDim().getNamespace());
            tag.putString("non_bz_dmensiontype_path", module.getNonBZDim().getPath());
            if (module.getNonBZPos() != null) {
                tag.putDouble("non_bz_position_x", module.getNonBZPos().x());
                tag.putDouble("non_bz_position_y", module.getNonBZPos().y());
                tag.putDouble("non_bz_position_z", module.getNonBZPos().z());
            }
        }

        @Override
        public void onPlayerCopy(EntityPosAndDimModule oldModule, EntityPosAndDimModule thisModule, ServerPlayer player, boolean isPersistent) {
            ModuleSerializer.super.onPlayerCopy(oldModule, thisModule, player, true);
        }
    }
}
