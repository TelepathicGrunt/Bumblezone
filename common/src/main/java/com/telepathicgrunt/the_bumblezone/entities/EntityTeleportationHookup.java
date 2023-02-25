package com.telepathicgrunt.the_bumblezone.entities;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzDimensionConfigs;
import com.telepathicgrunt.the_bumblezone.events.entity.EntityTickEvent;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerTickEvent;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzDimension;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.modules.EntityPosAndDimModule;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleHelper;
import com.telepathicgrunt.the_bumblezone.modules.registry.ModuleRegistry;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import com.telepathicgrunt.the_bumblezone.world.dimension.BzWorldSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EntityTeleportationHookup {

    ////////////////////////////////////////////////////////////
    // Methods that setup and call PlayerTeleportationBackend //

    //Notify people of Bumblezone's advancements so they know how to enter dimension
    public static void playerTick(PlayerTickEvent event) {
        if (event.player() instanceof ServerPlayer serverPlayer) {
            Level level = serverPlayer.level;

            if (level instanceof ServerLevel serverLevel &&
                (serverLevel.getGameTime() + serverPlayer.getUUID().getLeastSignificantBits()) % 100 == 0 &&
                !serverLevel.dimension().equals(BzDimension.BZ_WORLD_KEY))
            {

                List<PoiRecord> poiInRange = serverLevel.getPoiManager().getInSquare(
                        (pointOfInterestType) -> pointOfInterestType.is(BzTags.IS_NEAR_BEEHIVE_ADVANCEMENT_TRIGGER_POI),
                        serverPlayer.blockPosition(),
                        8,
                        PoiManager.Occupancy.ANY
                    ).toList();

                if (poiInRange.size() > 0) {
                    BzCriterias.IS_NEAR_BEEHIVE_TRIGGER.trigger(serverPlayer);
                }
            }
        }
    }

    //Living Entity ticks
    public static void entityTick(EntityTickEvent event) {
        LivingEntity livingEntity = event.entity();

        //Makes it so player does not get killed for falling into the void
        if (livingEntity.level.dimension().location().equals(Bumblezone.MOD_DIMENSION_ID)) {
            if (livingEntity.getY() < -2) {
                if(BzDimensionConfigs.enableExitTeleportation) {
                    if(livingEntity instanceof ServerPlayer) {
                        BzCriterias.TELEPORT_OUT_OF_BUMBLEZONE_TRIGGER.trigger((ServerPlayer) livingEntity);
                    }

                    if (livingEntity.getY() < -4) {
                        livingEntity.moveTo(livingEntity.getX(), -4, livingEntity.getZ());
                        livingEntity.absMoveTo(livingEntity.getX(), -4, livingEntity.getZ());
                        livingEntity.setDeltaMovement(0, 0, 0);
                        if (!livingEntity.level.isClientSide()) {
                            livingEntity.addEffect(new MobEffectInstance(
                                    MobEffects.SLOW_FALLING,
                                    12,
                                    100,
                                    false,
                                    false,
                                    true));
                        }
                    }
                    livingEntity.fallDistance = 0;

                    if (!livingEntity.level.isClientSide()) {
                        teleportOutOfBz(livingEntity);
                    }
                }
            }
            else if (livingEntity.getY() > 255) {
                if(BzDimensionConfigs.enableExitTeleportation) {
                    if(livingEntity instanceof ServerPlayer) {
                        BzCriterias.TELEPORT_OUT_OF_BUMBLEZONE_TRIGGER.trigger((ServerPlayer) livingEntity);
                    }

                    if (livingEntity.getY() > 257) {
                        livingEntity.moveTo(livingEntity.getX(), 257, livingEntity.getZ());
                        livingEntity.absMoveTo(livingEntity.getX(), 257, livingEntity.getZ());
                    }

                    if (!livingEntity.level.isClientSide()) {
                        teleportOutOfBz(livingEntity);
                    }
                }
            }
        }
    }


    public static void teleportOutOfBz(LivingEntity livingEntity) {
        if (!livingEntity.level.isClientSide()) {
            checkAndCorrectStoredDimension(livingEntity);
            MinecraftServer minecraftServer = livingEntity.getServer(); // the server itself
            ResourceKey<Level> worldKey = null;

            if (livingEntity.getControllingPassenger() == null) {
                Optional<EntityPosAndDimModule> capOptional = ModuleHelper.getModule(livingEntity, ModuleRegistry.ENTITY_POS_AND_DIM);
                if (capOptional.isPresent()) {
                    EntityPosAndDimModule capability = capOptional.orElseThrow(RuntimeException::new);
                    worldKey = ResourceKey.create(Registries.DIMENSION, capability.getNonBZDim());
                }
            }
            else {
                if(livingEntity.getControllingPassenger() instanceof LivingEntity livingEntity2) {
                    checkAndCorrectStoredDimension(livingEntity2);
                }
                Optional<EntityPosAndDimModule> capOptional = ModuleHelper.getModule(livingEntity, ModuleRegistry.ENTITY_POS_AND_DIM);
                if (capOptional.isPresent()) {
                    EntityPosAndDimModule capability = capOptional.orElseThrow(RuntimeException::new);
                    worldKey = ResourceKey.create(Registries.DIMENSION, capability.getNonBZDim());
                }
            }

            ServerLevel serverWorld = worldKey == null ? null : minecraftServer.getLevel(worldKey);
            if(serverWorld == null) {
                serverWorld = minecraftServer.getLevel(ResourceKey.create(Registries.DIMENSION, new ResourceLocation(BzDimensionConfigs.defaultDimension)));
            }
            BzWorldSavedData.queueEntityToTeleport(livingEntity, serverWorld.dimension());
        }
    }

    // Projectiles
    public static boolean runTeleportProjectileImpact(HitResult hitResult, Entity thrower, Entity projectile) {
        Level world = thrower.level; // world we threw in

        // Make sure we are on server by checking if thrower is ServerPlayer and that we are not in bumblezone.
        // If onlyOverworldHivesTeleports is set to true, then only run this code in Overworld.
        if (BzDimensionConfigs.enableEntranceTeleportation &&
            thrower instanceof Player playerEntity &&
            !world.dimension().location().equals(Bumblezone.MOD_DIMENSION_ID) &&
            (!BzDimensionConfigs.onlyOverworldHivesTeleports || world.dimension().equals(ResourceKey.create(Registries.DIMENSION, new ResourceLocation(BzDimensionConfigs.defaultDimension)))))
        {
            // get nearby hives
            BlockPos hivePos = null;
            if (hitResult instanceof BlockHitResult blockHitResult) {
                BlockState block = world.getBlockState(blockHitResult.getBlockPos());
                if(EntityTeleportationBackend.isValidBeeHive(block)) {
                    hivePos = blockHitResult.getBlockPos();
                }
            }

            if(hivePos == null) {
                hivePos = getNearbyHivePos(hitResult.getLocation(), world);
            }

            // if fail, move the hit pos one step based on pearl velocity and try again
            if(hivePos == null && projectile != null) {
                hivePos = getNearbyHivePos(hitResult.getLocation().add(projectile.getDeltaMovement()), world);
            }

            // no hive hit, exit early
            if(hivePos == null) {
                return false;
            }

            //checks if block under hive is correct if config needs one
            boolean validBelowBlock = isValidBelowBlock(world, playerEntity, hivePos);

            //if the pearl hit a beehive, begin the teleportation.
            if (validBelowBlock) {
                if (thrower instanceof ServerPlayer serverPlayer) {
                    if (projectile != null && BuiltInRegistries.ENTITY_TYPE.getKey(projectile.getType()).getPath().contains("pearl")) {
                        BzCriterias.TELEPORT_TO_BUMBLEZONE_PEARL_TRIGGER.trigger(serverPlayer);
                        projectile.remove(Entity.RemovalReason.DISCARDED);
                    }
                    BzWorldSavedData.queueEntityToTeleport(serverPlayer, BzDimension.BZ_WORLD_KEY);
                }
                return true;
            }
        }
        return false;
    }

    public static boolean runEntityHitCheck(HitResult hitResult, Entity thrower, Projectile projectile) {
        Level world = thrower.level; // world we threw in

        // Make sure we are on server by checking if thrower is ServerPlayer and that we are not in bumblezone.
        // If onlyOverworldHivesTeleports is set to true, then only run this code in Overworld.
        if (hitResult instanceof EntityHitResult entityHitResult &&
            thrower instanceof Player player &&
            BzDimensionConfigs.enableEntranceTeleportation &&
            !world.dimension().location().equals(Bumblezone.MOD_DIMENSION_ID) &&
            (!BzDimensionConfigs.onlyOverworldHivesTeleports || world.dimension().equals(ResourceKey.create(Registries.DIMENSION, new ResourceLocation(BzDimensionConfigs.defaultDimension)))))
        {
            Entity hitEntity = entityHitResult.getEntity();
            boolean passedCheck = false;

            // Entity type check
            if (hitEntity.getType().is(BzTags.TARGET_ENTITY_HIT_BY_TELEPORT_PROJECTILE_ANYWHERE) ||
                hitEntity.getType().is(BzTags.TARGET_ENTITY_HIT_BY_TELEPORT_PROJECTILE_HIGH) ||
                hitEntity.getType().is(BzTags.TARGET_ENTITY_HIT_BY_TELEPORT_PROJECTILE_LOW))
            {
                Vec3 hitPos = hitResult.getLocation();
                AABB boundBox = entityHitResult.getEntity().getBoundingBox();
                double relativeHitY = hitPos.y() - boundBox.minY;
                double entityBoundHeight = boundBox.maxY - boundBox.minY;

                double minYThreshold = Integer.MIN_VALUE;
                double maxYThreshold = Integer.MAX_VALUE;

                if (hitEntity.getType().is(BzTags.TARGET_ENTITY_HIT_BY_TELEPORT_PROJECTILE_HIGH)) {
                    minYThreshold = entityBoundHeight / 2;
                }
                if (hitEntity.getType().is(BzTags.TARGET_ENTITY_HIT_BY_TELEPORT_PROJECTILE_HIGH)) {
                    maxYThreshold = entityBoundHeight / 2;
                }

                if (minYThreshold != maxYThreshold && (relativeHitY > maxYThreshold || relativeHitY < minYThreshold)) {
                    return false;
                }

                passedCheck = true;
            }

            // Held item check
            for (ItemStack stack : hitEntity.getHandSlots()) {
                if (stack == null) {
                    continue;
                }
                if (stack.is(BzTags.TARGET_WITH_HELD_ITEM_HIT_BY_TELEPORT_PROJECTILE)) {
                    passedCheck = true;
                    break;
                }
            }

            // Armor item check
            for (ItemStack stack : hitEntity.getArmorSlots()) {
                if (stack == null) {
                    continue;
                }
                if (stack.is(BzTags.TARGET_ARMOR_HIT_BY_TELEPORT_PROJECTILE)) {
                    Vec3 hitPos = hitResult.getLocation();
                    AABB boundBox = entityHitResult.getEntity().getBoundingBox();
                    double relativeHitY = hitPos.y() - boundBox.minY;
                    double entityBoundHeight = boundBox.maxY - boundBox.minY;

                    double minYThreshold = Integer.MIN_VALUE;
                    double maxYThreshold = Integer.MAX_VALUE;

                    if (stack.getItem() instanceof ArmorItem armorItem) {
                        switch (armorItem.getSlot()) {
                            case HEAD -> minYThreshold = entityBoundHeight * 0.66d;
                            case CHEST -> minYThreshold = entityBoundHeight * 0.4d;
                            case LEGS -> maxYThreshold = entityBoundHeight * 0.6d;
                            case FEET -> maxYThreshold = entityBoundHeight * 0.33d;
                        }
                    }

                    if (relativeHitY > maxYThreshold || relativeHitY < minYThreshold) {
                        continue;
                    }

                    passedCheck = true;
                    break;
                }
            }

            if (!passedCheck) {
                return false;
            }

            BlockPos hivePos = entityHitResult.getEntity().blockPosition();

            //checks if block under hive is correct if config needs one
            boolean validBelowBlock = isValidBelowBlock(world, player, hivePos);

            //if the pearl hit a beehive, begin the teleportation.
            if (validBelowBlock) {
                if (player instanceof ServerPlayer serverPlayer) {
                    if (projectile != null && BuiltInRegistries.ENTITY_TYPE.getKey(projectile.getType()).getPath().contains("pearl")) {
                        BzCriterias.TELEPORT_TO_BUMBLEZONE_PEARL_TRIGGER.trigger(serverPlayer);
                        projectile.remove(Entity.RemovalReason.DISCARDED);
                    }
                    BzWorldSavedData.queueEntityToTeleport(serverPlayer, BzDimension.BZ_WORLD_KEY);
                }
                return true;
            }
        }

        return false;
    }


    public static boolean runItemUseOn(Player user, BlockPos clickedPos, BlockState blockstate, ItemStack usingStack) {
        Level world = user.level; // world we use in

        // Make sure we are on server by checking if user is ServerPlayer and that we are not in bumblezone.
        // If onlyOverworldHivesTeleports is set to true, then only run this code in Overworld.
        if (BzDimensionConfigs.enableEntranceTeleportation &&
            !world.dimension().location().equals(Bumblezone.MOD_DIMENSION_ID) &&
            (!BzDimensionConfigs.onlyOverworldHivesTeleports || world.dimension().equals(ResourceKey.create(Registries.DIMENSION, new ResourceLocation(BzDimensionConfigs.defaultDimension)))))
        {
            if(!EntityTeleportationBackend.isValidBeeHive(blockstate)) {
                return false;
            }

            boolean isAllowTeleportItem = usingStack.is(BzTags.TELEPORT_ITEM_RIGHT_CLICKED_BEEHIVE) ||
                    (usingStack.is(BzTags.TELEPORT_ITEM_RIGHT_CLICKED_BEEHIVE_CROUCHING) && user.isShiftKeyDown());

            if (!isAllowTeleportItem) {
                Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(usingStack);
                for (Enchantment enchantment : enchantments.keySet()) {
                    if (GeneralUtils.isInTag(BuiltInRegistries.ENCHANTMENT, BzTags.ITEM_WITH_TELEPORT_ENCHANT, enchantment)) {
                        isAllowTeleportItem = true;
                        break;
                    }
                    else if (user.isShiftKeyDown() && GeneralUtils.isInTag(BuiltInRegistries.ENCHANTMENT, BzTags.ITEM_WITH_TELEPORT_ENCHANT_CROUCHING, enchantment)) {
                        isAllowTeleportItem = true;
                        break;
                    }
                }
            }

            if (!isAllowTeleportItem) {
                return false;
            }

            //checks if block under hive is correct if config needs one
            boolean validBelowBlock = isValidBelowBlock(world, user, clickedPos);

            //if the item is valid for teleport on a beehive, begin the teleportation.
            if (validBelowBlock) {
                if (user instanceof ServerPlayer serverPlayer) {
                    BzCriterias.TELEPORT_TO_BUMBLEZONE_PEARL_TRIGGER.trigger(serverPlayer);
                    BzWorldSavedData.queueEntityToTeleport(serverPlayer, BzDimension.BZ_WORLD_KEY);
                }
                return true;
            }
        }
        return false;
    }

    private static boolean isValidBelowBlock(Level world, Player playerEntity, BlockPos hivePos) {
        Optional<HolderSet.Named<Block>> blockTag = BuiltInRegistries.BLOCK.getTag(BzTags.REQUIRED_BLOCKS_UNDER_HIVE_TO_TELEPORT);
        if(blockTag.isPresent() && GeneralUtils.getListOfNonDummyBlocks(blockTag).size() != 0) {
            if(world.getBlockState(hivePos.below()).is(BzTags.REQUIRED_BLOCKS_UNDER_HIVE_TO_TELEPORT)) {
                return true;
            }
            else if(BzDimensionConfigs.warnPlayersOfWrongBlockUnderHive && playerEntity instanceof ServerPlayer serverPlayer) {
                //failed. Block below isn't the required block
                Bumblezone.LOGGER.error("Bumblezone: The attempt to teleport to Bumblezone failed due to not having a block from the following block tag below the hive: the_bumblezone:required_blocks_under_hive_to_teleport");
                Component message = Component.translatable("system.the_bumblezone.require_hive_blocks_failed");
                serverPlayer.displayClientMessage(message, true);
            }
        }
        else {
            return true;
        }
        return false;
    }

    private static BlockPos getNearbyHivePos(Vec3 hitBlockPos, Level world) {
        double checkRadius = 0.5D;
        //check with offset in all direction as the position of exact hit point could barely be outside the hive block
        //even through the pearl hit the block directly.
        for(double offset = -checkRadius; offset <= checkRadius; offset += checkRadius) {
            for(double offset2 = -checkRadius; offset2 <= checkRadius; offset2 += checkRadius) {
                for (double offset3 = -checkRadius; offset3 <= checkRadius; offset3 += checkRadius) {
                    BlockPos offsettedHitPos = new BlockPos(hitBlockPos.add(offset, offset2, offset3));
                    BlockState block = world.getBlockState(offsettedHitPos);
                    if(EntityTeleportationBackend.isValidBeeHive(block)) {
                        return offsettedHitPos;
                    }
                }
            }
        }
        return null;
    }


    public static void runPistonPushed(Direction direction, LivingEntity pushedEntity) {
        ServerLevel world = (ServerLevel) pushedEntity.level;

        // If onlyOverworldHivesTeleports is set to true, then only run this code in Overworld.
        if (BzDimensionConfigs.enableEntranceTeleportation &&
            !world.dimension().location().equals(Bumblezone.MOD_DIMENSION_ID) &&
            (!BzDimensionConfigs.onlyOverworldHivesTeleports || world.dimension().equals(ResourceKey.create(Registries.DIMENSION, new ResourceLocation(BzDimensionConfigs.defaultDimension)))))
        {
            if(BzWorldSavedData.isEntityQueuedToTeleportAlready(pushedEntity)) return; // Skip checks if entity is teleporting already to Bz.

            BlockPos.MutableBlockPos entityPos = new BlockPos.MutableBlockPos().set(pushedEntity.blockPosition());
            BlockPos[] blockPositions = new BlockPos[]{
                    entityPos,
                    entityPos.relative(direction),
                    entityPos.relative(Direction.UP),
                    entityPos.relative(Direction.UP).relative(direction)
            };
            List<BlockState> belowHiveBlocks = new ArrayList<>();

            // Checks if entity is pushed into hive block
            boolean isPushedIntoBeehive = false;
            for(BlockPos pos : blockPositions) {
                if(EntityTeleportationBackend.isValidBeeHive(world.getBlockState(pos))) {
                    isPushedIntoBeehive = true;
                    belowHiveBlocks.add(world.getBlockState(pos.below()));
                }
            }

            if (isPushedIntoBeehive) {
                //checks if block under hive is correct if config needs one
                boolean validBelowBlock = false;
                Optional<HolderSet.Named<Block>> blockTag = BuiltInRegistries.BLOCK.getTag(BzTags.REQUIRED_BLOCKS_UNDER_HIVE_TO_TELEPORT);
                if(blockTag.isPresent() && GeneralUtils.getListOfNonDummyBlocks(blockTag).size() != 0) {

                    for(BlockState belowBlock : belowHiveBlocks) {
                        if(belowBlock.is(BzTags.REQUIRED_BLOCKS_UNDER_HIVE_TO_TELEPORT)) {
                            validBelowBlock = true;
                        }
                    }

                    if(!validBelowBlock && BzDimensionConfigs.warnPlayersOfWrongBlockUnderHive) {
                        if(pushedEntity instanceof Player playerEntity) {
                            //failed. Block below isn't the required block
                            Bumblezone.LOGGER.info("Bumblezone: The attempt to teleport to Bumblezone failed due to not having a block from the following block tag below the hive: the_bumblezone:required_blocks_under_hive_to_teleport");
                            Component message = Component.translatable("system.the_bumblezone.require_hive_blocks_failed");
                            playerEntity.displayClientMessage(message, true);
                        }
                        return;
                    }
                }
                else {
                    validBelowBlock = true;
                }

                //if the entity was pushed into a beehive, begin the teleportation.
                if (validBelowBlock) {
                    if(pushedEntity instanceof ServerPlayer) {
                        BzCriterias.TELEPORT_TO_BUMBLEZONE_PISTON_TRIGGER.trigger((ServerPlayer) pushedEntity);
                    }
                    BzWorldSavedData.queueEntityToTeleport(pushedEntity, BzDimension.BZ_WORLD_KEY);
                }
            }
        }
    }


    /**
     * Looks at stored non-bz dimension and changes it to default dimension if it is
     * BZ dimension or the config forces going to default dimension.
     */
    private static void checkAndCorrectStoredDimension(LivingEntity livingEntity) {
        ModuleHelper.getModule(livingEntity, ModuleRegistry.ENTITY_POS_AND_DIM).ifPresent(capability -> {
            if (capability.getNonBZDim().equals(Bumblezone.MOD_DIMENSION_ID) || BzDimensionConfigs.forceExitToOverworld) {
                //Go to default dimension instead
                //update stored dimension
                capability.setNonBZDim(new ResourceLocation(BzDimensionConfigs.defaultDimension));
            }
        });
    }
}
