package com.telepathicgrunt.bumblezone.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;

public class PollenPuff extends TextureSheetParticle {
    private PollenPuff(ClientLevel clientWorld, double xPos, double yPos, double zPos, double xSpeed, double ySpeed, double zSpeed, TextureAtlasSprite sprite) {
        super(clientWorld, xPos, yPos, zPos);
        this.xd += xSpeed;
        this.yd += ySpeed;
        this.zd += zSpeed;
        this.setSize(0.1F, 0.1F);
        this.gravity = -0.0005F * (this.random.nextFloat() * 0.5f + 0.5f);
        this.quadSize *= (this.random.nextFloat() * 0.5f + 0.63f);
        this.age = 90 + this.random.nextInt(90);
        this.hasPhysics = false;
        this.sprite = sprite;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.preMoveUpdate();
        if (!this.removed) {
            this.yd -= this.gravity;
            this.move(this.xd, this.yd, this.zd);
            this.xd *= 0.98F;
            this.yd *= 0.98F;
            this.zd *= 0.98F;
        }
    }

    protected void preMoveUpdate() {
        if (this.age-- <= 0) {
            this.remove();
        }
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Factory(SpriteSet sprite) {
            this.sprites = sprite;
        }

        @Override
        public Particle createParticle(SimpleParticleType particleType, ClientLevel clientWorld, double xPos, double yPos, double zPos, double xSpeed, double ySpeed, double zSpeed) {
            return new PollenPuff(clientWorld, xPos, yPos, zPos, xSpeed, ySpeed, zSpeed, sprites.get(clientWorld.random));
        }
    }
}