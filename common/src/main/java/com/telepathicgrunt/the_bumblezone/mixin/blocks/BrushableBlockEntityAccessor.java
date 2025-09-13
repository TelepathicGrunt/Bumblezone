package com.telepathicgrunt.the_bumblezone.mixin.blocks;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BrushableBlockEntity.class)
public interface BrushableBlockEntityAccessor {
    @Invoker("dropContent")
    void bumblezone$callDropContent(ServerLevel p_373112_, LivingEntity p_393620_, ItemStack p_372836_);
}
