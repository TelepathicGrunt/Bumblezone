package net.telepathicgrunt.bumblezone.dimension;

import net.fabricmc.fabric.api.dimension.v1.EntityPlacer;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

/**
 * Derived from modmuss50 at https://github.com/modmuss50/SimpleVoidWorld/blob/1.15/src/main/java/me/modmuss50/svw/VoidPlacementHandler.java
 *
 */
public class BzPlacement
{
	public static final EntityPlacer ENTERING = (teleported, destination, portalDir, horizontalOffset, verticalOffset) ->
	{
		BlockPos pos = enterBZ(teleported, (ServerWorld) teleported.getEntityWorld(), destination);
		return new BlockPattern.TeleportTarget(new Vec3d(pos), Vec3d.ZERO, 0);
	};

	public static final EntityPlacer LEAVING = (teleported, destination, portalDir, horizontalOffset, verticalOffset) ->
	{
		BlockPos pos = leaveBZ(teleported, (ServerWorld) teleported.getEntityWorld(), destination);
		return new BlockPattern.TeleportTarget(new Vec3d(pos), Vec3d.ZERO, 0);
	};


	private static BlockPos enterBZ(Entity entity, ServerWorld previousWorld, ServerWorld newWorld)
	{
		BlockPos spawnPos = new BlockPos(0, 100, 0);
		entity.refreshPositionAndAngles(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), 0, 0);

		return spawnPos;
	}


	private static BlockPos leaveBZ(Entity entity, ServerWorld previousWorld, ServerWorld newWorld)
	{
		BlockPos spawnPos = new BlockPos(0, 100, 0);
		entity.refreshPositionAndAngles(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), 0, 0);
		return spawnPos;
	}
}
