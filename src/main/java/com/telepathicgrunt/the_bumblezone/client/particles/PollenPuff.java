package com.telepathicgrunt.the_bumblezone.client.particles;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;

public class PollenPuff extends SpriteTexturedParticle {
    private PollenPuff(ClientWorld clientWorld, double xPos, double yPos, double zPos, double xSpeed, double ySpeed, double zSpeed, TextureAtlasSprite sprite) {
        super(clientWorld, xPos, yPos, zPos);
        this.xd += xSpeed;
        this.yd += ySpeed;
        this.zd += zSpeed;
        this.setSize(0.1F, 0.1F);
        this.gravity = -0.0005F * (this.random.nextFloat() * 0.5f + 0.5f);
        this.quadSize *= (this.random.nextFloat() * 0.5f + 0.63f);
        this.lifetime = 90 + this.random.nextInt(90);
        this.hasPhysics = false;
        this.sprite = sprite;
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
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
            if (!this.removed) {
                this.xd *= 0.98F;
                this.yd *= 0.98F;
                this.zd *= 0.98F;
            }
        }
    }

    protected void preMoveUpdate() {
        if (this.lifetime-- <= 0) {
            this.remove();
        }
    }

    public static class Factory implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite sprites;

        public Factory(IAnimatedSprite sprite) {
            this.sprites = sprite;
        }

        @Override
        public Particle createParticle(BasicParticleType particleType, ClientWorld clientWorld, double xPos, double yPos, double zPos, double xSpeed, double ySpeed, double zSpeed) {
            return new PollenPuff(clientWorld, xPos, yPos, zPos, xSpeed, ySpeed, zSpeed, sprites.get(clientWorld.random));
        }
    }
}