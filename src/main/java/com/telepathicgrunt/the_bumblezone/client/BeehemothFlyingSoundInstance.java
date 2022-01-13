package com.telepathicgrunt.the_bumblezone.client;

import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;

public class BeehemothFlyingSoundInstance extends AbstractTickableSoundInstance {
    protected final BeehemothEntity beehemothEntity;

    public BeehemothFlyingSoundInstance(BeehemothEntity beehemothEntity, SoundEvent soundEvent, SoundSource soundSource) {
        super(soundEvent, soundSource);
        this.beehemothEntity = beehemothEntity;
        this.x = (float)beehemothEntity.getX();
        this.y = (float)beehemothEntity.getY();
        this.z = (float)beehemothEntity.getZ();
        this.looping = true;
        this.delay = 0;
        this.volume = 0.0F;
    }

    public void tick() {
        if (!this.beehemothEntity.isRemoved()) {
            this.x = (float)this.beehemothEntity.getX();
            this.y = (float)this.beehemothEntity.getY();
            this.z = (float)this.beehemothEntity.getZ();
            if (!this.beehemothEntity.isOnGround()) {
                float speed = (float)this.beehemothEntity.getDeltaMovement().length();
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
        }
    }

    private float getMinPitch() {
        return this.beehemothEntity.isBaby() ? 1.1F : 0.7F;
    }

    private float getMaxPitch() {
        return this.beehemothEntity.isBaby() ? 1.5F : 1.1F;
    }

    public boolean canStartSilent() {
        return true;
    }

    public boolean canPlaySound() {
        return !this.beehemothEntity.isSilent();
    }

    public static void playSound(BeehemothEntity beehemothEntity) {
        BeehemothFlyingSoundInstance beesoundinstance = new BeehemothFlyingSoundInstance(beehemothEntity, BzSounds.BEEHEMOTH_LOOP.get(), SoundSource.NEUTRAL);
        Minecraft.getInstance().getSoundManager().queueTickingSound(beesoundinstance);
    }
}