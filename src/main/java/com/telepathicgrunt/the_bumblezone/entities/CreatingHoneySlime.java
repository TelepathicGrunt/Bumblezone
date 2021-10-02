package com.telepathicgrunt.the_bumblezone.entities;

import com.telepathicgrunt.the_bumblezone.entities.mobs.HoneySlimeEntity;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.tags.BzItemTags;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

public class CreatingHoneySlime {
    // heal bees with sugar water bottle or honey bottle
    public static ActionResultType createHoneySlime(World world, PlayerEntity playerEntity, Hand hand, SlimeEntity slimeEntity) {
        ItemStack itemstack = playerEntity.getItemInHand(hand);
        if (!world.isClientSide() && BzItemTags.TURN_SLIME_TO_HONEY_SLIME.contains(itemstack.getItem())) {

            int slimeSize = slimeEntity.getSize();
            HoneySlimeEntity honeySlimeMob = BzEntities.HONEY_SLIME.get().create(world);
            if(honeySlimeMob == null || slimeSize > 2) return ActionResultType.PASS;

            honeySlimeMob.moveTo(
                    slimeEntity.getX(),
                    slimeEntity.getY(),
                    slimeEntity.getZ(),
                    slimeEntity.yRot,
                    slimeEntity.xRot);

            honeySlimeMob.setBaby(slimeSize == 1);
            honeySlimeMob.finalizeSpawn((IServerWorld) world, world.getCurrentDifficultyAt(new BlockPos(honeySlimeMob.position())), SpawnReason.TRIGGERED, null, null);
            world.addFreshEntity(honeySlimeMob);
            slimeEntity.remove();

            world.playSound(
                    playerEntity,
                    playerEntity.getX(),
                    playerEntity.getY(),
                    playerEntity.getZ(),
                    SoundEvents.SLIME_BLOCK_STEP,
                    SoundCategory.NEUTRAL,
                    1.0F,
                    1.0F);

            if (!playerEntity.isCreative()) {
                // remove current honey item
                itemstack.shrink(1);
                GeneralUtils.givePlayerItem(playerEntity, hand, itemstack, true);
            }

            playerEntity.swing(hand, true);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }
}
