package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.WorldGenRegion;
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
                    "Error! List of Resourceful bees is empty! Cannot spawn their bees. " +
                            "Please let TelepathicGrunt (The Bumblezone dev) know about this!");
            return;
        }

        MobEntity entity = (MobEntity) event.getEntity();
        IServerWorld world = (IServerWorld) event.getWorld();

        // Pokecube mobs crash if done in worldgen due to onInitialSpawn being worldgen safe
        if(world instanceof WorldGenRegion) return;

        // randomly pick a Resourceful bee (the nbt determines the bee)
        MobEntity pokemon = (MobEntity) POKECUBE_POKEMON_LIST.get(world.getRandom().nextInt(POKECUBE_POKEMON_LIST.size())).create(entity.world);
        if (pokemon == null) return;

        pokemon.setChild(isChild);
        BlockPos.Mutable blockpos = new BlockPos.Mutable().setPos(entity.getPosition());
        pokemon.setLocationAndAngles(
                blockpos.getX() + 0.5f,
                blockpos.getY() + 0.5f,
                blockpos.getZ() + 0.5f,
                world.getRandom().nextFloat() * 360.0F,
                0.0F);

        pokemon.onInitialSpawn(
                world,
                world.getDifficultyForLocation(pokemon.getPosition()),
                event.getSpawnReason(),
                null,
                null);

        world.addEntity(pokemon);
    }
}
