package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.telepathicgrunt.the_bumblezone.client.dimension.BzDimensionSpecialEffects;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.environment.AtmosphericFogEnvironment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AtmosphericFogEnvironment.class)
public abstract class AtmosphericFogEnvironmentMixin {

    @Definition(id = "Integer", type = Integer.class)
    @Definition(id = "getValue", method = "Lnet/minecraft/world/attribute/EnvironmentAttributeProbe;getValue(Lnet/minecraft/world/attribute/EnvironmentAttribute;F)Ljava/lang/Object;")
    @Definition(id = "FOG_COLOR", field = "Lnet/minecraft/world/attribute/EnvironmentAttributes;FOG_COLOR:Lnet/minecraft/world/attribute/EnvironmentAttribute;")
    @Expression("(Integer) ?.getValue(FOG_COLOR, ?)")
    @ModifyExpressionValue(method = "getBaseColor(Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/client/Camera;IF)I",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/attribute/EnvironmentAttributeProbe;getValue(Lnet/minecraft/world/attribute/EnvironmentAttribute;F)Ljava/lang/Object;", ordinal = 0))
    private Integer bumblezone$changeFogColor(Integer original, @Local(argsOnly = true) ClientLevel level) {
        return BzDimensionSpecialEffects.getBrightnessDependentFogColor(level, original);
    }
}