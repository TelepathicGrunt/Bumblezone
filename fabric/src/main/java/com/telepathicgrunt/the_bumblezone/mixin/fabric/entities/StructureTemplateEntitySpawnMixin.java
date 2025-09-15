package com.telepathicgrunt.the_bumblezone.mixin.fabric.entities;

import com.telepathicgrunt.the_bumblezone.events.entity.BzEntitySpawnEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StructureTemplate.class)
public class StructureTemplateEntitySpawnMixin {

    @Inject(method = "method_17917",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;finalizeSpawn(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/world/DifficultyInstance;Lnet/minecraft/world/entity/EntitySpawnReason;Lnet/minecraft/world/entity/SpawnGroupData;)Lnet/minecraft/world/entity/SpawnGroupData;"),
            cancellable = true)
    private static void bumblezone$onEntitySpawn(
            Rotation rotation,
            Mirror mirror,
            Vec3 vec3,
            boolean bl,
            ServerLevelAccessor serverLevel,
            Entity entity,
            CallbackInfo ci)
    {
        if (entity instanceof Mob mob && BzEntitySpawnEvent.EVENT.invoke(new BzEntitySpawnEvent(mob, serverLevel, mob.isBaby(), EntitySpawnReason.STRUCTURE))) {
            ci.cancel();
        }
    }
}
