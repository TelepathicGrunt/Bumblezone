package com.telepathicgrunt.the_bumblezone.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.WeakHashMap;

public class LivingEntityFlyingSoundInstance extends AbstractTickableSoundInstance {
    protected final LivingEntity livingEntity;
    private static final WeakHashMap<Entity, SoundInstance> activeSounds = new WeakHashMap<>();

    public LivingEntityFlyingSoundInstance(LivingEntity livingEntity, SoundEvent soundEvent, SoundSource soundSource) {
        super(soundEvent, soundSource, livingEntity.getRandom());
        this.livingEntity = livingEntity;
        this.x = (float) livingEntity.getX();
        this.y = (float) livingEntity.getY();
        this.z = (float) livingEntity.getZ();
        this.looping = true;
        this.delay = 0;
        this.volume = 0.0F;
    }

    public void tick() {
        if (!this.livingEntity.isRemoved()) {
            this.x = (float)this.livingEntity.getX();
            this.y = (float)this.livingEntity.getY();
            this.z = (float)this.livingEntity.getZ();
            if (!this.livingEntity.isOnGround()) {
                float speed = (float)this.livingEntity.getDeltaMovement().length();
                this.pitch = Mth.lerp(Mth.clamp(speed, this.getMinPitch(), this.getMaxPitch()), this.getMinPitch(), this.getMaxPitch());
                this.volume = Mth.lerp(Mth.clamp(speed, 0.05F, 0.5F), 0.0F, 1.2F);
            }
            else {
                this.pitch = 0.0F;
                this.volume = 0.0F;
            }
        }
        else {
            this.stop();
            activeSounds.remove(this.livingEntity);
        }
    }

    private float getMinPitch() {
        return this.livingEntity.isBaby() ? 1.1F : 0.7F;
    }

    private float getMaxPitch() {
        return this.livingEntity.isBaby() ? 1.5F : 1.1F;
    }

    public boolean canStartSilent() {
        return true;
    }

    public boolean canPlaySound() {
        return !this.livingEntity.isSilent();
    }

    public static void playSound(LivingEntity livingEntity, SoundEvent soundEvent) {
        if(!activeSounds.containsKey(livingEntity)) {
            LivingEntityFlyingSoundInstance beesoundinstance = new LivingEntityFlyingSoundInstance(livingEntity, soundEvent, SoundSource.NEUTRAL);
            activeSounds.put(livingEntity, beesoundinstance);
            Minecraft.getInstance().getSoundManager().queueTickingSound(beesoundinstance);
        }
    }

    public static void stopSound(LivingEntity livingEntity, SoundEvent soundEvent) {
        if(activeSounds.containsKey(livingEntity)) {
            Minecraft.getInstance().getSoundManager().stop(activeSounds.get(livingEntity));
            activeSounds.remove(livingEntity);
        }
    }
}