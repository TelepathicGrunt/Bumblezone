package com.telepathicgrunt.the_bumblezone.client.rendering.armor;

/// Needed so we can send needed data to armor model animations to do proper animating
public interface HumanoidRenderStateInterface {

	boolean theBumblezone$isLeggingsPollinated();

	void theBumblezone$setIsLeggingsPollinated(boolean value);

	int theBumblezone$getChestplateVariant();

	void theBumblezone$setChestplateVariant(int value);

	boolean theBumblezone$isChestplateFlying();

	void theBumblezone$setIsChestplateFlying(boolean value);
}
