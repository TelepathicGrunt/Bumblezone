package com.telepathicgrunt.the_bumblezone.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.telepathicgrunt.the_bumblezone.capabilities.BzCapabilities;
import com.telepathicgrunt.the_bumblezone.capabilities.EntityMisc;
import com.telepathicgrunt.the_bumblezone.items.EssenceOfTheBees;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntitySummonArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.RegisterCommandsEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class NonOpCommands {
    private static MinecraftServer currentMinecraftServer = null;
    private static Set<String> cachedSuggestion = new HashSet<>();
    enum DATA_ARG {
        IS_BEE_ESSENCED,
        QUEENS_DESIRED_CRAFTED_BEEHIVE,
        QUEENS_DESIRED_BEES_BRED,
        QUEENS_DESIRED_FLOWERS_SPAWNED,
        QUEENS_DESIRED_HONEY_BOTTLE_DRANK,
        QUEENS_DESIRED_BEE_STINGERS_FIRED,
        QUEENS_DESIRED_BEE_SAVED,
        QUEENS_DESIRED_POLLEN_PUFF_HITS,
        QUEENS_DESIRED_HONEY_SLIME_BRED,
        QUEENS_DESIRED_BEES_FED,
        QUEENS_DESIRED_QUEEN_BEE_TRADE,
        QUEENS_DESIRED_KILLED_ENTITY_COUNTER
    }

    public static void createCommand(RegisterCommandsEvent commandEvent) {
        String commandString = "bumblezone_read_self_data";
        String dataArg = "data_to_check";
        String entityArg = "entity_to_check";

        LiteralCommandNode<CommandSourceStack> source = commandEvent.getDispatcher().register(Commands.literal(commandString)
                .requires((permission) -> permission.hasPermission(0))
                .then(Commands.argument(dataArg, StringArgumentType.string())
                .suggests((ctx, sb) -> SharedSuggestionProvider.suggest(methodSuggestions(ctx), sb))
                .executes(cs -> {
                    runMethod(cs.getArgument(dataArg, String.class), null, cs);
                    return 1;
                })
        ));

        commandEvent.getDispatcher().register(Commands.literal(commandString).redirect(source));


        LiteralCommandNode<CommandSourceStack> source2 = commandEvent.getDispatcher().register(Commands.literal(commandString)
                .requires((permission) -> permission.hasPermission(0))
                .then(Commands.literal(DATA_ARG.QUEENS_DESIRED_KILLED_ENTITY_COUNTER.name().toLowerCase(Locale.ROOT))
                .then(Commands.argument(entityArg, EntitySummonArgument.id())
                .suggests(SuggestionProviders.SUMMONABLE_ENTITIES)
                .executes(cs -> {
                    runMethod(DATA_ARG.QUEENS_DESIRED_KILLED_ENTITY_COUNTER.name(), EntitySummonArgument.getSummonableEntity(cs, entityArg), cs);
                    return 1;
                })
        )));

        commandEvent.getDispatcher().register(Commands.literal(commandString).redirect(source2));
    }

    private static Set<String> methodSuggestions(CommandContext<CommandSourceStack> cs) {
        if(currentMinecraftServer == cs.getSource().getServer()) {
            return cachedSuggestion;
        }

        Set<String> suggestedStrings = new HashSet<>();
        Arrays.stream(DATA_ARG.values()).forEach(e -> suggestedStrings.add(e.name().toLowerCase(Locale.ROOT)));

        currentMinecraftServer = cs.getSource().getServer();
        cachedSuggestion = suggestedStrings;
        return suggestedStrings;
    }

    public static void runMethod(String dataString, ResourceLocation killedEntityRL, CommandContext<CommandSourceStack> cs) {
        Player player = cs.getSource().getEntity() instanceof Player player1 ? player1 : null;
        if (player instanceof ServerPlayer serverPlayer) {
            DATA_ARG dataArg;
            try {
                dataArg = DATA_ARG.valueOf(dataString.toUpperCase(Locale.ROOT));
            }
            catch (Exception e) {
                MutableComponent mutableComponent = Component.translatable("command.the_bumblezone.invalid_data_arg");
                player.displayClientMessage(mutableComponent, false);
                return;
            }

            if (DATA_ARG.IS_BEE_ESSENCED.equals(dataArg)) {
                boolean hasBeeEssence = EssenceOfTheBees.hasEssence(serverPlayer);
                MutableComponent mutableComponent = Component.translatable(
                        hasBeeEssence ?
                                "command.the_bumblezone.have_bee_essence" :
                                "command.the_bumblezone.does_not_have_bee_essence",
                        serverPlayer.getDisplayName()
                );
                player.displayClientMessage(mutableComponent, false);
                return;
            }

            if (!EntityMisc.rootAdvancementDone(serverPlayer)) {
                player.displayClientMessage(Component.translatable("command.the_bumblezone.queens_desired_not_active", serverPlayer.getDisplayName()), false);
                return;
            }

            switch (dataArg) {
                case QUEENS_DESIRED_CRAFTED_BEEHIVE ->
                        serverPlayer.getCapability(BzCapabilities.ENTITY_MISC).ifPresent(capability ->
                                player.displayClientMessage(
                                        Component.translatable("command.the_bumblezone.queens_desired_crafted_beehive", serverPlayer.getDisplayName(), capability.craftedBeehives),
                                        false));
                case QUEENS_DESIRED_BEES_BRED ->
                        serverPlayer.getCapability(BzCapabilities.ENTITY_MISC).ifPresent(capability ->
                                player.displayClientMessage(
                                        Component.translatable("command.the_bumblezone.queens_desired_bees_bred", serverPlayer.getDisplayName(), capability.beesBred),
                                        false));
                case QUEENS_DESIRED_FLOWERS_SPAWNED ->
                        serverPlayer.getCapability(BzCapabilities.ENTITY_MISC).ifPresent(capability ->
                                player.displayClientMessage(
                                        Component.translatable("command.the_bumblezone.queens_desired_flowers_spawned", serverPlayer.getDisplayName(), capability.flowersSpawned),
                                        false));
                case QUEENS_DESIRED_HONEY_BOTTLE_DRANK ->
                        serverPlayer.getCapability(BzCapabilities.ENTITY_MISC).ifPresent(capability ->
                                player.displayClientMessage(
                                        Component.translatable("command.the_bumblezone.queens_desired_honey_bottle_drank", serverPlayer.getDisplayName(), capability.honeyBottleDrank),
                                        false));
                case QUEENS_DESIRED_BEE_STINGERS_FIRED ->
                        serverPlayer.getCapability(BzCapabilities.ENTITY_MISC).ifPresent(capability ->
                                player.displayClientMessage(
                                        Component.translatable("command.the_bumblezone.queens_desired_bee_stingers_fired", serverPlayer.getDisplayName(), capability.beeStingersFired),
                                        false));
                case QUEENS_DESIRED_BEE_SAVED ->
                        serverPlayer.getCapability(BzCapabilities.ENTITY_MISC).ifPresent(capability ->
                                player.displayClientMessage(
                                        Component.translatable("command.the_bumblezone.queens_desired_bee_saved", serverPlayer.getDisplayName(), capability.beeSaved),
                                        false));
                case QUEENS_DESIRED_POLLEN_PUFF_HITS ->
                        serverPlayer.getCapability(BzCapabilities.ENTITY_MISC).ifPresent(capability ->
                                player.displayClientMessage(
                                        Component.translatable("command.the_bumblezone.queens_desired_pollen_puff_hits", serverPlayer.getDisplayName(), capability.pollenPuffHits),
                                        false));
                case QUEENS_DESIRED_HONEY_SLIME_BRED ->
                        serverPlayer.getCapability(BzCapabilities.ENTITY_MISC).ifPresent(capability ->
                                player.displayClientMessage(
                                        Component.translatable("command.the_bumblezone.queens_desired_honey_slime_bred", serverPlayer.getDisplayName(), capability.honeySlimeBred),
                                        false));
                case QUEENS_DESIRED_BEES_FED ->
                        serverPlayer.getCapability(BzCapabilities.ENTITY_MISC).ifPresent(capability ->
                                player.displayClientMessage(
                                        Component.translatable("command.the_bumblezone.queens_desired_bees_fed", serverPlayer.getDisplayName(), capability.beesFed),
                                        false));
                case QUEENS_DESIRED_QUEEN_BEE_TRADE ->
                        serverPlayer.getCapability(BzCapabilities.ENTITY_MISC).ifPresent(capability ->
                                player.displayClientMessage(
                                        Component.translatable("command.the_bumblezone.queens_desired_queen_bee_trade", serverPlayer.getDisplayName(), capability.queenBeeTrade),
                                        false));
                case QUEENS_DESIRED_KILLED_ENTITY_COUNTER -> {
                    if (killedEntityRL != null) {
                        serverPlayer.getCapability(BzCapabilities.ENTITY_MISC).ifPresent(capability -> {
                            int killed = capability.mobsKilledTracker.getOrDefault(killedEntityRL, 0);
                            String translationKey;
                            if (killedEntityRL.equals(new ResourceLocation("minecraft", "ender_dragon"))) {
                                translationKey = "command.the_bumblezone.queens_desired_killed_entity_counter_ender_dragon";
                            }
                            else if (killedEntityRL.equals(new ResourceLocation("minecraft", "wither"))) {
                                translationKey = "command.the_bumblezone.queens_desired_killed_entity_counter_wither";
                            }
                            else {
                                translationKey = "command.the_bumblezone.queens_desired_killed_entity_counter";
                            }
                            
                            player.displayClientMessage(
                                    Component.translatable(translationKey,
                                            serverPlayer.getDisplayName(),
                                            killed,
                                            Component.translatable(Util.makeDescriptionId("entity", killedEntityRL))),
                                    false);
                        });
                    }
                    else {
                        player.displayClientMessage(Component.translatable("command.the_bumblezone.invalid_entity_arg", serverPlayer.getDisplayName()), false);
                    }
                }
                default -> {}
            }
        }
    }
}
