package com.telepathicgrunt.bumblezone.entities;

import com.telepathicgrunt.bumblezone.entities.mobs.HoneySlimeEntity;
import com.telepathicgrunt.bumblezone.modinit.BzEntities;
import com.telepathicgrunt.bumblezone.tags.BZItemTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

public class CreatingHoneySlime {
    // heal bees with sugar water bottle or honey bottle
    public static void createHoneySlime(World world, PlayerEntity playerEntity, Hand hand, Entity target) {
        ItemStack itemstack = playerEntity.getStackInHand(hand);
        if (!world.isClient() && target.getType().equals(EntityType.SLIME) && BZItemTags.TURN_SLIME_TO_HONEY_SLIME.contains(itemstack.getItem())) {

            SlimeEntity slimeEntity = (SlimeEntity)target;
            int slimeSize = slimeEntity.getSize();
            HoneySlimeEntity honeySlimeMob = BzEntities.HONEY_SLIME.create(world);
            if(honeySlimeMob == null || slimeSize > 2) return;

            honeySlimeMob.refreshPositionAndAngles(
                    target.getX(),
                    target.getY(),
                    target.getZ(),
                    target.yaw,
                    target.pitch);

            honeySlimeMob.setBaby(slimeSize == 1);
            honeySlimeMob.initialize((ServerWorldAccess) world, world.getLocalDifficulty(new BlockPos(honeySlimeMob.getPos())), SpawnReason.TRIGGERED, null, null);
            world.spawnEntity(honeySlimeMob);
            target.remove();

            world.playSound(
                    playerEntity,
                    playerEntity.getX(),
                    playerEntity.getY(),
                    playerEntity.getZ(),
                    SoundEvents.BLOCK_SLIME_BLOCK_STEP,
                    SoundCategory.NEUTRAL,
                    1.0F,
                    1.0F);

            if (!playerEntity.isCreative()) {
                // remove current honey item
                itemstack.decrement(1);
            }

            playerEntity.swingHand(hand, true);
        }
    }
}
