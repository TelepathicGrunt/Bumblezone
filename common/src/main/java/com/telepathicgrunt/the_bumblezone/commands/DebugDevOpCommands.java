package com.telepathicgrunt.the_bumblezone.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzRegisterCommandsEvent;
import com.telepathicgrunt.the_bumblezone.utils.PlatformHooks;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;

public class DebugDevOpCommands {
    public static void createCommand(BzRegisterCommandsEvent commandEvent) {
        if (!PlatformHooks.isDevEnvironment()) {
            return;
        }

        CommandDispatcher<CommandSourceStack> commandDispatcher = commandEvent.dispatcher();

        String commandCooldownString = "bumblezone_cooldown";
        String cooldownTimeArg = "cooldown_time";
        String entityArg = "entity_to_check";

        LiteralCommandNode<CommandSourceStack> source = commandDispatcher.register(Commands.literal(commandCooldownString)
                .requires((permission) -> permission.hasPermission(2))
                .then(Commands.argument(entityArg, EntityArgument.players())
                .then(Commands.argument(cooldownTimeArg, IntegerArgumentType.integer())
                .executes(cs -> {
                    setCooldown(cs.getSource(), EntityArgument.getPlayers(cs, entityArg), cs.getArgument(cooldownTimeArg, Integer.class), cs);
                    return 1;
                })
        )));

        commandDispatcher.register(Commands.literal(commandCooldownString).redirect(source));
    }

    private static void setCooldown(CommandSourceStack commandSourceStack, Collection<ServerPlayer> targets, int cooldownTime, CommandContext<CommandSourceStack> cs) {
        for (ServerPlayer targetPlayer : targets) {
            for (ItemStack itemStack : targetPlayer.getHandSlots()) {
                if (!itemStack.isEmpty()) {
                    targetPlayer.getCooldowns().addCooldown(itemStack.getItem(), cooldownTime);
                }
            }

            for (ItemStack itemStack : targetPlayer.getArmorSlots()) {
                if (!itemStack.isEmpty()) {
                    targetPlayer.getCooldowns().addCooldown(itemStack.getItem(), cooldownTime);
                }
            }

            for (ItemStack itemStack : targetPlayer.getInventory().items) {
                if (!itemStack.isEmpty()) {
                    targetPlayer.getCooldowns().addCooldown(itemStack.getItem(), cooldownTime);
                }
            }
        }

        MutableComponent mutableComponent = Component.literal("Cooldown Applied");
        commandSourceStack.sendSuccess(() -> mutableComponent, true);
    }
}
