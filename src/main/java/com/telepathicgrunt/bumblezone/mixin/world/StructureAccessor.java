package com.telepathicgrunt.bumblezone.mixin.world;

import net.minecraft.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(Structure.class)
public interface StructureAccessor {

    @Accessor("blockInfoLists")
    List<Structure.PalettedBlockInfoList> thebumblezone_getBlocks();
}
