package com.telepathicgrunt.the_bumblezone.entities;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.effects.WrathOfTheHiveEffect;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.tags.BZItemTags;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;

public class BeeInteractivity {

    private static final ResourceLocation STICKY_HONEY_WAND = new ResourceLocation("buzzier_bees:sticky_honey_wand");

    // heal bees with sugar water bottle or honey bottle
    public static void beeFeeding(World world, PlayerEntity playerEntity, Hand hand, BeeEntity beeEntity) {
        if (!world.isClientSide) {
            ItemStack itemstack = playerEntity.getItemInHand(hand);
            ResourceLocation itemRL = itemstack.getItem().getRegistryName();

            // Disallow all non-tagged items from being fed to bees
            if(itemRL == null || !BZItemTags.BEE_FEEDING_ITEMS.contains(itemstack.getItem()))
                return;

            // Special case of item feeding
            if (ModChecker.buzzierBeesPresent &&
                    itemRL.equals(STICKY_HONEY_WAND) && Bumblezone.BzModCompatibilityConfig.allowHoneyWandCompat.get())
            {
                // Heal bee a bit
                beeEntity.addEffect(new EffectInstance(Effects.HEAL, 1, 1, false, false, false));

                // neutral chance to remove wrath of the hive from player
                calmAndSpawnHearts(world, playerEntity, beeEntity, 0.2f, 3);
                consumeItem(playerEntity, hand, itemstack, ForgeRegistries.ITEMS.getValue(STICKY_HONEY_WAND));
            }
            // generalized feeding
            else {
                if(itemRL.getPath().contains("honey")){
                    beeEntity.addEffect(new EffectInstance(Effects.HEAL, 1, 2, false, false, false));
                    calmAndSpawnHearts(world, playerEntity, beeEntity, 0.3f, 3);
                }
                else{
                    beeEntity.addEffect(new EffectInstance(Effects.HEAL, 1, 1, false, false, false));
                    calmAndSpawnHearts(world, playerEntity, beeEntity, 0.1f, 3);
                }

                if (!playerEntity.isCreative()) {
                    // remove current item
                    itemstack.shrink(1);

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
                playerEntity.swing(hand, true);
            }
        }
    }

    public static ActionResultType beeUnpollinating(World world, PlayerEntity playerEntity, Hand hand, BeeEntity beeEntity) {
        if (!world.isClientSide) {
            ItemStack itemstack = playerEntity.getItemInHand(hand);
            Item item = itemstack.getItem();

            // right clicking on pollinated bee with empty hand or pollen puff with room, gets pollen puff into hand.
            // else, if done with watery items or pollen puff without room, drops pollen puff in world
            if(beeEntity.hasNectar()) {
                 if(itemstack.isEmpty() ||
                        itemstack.getOrCreateTag().getString("Potion").contains("water") ||
                        item == Items.WET_SPONGE ||
                        item == BzItems.SUGAR_WATER_BOTTLE.get() ||
                        (item instanceof BucketItem && ((BucketItem) item).getFluid().is(FluidTags.WATER))) {
                    Block.popResource(world, beeEntity.blockPosition(), new ItemStack(BzItems.POLLEN_PUFF.get(), 1));
                    playerEntity.swing(hand, true);
                    beeEntity.dropOffNectar();
                    return ActionResultType.SUCCESS;
                 }
            }
        }

        return ActionResultType.PASS;
    }

    private static void calmAndSpawnHearts(World world, PlayerEntity playerEntity, BeeEntity beeEntity, float calmChance, int hearts) {
        boolean calmed = world.random.nextFloat() < calmChance;
        if (calmed) {
            if(playerEntity.hasEffect(BzEffects.WRATH_OF_THE_HIVE.get())){
                playerEntity.removeEffect(BzEffects.WRATH_OF_THE_HIVE.get());
                WrathOfTheHiveEffect.calmTheBees(playerEntity.level, playerEntity);
            }
            else{
                playerEntity.addEffect(new EffectInstance(
                        BzEffects.PROTECTION_OF_THE_HIVE.get(),
                        Bumblezone.BzBeeAggressionConfig.howLongProtectionOfTheHiveLasts.get(),
                        2,
                        false,
                        false,
                        true));
            }
        }

        if (!beeEntity.isAngry() || calmed)
            ((ServerWorld) world).sendParticles(
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

    private static void consumeItem(PlayerEntity playerEntity, Hand hand, ItemStack handItemstack, Item replacementItem) {
        if (!playerEntity.isCreative()) {

            // remove current bee soup
            handItemstack.shrink(1);

            givePlayerContainer(playerEntity, hand, handItemstack, replacementItem);
        }
        else {
            playerEntity.swing(hand, true);
        }
    }

    private static void givePlayerContainer(PlayerEntity playerEntity, Hand hand, ItemStack itemstack, Item itemToGive) {
        if (itemstack.isEmpty()) {
            // places empty bowl in hand
            playerEntity.setItemInHand(hand, new ItemStack(itemToGive));
        }
        // places empty bottle in inventory
        else if (!playerEntity.inventory.add(new ItemStack(itemToGive))) {
            // drops empty bottle if inventory is full
            playerEntity.drop(new ItemStack(itemToGive), false);
        }
    }
}