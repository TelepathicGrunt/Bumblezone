package com.telepathicgrunt.the_bumblezone.modcompat;

import net.minecraftforge.event.entity.living.LivingSpawnEvent;

public class PokecubeCompat {
//    private static final List<PokedexEntry> BABY_POKECUBE_POKEMON_LIST = new ArrayList<>();
//    private static final List<PokedexEntry> POKECUBE_POKEMON_LIST = new ArrayList<>();

    public static void setupPokecube() {
//
//        // get all baby bees
//        BABY_POKECUBE_POKEMON_LIST.add(Database.getEntry("Combee"));
//        BABY_POKECUBE_POKEMON_LIST.add(Database.getEntry("Weedle"));
//        BABY_POKECUBE_POKEMON_LIST.add(Database.getEntry("Cutiefly"));
//
//        POKECUBE_POKEMON_LIST.addAll(BABY_POKECUBE_POKEMON_LIST);
//        POKECUBE_POKEMON_LIST.add(Database.getEntry("Ribombee"));
//        POKECUBE_POKEMON_LIST.add(Database.getEntry("Vespiquen"));
//        POKECUBE_POKEMON_LIST.add(Database.getEntry("Kakuna"));
//        POKECUBE_POKEMON_LIST.add(Database.getEntry("Beedrill"));
//
//        // Keep at end so it is only set to true if no exceptions was thrown during setup
//        ModChecker.pokecubePresent = true;
    }

    /**
     * Spawn Pokecube bees
     */
    public static void PCMobSpawnEvent(LivingSpawnEvent.CheckSpawn event, boolean isChild) {
//        List<PokedexEntry> pokemonListToUse = isChild ? BABY_POKECUBE_POKEMON_LIST : POKECUBE_POKEMON_LIST;
//        if (pokemonListToUse.size() == 0) {
//            Bumblezone.LOGGER.warn(
//                    "Error! List of POKECUBE_POKEMON_LIST is empty! Cannot spawn their bees. " +
//                    "Please let TelepathicGrunt (The Bumblezone dev) know about this!");
//            return;
//        }
//
//        Mob entity = (Mob) event.getEntity();
//        LevelAccessor world = event.getWorld();
//
//        // Pokecube mobs crash if done in worldgen due to onInitialSpawn not being worldgen safe in their code.
//        if(world instanceof WorldGenRegion) return;
//
//        // randomly pick a Pokecube mob entry
//        PokedexEntry pokemonDatabase = pokemonListToUse.get(world.getRandom().nextInt(pokemonListToUse.size()));
//        PokedexEntry.SpawnData spawn = pokemonDatabase.getSpawnData();
//        if(spawn == null) return;
//        Mob pokemon = PokecubeCore.createPokemob(pokemonDatabase, entity.level);
//        if (pokemon == null) return;
//
//        pokemon.setHealth(pokemon.getMaxHealth());
//        pokemon.setBaby(isChild);
//        BlockPos.MutableBlockPos blockpos = new BlockPos.MutableBlockPos().set(entity.blockPosition());
//        pokemon.moveTo(
//                blockpos.getX() + 0.5f,
//                blockpos.getY() + 0.5f,
//                blockpos.getZ() + 0.5f,
//                world.getRandom().nextFloat() * 360.0F,
//                0.0F);
//
//        IPokemob pokemob = CapabilityPokemob.getPokemobFor(pokemon);
//        if(!isChild) {
//            SpawnBiomeMatcher spawnMatcher = spawn.getMatcher(((ServerLevel) world), Vector3.entity(pokemon), null);
//            pokemob.spawnInit(spawnMatcher.spawnRule);
//        }
//        else {
//            pokemob.setExp(0, false);
//        }
//
//        pokemon.finalizeSpawn(
//                (ServerLevelAccessor) world,
//                world.getCurrentDifficultyAt(pokemon.blockPosition()),
//                event.getSpawnReason(),
//                null,
//                null);
//
//        world.addFreshEntity(pokemon);
    }
}
