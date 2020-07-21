package net.telepathicgrunt.bumblezone;

import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.telepathicgrunt.bumblezone.dimension.BzDimensionRegistration;

public class BumblezoneAmbientSounds extends TickableSound {
	protected final ClientPlayerEntity clientPlayer;

	public BumblezoneAmbientSounds(ClientPlayerEntity player) {
		super(SoundEvents.ENTITY_BEE_LOOP, SoundCategory.NEUTRAL);
		this.clientPlayer = player;
		this.x = (float) player.getPosX();
		this.y = (float) player.getPosY();
		this.z = (float) player.getPosZ();
		this.repeat = true;
		this.repeatDelay = 0;
		this.volume = 0.0F;
	}

	public void tick() {
		if (this.clientPlayer.isAlive() && this.clientPlayer.dimension == BzDimensionRegistration.bumblezone()) {
			this.x = (float) this.clientPlayer.getPosX();
			this.y = (float) this.clientPlayer.getPosY();
			this.z = (float) this.clientPlayer.getPosZ();
			float f = MathHelper.sqrt(Entity.horizontalMag(this.clientPlayer.getMotion()));
			
			float multiplier = 1f;
			if(this.clientPlayer.world.getBlockState(this.clientPlayer.getPosition()).getBlock() == Blocks.CAVE_AIR ||
				this.clientPlayer.world.getBlockState(this.clientPlayer.getPosition().up()).getBlock() == Blocks.CAVE_AIR) 
			{
				multiplier = 1.75f;
			}
			
			if ((double) f >= 0.01D) {
				this.pitch = MathHelper.lerp(MathHelper.clamp(f, this.getMinPitch() * multiplier, this.getMaxPitch() * multiplier),
						this.getMinPitch() * multiplier, this.getMaxPitch() * multiplier);
				this.volume = MathHelper.lerp(MathHelper.clamp(f, 0.005F * multiplier, 0.02F * multiplier), 0.005F * multiplier, 0.02F * multiplier);
			} else {
				this.pitch = 1.32F * multiplier;
				this.volume = 0.0052F * multiplier;
			}
			
			

		} else {
			this.donePlaying = true;
		}
	}

	private float getMinPitch() {
		return 0.8F;
	}

	private float getMaxPitch() {
		return 1.5F;
	}

	protected TickableSound func_225642_o_() {
		return new BeeBuzzSound(this.clientPlayer);
	}

	protected boolean func_225643_p_() {
		return true;
	}

	class BeeBuzzSound extends TickableSound {
		protected final ClientPlayerEntity player;
		private boolean field_229358_p_;

		public BeeBuzzSound(ClientPlayerEntity player) {
			super(SoundEvents.ENTITY_BEE_LOOP_AGGRESSIVE, SoundCategory.NEUTRAL);
			this.player = player;
			this.x = (float) player.getPosX();
			this.y = (float) player.getPosY();
			this.z = (float) player.getPosZ();
			this.repeat = true;
			this.repeatDelay = 0;
			this.volume = 0.0F;
			this.repeatDelay = 0;
		}

		public void tick() {
			boolean flag = this.func_225643_p_();
			if (flag && !this.donePlaying) {
				Minecraft.getInstance().getSoundHandler().func_229364_a_(this.func_225642_o_());
				this.field_229358_p_ = true;
			}

			if (this.player.isAlive() && !this.field_229358_p_) {
				this.x = (float) this.player.getPosX();
				this.y = (float) this.player.getPosY();
				this.z = (float) this.player.getPosZ();
				float f = MathHelper.sqrt(Entity.horizontalMag(this.player.getMotion()));
				if ((double) f >= 0.01D) {
					this.pitch = MathHelper.lerp(MathHelper.clamp(f, this.getMinPitch(), this.getMaxPitch()),
							this.getMinPitch(), this.getMaxPitch());
					this.volume = MathHelper.lerp(MathHelper.clamp(f, 0.0F, 0.5F), 0.0F, 1.2F);
				} else {
					this.pitch = 0.0F;
					this.volume = 0.0F;
				}

			} else {
				this.donePlaying = true;
			}
		}

		private float getMinPitch() {
			return this.player.isChild() ? 1.1F : 0.7F;
		}

		private float getMaxPitch() {
			return this.player.isChild() ? 1.5F : 1.1F;
		}

		public boolean canBeSilent() {
			return true;
		}

		protected TickableSound func_225642_o_() {
			return new BeeBuzzSound(this.player);
		}

		protected boolean func_225643_p_() {
			return true;
		}
	}
}