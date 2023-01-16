package com.telepathicgrunt.the_bumblezone.modcompat.forge;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.events.entity.EntitySpawnEvent;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import pokecube.api.data.PokedexEntry;
import pokecube.api.data.spawns.SpawnBiomeMatcher;
import pokecube.api.entity.pokemob.IPokemob;
import pokecube.api.entity.pokemob.PokemobCaps;
import pokecube.core.PokecubeCore;
import pokecube.core.database.Database;
import pokecube.core.entity.pokemobs.helper.PokemobBase;
import thut.api.maths.Vector3;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class PokecubeCompat implements ModCompat {
    private static final List<PokedexEntry> BABY_POKECUBE_POKEMON_LIST = new ArrayList<>();
    private static final List<PokedexEntry> POKECUBE_POKEMON_LIST = new ArrayList<>();

    public PokecubeCompat() {
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

    @Override
    public EnumSet<Type> compatTypes() {
        return EnumSet.of(Type.SPAWNS, Type.DIMENSION_SPAWN);
    }

    @Override
    public boolean onBeeSpawn(EntitySpawnEvent event, boolean isBaby) {
        if (event.entity().getRandom().nextFloat() >= BzModCompatibilityConfigs.spawnrateOfPokecubeBeePokemon) return false;
        if (!isBaby) return false;
        List<PokedexEntry> pokemonListToUse = isBaby ? BABY_POKECUBE_POKEMON_LIST : POKECUBE_POKEMON_LIST;
        if (pokemonListToUse.size() == 0) {
            Bumblezone.LOGGER.warn(
                    "Error! List of POKECUBE_POKEMON_LIST is empty! Cannot spawn their bees. " +
                    "Please let TelepathicGrunt (The Bumblezone dev) know about this!");
            return false;
        }

        Mob entity = event.entity();
        LevelAccessor world = event.level();

        // Pokecube mobs crash if done in worldgen due to onInitialSpawn not being worldgen safe in their code.
        if(world instanceof WorldGenRegion) return false;

        // randomly pick a Pokecube mob entry
        PokedexEntry pokemonDatabase = pokemonListToUse.get(world.getRandom().nextInt(pokemonListToUse.size()));
        PokedexEntry.SpawnData spawn = pokemonDatabase.getSpawnData();
        if(spawn == null) return false;
        Mob pokemon = PokecubeCore.createPokemob(pokemonDatabase, entity.level);
        if (pokemon == null) return false;

        pokemon.setHealth(pokemon.getMaxHealth());
        pokemon.setBaby(isBaby);

        BlockPos.MutableBlockPos blockpos = new BlockPos.MutableBlockPos().set(entity.blockPosition());
        ChunkAccess chunkAccess = world.getChunk(blockpos);
        if (!isBaby) {
            BlockState currentState = chunkAccess.getBlockState(blockpos);
            while (!currentState.canOcclude() && currentState.getFluidState().isEmpty() && blockpos.getY() > world.getMinBuildHeight()) {
                blockpos.move(Direction.DOWN);
                currentState = chunkAccess.getBlockState(blockpos);
            }
            blockpos.move(Direction.UP);
        }

        pokemon.moveTo(
                blockpos.getX() + 0.5f,
                blockpos.getY() + 0.5f,
                blockpos.getZ() + 0.5f,
                world.getRandom().nextFloat() * 360.0F,
                0.0F);

        IPokemob pokemob = PokemobCaps.getPokemobFor(pokemon);
        if(!isBaby) {
            try {
                SpawnBiomeMatcher spawnMatcher = spawn.getMatcher(((ServerLevel) world), Vector3.entity(pokemon), null);
                pokemob.spawnInit(spawnMatcher.spawnRule);
            }
            catch (Exception e) {
                e.addSuppressed(new RuntimeException("Mob: " + pokemob.getDisplayName() + ", Position: " + blockpos + ", Dimension: " + ((ServerLevel) world).dimension().location()));
                Bumblezone.LOGGER.error("", e);
            }
        } else {
            pokemob.setExp(0, false);
        }

        pokemon.finalizeSpawn(
                (ServerLevelAccessor) world,
                world.getCurrentDifficultyAt(pokemon.blockPosition()),
                event.spawnType(),
                null,
                null);

        world.addFreshEntity(pokemon);
        return true;
    }

    @Override
    public void onEntitySpawnInDimension(Entity entity) {
        if (BzModCompatibilityConfigs.beePokemonGetsProtectionEffect && entity instanceof PokemobBase pokemobBase) {
            if (POKECUBE_POKEMON_LIST.contains(pokemobBase.pokemobCap.getPokedexEntry())) {
                pokemobBase.addEffect(new MobEffectInstance(
                        BzEffects.PROTECTION_OF_THE_HIVE.get(),
                        Integer.MAX_VALUE - 5,
                        0,
                        true,
                        false));
            }
        }
    }
}
