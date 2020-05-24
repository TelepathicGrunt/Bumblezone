package net.telepathicgrunt.bumblezone.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;

public class ObtainSugarWaterBottle {

    public static boolean useBottleOnSugarWater(World world , PlayerEntity playerEntity, Hand playerHand){
        HitResult raytraceresult = rayTrace(world, playerEntity, RayTraceContext.FluidHandling.SOURCE_ONLY);

        if (raytraceresult.getType() == HitResult.Type.BLOCK &&
                world.getBlockState(((BlockHitResult)raytraceresult).getBlockPos()) == BzBlocks.SUGAR_WATER_BLOCK.getDefaultState())
        {
            ItemStack itemstack = playerEntity.getStackInHand(playerHand);

            if (itemstack.getItem() == Items.GLASS_BOTTLE)
            {
                world.playSound(playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                itemstack.decrement(1); // remove current honey bottle

                if (itemstack.isEmpty())
                {
                    playerEntity.setStackInHand(playerHand, new ItemStack(BzItems.SUGAR_WATER_BOTTLE)); // places sugar water bottle in hand
                }
                else if (!playerEntity.inventory.insertStack(new ItemStack(BzItems.SUGAR_WATER_BOTTLE))) // places sugar water bottle in inventory
                {
                    playerEntity.dropItem(new ItemStack(BzItems.SUGAR_WATER_BOTTLE), false); // drops sugar water bottle if inventory is full
                }

                return true;
            }
        }

        return false;
    }


    //*borrowed* from the Item class lol
    protected static HitResult rayTrace(World worldIn, PlayerEntity player, RayTraceContext.FluidHandling fluidMode)
    {
        float f = player.pitch;
        float f1 = player.yaw;
        Vec3d vec3d = player.getCameraPosVec(1.0F);
        float f2 = MathHelper.cos(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
        float f3 = MathHelper.sin(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
        float f4 = -MathHelper.cos(-f * ((float) Math.PI / 180F));
        float f5 = MathHelper.sin(-f * ((float) Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        Vec3d vec3d1 = vec3d.add((double) f6 * 5.0D, (double) f5 * 5.0D, (double) f7 * 5.0D);
        return worldIn.rayTrace(new RayTraceContext(vec3d, vec3d1, RayTraceContext.ShapeType.OUTLINE, fluidMode, player));
    }
}
