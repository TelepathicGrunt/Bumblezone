package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.rendering.beequeen.BeeQueenPose;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BzEntityDataSerializer {
    public static final DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZER = DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, Bumblezone.MODID);

    public static final RegistryObject<EntityDataSerializer<BeeQueenPose>> QUEEN_POSE_SERIALIZER = ENTITY_DATA_SERIALIZER.register("queen_pose_serializer", () -> EntityDataSerializer.simpleEnum(BeeQueenPose.class));
}
