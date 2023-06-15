package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.EssenceBlockEntity;
import com.telepathicgrunt.the_bumblezone.items.essence.EssenceOfTheBees;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.screens.ServerEssenceEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.scores.PlayerTeam;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class EssenceBlockRed extends EssenceBlock {
    private static final float ENTITIES_TO_KILL = 100;

    public EssenceBlockRed() {
        super(Properties.of().mapColor(MapColor.COLOR_RED));
    }

    @Override
    public ResourceLocation getArenaNbt() {
        return new ResourceLocation(Bumblezone.MODID, "essence/red_arena");
    }

    @Override
    public int getEventTimeFrame() {
        return 8000;
    }

    @Override
    public ServerEssenceEvent getServerEssenceEvent() {
        return (ServerEssenceEvent) new ServerEssenceEvent(
                "essence.the_bumblezone.red_essence_event",
                BossEvent.BossBarColor.RED,
                BossEvent.BossBarOverlay.NOTCHED_20
        ).setDarkenScreen(true);
    }

    @Override
    public ItemStack getEssenceItemReward() {
        return BzItems.ESSENCE_RED.get().getDefaultInstance();
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

        float newProgress = entitiesKilled / ENTITIES_TO_KILL;
        essenceBlockEntity.getEventBar().setProgress(1 - newProgress);
        essenceBlockEntity.setExtraEventTrackingProgress(entitiesKilled);
        if (entitiesKilled == ENTITIES_TO_KILL) {
            EssenceBlockEntity.EndEvent(serverLevel, blockPos, blockState, essenceBlockEntity, true);
        }
    }

    private static void SpawnNewEnemy(ServerLevel serverLevel, BlockPos blockPos, EssenceBlockEntity essenceBlockEntity, int currentEntityCount, List<EssenceBlockEntity.EventEntities> eventEntitiesInArena) {
        TagKey<EntityType<?>> enemyTagToUse = BzTags.ESSENCE_RAGING_ARENA_NORMAL_ENEMY;
        int entityToSpawnIndex = currentEntityCount + 1;
        if ((entityToSpawnIndex % 25) == 0 ||
            entityToSpawnIndex == 49 ||
            entityToSpawnIndex == 73 ||
            entityToSpawnIndex == 74 ||
            entityToSpawnIndex == 97 ||
            entityToSpawnIndex == 98 ||
            entityToSpawnIndex == 99)
        {
            enemyTagToUse = BzTags.ESSENCE_RAGING_ARENA_BOSS_ENEMY; // boss at 25, 49, 50, 73, 74, 75, 97, 98, 99, and 100
        }
        else if ((entityToSpawnIndex % 5) == 0) {
            enemyTagToUse = BzTags.ESSENCE_RAGING_ARENA_STRONG_ENEMY; // strong at every 5 except when boss
        }
        else if ((entityToSpawnIndex % 3) == 0) {
            enemyTagToUse = BzTags.ESSENCE_RAGING_ARENA_RANGED_ENEMY; // ranged at every 3 except when boss or strong
        }

        List<? extends EntityType<?>> entityTypeList = BuiltInRegistries.ENTITY_TYPE
                .getTag(enemyTagToUse)
                .map(holders -> holders
                        .stream()
                        .map(Holder::value)
                        .toList()
                ).orElseGet(ArrayList::new);

        EntityType<?> entityTypeToSpawn = entityTypeList.get(serverLevel.getRandom().nextInt(entityTypeList.size()));
        int yOffset = (essenceBlockEntity.getArenaSize().getY() - 4) / 2;
        Entity entity = entityTypeToSpawn.spawn(serverLevel, blockPos.offset(0, yOffset, 0), MobSpawnType.TRIGGERED);
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
                                isEssenced ? 0.05 : 0.065,
                                AttributeModifier.Operation.ADDITION));
                    }

                    AttributeInstance livingEntityAttributeFlyingSpeed = livingEntity.getAttribute(Attributes.FLYING_SPEED);
                    if (livingEntityAttributeFlyingSpeed != null) {
                        livingEntityAttributeFlyingSpeed.addPermanentModifier(new AttributeModifier(
                                UUID.fromString("c762c216-0a3a-11ee-be56-0242ac120002"),
                                "Essence Arena Flying Speed Boost",
                                0.065,
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
                    if (entity instanceof Rabbit rabbit) {
                        rabbit.setVariant(Rabbit.Variant.EVIL);
                    }
                }
            }
        }
    }
}
