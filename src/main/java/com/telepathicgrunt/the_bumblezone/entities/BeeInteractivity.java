package com.telepathicgrunt.the_bumblezone.entities;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.effects.BzEffects;
import com.telepathicgrunt.the_bumblezone.effects.WrathOfTheHiveEffect;
import com.telepathicgrunt.the_bumblezone.items.BzItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class BeeInteractivity {

    // heal bees with sugar water bottle or honey bottle
    public static void beeFeeding(World world, PlayerEntity playerEntity, Hand hand, Entity target) {
        if (!world.isRemote && target instanceof BeeEntity) {

            BeeEntity beeEntity = (BeeEntity) target;
            ItemStack itemstack = playerEntity.getHeldItem(hand);

            if (itemstack.getItem() == Items.HONEY_BOTTLE || itemstack.getItem() == BzItems.SUGAR_WATER_BOTTLE.get()) {

                world.playSound(
                        playerEntity,
                        playerEntity.getX(),
                        playerEntity.getY(),
                        playerEntity.getZ(),
                        SoundEvents.ITEM_BOTTLE_EMPTY,
                        SoundCategory.NEUTRAL,
                        1.0F,
                        1.0F);

                if (itemstack.getItem() == Items.HONEY_BOTTLE) {

                    // Heal bee a lot
                    beeEntity.addPotionEffect(new EffectInstance(
                            Effects.INSTANT_HEALTH,
                            1,
                            1,
                            false,
                            false,
                            false));

                    // high chance to remove wrath of the hive from player
                    boolean calmed = world.getRandom().nextFloat() < 0.3f;
                    if (calmed) {
                        if(playerEntity.isPotionActive(BzEffects.WRATH_OF_THE_HIVE.get())){
                            playerEntity.removePotionEffect(BzEffects.WRATH_OF_THE_HIVE.get());
                            WrathOfTheHiveEffect.calmTheBees(playerEntity.world, playerEntity);
                        }
                        else{
                            playerEntity.addPotionEffect(new EffectInstance(
                                    BzEffects.PROTECTION_OF_THE_HIVE.get(),
                                    Bumblezone.BzBeeAggressionConfig.howLongProtectionOfTheHiveLasts.get(),
                                    2,
                                    false,
                                    false,
                                    true));
                        }
                    }

                    if (!beeEntity.hasAngerTime() || calmed)
                        ((ServerWorld) world).spawnParticle(
                                ParticleTypes.HEART,
                                beeEntity.getX(),
                                beeEntity.getY(),
                                beeEntity.getZ(),
                                3,
                                world.getRandom().nextFloat() * 0.5 - 1f,
                                world.getRandom().nextFloat() * 0.2f + 0.2f,
                                world.getRandom().nextFloat() * 0.5 - 1f,
                                world.getRandom().nextFloat() * 0.4 + 0.2f);

                }
                // Sugar water bottle
                else {
                    // Heal bee slightly but they remain angry
                    beeEntity.addPotionEffect(new EffectInstance(
                            Effects.INSTANT_HEALTH,
                            1,
                            0,
                            false,
                            false,
                            false));

                    // very low chance to remove wrath of the hive from player
                    boolean calmed = world.getRandom().nextFloat() < 0.07f;
                    if (calmed) {
                        if(playerEntity.isPotionActive(BzEffects.WRATH_OF_THE_HIVE.get())){
                            playerEntity.removePotionEffect(BzEffects.WRATH_OF_THE_HIVE.get());
                            WrathOfTheHiveEffect.calmTheBees(playerEntity.world, playerEntity);
                        }
                        else{
                            playerEntity.addPotionEffect(new EffectInstance(
                                    BzEffects.PROTECTION_OF_THE_HIVE.get(),
                                    Bumblezone.BzBeeAggressionConfig.howLongProtectionOfTheHiveLasts.get(),
                                    2,
                                    false,
                                    false,
                                    true));
                        }
                    }

                    if (!beeEntity.hasAngerTime() || calmed)
                        ((ServerWorld) world).spawnParticle(
                                ParticleTypes.HEART,
                                beeEntity.getX(),
                                beeEntity.getY(),
                                beeEntity.getZ(),
                                1,
                                world.getRandom().nextFloat() * 0.5 - 0.25f,
                                world.getRandom().nextFloat() * 0.2f + 0.2f,
                                world.getRandom().nextFloat() * 0.5 - 0.25f,
                                world.getRandom().nextFloat() * 0.4 + 0.2f);
                }

                if (!playerEntity.isCreative()) {

                    // remove current honey bottle
                    itemstack.shrink(1);

                    if (itemstack.isEmpty()) {
                        // places empty bottle in hand
                        playerEntity.setHeldItem(hand, new ItemStack(Items.GLASS_BOTTLE));
                    }
                    // places empty bottle in inventory
                    else if (!playerEntity.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE))) {
                        // drops empty bottle if inventory is full
                        playerEntity.dropItem(new ItemStack(Items.GLASS_BOTTLE), false);
                    }
                }

                playerEntity.swingHand(hand, true);
            }
        }
    }
}