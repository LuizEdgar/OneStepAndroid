package com.lutzed.servoluntario.util;

import java.io.File;

/**
 * Created by luizfreitas on 25/05/2017.
 */

public class FileAndPathHolder {
    public String path;
    public File file;

    public FileAndPathHolder(String path, File file) {
        this.path = path;
        this.file = file;
    }
}
