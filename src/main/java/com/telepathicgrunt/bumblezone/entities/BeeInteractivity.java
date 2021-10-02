package com.telepathicgrunt.bumblezone.entities;

import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.effects.WrathOfTheHiveEffect;
import com.telepathicgrunt.bumblezone.items.PollenPuff;
import com.telepathicgrunt.bumblezone.mixin.entities.BeeEntityInvoker;
import com.telepathicgrunt.bumblezone.mixin.items.BucketItemAccessor;
import com.telepathicgrunt.bumblezone.modinit.BzEffects;
import com.telepathicgrunt.bumblezone.modinit.BzItems;
import com.telepathicgrunt.bumblezone.tags.BzEntityTags;
import com.telepathicgrunt.bumblezone.tags.BzItemTags;
import com.telepathicgrunt.bumblezone.utils.GeneralUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class BeeInteractivity {

    // heal bees with sugar water bottle or honey bottle
    public static ActionResult beeFeeding(World world, PlayerEntity playerEntity, Hand hand, Entity target) {
        if (!world.isClient() && target instanceof BeeEntity beeEntity) {

            ItemStack itemstack = playerEntity.getStackInHand(hand);
            Identifier itemRL = Registry.ITEM.getId(itemstack.getItem());

            // Disallow all non-tagged items from being fed to bees
            if(!BzItemTags.BEE_FEEDING_ITEMS.contains(itemstack.getItem()))
                return ActionResult.PASS;

            if (itemstack.getItem() == BzItems.HONEY_BUCKET) {
                beeEntity.heal(beeEntity.getMaxHealth() - beeEntity.getHealth());
                calmAndSpawnHearts(world, playerEntity, beeEntity, 0.8f, 5);
                if (beeEntity.isBaby()) {
                    if (world.getRandom().nextBoolean()) {
                        beeEntity.setBaby(false);
                    }
                }
                else {
                    for (BeeEntity nearbyBee : world.getEntitiesByClass(BeeEntity.class, beeEntity.getBoundingBox().expand(4), beeEntity1 -> true)) {
                        nearbyBee.lovePlayer(playerEntity);
                    }
                }
            }
            else if(itemRL.getPath().contains("honey")){
                beeEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1, 2, false, false, false));
                calmAndSpawnHearts(world, playerEntity, beeEntity, 0.3f, 3);
            }
            else{
                beeEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1, 1, false, false, false));
                calmAndSpawnHearts(world, playerEntity, beeEntity, 0.1f, 3);
            }

            if (!playerEntity.isCreative()) {
                // remove current item
                itemstack.decrement(1);
                GeneralUtils.givePlayerItem(playerEntity, hand, itemstack, true);
            }
            playerEntity.swingHand(hand, true);
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }


    public static ActionResult beeUnpollinating(World world, PlayerEntity playerEntity, Hand hand, BeeEntity beeEntity) {
        if (!world.isClient() && beeEntity.getType().isIn(BzEntityTags.POLLEN_PUFF_CAN_POLLINATE)) {
            ItemStack itemstack = playerEntity.getStackInHand(hand);
            Item item = itemstack.getItem();

            // right clicking on pollinated bee with empty hand or pollen puff with room, gets pollen puff into hand.
            // else, if done with watery items or pollen puff without room, drops pollen puff in world
            if(beeEntity.hasNectar()) {
                if((itemstack.getTag() != null && itemstack.getTag().getString("Potion").contains("water")) ||
                        item == Items.WET_SPONGE ||
                        item == BzItems.SUGAR_WATER_BOTTLE ||
                        (item instanceof BucketItem && ((BucketItemAccessor) item).thebumblezone_getFluid().isIn(FluidTags.WATER))) {
                    PollenPuff.spawnItemstackEntity(world, beeEntity.getBlockPos(), new ItemStack(BzItems.POLLEN_PUFF, 1));
                    playerEntity.swingHand(hand, true);
                    ((BeeEntityInvoker)beeEntity).thebumblezone_callSetHasNectar(false);
                    return ActionResult.SUCCESS;
                }
            }
        }

        return ActionResult.PASS;
    }

    private static void calmAndSpawnHearts(World world, PlayerEntity playerEntity, BeeEntity beeEntity, float calmChance, int hearts) {
        boolean calmed = world.random.nextFloat() < calmChance;
        if (calmed) {
            if(playerEntity.hasStatusEffect(BzEffects.WRATH_OF_THE_HIVE)){
                playerEntity.removeStatusEffect(BzEffects.WRATH_OF_THE_HIVE);
                WrathOfTheHiveEffect.calmTheBees(playerEntity.world, playerEntity);
            }
            else{
                playerEntity.addStatusEffect(new StatusEffectInstance(
                        BzEffects.PROTECTION_OF_THE_HIVE,
                        Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.howLongProtectionOfTheHiveLasts,
                        2,
                        false,
                        false,
                        true));
            }
        }

        if (!beeEntity.hasAngerTime() || calmed)
            ((ServerWorld) world).spawnParticles(
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
}