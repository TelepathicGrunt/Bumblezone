package com.telepathicgrunt.the_bumblezone.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.telepathicgrunt.the_bumblezone.events.RegisterCommandsEvent;
import com.telepathicgrunt.the_bumblezone.items.EssenceOfTheBees;
import com.telepathicgrunt.the_bumblezone.modules.EntityMiscHandler;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleHelper;
import com.telepathicgrunt.the_bumblezone.modules.registry.ModuleRegistry;
import net.minecraft.Util;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class OpCommands {
    private static MinecraftServer currentMinecraftServer = null;
    private static Set<String> cachedSuggestion = new HashSet<>();
    enum DATA_BOOLEAN_WRITE_ARG {
        IS_BEE_ESSENCED
    }
    enum DATA_READ_ARG {
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
        CommandDispatcher<CommandSourceStack> commandDispatcher = commandEvent.dispatcher();
        CommandBuildContext buildContext = commandEvent.context();

        String commandWriteString = "bumblezone_modify_data";
        String commandReadString = "bumblezone_read_data";
        String dataArg = "data_to_modify";
        String newDataArg = "new_value";
        String entityArg = "entity_to_check";

        LiteralCommandNode<CommandSourceStack> source = commandDispatcher.register(Commands.literal(commandWriteString)
                .requires((permission) -> permission.hasPermission(2))
                .then(Commands.argument(dataArg, StringArgumentType.string())
                .suggests((ctx, sb) -> SharedSuggestionProvider.suggest(methodBooleanWriteSuggestions(ctx), sb))
                .then(Commands.argument("targets", EntityArgument.players())
                .then(Commands.argument(newDataArg, BoolArgumentType.bool())
                .executes(cs -> {
                    runBooleanSetMethod(cs.getSource(), cs.getArgument(dataArg, String.class), EntityArgument.getPlayers(cs, "targets"), cs.getArgument(newDataArg, boolean.class), cs);
                    return 1;
                })
        ))));

        commandDispatcher.register(Commands.literal(commandWriteString).redirect(source));

        LiteralCommandNode<CommandSourceStack> source2 = commandDispatcher.register(Commands.literal(commandReadString)
                .requires((permission) -> permission.hasPermission(2))
                .then(Commands.argument(dataArg, StringArgumentType.string())
                .suggests((ctx, sb) -> SharedSuggestionProvider.suggest(methodReadSuggestions(ctx), sb))
                .then(Commands.argument("targets", EntityArgument.players())
                .executes(cs -> {
                    runReadMethod(cs.getSource(), cs.getArgument(dataArg, String.class), null, EntityArgument.getPlayers(cs, "targets"), cs);
                    return 1;
                })
        )));

        commandDispatcher.register(Commands.literal(commandReadString).redirect(source2));


        LiteralCommandNode<CommandSourceStack> source3 = commandDispatcher.register(Commands.literal(commandReadString)
                .requires((permission) -> permission.hasPermission(2))
                .then(Commands.literal(DATA_READ_ARG.QUEENS_DESIRED_KILLED_ENTITY_COUNTER.name().toLowerCase(Locale.ROOT))
                .then(Commands.argument(entityArg, ResourceArgument.resource(buildContext, Registries.ENTITY_TYPE))
                .suggests(SuggestionProviders.SUMMONABLE_ENTITIES)
                .then(Commands.argument("targets", EntityArgument.players())
                .executes(cs -> {
                    runReadMethod(cs.getSource(), DATA_READ_ARG.QUEENS_DESIRED_KILLED_ENTITY_COUNTER.name(), ResourceArgument.getSummonableEntityType(cs, entityArg).key().location(), EntityArgument.getPlayers(cs, "targets"), cs);
                    return 1;
                })
        ))));

        commandDispatcher.register(Commands.literal(commandReadString).redirect(source3));
    }

    private static Set<String> methodBooleanWriteSuggestions(CommandContext<CommandSourceStack> cs) {
        if(currentMinecraftServer == cs.getSource().getServer()) {
            return cachedSuggestion;
        }

        Set<String> suggestedStrings = new HashSet<>();
        Arrays.stream(DATA_BOOLEAN_WRITE_ARG.values()).forEach(e -> suggestedStrings.add(e.name().toLowerCase(Locale.ROOT)));

        currentMinecraftServer = cs.getSource().getServer();
        cachedSuggestion = suggestedStrings;
        return suggestedStrings;
    }

    private static Set<String> methodReadSuggestions(CommandContext<CommandSourceStack> cs) {
        if(currentMinecraftServer == cs.getSource().getServer()) {
            return cachedSuggestion;
        }

        Set<String> suggestedStrings = new HashSet<>();
        Arrays.stream(DATA_READ_ARG.values()).forEach(e -> suggestedStrings.add(e.name().toLowerCase(Locale.ROOT)));

        currentMinecraftServer = cs.getSource().getServer();
        cachedSuggestion = suggestedStrings;
        return suggestedStrings;
    }


    public static void runBooleanSetMethod(CommandSourceStack commandSourceStack, String dataString, Collection<ServerPlayer> targets, boolean bool, CommandContext<CommandSourceStack> cs) {
        DATA_READ_ARG dataArg;
        try {
            dataArg = DATA_READ_ARG.valueOf(dataString.toUpperCase(Locale.ROOT));
        }
        catch (Exception e) {
            MutableComponent mutableComponent = Component.translatable("command.the_bumblezone.invalid_data_arg");
            commandSourceStack.sendFailure(mutableComponent);
            return;
        }

        if (DATA_READ_ARG.IS_BEE_ESSENCED.equals(dataArg)) {
            for (ServerPlayer targetPlayer : targets) {
                EssenceOfTheBees.setEssence(targetPlayer, bool);
            }
            MutableComponent mutableComponent = Component.translatable("command.the_bumblezone.data_change_success");
            commandSourceStack.sendSuccess(mutableComponent, true);
            return;
        }
    }

    public static void runReadMethod(CommandSourceStack commandSourceStack, String dataString, ResourceLocation killedEntityRL, Collection<ServerPlayer> targets, CommandContext<CommandSourceStack> cs) {
        NonOpCommands.DATA_ARG dataArg;
        try {
            dataArg = NonOpCommands.DATA_ARG.valueOf(dataString.toUpperCase(Locale.ROOT));
        }
        catch (Exception e) {
            MutableComponent mutableComponent = Component.translatable("command.the_bumblezone.invalid_data_arg");
            commandSourceStack.sendFailure(mutableComponent);
            return;
        }

        for (ServerPlayer targetPlayer : targets) {
            if (NonOpCommands.DATA_ARG.IS_BEE_ESSENCED.equals(dataArg)) {
                boolean hasBeeEssence = EssenceOfTheBees.hasEssence(targetPlayer);
                MutableComponent mutableComponent = Component.translatable(
                        hasBeeEssence ?
                                "command.the_bumblezone.have_bee_essence" :
                                "command.the_bumblezone.does_not_have_bee_essence",
                        targetPlayer.getDisplayName()
                );
                commandSourceStack.sendSuccess(mutableComponent, false);
                return;
            }

            if (!EntityMiscHandler.rootAdvancementDone(targetPlayer)) {
                commandSourceStack.sendFailure(Component.translatable("command.the_bumblezone.queens_desired_not_active", targetPlayer.getDisplayName()));
                continue;
            }

            switch (dataArg) {
                case QUEENS_DESIRED_CRAFTED_BEEHIVE -> ModuleHelper.getModule(targetPlayer, ModuleRegistry.ENTITY_MISC).ifPresent(capability -> commandSourceStack.sendSuccess(
                        Component.translatable("command.the_bumblezone.queens_desired_crafted_beehive", targetPlayer.getDisplayName(), capability.craftedBeehives),
                        false));
                case QUEENS_DESIRED_BEES_BRED -> ModuleHelper.getModule(targetPlayer, ModuleRegistry.ENTITY_MISC).ifPresent(capability -> commandSourceStack.sendSuccess(
                        Component.translatable("command.the_bumblezone.queens_desired_bees_bred", targetPlayer.getDisplayName(), capability.beesBred),
                        false));
                case QUEENS_DESIRED_FLOWERS_SPAWNED -> ModuleHelper.getModule(targetPlayer, ModuleRegistry.ENTITY_MISC).ifPresent(capability -> commandSourceStack.sendSuccess(
                        Component.translatable("command.the_bumblezone.queens_desired_flowers_spawned", targetPlayer.getDisplayName(), capability.flowersSpawned),
                        false));
                case QUEENS_DESIRED_HONEY_BOTTLE_DRANK -> ModuleHelper.getModule(targetPlayer, ModuleRegistry.ENTITY_MISC).ifPresent(capability -> commandSourceStack.sendSuccess(
                        Component.translatable("command.the_bumblezone.queens_desired_honey_bottle_drank", targetPlayer.getDisplayName(), capability.honeyBottleDrank),
                        false));
                case QUEENS_DESIRED_BEE_STINGERS_FIRED -> ModuleHelper.getModule(targetPlayer, ModuleRegistry.ENTITY_MISC).ifPresent(capability -> commandSourceStack.sendSuccess(
                        Component.translatable("command.the_bumblezone.queens_desired_bee_stingers_fired", targetPlayer.getDisplayName(), capability.beeStingersFired),
                        false));
                case QUEENS_DESIRED_BEE_SAVED -> ModuleHelper.getModule(targetPlayer, ModuleRegistry.ENTITY_MISC).ifPresent(capability -> commandSourceStack.sendSuccess(
                        Component.translatable("command.the_bumblezone.queens_desired_bee_saved", targetPlayer.getDisplayName(), capability.beeSaved),
                        false));
                case QUEENS_DESIRED_POLLEN_PUFF_HITS -> ModuleHelper.getModule(targetPlayer, ModuleRegistry.ENTITY_MISC).ifPresent(capability -> commandSourceStack.sendSuccess(
                        Component.translatable("command.the_bumblezone.queens_desired_pollen_puff_hits", targetPlayer.getDisplayName(), capability.pollenPuffHits),
                        false));
                case QUEENS_DESIRED_HONEY_SLIME_BRED -> ModuleHelper.getModule(targetPlayer, ModuleRegistry.ENTITY_MISC).ifPresent(capability -> commandSourceStack.sendSuccess(
                        Component.translatable("command.the_bumblezone.queens_desired_honey_slime_bred", targetPlayer.getDisplayName(), capability.honeySlimeBred),
                        false));
                case QUEENS_DESIRED_BEES_FED -> ModuleHelper.getModule(targetPlayer, ModuleRegistry.ENTITY_MISC).ifPresent(capability -> commandSourceStack.sendSuccess(
                        Component.translatable("command.the_bumblezone.queens_desired_bees_fed", targetPlayer.getDisplayName(), capability.beesFed),
                        false));
                case QUEENS_DESIRED_QUEEN_BEE_TRADE -> ModuleHelper.getModule(targetPlayer, ModuleRegistry.ENTITY_MISC).ifPresent(capability -> commandSourceStack.sendSuccess(
                        Component.translatable("command.the_bumblezone.queens_desired_queen_bee_trade", targetPlayer.getDisplayName(), capability.queenBeeTrade),
                        false));
                case QUEENS_DESIRED_KILLED_ENTITY_COUNTER -> {
                    if (killedEntityRL != null) {
                        ModuleHelper.getModule(targetPlayer, ModuleRegistry.ENTITY_MISC).ifPresent(capability -> {
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

                            commandSourceStack.sendSuccess(
                                    Component.translatable(translationKey,
                                            targetPlayer.getDisplayName(),
                                            killed,
                                            Component.translatable(Util.makeDescriptionId("entity", killedEntityRL))),
                                    false);
                        });
                    }
                    else {
                        commandSourceStack.sendSuccess(Component.translatable("command.the_bumblezone.invalid_entity_arg"), false);
                        return;
                    }
                }
                default -> {}
            }
        }
    }
}
