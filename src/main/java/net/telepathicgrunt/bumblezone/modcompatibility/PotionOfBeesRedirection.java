package net.telepathicgrunt.bumblezone.modcompatibility;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This class is used so java wont load BeesourcefulCompat class and crash
 * if the mod isn't on. This is because java will load classes if their method
 * is present even though it isn't called when going through some code. However,
 * java won't load classes referenced in the method ahead of time so the redirection
 * works to keep BeesourcefulCompat unloaded by "nesting" the method dependant on the mod.
 */
public class PotionOfBeesRedirection
{
	public static void POBReviveLarvaBlockEvent(net.minecraftforge.event.entity.ProjectileImpactEvent.Throwable event)
	{
	    PotionOfBeesCompat.POBReviveLarvaBlockEvent(event);
	}
	
	public static ActionResultType potionOfBeeInteract(ItemStack itemstack, BlockState thisBlockState, World world, BlockPos position, PlayerEntity playerEntity, Hand playerHand)
	{
	    return PotionOfBeesCompat.potionOfBeeInteract(itemstack, thisBlockState, world, position, playerEntity, playerHand);
	}
}
