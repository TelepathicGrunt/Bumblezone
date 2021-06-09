package com.telepathicgrunt.bumblezone.entities;

import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.effects.WrathOfTheHiveEffect;
import com.telepathicgrunt.bumblezone.modinit.BzEffects;
import com.telepathicgrunt.bumblezone.modinit.BzItems;
import com.telepathicgrunt.bumblezone.tags.BZItemTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class BeeInteractivity {

    // heal bees with sugar water bottle or honey bottle
    public static void beeFeeding(World world, PlayerEntity playerEntity, Hand hand, Entity target) {
        if (!world.isClient && target instanceof BeeEntity) {

            BeeEntity beeEntity = (BeeEntity) target;
            ItemStack itemstack = playerEntity.getStackInHand(hand);
            Identifier itemRL = Registry.ITEM.getId(itemstack.getItem());

            // Disallow all non-tagged items from being fed to bees
            if(BZItemTags.BEE_FEEDING_ITEMS.contains(itemstack.getItem()))
                return;

            if(itemRL.getPath().contains("honey")){
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

                if(itemRL.getPath().contains("bowl") && !itemstack.getItem().equals(Items.BOWL)){
                    givePlayerContainer(playerEntity, hand, itemstack, Items.BOWL);
                }
                else if(itemRL.getPath().contains("bucket") && !itemstack.getItem().equals(Items.BUCKET)){
                    givePlayerContainer(playerEntity, hand, itemstack, Items.BOWL);
                }
                else if(itemRL.getPath().contains("bottle") && !itemstack.getItem().equals(Items.GLASS_BOTTLE)){
                    givePlayerContainer(playerEntity, hand, itemstack, Items.GLASS_BOTTLE);
                }
            }
            playerEntity.swingHand(hand, true);
        }
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

    private static void givePlayerContainer(PlayerEntity playerEntity, Hand hand, ItemStack itemstack, Item itemToGive) {
        if (itemstack.isEmpty()) {
            // places empty bowl in hand
            playerEntity.setStackInHand(hand, new ItemStack(itemToGive));
        }
        // places empty bottle in inventory
        else if (!playerEntity.inventory.insertStack(new ItemStack(itemToGive))) {
            // drops empty bottle if inventory is full
            playerEntity.dropItem(new ItemStack(itemToGive), false);
        }
    }
}