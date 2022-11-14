package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.commands.NoneOpCommands;
import net.minecraftforge.event.RegisterCommandsEvent;

public class BzCommands {
    public static void registerCommand(RegisterCommandsEvent event) {
        NoneOpCommands.createCommand(event.getDispatcher());
    }
}
