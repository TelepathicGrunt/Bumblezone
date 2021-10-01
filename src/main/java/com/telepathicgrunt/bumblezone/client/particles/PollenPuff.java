package com.telepathicgrunt.bumblezone.client.particles;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

public class PollenPuff extends SpriteBillboardParticle {
    private PollenPuff(ClientWorld clientWorld, double xPos, double yPos, double zPos, double xSpeed, double ySpeed, double zSpeed, Sprite sprite) {
        super(clientWorld, xPos, yPos, zPos);
        this.velocityX += xSpeed;
        this.velocityY += ySpeed;
        this.velocityZ += zSpeed;
        this.setBoundingBoxSpacing(0.1F, 0.1F);
        this.gravityStrength = -0.0005F * (this.random.nextFloat() * 0.5f + 0.5f);
        this.scale *= (this.random.nextFloat() * 0.5f + 0.63f);
        this.age = 90 + this.random.nextInt(90);
        this.collidesWithWorld = false;
        this.sprite = sprite;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        this.preMoveUpdate();
        if (!this.dead) {
            this.velocityY -= this.gravityStrength;
            this.move(this.velocityX, this.velocityY, this.velocityZ);
            this.velocityX *= 0.98F;
            this.velocityY *= 0.98F;
            this.velocityZ *= 0.98F;
        }
    }

    protected void preMoveUpdate() {
        if (this.age-- <= 0) {
            this.markDead();
        }
    }

    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider sprites;

        public Factory(SpriteProvider sprite) {
            this.sprites = sprite;
        }

        @Override
        public Particle createParticle(DefaultParticleType particleType, ClientWorld clientWorld, double xPos, double yPos, double zPos, double xSpeed, double ySpeed, double zSpeed) {
            return new PollenPuff(clientWorld, xPos, yPos, zPos, xSpeed, ySpeed, zSpeed, sprites.getSprite(clientWorld.random));
        }
    }
}