package com.telepathicgrunt.bumblezone.client.particles;

import com.telepathicgrunt.bumblezone.tags.BzFluidTags;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.particle.WaterSuspendParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.FluidState;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;

public class HoneyParticle extends WaterSuspendParticle {
    private HoneyParticle(ClientWorld clientWorld, SpriteProvider spriteProvider, double xPos, double yPos, double zPos) {
        super(clientWorld, spriteProvider, xPos, yPos, zPos);
        this.colorRed = 1F;
        this.colorGreen = 0.65F;
        this.colorBlue = 0.0F;
        this.scale *= this.random.nextFloat() * 8.5F + 0.5F;
        this.age = (int)(440 * (Math.random() * 0.55D + 0.45D));
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.age-- <= 0) {
            this.markDead();
        }
        else {
            FluidState fluidState = this.world.getFluidState(new BlockPos(this.x, this.y, this.z));
            if (fluidState.isIn(BzFluidTags.HONEY_FLUID)) {
                if(fluidState.isStill()) {
                    this.move(this.velocityX, this.velocityY + 0.001D, this.velocityZ);
                }
                else {
                    this.move(this.velocityX, this.velocityY - 0.001D, this.velocityZ);
                }
            }
            else {
                this.markDead();
            }
        }
    }

    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider sprites;

        public Factory(SpriteProvider sprite) {
            this.sprites = sprite;
        }

        @Override
        public Particle createParticle(DefaultParticleType particleType, ClientWorld clientWorld, double xPos, double yPos, double zPos, double xSpeed, double ySpeed, double zSpeed) {
            HoneyParticle honeyParticle = new HoneyParticle(clientWorld, sprites, xPos, yPos, zPos);
            honeyParticle.setSprite(this.sprites);
            return honeyParticle;
        }
    }
}