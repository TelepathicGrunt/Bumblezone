package com.telepathicgrunt.the_bumblezone.forge;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.BumblezoneClient;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.AddBuiltinResourcePacks;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.resource.PathPackResources;

import java.nio.file.Path;

public class BumblezoneForge {

    public BumblezoneForge() {
        Bumblezone.init();

        if (FMLEnvironment.dist == Dist.CLIENT) {
            BumblezoneClient.subscribeClientEvents();
        }

        FMLJavaModLoadingContext.get().getModEventBus().addListener(BumblezoneForge::onRegisterPackFinder);
    }

    public static void onRegisterPackFinder(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            AddBuiltinResourcePacks.EVENT.invoke(new AddBuiltinResourcePacks((id, displayName, mode) -> {
                Path resourcePath = ModList.get().getModFileById(id.getNamespace()).getFile().findResource("resourcepacks/" + id.getPath());

                var pack = Pack.readMetaAndCreate("builtin/add_pack_finders_test", displayName, mode == AddBuiltinResourcePacks.PackMode.FORCE_ENABLED,
                        (path) -> new PathPackResources(path, true, resourcePath), event.getPackType(), Pack.Position.BOTTOM, createSource(mode));
                event.addRepositorySource((packConsumer) -> packConsumer.accept(pack));
            }));
        }
    }

    private static PackSource createSource(AddBuiltinResourcePacks.PackMode mode) {
        final Component text = Component.translatable("pack.source.builtin");
        return PackSource.create(
                component -> Component.translatable("pack.nameAndSource", component, text).withStyle(ChatFormatting.GRAY),
                mode != AddBuiltinResourcePacks.PackMode.USER_CONTROLLED
        );
    }
}
