package net.telepathicgrunt.bumblezone;

import net.minecraft.block.Blocks;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;

public class BumblezoneAmbientSounds extends TickableSound {
	protected final ClientPlayerEntity clientPlayer;

	public BumblezoneAmbientSounds(ClientPlayerEntity player) {
		super(SoundEvents.ENTITY_BEE_LOOP, SoundCategory.AMBIENT);
		this.clientPlayer = player;
		this.repeat = false;
		this.repeatDelay = 0;
		this.volume = 1.0F;
        this.global = true;
	}

	public void tick() {
		if (this.clientPlayer.isAlive()) {
			float volumeMultiplier = 1f;
			float pitchMultiplier = 1f;
			
			if(this.clientPlayer.world.getBlockState(this.clientPlayer.getPosition()).getBlock() == Blocks.CAVE_AIR ||
				this.clientPlayer.world.getBlockState(this.clientPlayer.getPosition().up()).getBlock() == Blocks.CAVE_AIR) 
			{
				volumeMultiplier = 1.75f;
				pitchMultiplier = 1.75f;
			}
			else if (this.clientPlayer.areEyesInFluid(FluidTags.WATER)) {
				volumeMultiplier = 0.75f;
				pitchMultiplier = 0.6f;
			}
			
			this.pitch = 1.32F * pitchMultiplier;
			this.volume = 0.015F * volumeMultiplier;

		} else {
			this.donePlaying = true;
		}
	}
}