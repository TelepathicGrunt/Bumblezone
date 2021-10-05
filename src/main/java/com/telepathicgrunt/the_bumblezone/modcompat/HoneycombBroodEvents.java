package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.HoneycombBrood;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class HoneycombBroodEvents
{
	/**
	 * Allow player to revive thus block with Potion of Bees item
	 */
	public static void reviveByPotionOfBees(PlayerInteractEvent.RightClickItem event)
	{
		if(!ModChecker.potionOfBeesPresent && Bumblezone.BzModCompatibilityConfig.allowPotionOfBeesCompat.get()) {
			return;
		}

		PlayerEntity playerEntity = (PlayerEntity) event.getEntity();
		Hand playerHand = event.getHand();
		ItemStack itemstack = playerEntity.getItemInHand(playerHand);

		if (PotionOfBeesCompat.POBIsPotionOfBeesItem(itemstack.getItem())) {
			World world = event.getWorld();
			RayTraceResult raytraceresult = rayTrace(world, playerEntity, RayTraceContext.BlockMode.COLLIDER);
			BlockPos raytracedPos = ((BlockRayTraceResult) raytraceresult).getBlockPos();
			BlockState foundBlock = world.getBlockState(raytracedPos);

			if (raytraceresult.getType() == RayTraceResult.Type.BLOCK && foundBlock.getBlock() == BzBlocks.EMPTY_HONEYCOMB_BROOD.get()) {

				playerEntity.swing(playerHand);
				world.playSound(playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.BOTTLE_EMPTY, SoundCategory.NEUTRAL, 1.0F, 1.0F);
				world.setBlockAndUpdate(raytracedPos, BzBlocks.HONEYCOMB_BROOD.get().defaultBlockState()
						.setValue(HoneycombBrood.STAGE, 0)
						.setValue(DirectionalBlock.FACING, foundBlock.getValue(DirectionalBlock.FACING)));

				if (!playerEntity.isCreative())
				{
					Item item = itemstack.getItem();
					itemstack.shrink(1);
					GeneralUtils.givePlayerItem(playerEntity, playerHand, new ItemStack(item), true);
				}

				event.setCanceled(true);
			}
		}
	}


	// *borrowed* from the Item class lol
	protected static RayTraceResult rayTrace(World world, PlayerEntity player, RayTraceContext.BlockMode blockMode) {
		float pitch = player.xRot;
		float yaw = player.yRot;
		Vector3d eyePos = player.getEyePosition(1.0F);
		float f2 = MathHelper.cos(-yaw * ((float) Math.PI / 180F) - (float) Math.PI);
		float f3 = MathHelper.sin(-yaw * ((float) Math.PI / 180F) - (float) Math.PI);
		float f4 = -MathHelper.cos(-pitch * ((float) Math.PI / 180F));
		float yTargetNormalized = MathHelper.sin(-pitch * ((float) Math.PI / 180F));
		float xTargetNormalized = f3 * f4;
		float zTargetNormalized = f2 * f4;
		double targetDistance = player.getAttribute(net.minecraftforge.common.ForgeMod.REACH_DISTANCE.get()).getValue();
		Vector3d targetPos = eyePos.add((double) xTargetNormalized * targetDistance, (double) yTargetNormalized * targetDistance, (double) zTargetNormalized * targetDistance);
		return world.clip(new RayTraceContext(eyePos, targetPos, blockMode, RayTraceContext.FluidMode.NONE, player));
	}
}