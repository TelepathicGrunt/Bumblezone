package net.telepathicgrunt.bumblezone.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.modinit.BzEffects;
import net.telepathicgrunt.bumblezone.effects.WrathOfTheHiveEffect;
import net.telepathicgrunt.bumblezone.modinit.BzItems;

public class BeeInteractivity {

    // heal bees with sugar water bottle or honey bottle
    public static void beeFeeding(World world, PlayerEntity playerEntity, Hand hand, Entity target) {
        if (!world.isClient && target instanceof BeeEntity) {

            BeeEntity beeEntity = (BeeEntity) target;
            ItemStack itemstack = playerEntity.getStackInHand(hand);

            if (itemstack.getItem() == Items.HONEY_BOTTLE || itemstack.getItem() == BzItems.SUGAR_WATER_BOTTLE) {

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
                    beeEntity.addStatusEffect(new StatusEffectInstance(
                            StatusEffects.INSTANT_HEALTH,
                            1,
                            1,
                            false,
                            false,
                            false));

                    // high chance to remove wrath of the hive from player
                    boolean calmed = world.getRandom().nextFloat() < 0.3f;
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
                                3,
                                world.getRandom().nextFloat() * 0.5 - 1f,
                                world.getRandom().nextFloat() * 0.2f + 0.2f,
                                world.getRandom().nextFloat() * 0.5 - 1f,
                                world.getRandom().nextFloat() * 0.4 + 0.2f);

                }
                // Sugar water bottle
                else {
                    // Heal bee slightly but they remain angry
                    beeEntity.addStatusEffect(new StatusEffectInstance(
                            StatusEffects.INSTANT_HEALTH,
                            1,
                            0,
                            false,
                            false,
                            false));

                    // very low chance to remove wrath of the hive from player
                    boolean calmed = world.getRandom().nextFloat() < 0.07f;
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
                                1,
                                world.getRandom().nextFloat() * 0.5 - 0.25f,
                                world.getRandom().nextFloat() * 0.2f + 0.2f,
                                world.getRandom().nextFloat() * 0.5 - 0.25f,
                                world.getRandom().nextFloat() * 0.4 + 0.2f);
                }

                if (!playerEntity.isCreative()) {

                    // remove current honey bottle
                    itemstack.decrement(1);

                    if (itemstack.isEmpty()) {
                        // places empty bottle in hand
                        playerEntity.setStackInHand(hand, new ItemStack(Items.GLASS_BOTTLE));
                    }
                    // places empty bottle in inventory
                    else if (!playerEntity.inventory.insertStack(new ItemStack(Items.GLASS_BOTTLE))) {
                        // drops empty bottle if inventory is full
                        playerEntity.dropItem(new ItemStack(Items.GLASS_BOTTLE), false);
                    }
                }

                playerEntity.swingHand(hand, true);
            }
        }
    }
}