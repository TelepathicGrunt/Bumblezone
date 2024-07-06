package com.telepathicgrunt.the_bumblezone.modules;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modules.base.Module;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.resources.ResourceLocation;

public class PlayerDataModule implements Module<PlayerDataModule> {
    public static final Codec<PlayerDataModule> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.BOOL.fieldOf("isBeeEssenced").forGetter(module -> module.isBeeEssenced),
            Codec.BOOL.fieldOf("gottenWelcomed").forGetter(module -> module.gottenWelcomed),
            Codec.BOOL.fieldOf("gottenWelcomedInDimension").forGetter(module -> module.gottenWelcomedInDimension),
            Codec.BOOL.fieldOf("receivedEssencePrize").forGetter(module -> module.receivedEssencePrize),
            Codec.LONG.fieldOf("tradeResetPrimedTime").forGetter(module -> module.tradeResetPrimedTime),
            Codec.INT.fieldOf("craftedBeehives").forGetter(module -> module.craftedBeehives),
            Codec.INT.fieldOf("beesBred").forGetter(module -> module.beesBred),
            Codec.INT.fieldOf("flowersSpawned").forGetter(module -> module.flowersSpawned),
            Codec.INT.fieldOf("honeyBottleDrank").forGetter(module -> module.honeyBottleDrank),
            Codec.INT.fieldOf("beeStingersFired").forGetter(module -> module.beeStingersFired),
            Codec.INT.fieldOf("beeSaved").forGetter(module -> module.beeSaved),
            Codec.INT.fieldOf("pollenPuffHits").forGetter(module -> module.pollenPuffHits),
            Codec.INT.fieldOf("honeySlimeBred").forGetter(module -> module.honeySlimeBred),
            Codec.INT.fieldOf("beesFed").forGetter(module -> module.beesFed),
            Codec.INT.fieldOf("queenBeeTrade").forGetter(module -> module.queenBeeTrade),
            Codec.unboundedMap(ResourceLocation.CODEC, Codec.INT).fieldOf("mobsKilledTracker")
                    .xmap(Object2IntOpenHashMap::new, Object2IntOpenHashMap::new).forGetter(module -> module.mobsKilledTracker)
    ).apply(instance, PlayerDataModule::new));

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "player_data");

    public boolean isBeeEssenced;
    public boolean gottenWelcomed;
    public boolean gottenWelcomedInDimension;
    public boolean receivedEssencePrize;
    public long tradeResetPrimedTime;
    public int craftedBeehives;
    public int beesBred;
    public int flowersSpawned;
    public int honeyBottleDrank;
    public int beeStingersFired;
    public int beeSaved;
    public int pollenPuffHits;
    public int honeySlimeBred;
    public int beesFed;
    public int queenBeeTrade;
    public final Object2IntOpenHashMap<ResourceLocation> mobsKilledTracker;

    public PlayerDataModule(boolean isBeeEssenced,
                            boolean gottenWelcomed,
                            boolean gottenWelcomedInDimension,
                            boolean receivedEssencePrize,
                            long tradeResetPrimedTime,
                            int craftedBeehives,
                            int beesBred,
                            int flowersSpawned,
                            int honeyBottleDrank,
                            int beeStingersFired,
                            int beeSaved,
                            int pollenPuffHits,
                            int honeySlimeBred,
                            int beesFed,
                            int queenBeeTrade,
                            Object2IntOpenHashMap<ResourceLocation> mobsKilledTracker) {
        this.isBeeEssenced = isBeeEssenced;
        this.gottenWelcomed = gottenWelcomed;
        this.gottenWelcomedInDimension = gottenWelcomedInDimension;
        this.receivedEssencePrize = receivedEssencePrize;
        this.tradeResetPrimedTime = tradeResetPrimedTime;
        this.craftedBeehives = craftedBeehives;
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
        this.craftedBeehives = 0;
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
        craftedBeehives = 0;
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
    public Codec<PlayerDataModule> codec() {
        return CODEC;
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
