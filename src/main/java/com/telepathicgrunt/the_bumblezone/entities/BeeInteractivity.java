package com.telepathicgrunt.the_bumblezone.entities;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.effects.WrathOfTheHiveEffect;
import com.telepathicgrunt.the_bumblezone.items.PollenPuff;
import com.telepathicgrunt.the_bumblezone.mixin.entities.BeeEntityInvoker;
import com.telepathicgrunt.the_bumblezone.mixin.items.BucketItemAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.tags.BzEntityTags;
import com.telepathicgrunt.the_bumblezone.tags.BzItemTags;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class BeeInteractivity {

    // heal bees with sugar water bottle or honey bottle
    public static InteractionResult beeFeeding(Level world, Player playerEntity, InteractionHand hand, Entity target) {
        if (target instanceof Bee beeEntity) {

            ItemStack itemstack = playerEntity.getItemInHand(hand);
            ResourceLocation itemRL = Registry.ITEM.getKey(itemstack.getItem());

            // Disallow all non-tagged items from being fed to bees
            if(!BzItemTags.BEE_FEEDING_ITEMS.contains(itemstack.getItem()))
                return InteractionResult.PASS;
            if(world.isClientSide())
                return InteractionResult.SUCCESS;

            boolean removedWrath;
            ItemStack itemstackOriginal = itemstack.copy();

            // Special cased items so the ActionResultType continues and make the item's behavior not lost.
            if (itemstackOriginal.getItem() == BzItems.BEE_BREAD) {
                removedWrath = calmAndSpawnHearts(world, playerEntity, beeEntity, 0.3f, 3);

                if(removedWrath && playerEntity instanceof ServerPlayer) {
                    BzCriterias.FOOD_REMOVED_WRATH_OF_THE_HIVE_TRIGGER.trigger((ServerPlayer) playerEntity, itemstackOriginal);
                }

                playerEntity.swing(hand, true);
                return InteractionResult.PASS;
            }

            if (itemstack.is(BzItemTags.HONEY_BUCKETS)) {
                beeEntity.heal(beeEntity.getMaxHealth() - beeEntity.getHealth());
                removedWrath = calmAndSpawnHearts(world, playerEntity, beeEntity, 0.8f, 5);
                if (beeEntity.isBaby()) {
                    if (world.getRandom().nextBoolean()) {
                        beeEntity.setBaby(false);
                        if(playerEntity instanceof ServerPlayer) {
                            BzCriterias.HONEY_BUCKET_BEE_GROW_TRIGGER.trigger((ServerPlayer) playerEntity);
                        }
                    }
                }
                else {
                    int nearbyAdultBees = 0;
                    for (Bee nearbyBee : world.getEntitiesOfClass(Bee.class, beeEntity.getBoundingBox().inflate(4), beeEntity1 -> true)) {
                        nearbyBee.setInLove(playerEntity);
                        if(!nearbyBee.isBaby()) nearbyAdultBees++;
                    }

                    if(nearbyAdultBees >= 2 && playerEntity instanceof ServerPlayer) {
                        BzCriterias.HONEY_BUCKET_BEE_LOVE_TRIGGER.trigger((ServerPlayer) playerEntity);
                    }
                }
            }
            else if(itemRL.getPath().contains("honey")) {
                beeEntity.heal(2);
                removedWrath = calmAndSpawnHearts(world, playerEntity, beeEntity, 0.3f, 3);
            }
            else{
                beeEntity.heal(1);
                removedWrath = calmAndSpawnHearts(world, playerEntity, beeEntity, 0.1f, 3);
            }

            if (!playerEntity.isCreative()) {
                GeneralUtils.givePlayerItem(playerEntity, hand, new ItemStack(itemstack.getItem()), true, true);
            }

            if(removedWrath && playerEntity instanceof ServerPlayer) {
                BzCriterias.FOOD_REMOVED_WRATH_OF_THE_HIVE_TRIGGER.trigger((ServerPlayer) playerEntity, itemstackOriginal);
            }

            playerEntity.swing(hand, true);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }


    public static InteractionResult beeUnpollinating(Level world, Player playerEntity, InteractionHand hand, Bee beeEntity) {
        if (beeEntity.getType().is(BzEntityTags.POLLEN_PUFF_CAN_POLLINATE)) {
            ItemStack itemstack = playerEntity.getItemInHand(hand);
            Item item = itemstack.getItem();

            // right clicking on pollinated bee with empty hand or pollen puff with room, gets pollen puff into hand.
            // else, if done with watery items or pollen puff without room, drops pollen puff in world
            if(beeEntity.hasNectar()) {
                if((itemstack.getTag() != null && itemstack.getTag().getString("Potion").contains("water")) ||
                        item == Items.WET_SPONGE ||
                        item == BzItems.SUGAR_WATER_BOTTLE ||
                        (item instanceof BucketItem && ((BucketItemAccessor) item).thebumblezone_getFluid().is(FluidTags.WATER))) {

                    if(world.isClientSide())
                        return InteractionResult.SUCCESS;

                    PollenPuff.spawnItemstackEntity(world, beeEntity.blockPosition(), new ItemStack(BzItems.POLLEN_PUFF, 1));
                    playerEntity.swing(hand, true);
                    ((BeeEntityInvoker)beeEntity).thebumblezone_callSetHasNectar(false);

                    if(playerEntity instanceof ServerPlayer) {
                        BzCriterias.BEE_DROP_POLLEN_PUFF_TRIGGER.trigger((ServerPlayer) playerEntity, itemstack);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.PASS;
    }

    public static boolean calmAndSpawnHearts(Level world, Player playerEntity, LivingEntity beeEntity, float calmChance, int hearts) {
        boolean calmed = world.random.nextFloat() < calmChance;
        boolean removedWrath = false;
        if (calmed) {
            if(playerEntity.hasEffect(BzEffects.WRATH_OF_THE_HIVE)) {
                playerEntity.removeEffect(BzEffects.WRATH_OF_THE_HIVE);
                WrathOfTheHiveEffect.calmTheBees(playerEntity.level, playerEntity);
                removedWrath = true;
            }

            playerEntity.addEffect(new MobEffectInstance(
                    BzEffects.PROTECTION_OF_THE_HIVE,
                    Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.howLongProtectionOfTheHiveLasts,
                    2,
                    false,
                    false,
                    true));
        }

        if (beeEntity instanceof Bee ?
            (!((Bee)beeEntity).isAngry() || calmed) :
            calmed)
        {
            ((ServerLevel) world).sendParticles(
                    ParticleTypes.HEART,
                    beeEntity.getX(),
                    beeEntity.getY(),
                    beeEntity.getZ(),
                    hearts,
                    world.getRandom().nextFloat() * 0.5 - 0.25f,
                    world.getRandom().nextFloat() * 0.2f + 0.2f,
                    world.getRandom().nextFloat() * 0.5 - 0.25f,
                    world.getRandom().nextFloat() * 0.4 + 0.2f);
        }

        return removedWrath;
    }
}