package com.telepathicgrunt.the_bumblezone.mixin.fabricbase.client;

import com.telepathicgrunt.the_bumblezone.events.client.RegisterParticleEvent;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.function.Function;

@Mixin(ParticleEngine.class)
public abstract class ParticleEngineMixin {


    @Final
    @Shadow
    private Map<ResourceLocation, ParticleEngine.MutableSpriteSet> spriteSets;

    @Final
    @Shadow
    private Int2ObjectMap<ParticleProvider<?>> providers;

    @Inject(
            method = "registerProviders",
            at = @At("RETURN")
    )
    private void bumblezone$onInit(CallbackInfo ci) {
        RegisterParticleEvent.EVENT.invoke(new RegisterParticleEvent(this::bumblezone$register));
        System.out.println("bumblezone: reigster particles");
    }

    @Unique
    private <T extends ParticleOptions> void bumblezone$register(ParticleType<T> particleType, Function<SpriteSet, ParticleProvider<T>> spriteParticleRegistration) {
        ParticleEngine.MutableSpriteSet mutableSpriteSet = new ParticleEngine.MutableSpriteSet();
        this.spriteSets.put(BuiltInRegistries.PARTICLE_TYPE.getKey(particleType), mutableSpriteSet);
        this.providers.put(BuiltInRegistries.PARTICLE_TYPE.getId(particleType), spriteParticleRegistration.apply(mutableSpriteSet));
    }
}
