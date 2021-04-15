package net.telepathicgrunt.bumblezone.mixin.world;

import net.minecraft.structure.Structure;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(Structure.class)
public interface StructureAccessor {

    @Accessor("blockInfoLists")
    List<Structure.PalettedBlockInfoList> bz_getBlocks();
}
