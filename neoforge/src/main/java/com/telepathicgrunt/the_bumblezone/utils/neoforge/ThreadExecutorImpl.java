package com.telepathicgrunt.the_bumblezone.utils.neoforge;

import net.neoforged.fml.util.thread.SidedThreadGroups;

public class ThreadExecutorImpl {
    public static Thread createServerThread(Runnable runnable, String name) {
        return new Thread(SidedThreadGroups.SERVER, runnable, name);
    }
}
