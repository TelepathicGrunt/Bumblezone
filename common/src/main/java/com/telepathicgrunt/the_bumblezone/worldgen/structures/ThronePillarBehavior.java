package com.telepathicgrunt.the_bumblezone.worldgen.structures;

import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeeQueenEntity;
import com.telepathicgrunt.the_bumblezone.items.datacomponents.HoneyCompassBaseData;
import com.telepathicgrunt.the_bumblezone.items.datacomponents.HoneyCompassStateData;
import com.telepathicgrunt.the_bumblezone.items.datacomponents.HoneyCompassTargetData;
import com.telepathicgrunt.the_bumblezone.items.essence.EssenceOfTheBees;
import com.telepathicgrunt.the_bumblezone.modinit.BzDataComponents;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.PlatformHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class ThronePillarBehavior {
    public static void applyFatigueAndSpawningBeeQueen(ServerPlayer serverPlayer) {
        ServerLevel level = (ServerLevel) serverPlayer.level();
        StructureManager structureManager = level.structureManager();
        StructureStart structureStart = structureManager.getStructureWithPieceAt(serverPlayer.blockPosition(), BzTags.BEE_QUEEN_MINING_FATIGUE);
        if (structureStart.isValid()) {
            BlockPos structureCenter = structureStart.getBoundingBox().getCenter();
            boolean hasBeeQueenNearStructureCenter = !level.getEntitiesOfClass(BeeQueenEntity.class, AABB.ofSize(structureCenter.getCenter(), 16, 16, 16), (e) -> !e.isNoAi()).isEmpty();
            boolean hasBeeQueenNearby = hasBeeQueenNearStructureCenter || !level.getEntitiesOfClass(BeeQueenEntity.class, serverPlayer.getBoundingBox().inflate(30.0D, 30.0D, 30.0D), (e) -> !e.isNoAi()).isEmpty();
            if (hasBeeQueenNearby && !serverPlayer.isCreative() && !serverPlayer.isSpectator() && !EssenceOfTheBees.hasEssence(serverPlayer)) {
                serverPlayer.addEffect(new MobEffectInstance(
                        MobEffects.DIG_SLOWDOWN,
                        100,
                        2,
                        false,
                        false,
                        true));
            }

            if (!BzGeneralConfigs.beeQueenRespawning) {
                return;
            }

            List<ItemStack> throneCompasses = new ArrayList<>();
            for (ItemStack item : serverPlayer.getInventory().items) {
                if (item.is(BzItems.HONEY_COMPASS.get())) {
                    HoneyCompassBaseData honeyCompassBaseData = item.get(BzDataComponents.HONEY_COMPASS_BASE_DATA.get());
                    HoneyCompassStateData honeyCompassStateData = item.get(BzDataComponents.HONEY_COMPASS_STATE_DATA.get());
                    HoneyCompassTargetData honeyCompassTargetData = item.get(BzDataComponents.HONEY_COMPASS_TARGET_DATA.get());
                    if (!honeyCompassStateData.locatedSpecialStructure() &&
                        honeyCompassBaseData.isStructureCompass() &&
                        honeyCompassBaseData.customName().isPresent() &&
                        honeyCompassBaseData.customName().get().equals("item.the_bumblezone.honey_compass_throne_structure") &&
                        honeyCompassTargetData.targetPos().isPresent() &&
                        honeyCompassTargetData.targetPos().get().above(128).closerThan(structureCenter, 128))
                    {
                        throneCompasses.add(item);
                    }
                }
            }

            if (!hasBeeQueenNearby && !throneCompasses.isEmpty()) {
                BeeQueenEntity newBeeQueen = BzEntities.BEE_QUEEN.get().create(level);

                BlockPos queenPos = new BlockPos(structureCenter.getX(), 133, structureCenter.getZ());
                newBeeQueen.setPos(Vec3.atCenterOf(queenPos));
                newBeeQueen.finalizeSpawn(level, level.getCurrentDifficultyAt(newBeeQueen.blockPosition()), MobSpawnType.STRUCTURE, null);

                PlatformHooks.finalizeSpawn(newBeeQueen, level, null, MobSpawnType.STRUCTURE);
                level.addFreshEntity(newBeeQueen);

                level.setBlock(queenPos.above(17), BzFluids.ROYAL_JELLY_FLUID_BLOCK.get().defaultBlockState(), 3);
            }

            throneCompasses.forEach(compass -> {
                HoneyCompassStateData honeyCompassStateData = compass.get(BzDataComponents.HONEY_COMPASS_STATE_DATA.get());
                compass.set(BzDataComponents.HONEY_COMPASS_STATE_DATA.get(), new HoneyCompassStateData(
                    honeyCompassStateData.locked(),
                    honeyCompassStateData.searchId(),
                    honeyCompassStateData.isLoading(),
                    honeyCompassStateData.isFailed(),
                    true
                ));
            });
        }
    }

}
