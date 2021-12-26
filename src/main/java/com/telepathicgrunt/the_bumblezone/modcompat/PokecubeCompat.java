package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import pokecube.core.PokecubeCore;
import pokecube.core.commands.Pokemake;
import pokecube.core.database.Database;
import pokecube.core.database.PokedexEntry;
import pokecube.core.entity.pokemobs.GenericPokemob;
import pokecube.core.events.pokemob.SpawnEvent;
import thut.api.maths.Vector3;
import thut.api.maths.vecmath.Vector3d;

import java.util.ArrayList;
import java.util.List;

public class PokecubeCompat {
    private static final List<PokedexEntry> BABY_POKECUBE_POKEMON_LIST = new ArrayList<>();
    private static final List<PokedexEntry> POKECUBE_POKEMON_LIST = new ArrayList<>();

    public static void setupPokecube() {

        // get all baby bees
        BABY_POKECUBE_POKEMON_LIST.add(Database.getEntry("Combee"));
        BABY_POKECUBE_POKEMON_LIST.add(Database.getEntry("Weedle"));
        BABY_POKECUBE_POKEMON_LIST.add(Database.getEntry("Cutiefly"));

        POKECUBE_POKEMON_LIST.addAll(BABY_POKECUBE_POKEMON_LIST);
        POKECUBE_POKEMON_LIST.add(Database.getEntry("Ribombee"));
        POKECUBE_POKEMON_LIST.add(Database.getEntry("Vespiquen"));
        POKECUBE_POKEMON_LIST.add(Database.getEntry("Kakuna"));
        POKECUBE_POKEMON_LIST.add(Database.getEntry("Beedrill"));

        // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.pokecubePresent = true;
    }

    /**
     * Spawn Pokecube bees 
     */
    public static void PCMobSpawnEvent(LivingSpawnEvent.CheckSpawn event, boolean isChild) {
        List<PokedexEntry> pokemonListToUse = isChild ? BABY_POKECUBE_POKEMON_LIST : POKECUBE_POKEMON_LIST;
        if (pokemonListToUse.size() == 0) {
            Bumblezone.LOGGER.warn(
                    "Error! List of POKECUBE_POKEMON_LIST is empty! Cannot spawn their bees. " +
                    "Please let TelepathicGrunt (The Bumblezone dev) know about this!");
            return;
        }

        Mob entity = (Mob) event.getEntity();
        LevelAccessor world = event.getWorld();

        // Pokecube mobs crash if done in worldgen due to onInitialSpawn not being worldgen safe in their code.
        if(world instanceof WorldGenRegion) return;

        // randomly pick a Pokecube mob entry
        PokedexEntry pokemonDatabase = pokemonListToUse.get(world.getRandom().nextInt(pokemonListToUse.size()));
        Mob pokemon = PokecubeCore.createPokemob(pokemonDatabase, entity.level);
        if (pokemon == null) return;

        pokemon.setHealth(pokemon.getMaxHealth());
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
