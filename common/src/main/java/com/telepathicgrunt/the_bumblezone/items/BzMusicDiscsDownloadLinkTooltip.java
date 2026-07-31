package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.utils.GeneralUtilsClient;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.JukeboxPlayable;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.TooltipFlag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class BzMusicDiscsDownloadLinkTooltip {

    private static final Map<Optional<ResourceKey<JukeboxSong>>, String> SONG_WITH_DOWNLOAD_LINKS = new HashMap<>();

    public static void SetupTooltipData() {
        SONG_WITH_DOWNLOAD_LINKS.clear();
        AddSong(BzItems.MUSIC_DISC_HONEY_BEE_RAT_FACED_BOY.get());
        AddSong(BzItems.MUSIC_DISC_RIVERS_OF_HONEY_MOSERAO.get());
        AddSong(BzItems.MUSIC_DISC_LA_BEE_DA_LOCA.get());
        AddSong(BzItems.MUSIC_DISC_BEE_LAXING_WITH_THE_HOM_BEES.get());
        AddSong(BzItems.MUSIC_DISC_BEE_WARE_OF_THE_TEMPLE.get());
        AddSong(BzItems.MUSIC_DISC_A_LAST_FIRST_LAST.get());
        AddSong(BzItems.MUSIC_DISC_DROWNING_IN_DESPAIR.get());
        AddSong(BzItems.MUSIC_DISC_BEENNA_BOX.get());
    }

    public static void AddSong(Item item) {
        JukeboxPlayable jukeboxPlayable = item.components().get(DataComponents.JUKEBOX_PLAYABLE);
        if (jukeboxPlayable != null) {
            SONG_WITH_DOWNLOAD_LINKS.put(jukeboxPlayable.song().unwrapKey(), item.getDescriptionId() + ".download");
        }
        else {
            Bumblezone.LOGGER.error("Unable to setup song download link data for {}", item);
        }
    }

    public static void appendDownloadLinkText(JukeboxPlayable jukeboxPlayable, Item.TooltipContext tooltipContext, Consumer<Component> consumer, TooltipFlag tooltipFlag) {
        if (SONG_WITH_DOWNLOAD_LINKS.containsKey(jukeboxPlayable.song().unwrapKey())) {
            if (tooltipContext.registries() != null) {
                Optional<Integer> songDescLength = jukeboxPlayable
                        .song()
                        .unwrap()
                        .right()
                        .map(holder -> holder.description().getString().length());

                int length = songDescLength.orElse(20);

                List<MutableComponent> componentList = GeneralUtilsClient.autoWrappedTooltip(
                        length,
                        SONG_WITH_DOWNLOAD_LINKS.get(jukeboxPlayable.song().unwrapKey()));

                componentList.forEach(component -> consumer.accept(
                        component.withStyle(tooltipFlag.isAdvanced() ? ChatFormatting.DARK_PURPLE : ChatFormatting.DARK_GRAY)
                ));
            }
        }
    }
}
