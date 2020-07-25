package net.telepathicgrunt.bumblezone.mixin;

import io.github.cottonmc.cotton.config.ConfigManager;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.server.command.ReloadCommand;
import net.minecraft.world.SaveProperties;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.configs.BzConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collection;

@Mixin(ReloadCommand.class)
public class ReloadCommandMixin {

    @Inject(method = "method_29478",
            at = @At(value = "HEAD"))
    private static void reloadBumblezoneConfig(ResourcePackManager<?> resourcePackManager, SaveProperties saveProperties, Collection<String> collection, CallbackInfoReturnable<Collection<String>> cir) {
        Bumblezone.BZ_CONFIG = ConfigManager.loadConfig(BzConfig.class);
    }
}