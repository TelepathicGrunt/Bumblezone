package net.telepathicgrunt.bumblezone.entities;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.entity.Entity;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.World;
import net.telepathicgrunt.bumblezone.dimension.BzDimensionType;


public class MobSpawnLocationBehavior
{

	public static boolean MobSpawnMixin(Entity entity)
	{
		World world = entity.world;

		//Make sure we are on client I think. Vanilla does this check too
		if (!world.isClient)
		{
			//NO SPAWNING ON MY DIMENSION'S ROOF!!!
			if (entity.dimension == BzDimensionType.BUMBLEZONE_TYPE && entity.getPos().getY() >= 256)
			{
				//STOP SPAWNING!!!!!!!!
				return false;
			}
		}
		
		return true;
	}
}
