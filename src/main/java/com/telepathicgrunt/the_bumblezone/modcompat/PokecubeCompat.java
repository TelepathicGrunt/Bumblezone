package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import pokecube.core.database.Database;

import java.util.ArrayList;
import java.util.List;

public class PokecubeCompat {
    private static final List<EntityType<?>> POKECUBE_POKEMON_LIST = new ArrayList<>();

    public static void setupPokecube() {

        // get all baby bees
        POKECUBE_POKEMON_LIST.add(Database.getEntry("Combee").entity_type);
        POKECUBE_POKEMON_LIST.add(Database.getEntry("Weedle").entity_type);
        POKECUBE_POKEMON_LIST.add(Database.getEntry("Cutiefly").entity_type);

        // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.pokecubePresent = true;
    }

    /**
     * 1/15th of bees spawning will also spawn Resourceful Bees' bees
     */
    public static void PCMobSpawnEvent(LivingSpawnEvent.CheckSpawn event, boolean isChild) {

        if (POKECUBE_POKEMON_LIST.size() == 0) {
            Bumblezone.LOGGER.warn(
                    "Error! List of POKECUBE_POKEMON_LIST is empty! Cannot spawn their bees. " +
                            "Please let TelepathicGrunt (The Bumblezone dev) know about this!");
            return;
        }

        Mob entity = (Mob) event.getEntity();
        LevelAccessor world = event.getWorld();

        // Pokecube mobs crash if done in worldgen due to onInitialSpawn not being worldgen safe in their code.
        if(world instanceof WorldGenRegion) return;

        // randomly pick a Resourceful bee (the nbt determines the bee)
        Mob pokemon = (Mob) POKECUBE_POKEMON_LIST.get(world.getRandom().nextInt(POKECUBE_POKEMON_LIST.size())).create(entity.level);
        if (pokemon == null) return;

        pokemon.setBaby(isChild);
        BlockPos.MutableBlockPos blockpos = new BlockPos.MutableBlockPos().set(entity.blockPosition());
        pokemon.moveTo(
                blockpos.getX() + 0.5f,
                blockpos.getY() + 0.5f,
                blockpos.getZ() + 0.5f,
                world.getRandom().nextFloat() * 360.0F,
                0.0F);

        pokemon.finalizeSpawn(
                (ServerLevelAccessor) world,
                world.getCurrentDifficultyAt(pokemon.blockPosition()),
                event.getSpawnReason(),
                null,
                null);

        world.addFreshEntity(pokemon);
    }
}
