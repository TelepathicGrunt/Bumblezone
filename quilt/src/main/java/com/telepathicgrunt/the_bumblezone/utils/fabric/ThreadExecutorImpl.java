package com.telepathicgrunt.the_bumblezone.utils.fabric;

public class ThreadExecutorImpl {
    public static Thread createServerThread(Runnable runnable, String name) {
        return new Thread(runnable, name);
    }
}
