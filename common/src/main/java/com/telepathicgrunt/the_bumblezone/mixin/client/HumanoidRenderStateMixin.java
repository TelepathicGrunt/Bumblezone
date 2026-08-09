package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.telepathicgrunt.the_bumblezone.client.rendering.armor.HumanoidRenderStateInterface;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(HumanoidRenderState.class)
public class HumanoidRenderStateMixin implements HumanoidRenderStateInterface {

    @Unique
    boolean bumblezone_isLeggingsPollinated = false;

    @Unique
    int bumblezone_chestplateVariant = 1;

    @Unique
    boolean bumblezone_isChestplateFlying = false;

    @Override
    public boolean theBumblezone$isLeggingsPollinated() {
        return bumblezone_isLeggingsPollinated;
    }

    @Override
    public void theBumblezone$setIsLeggingsPollinated(boolean value) {
        bumblezone_isLeggingsPollinated = value;
    }

    @Override
    public int theBumblezone$getChestplateVariant() {
        return bumblezone_chestplateVariant;
    }

    @Override
    public void theBumblezone$setChestplateVariant(int value) {
        bumblezone_chestplateVariant = value;
    }

    @Override
    public boolean theBumblezone$isChestplateFlying() {
        return bumblezone_isChestplateFlying;
    }

    @Override
    public void theBumblezone$setIsChestplateFlying(boolean value) {
        bumblezone_isChestplateFlying = value;
    }
}