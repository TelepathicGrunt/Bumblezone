package net.telepathicgrunt.bumblezone.blocks;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.items.BzItems;


@Mod.EventBusSubscriber(modid = Bumblezone.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SugarWaterEvents
{

	private static final ResourceLocation	TEXTURE_UNDERWATER	= new ResourceLocation(Bumblezone.MODID + ":textures/misc/sugar_water_underwater.png");
	public static Block						SUGAR_WATER_BLOCK;

	public static void setup()
	{
		SUGAR_WATER_BLOCK = BzBlocks.SUGAR_WATER_BLOCK.get().getDefaultState().getBlock();
	}


	@SubscribeEvent
	public static void sugarWaterOverlay(RenderBlockOverlayEvent event)
	{
		if (event.getPlayer().world.getBlockState(event.getBlockPos()).getBlock() == SUGAR_WATER_BLOCK)
		{
			Minecraft minecraftIn = Minecraft.getInstance();
			minecraftIn.getTextureManager().bindTexture(TEXTURE_UNDERWATER);
			BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
			float f = minecraftIn.player.getBrightness();
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
			float f7 = -minecraftIn.player.rotationYaw / 64.0F;
			float f8 = minecraftIn.player.rotationPitch / 64.0F;
			Matrix4f matrix4f = event.getMatrixStack().getLast().getMatrix();
			bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR_TEX);
			bufferbuilder.pos(matrix4f, -1.0F, -1.0F, -0.5F).color(f, f, f, 0.42F).tex(4.0F + f7, 4.0F + f8).endVertex();
			bufferbuilder.pos(matrix4f, 1.0F, -1.0F, -0.5F).color(f, f, f, 0.42F).tex(0.0F + f7, 4.0F + f8).endVertex();
			bufferbuilder.pos(matrix4f, 1.0F, 1.0F, -0.5F).color(f, f, f, 0.42F).tex(0.0F + f7, 0.0F + f8).endVertex();
			bufferbuilder.pos(matrix4f, -1.0F, 1.0F, -0.5F).color(f, f, f, 0.42F).tex(4.0F + f7, 0.0F + f8).endVertex();
			bufferbuilder.finishDrawing();
			WorldVertexBufferUploader.draw(bufferbuilder);
			RenderSystem.disableBlend();
			event.setCanceled(true);
		}
	}


	/**
	 * Allow player to harvest sugar water with bottles
	 */
	@SubscribeEvent
	public static void getSugarWater(PlayerInteractEvent.RightClickItem event)
	{
		PlayerEntity playerEntity = (PlayerEntity) event.getEntity();
		World world = event.getWorld();
		RayTraceResult raytraceresult = rayTrace(world, playerEntity, RayTraceContext.FluidMode.SOURCE_ONLY);

		if (raytraceresult.getType() == RayTraceResult.Type.BLOCK && 
			world.getBlockState(((BlockRayTraceResult)raytraceresult).getPos()) == BzBlocks.SUGAR_WATER_BLOCK.get().getDefaultState())
		{
			Hand playerHand = event.getHand();
			ItemStack itemstack = playerEntity.getHeldItem(playerHand);

			if (itemstack.getItem() == Items.GLASS_BOTTLE)
			{
				world.playSound(playerEntity, playerEntity.getPosX(), playerEntity.getPosY(), playerEntity.getPosZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
				itemstack.shrink(1); // remove current honey bottle

				if (itemstack.isEmpty())
				{
					playerEntity.setHeldItem(playerHand, new ItemStack(BzItems.SUGAR_WATER_BOTTLE.get())); // places sugar water bottle in hand
				}
				else if (!playerEntity.inventory.addItemStackToInventory(new ItemStack(BzItems.SUGAR_WATER_BOTTLE.get()))) // places sugar water bottle in inventory
				{
					playerEntity.dropItem(new ItemStack(BzItems.SUGAR_WATER_BOTTLE.get()), false); // drops sugar water bottle if inventory is full
				}

				event.setCanceled(true);
			}
		}
	}


	//*borrowed* from the Item class lol
	protected static RayTraceResult rayTrace(World worldIn, PlayerEntity player, RayTraceContext.FluidMode fluidMode)
	{
		float f = player.rotationPitch;
		float f1 = player.rotationYaw;
		Vec3d vec3d = player.getEyePosition(1.0F);
		float f2 = MathHelper.cos(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
		float f3 = MathHelper.sin(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
		float f4 = -MathHelper.cos(-f * ((float) Math.PI / 180F));
		float f5 = MathHelper.sin(-f * ((float) Math.PI / 180F));
		float f6 = f3 * f4;
		float f7 = f2 * f4;
		double d0 = player.getAttribute(PlayerEntity.REACH_DISTANCE).getValue();;
		Vec3d vec3d1 = vec3d.add((double) f6 * d0, (double) f5 * d0, (double) f7 * d0);
		return worldIn.rayTraceBlocks(new RayTraceContext(vec3d, vec3d1, RayTraceContext.BlockMode.OUTLINE, fluidMode, player));
	}
}
