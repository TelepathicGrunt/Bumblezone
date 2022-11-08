package com.telepathicgrunt.the_bumblezone.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.telepathicgrunt.the_bumblezone.items.EssenceOfTheBees;
import net.minecraft.commands.CommandBuildContext;
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
                                "command.the_bumblezone.dow_not_have_bee_essence"
                );
                player.displayClientMessage(mutableComponent, false);
            }
        }
    }
}
