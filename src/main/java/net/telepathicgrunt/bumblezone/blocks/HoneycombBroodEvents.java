package net.telepathicgrunt.bumblezone.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.modcompatibility.ModChecking;
import net.telepathicgrunt.bumblezone.modcompatibility.PotionOfBeesRedirection;


@Mod.EventBusSubscriber(modid = Bumblezone.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HoneycombBroodEvents
{
    /**
     * Allow player to revive thus block with Potion of Bees item
     */
    @SubscribeEvent
    public static void getSugarWater(PlayerInteractEvent.RightClickItem event) 
    {
	if(!ModChecking.potionOfBeesPresent && Bumblezone.BzConfig.allowPotionOfBeesCompat.get()) {
	    return;
	}
	
	PlayerEntity playerEntity = (PlayerEntity) event.getEntity();
	Hand playerHand = event.getHand();
	ItemStack itemstack = playerEntity.getHeldItem(playerHand);

	if (PotionOfBeesRedirection.POBIsPotionOfBeesItem(itemstack.getItem())) {
	    World world = event.getWorld();
	    RayTraceResult raytraceresult = rayTrace(world, playerEntity, RayTraceContext.BlockMode.COLLIDER);
	    BlockPos raytracedPos = ((BlockRayTraceResult) raytraceresult).getPos();
	    BlockState foundBlock = world.getBlockState(raytracedPos);
	   
	    if (raytraceresult.getType() == RayTraceResult.Type.BLOCK && foundBlock.getBlock() == BzBlocks.DEAD_HONEYCOMB_BROOD.get()) {

		playerEntity.swingArm(playerHand);
		world.playSound(playerEntity, playerEntity.getPosX(), playerEntity.getPosY(), playerEntity.getPosZ(), SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.NEUTRAL, 1.0F, 1.0F);
		world.setBlockState(raytracedPos, BzBlocks.HONEYCOMB_BROOD.get().getDefaultState()
			.with(HoneycombBrood.STAGE, Integer.valueOf(0))
			.with(DirectionalBlock.FACING, foundBlock.get(DirectionalBlock.FACING)));

		if (!playerEntity.isCreative())
		{
		    	itemstack.shrink(1); // remove current bee bottle

			if (itemstack.isEmpty())
			{
				playerEntity.setHeldItem(playerHand, new ItemStack(Items.GLASS_BOTTLE)); // places glass bottle in hand
			}
			else if (!playerEntity.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE))) // places glass bottle in inventory
			{
				playerEntity.dropItem(new ItemStack(Items.GLASS_BOTTLE), false); // drops glass bottle if inventory is full
			}
		}
		
		event.setCanceled(true);
	    }
	}
    }


    // *borrowed* from the Item class lol
    protected static RayTraceResult rayTrace(World world, PlayerEntity player, RayTraceContext.BlockMode blockMode) {
	float pitch = player.rotationPitch;
	float yaw = player.rotationYaw;
	Vec3d eyePos = player.getEyePosition(1.0F);
	float f2 = MathHelper.cos(-yaw * ((float) Math.PI / 180F) - (float) Math.PI);
	float f3 = MathHelper.sin(-yaw * ((float) Math.PI / 180F) - (float) Math.PI);
	float f4 = -MathHelper.cos(-pitch * ((float) Math.PI / 180F));
	float yTargetNormalized = MathHelper.sin(-pitch * ((float) Math.PI / 180F));
	float xTargetNormalized = f3 * f4;
	float zTargetNormalized = f2 * f4;
	double targetDistance = player.getAttribute(PlayerEntity.REACH_DISTANCE).getValue();
	Vec3d targetPos = eyePos.add((double) xTargetNormalized * targetDistance, (double) yTargetNormalized * targetDistance, (double) zTargetNormalized * targetDistance);
	return world.rayTraceBlocks(new RayTraceContext(eyePos, targetPos, blockMode, RayTraceContext.FluidMode.NONE, player));
    }
}
