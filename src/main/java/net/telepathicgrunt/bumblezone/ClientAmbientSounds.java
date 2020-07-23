package net.telepathicgrunt.bumblezone;

import net.minecraft.client.audio.IAmbientSoundHandler;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.telepathicgrunt.bumblezone.dimension.BzDimensionRegistration;

public class ClientAmbientSounds implements IAmbientSoundHandler {
	private final ClientPlayerEntity player;
	private final SoundHandler soundHandler;
	private int delay = 0;

	public ClientAmbientSounds(ClientPlayerEntity playerIn, SoundHandler soundHandlerIn) {
	      this.player = playerIn;
	      this.soundHandler = soundHandlerIn;
	   }

	public void tick() {
		--this.delay;
		if (this.delay <= 0 && Bumblezone.BzConfig.ambienceNoise.get() && this.player.dimension == BzDimensionRegistration.bumblezone()) {
			this.delay = 0;
			this.soundHandler.play(new BumblezoneAmbientSounds(player));
		}

	}
}