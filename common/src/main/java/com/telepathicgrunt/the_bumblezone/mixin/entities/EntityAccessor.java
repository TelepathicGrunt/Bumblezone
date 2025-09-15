package com.telepathicgrunt.the_bumblezone.mixin.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityAccessor {

    @Accessor("nextStep")
    void bumblezone$setNextStep(float nextStep);

    @Accessor("nextStep")
    float bumblezone$getNextStep();

    @Invoker("vibrationAndSoundEffectsFromBlock")
    boolean bumblezone$callVibrationAndSoundEffectsFromBlock(BlockPos blockPos, BlockState blockState, boolean bl, boolean bl2, Vec3 vec3);

    @Invoker("getTypeName")
    Component bumblezone$callGetTypeName();

    @Accessor("dimensions")
    EntityDimensions bumblezone$getDimensions();

    @Invoker("collide")
    Vec3 bumblezone$callCollide(Vec3 vec3);

    @Invoker("getEncodeId")
    String bumblezone$callGetEncodeId();
}