package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.entities.nonliving.PollenPuffEntity;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.ProjectileDispenseBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.world.World;

public class PollenPuff extends Item {
    public PollenPuff(Item.Properties properties) {
        super(properties);

        DispenserBlock.registerBehavior(this, new ProjectileDispenseBehavior() {
            protected ProjectileEntity getProjectile(World world, IPosition position, ItemStack itemStack) {
                return Util.make(new PollenPuffEntity(
                        world,
                        position.x(),
                        position.y(),
                        position.z()),
                        (pollenPuffEntity) -> pollenPuffEntity.setItem(itemStack));
            }
        });
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        ItemStack itemstack = playerEntity.getItemInHand(hand);
        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
        if (!world.isClientSide) {
            PollenPuffEntity pollenPuffEntity = new PollenPuffEntity(world, playerEntity);
            pollenPuffEntity.setItem(itemstack);
            pollenPuffEntity.shootFromRotation(playerEntity, playerEntity.xRot, playerEntity.yRot, 0.0F, 1.5F, 1.0F);
            world.addFreshEntity(pollenPuffEntity);
        }

        playerEntity.awardStat(Stats.ITEM_USED.get(this));
        if (!playerEntity.abilities.instabuild) {
            itemstack.shrink(1);
        }

        return ActionResult.sidedSuccess(itemstack, world.isClientSide());
    }
}