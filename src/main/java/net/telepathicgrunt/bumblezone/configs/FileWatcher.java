package net.telepathicgrunt.bumblezone.configs;


import java.io.File;
import java.util.TimerTask;

public abstract class FileWatcher extends TimerTask {
    private long timeStamp;
    private File file;

    public FileWatcher( File file ) {
        this.file = file;
        this.timeStamp = file.lastModified();
    }

    public final void run() {
        long timeStamp = file.lastModified();

        if( this.timeStamp != timeStamp ) {
            onChange(file);
            timeStamp = file.lastModified();
            this.timeStamp = timeStamp;
        }
    }

    protected abstract void onChange( File file );
}