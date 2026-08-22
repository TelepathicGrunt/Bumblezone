package com.telepathicgrunt.the_bumblezone.commands.debug;

import com.mojang.brigadier.Command;
import com.telepathicgrunt.the_bumblezone.entities.living.CosmicCrystalState;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzRegisterCommandsEvent;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.services.PlatformService;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;

public class DebugSpawnLaserCommand {

    public static void createCommand(BzRegisterCommandsEvent commandEvent) {
        if(!PlatformService.INSTANCE.isDevEnvironment()) {
            return;
        }

        commandEvent.dispatcher().register(Commands.literal("bumblezone_debug_spawn_laser").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)).executes(ctx -> {
            var source = ctx.getSource();
            var sourceEntity = source.getEntity();

            var entity = BzEntities.COSMIC_CRYSTAL_ENTITY.get().create(
                    source.getLevel(),
                    crystal -> {
                        crystal.setPos(source.getPosition());
                        crystal.setNoAi(true);
                        crystal.setInvulnerable(true);
                        if(sourceEntity instanceof LivingEntity livingEntity) {
                            crystal.setTarget(livingEntity);
                        }

                        crystal.setCosmicCrystalState(CosmicCrystalState.TRACKING_LASER);
                    },
                    BlockPos.containing(source.getPosition()),
                    EntitySpawnReason.COMMAND,
                    false,
                    false
                    );
            if(entity == null) {
                return 0;
            }

            source.getLevel().addFreshEntity(entity);

            return Command.SINGLE_SUCCESS;
        }));
    }
}
