package com.telepathicgrunt.the_bumblezone.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

public class WindParticle extends TextureSheetParticle {
    private WindParticle(ClientLevel clientWorld, double xPos, double yPos, double zPos, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(clientWorld, xPos, yPos, zPos);
        this.xd += xSpeed;
        this.yd += ySpeed;
        this.zd += zSpeed;
        this.gravity = 0;
        this.quadSize *= (this.random.nextFloat() * 0.2f) + 0.7f;
        this.lifetime = 25 + this.random.nextInt(5);
        this.hasPhysics = false;

        boolean isVertical = Math.abs(this.yd) > ((Math.abs(this.xd) + Math.abs(this.zd)) / 2);
        this.sprite = sprites.get(isVertical? 1 : 0, 1);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age > this.lifetime / 2) {
            this.setAlpha(1.0f - ((float)this.age - (float)(this.lifetime / 2)) / (float)this.lifetime);
        }
    }

    @Override
    public void move(double d, double e, double f) {
        this.setBoundingBox(this.getBoundingBox().move(d, e, f));
        this.setLocationFromBoundingbox();
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Factory(SpriteSet sprite) {
            this.sprites = sprite;
        }

        @Override
        public Particle createParticle(SimpleParticleType particleType, ClientLevel clientWorld, double xPos, double yPos, double zPos, double xSpeed, double ySpeed, double zSpeed) {
            return new WindParticle(clientWorld, xPos, yPos, zPos, xSpeed, ySpeed, zSpeed, sprites);
        }
    }
}