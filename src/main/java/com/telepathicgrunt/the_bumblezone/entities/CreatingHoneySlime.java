package com.telepathicgrunt.the_bumblezone.entities;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.effects.BzEffects;
import com.telepathicgrunt.the_bumblezone.effects.WrathOfTheHiveEffect;
import com.telepathicgrunt.the_bumblezone.entities.mobs.HoneySlimeEntity;
import com.telepathicgrunt.the_bumblezone.items.BzItems;
import com.telepathicgrunt.the_bumblezone.modCompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.tags.BZItemTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;

public class CreatingHoneySlime {
    // heal bees with sugar water bottle or honey bottle
    public static void createHoneySlime(World world, PlayerEntity playerEntity, Hand hand, Entity target) {
        ItemStack itemstack = playerEntity.getHeldItem(hand);
        if (!world.isRemote && target.getType().equals(EntityType.SLIME) && BZItemTags.TURN_SLIME_TO_HONEY_SLIME.contains(itemstack.getItem())) {

            SlimeEntity slimeEntity = (SlimeEntity)target;
            int slimeSize = slimeEntity.getSlimeSize();
            HoneySlimeEntity honeySlimeMob = BzEntities.HONEY_SLIME.get().create(world);
            if(honeySlimeMob == null || slimeSize > 2) return;

            honeySlimeMob.setLocationAndAngles(
                    target.getPosX(),
                    target.getPosY(),
                    target.getPosZ(),
                    target.rotationYaw,
                    target.rotationPitch);

            honeySlimeMob.setChild(slimeSize == 1);
            honeySlimeMob.onInitialSpawn((IServerWorld) world, world.getDifficultyForLocation(new BlockPos(honeySlimeMob.getPositionVec())), SpawnReason.TRIGGERED, null, null);
            world.addEntity(honeySlimeMob);
            target.remove();

            world.playSound(
                    playerEntity,
                    playerEntity.getPosX(),
                    playerEntity.getPosY(),
                    playerEntity.getPosZ(),
                    SoundEvents.BLOCK_SLIME_BLOCK_STEP,
                    SoundCategory.NEUTRAL,
                    1.0F,
                    1.0F);

            if (!playerEntity.isCreative()) {
                // remove current honey item
                itemstack.shrink(1);
            }

            playerEntity.swing(hand, true);
        }
    }
}
