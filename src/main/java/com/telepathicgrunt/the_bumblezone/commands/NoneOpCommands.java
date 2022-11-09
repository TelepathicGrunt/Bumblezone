package com.telepathicgrunt.the_bumblezone.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.telepathicgrunt.the_bumblezone.capabilities.BzCapabilities;
import com.telepathicgrunt.the_bumblezone.capabilities.EntityMisc;
import com.telepathicgrunt.the_bumblezone.items.EssenceOfTheBees;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
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

    public static void createCommand(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext) {
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

            if (!EntityMisc.rootAdvancementDone(serverPlayer)) {
                player.displayClientMessage(Component.translatable("command.the_bumblezone.queens_desired_not_active"), false);
                return;
            }

            switch (method) {
                case QUEENS_DESIRED_CRAFTED_BEEHIVE_METHOD ->
                        serverPlayer.getCapability(BzCapabilities.ENTITY_MISC).ifPresent(capability ->
                                player.displayClientMessage(
                                        Component.translatable("command.the_bumblezone.queens_desired_crafted_beehive_method", capability.craftedBeehives),
                                        false));
                case QUEENS_DESIRED_BEES_BRED_METHOD ->
                        serverPlayer.getCapability(BzCapabilities.ENTITY_MISC).ifPresent(capability ->
                                player.displayClientMessage(
                                        Component.translatable("command.the_bumblezone.queens_desired_bees_bred_method", capability.beesBred),
                                        false));
                case QUEENS_DESIRED_FLOWERS_SPAWNED_METHOD ->
                        serverPlayer.getCapability(BzCapabilities.ENTITY_MISC).ifPresent(capability ->
                                player.displayClientMessage(
                                        Component.translatable("command.the_bumblezone.queens_desired_flowers_spawned_method", capability.flowersSpawned),
                                        false));
                case QUEENS_DESIRED_HONEY_BOTTLE_DRANK_METHOD ->
                        serverPlayer.getCapability(BzCapabilities.ENTITY_MISC).ifPresent(capability ->
                                player.displayClientMessage(
                                        Component.translatable("command.the_bumblezone.queens_desired_honey_bottle_drank_method", capability.honeyBottleDrank),
                                        false));
                case QUEENS_DESIRED_BEE_STINGERS_FIRED_METHOD ->
                        serverPlayer.getCapability(BzCapabilities.ENTITY_MISC).ifPresent(capability ->
                                player.displayClientMessage(
                                        Component.translatable("command.the_bumblezone.queens_desired_bee_stingers_fired_method", capability.beeStingersFired),
                                        false));
                case QUEENS_DESIRED_BEE_SAVED_METHOD ->
                        serverPlayer.getCapability(BzCapabilities.ENTITY_MISC).ifPresent(capability ->
                                player.displayClientMessage(
                                        Component.translatable("command.the_bumblezone.queens_desired_bee_saved_method", capability.beeSaved),
                                        false));
                case QUEENS_DESIRED_POLLEN_PUFF_HITS_METHOD ->
                        serverPlayer.getCapability(BzCapabilities.ENTITY_MISC).ifPresent(capability ->
                                player.displayClientMessage(
                                        Component.translatable("command.the_bumblezone.queens_desired_pollen_puff_hits_method", capability.pollenPuffHits),
                                        false));
                case QUEENS_DESIRED_HONEY_SLIME_BRED_METHOD ->
                        serverPlayer.getCapability(BzCapabilities.ENTITY_MISC).ifPresent(capability ->
                                player.displayClientMessage(
                                        Component.translatable("command.the_bumblezone.queens_desired_honey_slime_bred_method", capability.honeySlimeBred),
                                        false));
                case QUEENS_DESIRED_BEES_FED_METHOD ->
                        serverPlayer.getCapability(BzCapabilities.ENTITY_MISC).ifPresent(capability ->
                                player.displayClientMessage(
                                        Component.translatable("command.the_bumblezone.queens_desired_bees_fed_method", capability.beesFed),
                                        false));
                case QUEENS_DESIRED_QUEEN_BEE_TRADE_METHOD ->
                        serverPlayer.getCapability(BzCapabilities.ENTITY_MISC).ifPresent(capability ->
                                player.displayClientMessage(
                                        Component.translatable("command.the_bumblezone.queens_desired_queen_bee_trade_method", capability.queenBeeTrade),
                                        false));
                default -> {}
            }
        }
    }
}
