package com.telepathicgrunt.the_bumblezone.world.structures;

import com.telepathicgrunt.the_bumblezone.entities.mobs.BeeQueenEntity;
import com.telepathicgrunt.the_bumblezone.items.EssenceOfTheBees;
import com.telepathicgrunt.the_bumblezone.items.HoneyCompass;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
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
        ServerLevel level = (ServerLevel) serverPlayer.level;
        StructureManager structureManager = level.structureManager();
        StructureStart structureStart = structureManager.getStructureWithPieceAt(serverPlayer.blockPosition(), BzTags.BEE_QUEEN_MINING_FATIGUE);
        if (structureStart.isValid()) {
            BlockPos structureCenter = structureStart.getBoundingBox().getCenter();
            boolean hasBeeQueenNearStructureCenter = !level.getEntitiesOfClass(BeeQueenEntity.class, AABB.ofSize(Vec3.atCenterOf(structureCenter), 16, 16, 16), (e) -> !e.isNoAi()).isEmpty();
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

            List<ItemStack> throneCompasses = new ArrayList<>();
            for (ItemStack item : serverPlayer.getInventory().items) {
                if (item.is(BzItems.HONEY_COMPASS)) {
                    CompoundTag tag = item.getOrCreateTag();
                    if (!tag.getBoolean(HoneyCompass.TAG_LOCATED_SPECIAL_STRUCTURE) &&
                        tag.getString(HoneyCompass.TAG_TYPE).equals("structure") &&
                        tag.getString("CustomName").equals("item.the_bumblezone.honey_compass_throne_structure") &&
                        NbtUtils.readBlockPos(tag.getCompound(HoneyCompass.TAG_TARGET_POS)).above(128).closerThan(structureCenter, 128))
                    {
                        throneCompasses.add(item);
                    }
                }
            }

            if (!hasBeeQueenNearby && !throneCompasses.isEmpty()) {
                BeeQueenEntity newBeeQueen = BzEntities.BEE_QUEEN.create(level);

                BlockPos queenPos = new BlockPos(structureCenter.getX(), 133, structureCenter.getZ());
                newBeeQueen.setPos(Vec3.atCenterOf(queenPos));
                newBeeQueen.finalizeSpawn(level, level.getCurrentDifficultyAt(newBeeQueen.blockPosition()), MobSpawnType.STRUCTURE, null, null);

                level.addFreshEntity(newBeeQueen);

                level.setBlock(queenPos.above(17), BzFluids.ROYAL_JELLY_FLUID_BLOCK.defaultBlockState(), 3);
            }

            throneCompasses.forEach(compass -> compass.getOrCreateTag().putBoolean(HoneyCompass.TAG_LOCATED_SPECIAL_STRUCTURE, true));
        }
    }

}
