package com.telepathicgrunt.the_bumblezone.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class VoiceParticle extends SimpleAnimatedParticle {
    VoiceParticle(ClientLevel level, double x, double y, double z, double lifetime, SpriteSet sprites) {
        super(level, x, y, z, sprites, 0F);
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        this.quadSize = 0.75f;
        this.lifetime = (int) lifetime;
        this.setSpriteFromAge(sprites);
    }

    public void move(double x, double y, double z) {
        this.setBoundingBox(this.getBoundingBox().move(x, y, z));
        this.setLocationFromBoundingbox();
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Factory(SpriteSet sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new VoiceParticle(level, x, y, z, xSpeed, this.sprites);
        }
    }
}