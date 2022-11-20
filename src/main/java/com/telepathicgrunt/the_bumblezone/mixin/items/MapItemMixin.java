package com.telepathicgrunt.the_bumblezone.mixin.items;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.telepathicgrunt.the_bumblezone.modinit.BzDimension;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MapItem.class)
public class MapItemMixin {
    @ModifyExpressionValue(method = "update(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/dimension/DimensionType;hasCeiling()Z", ordinal = 1),
            require = 0)
    private boolean thebumblezone_filledMapForDimension1(boolean ceiling, Level level) {
        if (level.dimension().equals(BzDimension.BZ_WORLD_KEY)) {
            return false;
        }
        return ceiling;
    }

    @WrapOperation(method = "update(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/LevelChunk;getHeight(Lnet/minecraft/world/level/levelgen/Heightmap$Types;II)I", ordinal = 0),
            require = 0)
    private int thebumblezone_filledMapForDimension2(LevelChunk levelChunk, Heightmap.Types type, int x, int z, Operation<Integer> operation, Level level) {
        int scanHeight = operation.call(levelChunk, type, x, z);
        if (level.dimension().equals(BzDimension.BZ_WORLD_KEY) && scanHeight >= 250) {
            return 110;
        }
        return scanHeight;
    }
}