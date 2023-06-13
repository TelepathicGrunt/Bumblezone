package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.EssenceBlockEntity;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.screens.ServerEssenceEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class EssenceBlockRed extends EssenceBlock {
    private static final float ENTITIES_TO_KILL = 50;

    public EssenceBlockRed() {
        super(Properties.of().mapColor(MapColor.COLOR_RED));
    }

    @Override
    public ResourceLocation getArenaNbt() {
        return new ResourceLocation(Bumblezone.MODID, "essence/red_arena");
    }

    @Override
    public int getEventTimeFrame() {
        return 12000;
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

        float currentProgress = Math.round((1 - essenceBlockEntity.getEventBar().getProgress()) * 100) / 100f;
        int entitiesKilled = (int) (currentProgress * ENTITIES_TO_KILL);

        List<EssenceBlockEntity.EventEntities> eventEntitiesInArena = essenceBlockEntity.getEventEntitiesInArena();
        if (entitiesKilled != ENTITIES_TO_KILL && eventEntitiesInArena.size() < Math.min(4 + (essenceBlockEntity.getPlayerInArena().size() / 2), ENTITIES_TO_KILL - entitiesKilled)) {
            // spawn a mob this tick.
            SpawnNewEnemy(serverLevel, blockPos, essenceBlockEntity, entitiesKilled, eventEntitiesInArena);
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
            }
        }

        float newProgress = entitiesKilled / ENTITIES_TO_KILL;
        essenceBlockEntity.getEventBar().setProgress(1 - newProgress);
        if (entitiesKilled == ENTITIES_TO_KILL) {
            EssenceBlockEntity.EndEvent(serverLevel, blockPos, blockState, essenceBlockEntity, true);
        }
    }

    private static void SpawnNewEnemy(ServerLevel serverLevel, BlockPos blockPos, EssenceBlockEntity essenceBlockEntity, int entitiesKilled, List<EssenceBlockEntity.EventEntities> eventEntitiesInArena) {
        TagKey<EntityType<?>> enemyTagToUse = BzTags.ESSENCE_RAGING_ARENA_NORMAL_ENEMY;
        if (((entitiesKilled + 1) % 25) == 0) {
            enemyTagToUse = BzTags.ESSENCE_RAGING_ARENA_BOSS_ENEMY; // boss at 25 and 50
        }
        else if (((entitiesKilled + 1) % 5) == 0) {
            enemyTagToUse = BzTags.ESSENCE_RAGING_ARENA_STRONG_ENEMY; // strong at every 5 except when boss
        }
        else if (((entitiesKilled + 1) % 3) == 0) {
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
        Entity entity = entityTypeToSpawn.spawn(serverLevel, blockPos.offset(0, -5, 0), MobSpawnType.TRIGGERED);
        if (entity != null) {
            eventEntitiesInArena.add(new EssenceBlockEntity.EventEntities(entity.getUUID()));

            UUID playerUUID = essenceBlockEntity.getPlayerInArena().get(serverLevel.getRandom().nextInt(essenceBlockEntity.getPlayerInArena().size()));
            Player player = serverLevel.getPlayerByUUID(playerUUID);
            if (player != null) {
                float maxHeart = player.getMaxHealth();
                float maxArmor = player.getArmorValue();
                float mobHealthBoost = (maxHeart / 15) + (maxArmor / 15);
                float mobAttackBoost = (maxHeart / 20) + (maxArmor / 20);

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
                }

                if (entity instanceof NeutralMob neutralMob) {
                    neutralMob.setRemainingPersistentAngerTime(Integer.MAX_VALUE);
                    neutralMob.setPersistentAngerTarget(playerUUID);
                    neutralMob.setTarget(player);
                }
                else if (entity instanceof Mob mob) {
                    mob.setTarget(player);
                    if (entity instanceof Rabbit rabbit) {
                        rabbit.setVariant(Rabbit.Variant.EVIL);
                    }
                }
            }
        }
    }
}
