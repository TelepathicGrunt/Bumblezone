package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.JukeboxSong;

public class BzJukeboxSongs {

    public static ResourceKey<JukeboxSong> FLIGHT_OF_THE_BUMBLEBEE_RIMSKY_KORSAKOV = create("flight_of_the_bumblebee_rimsky_korsakov");
    public static ResourceKey<JukeboxSong> HONEY_BEE_RAT_FACED_BOY = create("honey_bee_rat_faced_boy");
    public static ResourceKey<JukeboxSong> LA_BEE_DA_LOCA = create("la_bee_da_loca");
    public static ResourceKey<JukeboxSong> BEE_LAXING_WITH_THE_HOM_BEES = create("bee_laxing_with_the_hom_bees");
    public static ResourceKey<JukeboxSong> BEE_WARE_OF_THE_TEMPLE = create("bee_ware_of_the_temple");
    public static ResourceKey<JukeboxSong> KNOWING_RENREN = create("knowing_renren");
    public static ResourceKey<JukeboxSong> RADIANCE_RENREN = create("radiance_renren");
    public static ResourceKey<JukeboxSong> LIFE_RENREN = create("life_renren");
    public static ResourceKey<JukeboxSong> A_LAST_FIRST_LAST = create("a_last_first_last");
    public static ResourceKey<JukeboxSong> DROWNING_IN_DESPAIR = create("drowning_in_despair");
    public static ResourceKey<JukeboxSong> BEENNA_BOX = create("beenna_box");

    private static ResourceKey<JukeboxSong> create(String string) {
        return ResourceKey.create(Registries.JUKEBOX_SONG, ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, string));
    }
}
