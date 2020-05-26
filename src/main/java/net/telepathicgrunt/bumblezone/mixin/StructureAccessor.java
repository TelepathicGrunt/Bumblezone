package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.structure.Structure;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(Structure.class)
public interface StructureAccessor {

    @Accessor("blocks")
    List<List<Structure.StructureBlockInfo>> getBlocks();

    @Accessor("entities")
    List<Structure.StructureEntityInfo> getEntities();

    @Accessor("size")
    BlockPos getSize();


    @Invoker("method_15179")
    void invokemethod_15179(IWorld iWorld, BlockPos blockPos, BlockMirror blockMirror, BlockRotation blockRotation, BlockPos blockPos2, BlockBox blockBox);
}
