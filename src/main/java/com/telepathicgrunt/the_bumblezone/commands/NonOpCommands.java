package com.telepathicgrunt.the_bumblezone.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.components.MiscComponent;
import com.telepathicgrunt.the_bumblezone.items.EssenceOfTheBees;
import net.minecraft.Util;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntitySummonArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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

    public static void createCommand(CommandDispatcher<CommandSourceStack> commandDispatcher, CommandBuildContext buildContext) {
        String commandString = "bumblezone_read_self_data";
        String dataArg = "data_to_check";
        String entityArg = "entity_to_check";

        LiteralCommandNode<CommandSourceStack> source = commandDispatcher.register(Commands.literal(commandString)
                .requires((permission) -> permission.hasPermission(0))
                .then(Commands.argument(dataArg, StringArgumentType.string())
                    .suggests((ctx, sb) -> SharedSuggestionProvider.suggest(methodSuggestions(ctx), sb))
                    .executes(cs -> {
                        runMethod(cs.getArgument(dataArg, String.class), null, cs);
                        return 1;
                    })
            ));

        commandDispatcher.register(Commands.literal(commandString).redirect(source));


        LiteralCommandNode<CommandSourceStack> source2 = commandDispatcher.register(Commands.literal(commandString)
                .requires((permission) -> permission.hasPermission(0))
                .then(Commands.literal(DATA_ARG.QUEENS_DESIRED_KILLED_ENTITY_COUNTER.name().toLowerCase(Locale.ROOT))
                .then(Commands.argument(entityArg, StringArgumentType.string())
                .suggests((ctx, sb) -> SharedSuggestionProvider.suggest(killedSuggestions(ctx), sb))
                .executes(cs -> {
                    runMethod(DATA_ARG.QUEENS_DESIRED_KILLED_ENTITY_COUNTER.name(), cs.getArgument(entityArg, String.class), cs);
                    return 1;
                })
        )));

        commandDispatcher.register(Commands.literal(commandString).redirect(source2));
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

    private static Set<String> killedSuggestions(CommandContext<CommandSourceStack> cs) {
        if (!cs.getSource().isPlayer()) {
            return new HashSet<>();
        }

        Player player = (Player) cs.getSource().getEntity();

        AtomicReference<Set<String>> suggestedStrings = new AtomicReference<>(new HashSet<>());
        suggestedStrings.set(
                Bumblezone.MISC_COMPONENT.get(player).mobsKilledTracker.keySet()
                        .stream()
                        .map(killed -> "\"" + killed.toString() + "\"").
                        collect(Collectors.toSet())
        );
        return suggestedStrings.get();
    }

    public static void runMethod(String dataString, String killedString, CommandContext<CommandSourceStack> cs) {
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

            if (!MiscComponent.rootAdvancementDone(serverPlayer)) {
                player.displayClientMessage(Component.translatable("command.the_bumblezone.queens_desired_not_active", serverPlayer.getDisplayName()), false);
                return;
            }

            switch (dataArg) {
                case QUEENS_DESIRED_CRAFTED_BEEHIVE -> 
                        player.displayClientMessage(
                            Component.translatable("command.the_bumblezone.queens_desired_crafted_beehive", serverPlayer.getDisplayName(),  Bumblezone.MISC_COMPONENT.get(serverPlayer).craftedBeehives),
                            false);
                case QUEENS_DESIRED_BEES_BRED ->
                        player.displayClientMessage(
                            Component.translatable("command.the_bumblezone.queens_desired_bees_bred", serverPlayer.getDisplayName(), Bumblezone.MISC_COMPONENT.get(serverPlayer).beesBred),
                            false);
                case QUEENS_DESIRED_FLOWERS_SPAWNED ->
                        player.displayClientMessage(
                            Component.translatable("command.the_bumblezone.queens_desired_flowers_spawned", serverPlayer.getDisplayName(), Bumblezone.MISC_COMPONENT.get(serverPlayer).flowersSpawned),
                            false);
                case QUEENS_DESIRED_HONEY_BOTTLE_DRANK ->
                        player.displayClientMessage(
                            Component.translatable("command.the_bumblezone.queens_desired_honey_bottle_drank", serverPlayer.getDisplayName(), Bumblezone.MISC_COMPONENT.get(serverPlayer).honeyBottleDrank),
                            false);
                case QUEENS_DESIRED_BEE_STINGERS_FIRED ->
                        player.displayClientMessage(
                            Component.translatable("command.the_bumblezone.queens_desired_bee_stingers_fired", serverPlayer.getDisplayName(), Bumblezone.MISC_COMPONENT.get(serverPlayer).beeStingersFired),
                            false);
                case QUEENS_DESIRED_BEE_SAVED ->
                        player.displayClientMessage(
                            Component.translatable("command.the_bumblezone.queens_desired_bee_saved", serverPlayer.getDisplayName(), Bumblezone.MISC_COMPONENT.get(serverPlayer).beeSaved),
                            false);
                case QUEENS_DESIRED_POLLEN_PUFF_HITS ->
                        player.displayClientMessage(
                            Component.translatable("command.the_bumblezone.queens_desired_pollen_puff_hits", serverPlayer.getDisplayName(), Bumblezone.MISC_COMPONENT.get(serverPlayer).pollenPuffHits),
                            false);
                case QUEENS_DESIRED_HONEY_SLIME_BRED ->
                        player.displayClientMessage(
                            Component.translatable("command.the_bumblezone.queens_desired_honey_slime_bred", serverPlayer.getDisplayName(), Bumblezone.MISC_COMPONENT.get(serverPlayer).honeySlimeBred),
                            false);
                case QUEENS_DESIRED_BEES_FED ->
                        player.displayClientMessage(
                            Component.translatable("command.the_bumblezone.queens_desired_bees_fed", serverPlayer.getDisplayName(), Bumblezone.MISC_COMPONENT.get(serverPlayer).beesFed),
                            false);
                case QUEENS_DESIRED_QUEEN_BEE_TRADE ->
                        player.displayClientMessage(
                            Component.translatable("command.the_bumblezone.queens_desired_queen_bee_trade", serverPlayer.getDisplayName(), Bumblezone.MISC_COMPONENT.get(serverPlayer).queenBeeTrade),
                            false);
                case QUEENS_DESIRED_KILLED_ENTITY_COUNTER -> {
                    if (killedString != null) {
                        ResourceLocation rl = new ResourceLocation(killedString);
                        int killed = Bumblezone.MISC_COMPONENT.get(serverPlayer).mobsKilledTracker.getOrDefault(rl, 0);
                        String translationKey;
                        if (rl.equals(new ResourceLocation("minecraft", "ender_dragon"))) {
                            translationKey = "command.the_bumblezone.queens_desired_killed_entity_counter_ender_dragon";
                        }
                        else if (rl.equals(new ResourceLocation("minecraft", "wither"))) {
                            translationKey = "command.the_bumblezone.queens_desired_killed_entity_counter_wither";
                        }
                        else {
                            translationKey = "command.the_bumblezone.queens_desired_killed_entity_counter";
                        }

                        if (Registry.ENTITY_TYPE.containsKey(rl)) {
                            player.displayClientMessage(
                                    Component.translatable(translationKey,
                                            serverPlayer.getDisplayName(),
                                            killed,
                                            Component.translatable(Util.makeDescriptionId("entity", rl))),
                                    false);
                        }
                        else {
                            player.displayClientMessage(
                                    Component.translatable(translationKey,
                                            serverPlayer.getDisplayName(),
                                            killed,
                                            Component.translatable("tag.entity_type." + killedString.replaceAll("[\\\\:/-]", "."))),
                                    false);
                        }
                    }
                    else {
                        player.displayClientMessage(Component.translatable("command.the_bumblezone.invalid_entity_arg"), false);
                    }
                }
                default -> {}
            }
        }
    }
}
