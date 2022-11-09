package com.telepathicgrunt.the_bumblezone.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.components.MiscComponent;
import com.telepathicgrunt.the_bumblezone.items.EssenceOfTheBees;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.HashSet;
import java.util.Set;

public class NoneOpCommands {
    private static MinecraftServer currentMinecraftServer = null;
    private static Set<String> cachedSuggestion = new HashSet<>();
    public static final String IS_BEE_ESSENCED_METHOD = "is_bee_essenced";
    public static final String QUEENS_DESIRED_CRAFTED_BEEHIVE_METHOD = "queens_desired_crafted_beehive";
    public static final String QUEENS_DESIRED_BEES_BRED_METHOD = "queens_desired_bees_bred";
    public static final String QUEENS_DESIRED_FLOWERS_SPAWNED_METHOD = "queens_desired_flowers_spawned";
    public static final String QUEENS_DESIRED_HONEY_BOTTLE_DRANK_METHOD = "queens_desired_honey_bottle_drank";
    public static final String QUEENS_DESIRED_BEE_STINGERS_FIRED_METHOD = "queens_desired_bee_stingers_fired";
    public static final String QUEENS_DESIRED_BEE_SAVED_METHOD = "queens_desired_bee_saved";
    public static final String QUEENS_DESIRED_POLLEN_PUFF_HITS_METHOD = "queens_desired_pollen_puff_hits";
    public static final String QUEENS_DESIRED_HONEY_SLIME_BRED_METHOD = "queens_desired_honey_slime_bred";
    public static final String QUEENS_DESIRED_BEES_FED_METHOD = "queens_desired_bees_fed";
    public static final String QUEENS_DESIRED_QUEEN_BEE_TRADE_METHOD = "queens_desired_queen_bee_trade";

    public static void createCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        String commandString = "bumblezone";
        String methodArg = "method_to_use";

        LiteralCommandNode<CommandSourceStack> source = dispatcher.register(Commands.literal(commandString)
                .requires((permission) -> permission.hasPermission(0))
                .then(Commands.argument(methodArg, StringArgumentType.string())
                .suggests((ctx, sb) -> SharedSuggestionProvider.suggest(methodSuggestions(ctx), sb))
                .executes(cs -> {
                    runMethod(cs.getArgument(methodArg, String.class), cs);
                    return 1;
                })
        ));

        dispatcher.register(Commands.literal(commandString).redirect(source));
    }

    private static Set<String> methodSuggestions(CommandContext<CommandSourceStack> cs) {
        if(currentMinecraftServer == cs.getSource().getServer()) {
            return cachedSuggestion;
        }

        Set<String> suggestedStrings = new HashSet<>();
        suggestedStrings.add(IS_BEE_ESSENCED_METHOD);
        suggestedStrings.add(QUEENS_DESIRED_CRAFTED_BEEHIVE_METHOD);
        suggestedStrings.add(QUEENS_DESIRED_BEES_BRED_METHOD);
        suggestedStrings.add(QUEENS_DESIRED_FLOWERS_SPAWNED_METHOD);
        suggestedStrings.add(QUEENS_DESIRED_HONEY_BOTTLE_DRANK_METHOD);
        suggestedStrings.add(QUEENS_DESIRED_BEE_STINGERS_FIRED_METHOD);
        suggestedStrings.add(QUEENS_DESIRED_BEE_SAVED_METHOD);
        suggestedStrings.add(QUEENS_DESIRED_POLLEN_PUFF_HITS_METHOD);
        suggestedStrings.add(QUEENS_DESIRED_HONEY_SLIME_BRED_METHOD);
        suggestedStrings.add(QUEENS_DESIRED_BEES_FED_METHOD);
        suggestedStrings.add(QUEENS_DESIRED_QUEEN_BEE_TRADE_METHOD);

        currentMinecraftServer = cs.getSource().getServer();
        cachedSuggestion = suggestedStrings;
        return suggestedStrings;
    }

    public static void runMethod(String method, CommandContext<CommandSourceStack> cs) {
        Player player = cs.getSource().getEntity() instanceof Player player1 ? player1 : null;
        if (player instanceof ServerPlayer serverPlayer) {
            if (method.equals(IS_BEE_ESSENCED_METHOD)) {
                boolean hasBeeEssence = EssenceOfTheBees.hasEssence(serverPlayer);
                MutableComponent mutableComponent = Component.translatable(
                        hasBeeEssence ?
                                "command.the_bumblezone.have_bee_essence" :
                                "command.the_bumblezone.does_not_have_bee_essence"
                );
                player.displayClientMessage(mutableComponent, false);
                return;
            }

            if (!MiscComponent.rootAdvancementDone(serverPlayer)) {
                player.displayClientMessage(Component.translatable("command.the_bumblezone.queens_desired_not_active"), false);
                return;
            }

            switch (method) {
                case QUEENS_DESIRED_CRAFTED_BEEHIVE_METHOD -> 
                        player.displayClientMessage(
                            Component.translatable("command.the_bumblezone.queens_desired_crafted_beehive_method", Bumblezone.MISC_COMPONENT.get(serverPlayer).craftedBeehives),
                            false);
                case QUEENS_DESIRED_BEES_BRED_METHOD ->
                        player.displayClientMessage(
                            Component.translatable("command.the_bumblezone.queens_desired_bees_bred_method", Bumblezone.MISC_COMPONENT.get(serverPlayer).beesBred),
                            false);
                case QUEENS_DESIRED_FLOWERS_SPAWNED_METHOD ->
                        player.displayClientMessage(
                            Component.translatable("command.the_bumblezone.queens_desired_flowers_spawned_method", Bumblezone.MISC_COMPONENT.get(serverPlayer).flowersSpawned),
                            false);
                case QUEENS_DESIRED_HONEY_BOTTLE_DRANK_METHOD ->
                        player.displayClientMessage(
                            Component.translatable("command.the_bumblezone.queens_desired_honey_bottle_drank_method", Bumblezone.MISC_COMPONENT.get(serverPlayer).honeyBottleDrank),
                            false);
                case QUEENS_DESIRED_BEE_STINGERS_FIRED_METHOD ->
                        player.displayClientMessage(
                            Component.translatable("command.the_bumblezone.queens_desired_bee_stingers_fired_method", Bumblezone.MISC_COMPONENT.get(serverPlayer).beeStingersFired),
                            false);
                case QUEENS_DESIRED_BEE_SAVED_METHOD ->
                        player.displayClientMessage(
                            Component.translatable("command.the_bumblezone.queens_desired_bee_saved_method", Bumblezone.MISC_COMPONENT.get(serverPlayer).beeSaved),
                            false);
                case QUEENS_DESIRED_POLLEN_PUFF_HITS_METHOD ->
                        player.displayClientMessage(
                            Component.translatable("command.the_bumblezone.queens_desired_pollen_puff_hits_method", Bumblezone.MISC_COMPONENT.get(serverPlayer).pollenPuffHits),
                            false);
                case QUEENS_DESIRED_HONEY_SLIME_BRED_METHOD ->
                        player.displayClientMessage(
                            Component.translatable("command.the_bumblezone.queens_desired_honey_slime_bred_method", Bumblezone.MISC_COMPONENT.get(serverPlayer).honeySlimeBred),
                            false);
                case QUEENS_DESIRED_BEES_FED_METHOD ->
                        player.displayClientMessage(
                            Component.translatable("command.the_bumblezone.queens_desired_bees_fed_method", Bumblezone.MISC_COMPONENT.get(serverPlayer).beesFed),
                            false);
                case QUEENS_DESIRED_QUEEN_BEE_TRADE_METHOD ->
                        player.displayClientMessage(
                            Component.translatable("command.the_bumblezone.queens_desired_queen_bee_trade_method", Bumblezone.MISC_COMPONENT.get(serverPlayer).queenBeeTrade),
                            false);
                default -> {}
            }
        }
    }
}
