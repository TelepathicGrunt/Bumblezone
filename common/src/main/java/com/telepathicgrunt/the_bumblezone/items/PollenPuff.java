package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.entities.nonliving.PollenPuffEntity;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.Vec3;

public class PollenPuff extends Item {
    public PollenPuff(Item.Properties properties) {
        super(properties);

        DispenserBlock.registerProjectileBehavior(this);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player playerEntity, InteractionHand hand) {
        ItemStack itemstack = playerEntity.getItemInHand(hand);

        if (!world.isClientSide()) {
            world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), BzSounds.POLLEN_PUFF_THROW.get(), SoundSource.PLAYERS, 0.5F, 0.4F / (playerEntity.getRandom().nextFloat() * 0.4F + 0.8F));
                PollenPuffEntity pollenPuffEntity = new PollenPuffEntity(world, playerEntity);
                pollenPuffEntity.setItem(itemstack);
                pollenPuffEntity.shootFromRotation(playerEntity, playerEntity.getXRot(), playerEntity.getYRot(), 0.0F, 1.5F, 1.0F);
                world.addFreshEntity(pollenPuffEntity);

            playerEntity.awardStat(Stats.ITEM_USED.get(this));
            if (!playerEntity.isCreative()) {
                itemstack.shrink(1);
            }
        }

        return InteractionResultHolder.sidedSuccess(itemstack, world.isClientSide());
    }

    public static void spawnItemstackEntity(Level world, RandomSource random, BlockPos blockPos, ItemStack itemStack) {
        if (!world.isClientSide() && !itemStack.isEmpty()) {
            double x = (double)(random.nextFloat() * 0.5F) + 0.25D;
            double y = (double)(random.nextFloat() * 0.5F) + 0.25D;
            double z = (double)(random.nextFloat() * 0.5F) + 0.25D;
            ItemEntity itemEntity = new ItemEntity(world, (double)blockPos.getX() + x, (double)blockPos.getY() + y, (double)blockPos.getZ() + z, itemStack);
            itemEntity.setDefaultPickUpDelay();
            world.addFreshEntity(itemEntity);
        }
    }

    public static void spawnItemstackEntity(Level world, Vec3 pos, ItemStack itemStack) {
        if (!world.isClientSide() && !itemStack.isEmpty()) {
            ItemEntity itemEntity = new ItemEntity(world, pos.x(), pos.y(), pos.z(), itemStack);
            itemEntity.setDefaultPickUpDelay();
            world.addFreshEntity(itemEntity);
        }
    }
}