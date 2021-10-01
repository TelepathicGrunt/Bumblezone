package com.telepathicgrunt.bumblezone.items;

import com.telepathicgrunt.bumblezone.entities.nonliving.PollenPuffEntity;
import com.telepathicgrunt.bumblezone.modinit.BzItems;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

public class PollenPuff extends Item {
    public PollenPuff(Item.Settings properties) {
        super(properties);

        DispenserBlock.registerBehavior(this, new ProjectileDispenserBehavior() {
            protected ProjectileEntity createProjectile(World world, Position position, ItemStack itemStack) {
                return Util.make(new PollenPuffEntity(
                        world,
                        position.getX(),
                        position.getY(),
                        position.getZ()),
                        (pollenPuffEntity) -> pollenPuffEntity.setItem(itemStack));
            }
        });
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity playerEntity, LivingEntity entity, Hand hand) {
        // No clientside exit early because if we return early, the use method runs on server and thus, throws pollen puff and depollinates bee at same time.
        if (!(entity instanceof BeeEntity)) return ActionResult.PASS;

        BeeEntity beeEntity = (BeeEntity)entity;
        ItemStack itemstack = playerEntity.getStackInHand(hand);

        // right clicking on pollinated bee with pollen puff with room, gets pollen puff into hand.
        // else, if done with pollen puff without room, drops pollen puff in world
        if(beeEntity.hasNectar() && itemstack.getItem().equals(BzItems.POLLEN_PUFF)) {
            PollenPuff.spawnItemstackEntity(entity.world, beeEntity.getBlockPos(), new ItemStack(BzItems.POLLEN_PUFF, 1));
            playerEntity.swingHand(hand, true);
            beeEntity.onHoneyDelivered();
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        ItemStack itemstack = playerEntity.getStackInHand(hand);

        if (!world.isClient()) {
            world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.random.nextFloat() * 0.4F + 0.8F));
                PollenPuffEntity pollenPuffEntity = new PollenPuffEntity(world, playerEntity);
                pollenPuffEntity.setItem(itemstack);
                pollenPuffEntity.setProperties(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 1.5F, 1.0F);
                world.spawnEntity(pollenPuffEntity);

            playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
            if (!playerEntity.isCreative()) {
                itemstack.decrement(1);
            }
        }

        return TypedActionResult.success(itemstack, world.isClient());
    }


    public static void spawnItemstackEntity(World world, BlockPos blockPos, ItemStack itemStack) {
        if (!world.isClient() && !itemStack.isEmpty()) {
            double x = (double)(world.random.nextFloat() * 0.5F) + 0.25D;
            double y = (double)(world.random.nextFloat() * 0.5F) + 0.25D;
            double z = (double)(world.random.nextFloat() * 0.5F) + 0.25D;
            ItemEntity itemEntity = new ItemEntity(world, (double)blockPos.getX() + x, (double)blockPos.getY() + y, (double)blockPos.getZ() + z, itemStack);
            itemEntity.setToDefaultPickupDelay();
            world.spawnEntity(itemEntity);
        }
    }
}