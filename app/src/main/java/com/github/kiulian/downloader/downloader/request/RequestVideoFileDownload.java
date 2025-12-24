package com.github.kiulian.downloader.downloader.request;

import com.github.kiulian.downloader.model.Utils;
import com.github.kiulian.downloader.model.videos.formats.Format;
import java.io.File;
import java.util.UUID;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/downloader/request/RequestVideoFileDownload.class */
public class RequestVideoFileDownload extends Request<RequestVideoFileDownload, File> {
    private final Format format;
    private File outputDirectory = new File("videos");
    private boolean overwrite = false;
    private String fileName = UUID.randomUUID().toString();

    public RequestVideoFileDownload(Format format) {
        this.format = format;
    }

    public Format getFormat() {
        return this.format;
    }

    public File getOutputDirectory() {
        return this.outputDirectory;
    }

    public File getOutputFile() {
        String strRemoveIllegalChars = Utils.removeIllegalChars(this.fileName);
        File file = new File(this.outputDirectory, strRemoveIllegalChars + "." + this.format.extension().value());
        File file2 = file;
        if (!this.overwrite) {
            int i = 1;
            while (true) {
                file2 = file;
                if (!file.exists()) {
                    break;
                }
                file = new File(file.getParentFile(), strRemoveIllegalChars + "(" + i + ")." + this.format.extension().value());
                i++;
            }
        }
        return file2;
    }

    public RequestVideoFileDownload overwriteIfExists(boolean z) {
        this.overwrite = z;
        return this;
    }

    public RequestVideoFileDownload renameTo(String str) {
        this.fileName = str;
        return this;
    }

    public RequestVideoFileDownload saveTo(File file) {
        this.outputDirectory = file;
        return this;
    }
}
