package com.telepathicgrunt.the_bumblezone.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

public class WindParticle extends TextureSheetParticle {
    private final SingleQuadParticle.FacingCameraMode facingCameraMode;

    private WindParticle(ClientLevel clientWorld, double xPos, double yPos, double zPos, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites, boolean isEnvironmental) {
        super(clientWorld, xPos, yPos, zPos);

        if (isEnvironmental) {
            this.xd += 0.1d;
            this.yd += 0;
            this.zd += 0;
            this.lifetime = 100 + this.random.nextInt(10);
        }
        else {
            this.xd += xSpeed;
            this.yd += ySpeed;
            this.zd += zSpeed;
            this.lifetime = 25 + this.random.nextInt(5);
        }

        this.gravity = 0;
        this.quadSize *= (this.random.nextFloat() * 0.2f) + 0.7f;
        this.hasPhysics = false;
        this.sprite = sprites.get(0, 1);

        Vector3f directionVec = new Vector3f((float) this.xd, (float) this.yd, (float) this.zd).normalize();
        facingCameraMode = (quaternionf, camera, f) -> {
            quaternionf.identity();
            quaternionf.rotateX(Mth.PI / 2);
            quaternionf.lookAlong(directionVec, camera.getLookVector()).conjugate();
        };
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

    @Override
    public SingleQuadParticle.FacingCameraMode getFacingCameraMode() {
        return this.facingCameraMode;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        private final boolean isEnvironmental;

        public Factory(SpriteSet sprite, boolean isEnvironmental) {
            this.sprites = sprite;
            this.isEnvironmental = isEnvironmental;
        }

        @Override
        public Particle createParticle(SimpleParticleType particleType, ClientLevel clientWorld, double xPos, double yPos, double zPos, double xSpeed, double ySpeed, double zSpeed) {
            return new WindParticle(clientWorld, xPos, yPos, zPos, xSpeed, ySpeed, zSpeed, sprites, isEnvironmental);
        }
    }
}