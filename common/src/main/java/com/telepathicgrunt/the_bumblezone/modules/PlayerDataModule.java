package com.telepathicgrunt.the_bumblezone.modules;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modules.base.Module;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.resources.Identifier;

public class PlayerDataModule implements Module<PlayerDataModule> {
    public static final MapCodec<PlayerDataModule> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            Codec.BOOL.lenientOptionalFieldOf("isBeeEssenced", false).forGetter(module -> module.isBeeEssenced),
            Codec.BOOL.lenientOptionalFieldOf("gottenWelcomed", false).forGetter(module -> module.gottenWelcomed),
            Codec.BOOL.lenientOptionalFieldOf("gottenWelcomedInDimension", false).forGetter(module -> module.gottenWelcomedInDimension),
            Codec.BOOL.lenientOptionalFieldOf("receivedEssencePrize", false).forGetter(module -> module.receivedEssencePrize),
            Codec.LONG.lenientOptionalFieldOf("tradeResetPrimedTime", 0L).forGetter(module -> module.tradeResetPrimedTime),
            Codec.INT.lenientOptionalFieldOf("dirtPelletExtraDamageKills", 0).forGetter(module -> module.dirtPelletExtraDamageKills),
            Codec.INT.lenientOptionalFieldOf("beesBred", 0).forGetter(module -> module.beesBred),
            Codec.INT.lenientOptionalFieldOf("flowersSpawned", 0).forGetter(module -> module.flowersSpawned),
            Codec.INT.lenientOptionalFieldOf("honeyBottleDrank", 0).forGetter(module -> module.honeyBottleDrank),
            Codec.INT.lenientOptionalFieldOf("beeStingersFired", 0).forGetter(module -> module.beeStingersFired),
            Codec.INT.lenientOptionalFieldOf("beeSaved", 0).forGetter(module -> module.beeSaved),
            Codec.INT.lenientOptionalFieldOf("pollenPuffHits", 0).forGetter(module -> module.pollenPuffHits),
            Codec.INT.lenientOptionalFieldOf("honeySlimeBred", 0).forGetter(module -> module.honeySlimeBred),
            Codec.INT.lenientOptionalFieldOf("beesFed", 0).forGetter(module -> module.beesFed),
            Codec.INT.lenientOptionalFieldOf("queenBeeTrade", 0).forGetter(module -> module.queenBeeTrade),
            Codec.unboundedMap(Identifier.CODEC, Codec.INT).lenientOptionalFieldOf("mobsKilledTracker", new Object2IntOpenHashMap<>())
                    .xmap(Object2IntOpenHashMap::new, Object2IntOpenHashMap::new).forGetter(module -> module.mobsKilledTracker)
    ).apply(instance, PlayerDataModule::new));

    public static final Identifier ID = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "player_data");

    public boolean isBeeEssenced;
    public boolean gottenWelcomed;
    public boolean gottenWelcomedInDimension;
    public boolean receivedEssencePrize;
    public long tradeResetPrimedTime;
    public int dirtPelletExtraDamageKills;
    public int beesBred;
    public int flowersSpawned;
    public int honeyBottleDrank;
    public int beeStingersFired;
    public int beeSaved;
    public int pollenPuffHits;
    public int honeySlimeBred;
    public int beesFed;
    public int queenBeeTrade;
    public final Object2IntOpenHashMap<Identifier> mobsKilledTracker;

    public PlayerDataModule(boolean isBeeEssenced,
                            boolean gottenWelcomed,
                            boolean gottenWelcomedInDimension,
                            boolean receivedEssencePrize,
                            long tradeResetPrimedTime,
                            int dirtPelletExtraDamageKills,
                            int beesBred,
                            int flowersSpawned,
                            int honeyBottleDrank,
                            int beeStingersFired,
                            int beeSaved,
                            int pollenPuffHits,
                            int honeySlimeBred,
                            int beesFed,
                            int queenBeeTrade,
                            Object2IntOpenHashMap<Identifier> mobsKilledTracker) {
        this.isBeeEssenced = isBeeEssenced;
        this.gottenWelcomed = gottenWelcomed;
        this.gottenWelcomedInDimension = gottenWelcomedInDimension;
        this.receivedEssencePrize = receivedEssencePrize;
        this.tradeResetPrimedTime = tradeResetPrimedTime;
        this.dirtPelletExtraDamageKills = dirtPelletExtraDamageKills;
        this.beesBred = beesBred;
        this.flowersSpawned = flowersSpawned;
        this.honeyBottleDrank = honeyBottleDrank;
        this.beeStingersFired = beeStingersFired;
        this.beeSaved = beeSaved;
        this.pollenPuffHits = pollenPuffHits;
        this.honeySlimeBred = honeySlimeBred;
        this.beesFed = beesFed;
        this.queenBeeTrade = queenBeeTrade;
        this.mobsKilledTracker = mobsKilledTracker;
    }

    public PlayerDataModule() {
        this.isBeeEssenced = false;
        this.gottenWelcomed = false;
        this.gottenWelcomedInDimension = false;
        this.receivedEssencePrize = false;
        this.tradeResetPrimedTime = -1000;
        this.dirtPelletExtraDamageKills = 0;
        this.beesBred = 0;
        this.flowersSpawned = 0;
        this.honeyBottleDrank = 0;
        this.beeStingersFired = 0;
        this.beeSaved = 0;
        this.pollenPuffHits = 0;
        this.honeySlimeBred = 0;
        this.beesFed = 0;
        this.queenBeeTrade = 0;
        this.mobsKilledTracker = new Object2IntOpenHashMap<>();
    }

    public void resetAllTrackerStats() {
        receivedEssencePrize = false;
        tradeResetPrimedTime = -1000;
        dirtPelletExtraDamageKills = 0;
        beesBred = 0;
        flowersSpawned = 0;
        honeyBottleDrank = 0;
        beeStingersFired = 0;
        beeSaved = 0;
        pollenPuffHits = 0;
        honeySlimeBred = 0;
        beesFed = 0;
        queenBeeTrade = 0;
        mobsKilledTracker.clear();
    }

    @Override
    public MapCodec<PlayerDataModule> codec() {
        return CODEC;
    }

    @Override
    public Identifier id() {
        return ID;
    }
}
