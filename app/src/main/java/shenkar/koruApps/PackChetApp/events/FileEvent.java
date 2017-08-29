package shenkar.koruApps.PackChetApp.events;

import java.io.File;

/**
 * Created by danielluzgarten on 29/08/2017.
 */

public class FileEvent {
    String action;
    File eventFile;
    String fileFullName;

    public File getEventFile() {
        return eventFile;
    }

    public String getFileFullName() {
        return fileFullName;
    }

    public FileEvent(String name, String msg, File f) {
        this.eventFile = f;
        this.action = msg;
        this.fileFullName = name;

    }

    public String getAction() {

        return action;
    }
}
