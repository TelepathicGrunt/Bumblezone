package com.telepathicgrunt.the_bumblezone.client.particles;

import com.telepathicgrunt.the_bumblezone.tags.BzFluidTags;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.UnderwaterParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.FluidState;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.BlockPos;

public class HoneyParticle extends UnderwaterParticle {
    private HoneyParticle(ClientWorld clientWorld, double xPos, double yPos, double zPos) {
        super(clientWorld, xPos, yPos, zPos);
        this.rCol = 1F;
        this.gCol = 0.65F;
        this.bCol = 0.0F;
        this.quadSize *= this.random.nextFloat() * 8.5F + 0.5F;
        this.lifetime = (int)(440 * (Math.random() * 0.55D + 0.45D));
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.lifetime-- <= 0) {
            this.remove();
        } else {
            FluidState fluidState = this.level.getFluidState(new BlockPos(this.x, this.y, this.z));
            if (fluidState.is(BzFluidTags.HONEY_FLUID)) {
                if(fluidState.isSource()) {
                    this.move(this.xd, this.yd + 0.001D, this.zd);
                }
                else {
                    this.move(this.xd, this.yd - 0.001D, this.zd);
                }
            }
            else {
                this.remove();
            }
        }
    }

    public static class Factory implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite sprites;

        public Factory(IAnimatedSprite sprite) {
            this.sprites = sprite;
        }

        @Override
        public Particle createParticle(BasicParticleType particleType, ClientWorld clientWorld, double xPos, double yPos, double zPos, double xSpeed, double ySpeed, double zSpeed) {
            HoneyParticle honeyParticle = new HoneyParticle(clientWorld, xPos, yPos, zPos);
            honeyParticle.pickSprite(this.sprites);
            return honeyParticle;
        }
    }
}