package com.telepathicgrunt.the_bumblezone.modules;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzDimensionConfigs;
import com.telepathicgrunt.the_bumblezone.modules.base.Module;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleSerializer;
import dev.architectury.injectables.targets.ArchitecturyTarget;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
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
            //TODO Change in a new Minecraft update, to keep consistency across versions.
            if ("forge".equals(ArchitecturyTarget.getCurrentTarget())) {
                return new ResourceLocation(Bumblezone.MODID, "entity_position_and_dimension");
            }
            return new ResourceLocation(Bumblezone.MODID, "entity_component");
        }

        @Override
        public void read(EntityPosAndDimModule module, CompoundTag tag) {
            //TODO Change in a new Minecraft update, to keep consistency across versions.
            if ("forge".equals(ArchitecturyTarget.getCurrentTarget())) {
                String namespace = tag.getString("PreviousDimensionNamespace");
                String path = tag.getString("PreviousDimensionPath");
                ResourceLocation storedDimension = path.trim().isEmpty() ? new ResourceLocation("minecraft", "overworld") : new ResourceLocation(namespace, path);

                Vec3 storedPositionNonBZ = null;
                if (tag.contains("NonBZ_X") && tag.contains("NonBZ_Y") && tag.contains("NonBZ_Z")) {
                    storedPositionNonBZ = new Vec3(tag.getDouble("NonBZ_X"), tag.getDouble("NonBZ_Y"), tag.getDouble("NonBZ_Z"));
                }

                module.setNonBZDim(storedDimension.getPath().isEmpty() ? new ResourceLocation("overworld") : storedDimension);
                module.setNonBZPos(storedPositionNonBZ);
            } else {
                module.setNonBZDim(new ResourceLocation(tag.getString("non_bz_dimensiontype_namespace"), tag.getString("non_bz_dmensiontype_path")));
                if (tag.contains("non_bz_position_x") && tag.contains("non_bz_position_y") && tag.contains("non_bz_position_z")) {
                    module.setNonBZPos(new Vec3(tag.getDouble("non_bz_position_x"), tag.getDouble("non_bz_position_y"), tag.getDouble("non_bz_position_z")));
                } else {
                    module.setNonBZPos(null);
                }
            }
        }

        @Override
        public void write(CompoundTag tag, EntityPosAndDimModule module) {
            //TODO Change in a new Minecraft update, to keep consistency across versions.
            if ("forge".equals(ArchitecturyTarget.getCurrentTarget())) {
                if (module.getNonBZDim() != null) {
                    tag.putString("PreviousDimensionNamespace", module.getNonBZDim().getNamespace());
                    tag.putString("PreviousDimensionPath", module.getNonBZDim().getPath());

                    if (module.getNonBZPos() != null) {
                        tag.putDouble("NonBZ_X", module.getNonBZPos().x());
                        tag.putDouble("NonBZ_Y", module.getNonBZPos().y());
                        tag.putDouble("NonBZ_Z", module.getNonBZPos().z());
                    }
                }
            } else {
                tag.putString("non_bz_dimensiontype_namespace", module.getNonBZDim().getNamespace());
                tag.putString("non_bz_dmensiontype_path", module.getNonBZDim().getPath());
                if (module.getNonBZPos() != null) {
                    tag.putDouble("non_bz_position_x", module.getNonBZPos().x());
                    tag.putDouble("non_bz_position_y", module.getNonBZPos().y());
                    tag.putDouble("non_bz_position_z", module.getNonBZPos().z());
                }
            }
        }
    }
}
