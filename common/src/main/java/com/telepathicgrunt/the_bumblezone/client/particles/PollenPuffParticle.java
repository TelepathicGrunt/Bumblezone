package com.telepathicgrunt.the_bumblezone.client.particles;

import com.telepathicgrunt.the_bumblezone.mixin.util.AABBAccessor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.AABB;

public class PollenPuffParticle extends SingleQuadParticle {
    private boolean stoppedByCollision = false;

    private PollenPuffParticle(ClientLevel clientWorld, double xPos, double yPos, double zPos, double xSpeed, double ySpeed, double zSpeed, TextureAtlasSprite sprite) {
        super(clientWorld, xPos, yPos, zPos, sprite);
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
    protected Layer getLayer() {
        return Layer.OPAQUE;
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

    @Override
    public void move(double x, double y, double z) {
        if (!this.stoppedByCollision) {
            if (x != 0.0 || y != 0.0 || z != 0.0) {

                // Reduce object allocation spam from my particle by a significant margin.
                // This particle spawns extremely frequently in Pollinated Fields biome.
                AABB aabb = this.getBoundingBox();
                ((AABBAccessor)aabb).bumblezone$setMinX(aabb.minX + x);
                ((AABBAccessor)aabb).bumblezone$setMaxX(aabb.maxX + x);
                ((AABBAccessor)aabb).bumblezone$setMinY(aabb.minY + y);
                ((AABBAccessor)aabb).bumblezone$setMaxY(aabb.maxY + y);
                ((AABBAccessor)aabb).bumblezone$setMinZ(aabb.minZ + z);
                ((AABBAccessor)aabb).bumblezone$setMaxZ(aabb.maxZ + z);

                this.setLocationFromBoundingbox();
            }

            if (Math.abs(y) >= 1.0E-5F && Math.abs(y) < 1.0E-5F) {
                this.stoppedByCollision = true;
            }
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
        public Particle createParticle(SimpleParticleType particleType, ClientLevel clientWorld, double xPos, double yPos, double zPos, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new PollenPuffParticle(clientWorld, xPos, yPos, zPos, xSpeed, ySpeed, zSpeed, sprites.get(random));
        }
    }
}