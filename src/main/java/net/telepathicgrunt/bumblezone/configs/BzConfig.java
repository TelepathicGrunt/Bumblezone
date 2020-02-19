package net.telepathicgrunt.bumblezone.configs;

import net.dblsaiko.qcommon.cfg.core.api.ConfigApi;
import net.dblsaiko.qcommon.cfg.core.api.cvar.CvarOptions;
import net.dblsaiko.qcommon.cfg.core.api.cvar.StringConVar;

import static net.telepathicgrunt.bumblezone.Bumblezone.MODID;

public class BzConfig {
    public static StringConVar allowWrathOfTheHiveOutsideBumblezone;

    public static void initalizeConfigs()
    {
        ConfigApi.Mutable api = ConfigApi.getInstanceMut();
        allowWrathOfTheHiveOutsideBumblezone = api.addConVar(MODID+"_allow_wrath_of_the_hive_outside_bumblezone:", StringConVar.owned("no"), CvarOptions.create().save(MODID).sync());
    }
}
