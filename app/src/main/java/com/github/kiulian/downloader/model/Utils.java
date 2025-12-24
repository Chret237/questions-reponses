package com.github.kiulian.downloader.model;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/model/Utils.class */
public class Utils {
    private static final char[] ILLEGAL_FILENAME_CHARACTERS = {'/', '\n', '\r', '\t', 0, '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':'};

    public static void closeSilently(Closeable closeable) throws IOException {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

    public static void createOutDir(File file) throws IOException {
        if (file.exists() || file.mkdirs()) {
            return;
        }
        throw new IOException("Could not create output directory: " + file);
    }

    public static String removeIllegalChars(String str) {
        for (char c : ILLEGAL_FILENAME_CHARACTERS) {
            str = str.replace(c, '_');
        }
        return str;
    }
}
