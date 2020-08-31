package com.telepathicgrunt.the_bumblezone.mixin;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(Template.class)
public interface TemplateInvoker {

    @Accessor("blocks")
    List<Template.Palette> getBlocks();

    @Accessor("entities")
    List<Template.EntityInfo> getEntities();

    @Accessor("size")
    BlockPos getSize();

    @Invoker("addEntitiesToWorld")
    void invokeSpawnEntities(IServerWorld serverIWorld, BlockPos pos, PlacementSettings placementIn);
}
