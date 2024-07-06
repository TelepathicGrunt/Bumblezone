package com.telepathicgrunt.the_bumblezone.mixin.blocks;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BrushableBlockEntity.class)
public interface BrushableBlockEntityAccessor {
    @Invoker("dropContent")
    void bumblezone$callDropContent(Player player);
}
