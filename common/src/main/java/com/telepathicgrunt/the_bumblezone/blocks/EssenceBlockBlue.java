package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.EssenceBlockEntity;
import com.telepathicgrunt.the_bumblezone.items.essence.EssenceOfTheBees;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.screens.ServerEssenceEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class EssenceBlockBlue extends EssenceBlock {
    private static final float ENTITIES_TO_KILL = 50;

    public EssenceBlockBlue() {
        super(Properties.of().mapColor(MapColor.COLOR_BLUE));
    }

    @Override
    public ResourceLocation getArenaNbt() {
        return new ResourceLocation(Bumblezone.MODID, "essence/blue_arena");
    }

    @Override
    public int getEventTimeFrame() {
        return 8000;
    }

    @Override
    public ServerEssenceEvent getServerEssenceEvent() {
        return (ServerEssenceEvent) new ServerEssenceEvent(
                "essence.the_bumblezone.blue_essence_event",
                BossEvent.BossBarColor.BLUE,
                BossEvent.BossBarOverlay.NOTCHED_20
        ).setDarkenScreen(true);
    }

    @Override
    public ItemStack getEssenceItemReward() {
        return BzItems.ESSENCE_BLUE.get().getDefaultInstance();
    }

    @Override
    public int getEssenceXpReward() {
        return 3000;
    }

    @Override
    public void performUniqueArenaTick(ServerLevel serverLevel, BlockPos blockPos, BlockState blockState, EssenceBlockEntity essenceBlockEntity) {
        if (essenceBlockEntity.getPlayerInArena().size() == 0) return;

        int entitiesKilled = essenceBlockEntity.getExtraEventTrackingProgress();
        List<EssenceBlockEntity.EventEntities> eventEntitiesInArena = essenceBlockEntity.getEventEntitiesInArena();

        if (entitiesKilled != ENTITIES_TO_KILL && eventEntitiesInArena.size() < Math.min(3 + (essenceBlockEntity.getPlayerInArena().size() * 1.5), ENTITIES_TO_KILL - entitiesKilled)) {
            // spawn a mob this tick.
            int currentEntityCount = eventEntitiesInArena.size() + entitiesKilled;
            SpawnNewEnemy(serverLevel, blockPos, essenceBlockEntity, currentEntityCount, eventEntitiesInArena);
        }
        else {
            // update how many entities are alive
            for (int i = eventEntitiesInArena.size() - 1; i >= 0; i--) {
                UUID entityToCheck = eventEntitiesInArena.get(i).uuid();
                Entity entity = serverLevel.getEntity(entityToCheck);
                if (entity == null) {
                    entitiesKilled++;
                    eventEntitiesInArena.remove(i);
                }
                else {
                    if (entity instanceof NeutralMob neutralMob && !(neutralMob.getTarget() instanceof Player)) {
                        UUID playerUUID = essenceBlockEntity.getPlayerInArena().get(serverLevel.getRandom().nextInt(essenceBlockEntity.getPlayerInArena().size()));
                        Player player = serverLevel.getPlayerByUUID(playerUUID);

                        neutralMob.setRemainingPersistentAngerTime(Integer.MAX_VALUE);
                        neutralMob.setPersistentAngerTarget(playerUUID);
                        neutralMob.setTarget(player);
                    }
                    else if (entity instanceof Mob mob && !(mob.getTarget() instanceof Player)) {
                        UUID playerUUID = essenceBlockEntity.getPlayerInArena().get(serverLevel.getRandom().nextInt(essenceBlockEntity.getPlayerInArena().size()));
                        Player player = serverLevel.getPlayerByUUID(playerUUID);

                        mob.setTarget(player);
                    }
                }
            }
        }

        handleGiantBubbles(serverLevel, blockPos, blockState, essenceBlockEntity);

        float newProgress = entitiesKilled / ENTITIES_TO_KILL;
        essenceBlockEntity.getEventBar().setProgress(1 - newProgress);
        essenceBlockEntity.setExtraEventTrackingProgress(entitiesKilled);
        if (entitiesKilled == ENTITIES_TO_KILL) {
            EssenceBlockEntity.EndEvent(serverLevel, blockPos, blockState, essenceBlockEntity, true);
        }
    }

    private static void SpawnNewEnemy(ServerLevel serverLevel, BlockPos blockPos, EssenceBlockEntity essenceBlockEntity, int currentEntityCount, List<EssenceBlockEntity.EventEntities> eventEntitiesInArena) {
        TagKey<EntityType<?>> enemyTagToUse = BzTags.ESSENCE_CALMING_ARENA_NORMAL_ENEMY;
        boolean isStrong = false;
        int entityToSpawnIndex = currentEntityCount + 1;
        if ((entityToSpawnIndex % 15) == 0 ||
            entityToSpawnIndex == 29 ||
            entityToSpawnIndex == 48 ||
            entityToSpawnIndex == 49 ||
            entityToSpawnIndex == 50)
        {
            enemyTagToUse = BzTags.ESSENCE_CALMING_ARENA_BOSS_ENEMY; // boss at 15, 29, 30, 48, 49, and 50
        }
        else if ((entityToSpawnIndex % 5) == 0) {
            enemyTagToUse = BzTags.ESSENCE_CALMING_ARENA_STRONG_ENEMY; // strong at every 5 except when boss
            isStrong = true;
        }

        List<? extends EntityType<?>> entityTypeList = BuiltInRegistries.ENTITY_TYPE
                .getTag(enemyTagToUse)
                .map(holders -> holders
                        .stream()
                        .map(Holder::value)
                        .toList()
                ).orElseGet(ArrayList::new);

        Direction direction = Direction.getRandom(serverLevel.getRandom());
        EntityType<?> entityTypeToSpawn = entityTypeList.get(serverLevel.getRandom().nextInt(entityTypeList.size()));
        Entity entity = entityTypeToSpawn.spawn(serverLevel, blockPos.offset(direction.getStepX() * 5, direction.getStepY() * 5, direction.getStepZ() * 5), MobSpawnType.TRIGGERED);

        if (entity != null) {
            eventEntitiesInArena.add(new EssenceBlockEntity.EventEntities(entity.getUUID()));

            UUID playerUUID = essenceBlockEntity.getPlayerInArena().get(serverLevel.getRandom().nextInt(essenceBlockEntity.getPlayerInArena().size()));
            Player player = serverLevel.getPlayerByUUID(playerUUID);
            if (player instanceof ServerPlayer serverPlayer) {
                float maxHeart = serverPlayer.getMaxHealth();
                float maxArmor = serverPlayer.getArmorValue();
                float mobHealthBoost = (maxHeart / 15) + (maxArmor / 10);
                float mobAttackBoost = (maxHeart / 20) + (maxArmor / 15);

                boolean isEssenced = EssenceOfTheBees.hasEssence(serverPlayer);
                if (!isEssenced) {
                    mobHealthBoost *= 1.5f;
                    mobAttackBoost *= 1.5f;
                }

                if (entity instanceof LivingEntity livingEntity) {
                    AttributeInstance livingEntityAttributeHealth = livingEntity.getAttribute(Attributes.MAX_HEALTH);
                    if (livingEntityAttributeHealth != null) {
                        livingEntityAttributeHealth.addPermanentModifier(new AttributeModifier(
                                UUID.fromString("03c85bd0-09eb-11ee-be56-0242ac120002"),
                                "Essence Arena Health Boost",
                                mobHealthBoost,
                                AttributeModifier.Operation.ADDITION));
                    }

                    AttributeInstance livingEntityAttributeAttack = livingEntity.getAttribute(Attributes.ATTACK_DAMAGE);
                    if (livingEntityAttributeAttack != null) {
                        livingEntityAttributeAttack.addPermanentModifier(new AttributeModifier(
                                UUID.fromString("355141f8-09eb-11ee-be56-0242ac120002"),
                                "Essence Arena Damage Boost",
                                mobAttackBoost,
                                AttributeModifier.Operation.ADDITION));
                    }

                    AttributeInstance livingEntityAttributeSpeed = livingEntity.getAttribute(Attributes.MOVEMENT_SPEED);
                    if (livingEntityAttributeSpeed != null) {
                        livingEntityAttributeSpeed.addPermanentModifier(new AttributeModifier(
                                UUID.fromString("39ca0496-fa37-488f-8199-c4779f1afe0c"),
                                "Essence Arena Speed Boost",
                                isEssenced ? 0.02 : 0.04,
                                AttributeModifier.Operation.ADDITION));
                    }

                    AttributeInstance livingEntityAttributeFlyingSpeed = livingEntity.getAttribute(Attributes.FLYING_SPEED);
                    if (livingEntityAttributeFlyingSpeed != null) {
                        livingEntityAttributeFlyingSpeed.addPermanentModifier(new AttributeModifier(
                                UUID.fromString("c762c216-0a3a-11ee-be56-0242ac120002"),
                                "Essence Arena Flying Speed Boost",
                                0.02,
                                AttributeModifier.Operation.ADDITION));
                    }

                    AttributeInstance livingEntityAttributeFollowRange = livingEntity.getAttribute(Attributes.FOLLOW_RANGE);
                    if (livingEntityAttributeFollowRange != null) {
                        livingEntityAttributeFollowRange.addPermanentModifier(new AttributeModifier(
                                UUID.fromString("23a7a8a9-85bc-4dc3-9417-a4bd4b1b95a2"),
                                "Essence Arena Sight Boost",
                                32,
                                AttributeModifier.Operation.ADDITION));
                    }
                }

                if (entity instanceof NeutralMob neutralMob) {
                    neutralMob.setRemainingPersistentAngerTime(Integer.MAX_VALUE);
                    neutralMob.setPersistentAngerTarget(playerUUID);
                    neutralMob.setTarget(serverPlayer);
                }
                else if (entity instanceof Mob mob) {
                    mob.setTarget(serverPlayer);
                    if (mob instanceof Drowned drowned) {
                        ItemStack swimBoots = Items.LEATHER_BOOTS.getDefaultInstance();
                        swimBoots.enchant(Enchantments.DEPTH_STRIDER, 3);
                        drowned.equipItemIfPossible(swimBoots);
                        drowned.setDropChance(EquipmentSlot.FEET, 0.2f);

                        if (isStrong) {
                            ItemStack trident = Items.TRIDENT.getDefaultInstance();
                            if (serverLevel.getRandom().nextFloat() < 0.25) {
                                trident.enchant(Enchantments.CHANNELING, 1);
                            }
                            else if (serverLevel.getRandom().nextFloat() < 0.25) {
                                trident.enchant(Enchantments.LOYALTY, 1);
                            }
                            else if (serverLevel.getRandom().nextFloat() < 0.25) {
                                trident.enchant(Enchantments.IMPALING, 1);
                            }
                            else {
                                trident.enchant(Enchantments.RIPTIDE, 1);
                            }
                            drowned.setItemSlot(EquipmentSlot.MAINHAND, trident);
                            drowned.setDropChance(EquipmentSlot.MAINHAND, 0.5f);
                        }
                        else {
                            drowned.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                        }
                    }
                }
            }
        }
    }

    public void handleGiantBubbles(ServerLevel serverLevel, BlockPos blockPos, BlockState blockState, EssenceBlockEntity essenceBlockEntity) {
        long gameTime = serverLevel.getGameTime();

        if (gameTime % 25 == 0) {
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                int offSetBubble = direction.getAxis() == Direction.Axis.X ? 0 : 8;
                int currentProgress = (int) (((gameTime / 25) + offSetBubble) % 16);
                BlockPos bubbleCenter = blockPos.offset(direction.getStepX() * 10, currentProgress - 8, direction.getStepZ() * 10);

                BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
                for (int y = -2; y <= 1; y++) {
                    mutableBlockPos.set(bubbleCenter).move(Direction.UP, y);
                    if (mutableBlockPos.getY() >= blockPos.getY() - 5 && mutableBlockPos.getY() <= blockPos.getY() + 5) {
                        for (int x = -1; x <= 1; x++) {
                            mutableBlockPos.move(Direction.EAST, x);
                            for (int z = -1; z <= 1; z++) {
                                mutableBlockPos.move(Direction.SOUTH, z);
                                if (y == -2) {
                                    serverLevel.setBlock(mutableBlockPos, Blocks.WATER.defaultBlockState(), 2);
                                }
                                else {
                                    serverLevel.setBlock(mutableBlockPos, Blocks.LIGHT.defaultBlockState(), 2);
                                }
                                mutableBlockPos.move(Direction.SOUTH, -z);
                            }
                            mutableBlockPos.move(Direction.EAST, -x);
                        }
                    }
                }
            }
        }
    }
}
