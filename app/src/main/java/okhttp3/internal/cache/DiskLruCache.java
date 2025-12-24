package okhttp3.internal.cache;

import com.github.clans.fab.BuildConfig;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Flushable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okhttp3.internal.p002io.FileSystem;
import okhttp3.internal.platform.Platform;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;
import okio.Source;

/* loaded from: classes-dex2jar.jar:okhttp3/internal/cache/DiskLruCache.class */
public final class DiskLruCache implements Closeable, Flushable {
    static final boolean $assertionsDisabled = false;
    static final long ANY_SEQUENCE_NUMBER = -1;
    private static final String CLEAN = "CLEAN";
    private static final String DIRTY = "DIRTY";
    static final String JOURNAL_FILE = "journal";
    static final String JOURNAL_FILE_BACKUP = "journal.bkp";
    static final String JOURNAL_FILE_TEMP = "journal.tmp";
    static final Pattern LEGAL_KEY_PATTERN = Pattern.compile("[a-z0-9_-]{1,120}");
    static final String MAGIC = "libcore.io.DiskLruCache";
    private static final String READ = "READ";
    private static final String REMOVE = "REMOVE";
    static final String VERSION_1 = "1";
    private final int appVersion;
    boolean closed;
    final File directory;
    private final Executor executor;
    final FileSystem fileSystem;
    boolean hasJournalErrors;
    boolean initialized;
    private final File journalFile;
    private final File journalFileBackup;
    private final File journalFileTmp;
    BufferedSink journalWriter;
    private long maxSize;
    boolean mostRecentRebuildFailed;
    boolean mostRecentTrimFailed;
    int redundantOpCount;
    final int valueCount;
    private long size = 0;
    final LinkedHashMap<String, Entry> lruEntries = new LinkedHashMap<>(0, 0.75f, true);
    private long nextSequenceNumber = 0;
    private final Runnable cleanupRunnable = new Runnable(this) { // from class: okhttp3.internal.cache.DiskLruCache.1
        final DiskLruCache this$0;

        {
            this.this$0 = this;
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (this.this$0) {
                if ((!this.this$0.initialized) || this.this$0.closed) {
                    return;
                }
                try {
                    this.this$0.trimToSize();
                } catch (IOException e) {
                    this.this$0.mostRecentTrimFailed = true;
                }
                try {
                    if (this.this$0.journalRebuildRequired()) {
                        this.this$0.rebuildJournal();
                        this.this$0.redundantOpCount = 0;
                    }
                } catch (IOException e2) {
                    this.this$0.mostRecentRebuildFailed = true;
                    this.this$0.journalWriter = Okio.buffer(Okio.blackhole());
                }
            }
        }
    };

    /* loaded from: classes-dex2jar.jar:okhttp3/internal/cache/DiskLruCache$Editor.class */
    public final class Editor {
        private boolean done;
        final Entry entry;
        final DiskLruCache this$0;
        final boolean[] written;

        Editor(DiskLruCache diskLruCache, Entry entry) {
            this.this$0 = diskLruCache;
            this.entry = entry;
            this.written = entry.readable ? null : new boolean[diskLruCache.valueCount];
        }

        public void abort() throws IOException {
            synchronized (this.this$0) {
                if (this.done) {
                    throw new IllegalStateException();
                }
                if (this.entry.currentEditor == this) {
                    this.this$0.completeEdit(this, false);
                }
                this.done = true;
            }
        }

        public void abortUnlessCommitted() {
            synchronized (this.this$0) {
                if (!this.done && this.entry.currentEditor == this) {
                    try {
                        this.this$0.completeEdit(this, false);
                    } catch (IOException e) {
                    }
                }
            }
        }

        public void commit() throws IOException {
            synchronized (this.this$0) {
                if (this.done) {
                    throw new IllegalStateException();
                }
                if (this.entry.currentEditor == this) {
                    this.this$0.completeEdit(this, true);
                }
                this.done = true;
            }
        }

        /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
            jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:12:0x003c
            	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1178)
            	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:53)
            	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
            	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
            */
        void detach() {
            /*
                r4 = this;
                r0 = r4
                okhttp3.internal.cache.DiskLruCache$Entry r0 = r0.entry
                okhttp3.internal.cache.DiskLruCache$Editor r0 = r0.currentEditor
                r1 = r4
                if (r0 != r1) goto L3b
                r0 = 0
                r5 = r0
            Ld:
                r0 = r5
                r1 = r4
                okhttp3.internal.cache.DiskLruCache r1 = r1.this$0
                int r1 = r1.valueCount
                if (r0 >= r1) goto L33
                r0 = r4
                okhttp3.internal.cache.DiskLruCache r0 = r0.this$0     // Catch: java.io.IOException -> L3c
                okhttp3.internal.io.FileSystem r0 = r0.fileSystem     // Catch: java.io.IOException -> L3c
                r1 = r4
                okhttp3.internal.cache.DiskLruCache$Entry r1 = r1.entry     // Catch: java.io.IOException -> L3c
                java.io.File[] r1 = r1.dirtyFiles     // Catch: java.io.IOException -> L3c
                r2 = r5
                r1 = r1[r2]     // Catch: java.io.IOException -> L3c
                r0.delete(r1)     // Catch: java.io.IOException -> L3c
            L2d:
                int r5 = r5 + 1
                goto Ld
            L33:
                r0 = r4
                okhttp3.internal.cache.DiskLruCache$Entry r0 = r0.entry
                r1 = 0
                r0.currentEditor = r1
            L3b:
                return
            L3c:
                r6 = move-exception
                goto L2d
            */
            throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.cache.DiskLruCache.Editor.detach():void");
        }

        public Sink newSink(int i) {
            synchronized (this.this$0) {
                if (this.done) {
                    throw new IllegalStateException();
                }
                if (this.entry.currentEditor != this) {
                    return Okio.blackhole();
                }
                if (!this.entry.readable) {
                    this.written[i] = true;
                }
                try {
                    return new FaultHidingSink(this, this.this$0.fileSystem.sink(this.entry.dirtyFiles[i])) { // from class: okhttp3.internal.cache.DiskLruCache.Editor.1
                        final Editor this$1;

                        {
                            this.this$1 = this;
                        }

                        @Override // okhttp3.internal.cache.FaultHidingSink
                        protected void onException(IOException iOException) {
                            synchronized (this.this$1.this$0) {
                                this.this$1.detach();
                            }
                        }
                    };
                } catch (FileNotFoundException e) {
                    return Okio.blackhole();
                }
            }
        }

        public Source newSource(int i) {
            synchronized (this.this$0) {
                if (this.done) {
                    throw new IllegalStateException();
                }
                if (!this.entry.readable || this.entry.currentEditor != this) {
                    return null;
                }
                try {
                    return this.this$0.fileSystem.source(this.entry.cleanFiles[i]);
                } catch (FileNotFoundException e) {
                    return null;
                }
            }
        }
    }

    /* loaded from: classes-dex2jar.jar:okhttp3/internal/cache/DiskLruCache$Entry.class */
    private final class Entry {
        final File[] cleanFiles;
        Editor currentEditor;
        final File[] dirtyFiles;
        final String key;
        final long[] lengths;
        boolean readable;
        long sequenceNumber;
        final DiskLruCache this$0;

        Entry(DiskLruCache diskLruCache, String str) {
            this.this$0 = diskLruCache;
            this.key = str;
            this.lengths = new long[diskLruCache.valueCount];
            this.cleanFiles = new File[diskLruCache.valueCount];
            this.dirtyFiles = new File[diskLruCache.valueCount];
            StringBuilder sb = new StringBuilder(str);
            sb.append('.');
            int length = sb.length();
            for (int i = 0; i < diskLruCache.valueCount; i++) {
                sb.append(i);
                this.cleanFiles[i] = new File(diskLruCache.directory, sb.toString());
                sb.append(".tmp");
                this.dirtyFiles[i] = new File(diskLruCache.directory, sb.toString());
                sb.setLength(length);
            }
        }

        private IOException invalidLengths(String[] strArr) throws IOException {
            throw new IOException("unexpected journal line: " + Arrays.toString(strArr));
        }

        void setLengths(String[] strArr) throws IOException {
            if (strArr.length != this.this$0.valueCount) {
                throw invalidLengths(strArr);
            }
            for (int i = 0; i < strArr.length; i++) {
                try {
                    this.lengths[i] = Long.parseLong(strArr[i]);
                } catch (NumberFormatException e) {
                    throw invalidLengths(strArr);
                }
            }
        }

        Snapshot snapshot() throws IOException {
            if (!Thread.holdsLock(this.this$0)) {
                throw new AssertionError();
            }
            Source[] sourceArr = new Source[this.this$0.valueCount];
            long[] jArr = (long[]) this.lengths.clone();
            for (int i = 0; i < this.this$0.valueCount; i++) {
                try {
                    sourceArr[i] = this.this$0.fileSystem.source(this.cleanFiles[i]);
                } catch (FileNotFoundException e) {
                    for (int i2 = 0; i2 < this.this$0.valueCount && sourceArr[i2] != null; i2++) {
                        Util.closeQuietly(sourceArr[i2]);
                    }
                    try {
                        this.this$0.removeEntry(this);
                        return null;
                    } catch (IOException e2) {
                        return null;
                    }
                }
            }
            return new Snapshot(this.this$0, this.key, this.sequenceNumber, sourceArr, jArr);
        }

        void writeLengths(BufferedSink bufferedSink) throws IOException {
            for (long j : this.lengths) {
                bufferedSink.writeByte(32).writeDecimalLong(j);
            }
        }
    }

    /* loaded from: classes-dex2jar.jar:okhttp3/internal/cache/DiskLruCache$Snapshot.class */
    public final class Snapshot implements Closeable {
        private final String key;
        private final long[] lengths;
        private final long sequenceNumber;
        private final Source[] sources;
        final DiskLruCache this$0;

        Snapshot(DiskLruCache diskLruCache, String str, long j, Source[] sourceArr, long[] jArr) {
            this.this$0 = diskLruCache;
            this.key = str;
            this.sequenceNumber = j;
            this.sources = sourceArr;
            this.lengths = jArr;
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            for (Source source : this.sources) {
                Util.closeQuietly(source);
            }
        }

        @Nullable
        public Editor edit() throws IOException {
            return this.this$0.edit(this.key, this.sequenceNumber);
        }

        public long getLength(int i) {
            return this.lengths[i];
        }

        public Source getSource(int i) {
            return this.sources[i];
        }

        public String key() {
            return this.key;
        }
    }

    DiskLruCache(FileSystem fileSystem, File file, int i, int i2, long j, Executor executor) {
        this.fileSystem = fileSystem;
        this.directory = file;
        this.appVersion = i;
        this.journalFile = new File(file, JOURNAL_FILE);
        this.journalFileTmp = new File(file, JOURNAL_FILE_TEMP);
        this.journalFileBackup = new File(file, JOURNAL_FILE_BACKUP);
        this.valueCount = i2;
        this.maxSize = j;
        this.executor = executor;
    }

    private void checkNotClosed() {
        synchronized (this) {
            if (isClosed()) {
                throw new IllegalStateException("cache is closed");
            }
        }
    }

    public static DiskLruCache create(FileSystem fileSystem, File file, int i, int i2, long j) {
        if (j <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        if (i2 > 0) {
            return new DiskLruCache(fileSystem, file, i, i2, j, new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue(), Util.threadFactory("OkHttp DiskLruCache", true)));
        }
        throw new IllegalArgumentException("valueCount <= 0");
    }

    private BufferedSink newJournalWriter() throws FileNotFoundException {
        return Okio.buffer(new FaultHidingSink(this, this.fileSystem.appendingSink(this.journalFile)) { // from class: okhttp3.internal.cache.DiskLruCache.2
            static final boolean $assertionsDisabled = false;
            final DiskLruCache this$0;

            {
                this.this$0 = this;
            }

            @Override // okhttp3.internal.cache.FaultHidingSink
            protected void onException(IOException iOException) {
                this.this$0.hasJournalErrors = true;
            }
        });
    }

    private void processJournal() throws IOException {
        this.fileSystem.delete(this.journalFileTmp);
        Iterator<Entry> it = this.lruEntries.values().iterator();
        while (it.hasNext()) {
            Entry next = it.next();
            if (next.currentEditor == null) {
                for (int i = 0; i < this.valueCount; i++) {
                    this.size += next.lengths[i];
                }
            } else {
                next.currentEditor = null;
                for (int i2 = 0; i2 < this.valueCount; i2++) {
                    this.fileSystem.delete(next.cleanFiles[i2]);
                    this.fileSystem.delete(next.dirtyFiles[i2]);
                }
                it.remove();
            }
        }
    }

    private void readJournal() throws IOException {
        BufferedSource bufferedSourceBuffer = Okio.buffer(this.fileSystem.source(this.journalFile));
        try {
            String utf8LineStrict = bufferedSourceBuffer.readUtf8LineStrict();
            String utf8LineStrict2 = bufferedSourceBuffer.readUtf8LineStrict();
            String utf8LineStrict3 = bufferedSourceBuffer.readUtf8LineStrict();
            String utf8LineStrict4 = bufferedSourceBuffer.readUtf8LineStrict();
            String utf8LineStrict5 = bufferedSourceBuffer.readUtf8LineStrict();
            if (!MAGIC.equals(utf8LineStrict) || !VERSION_1.equals(utf8LineStrict2) || !Integer.toString(this.appVersion).equals(utf8LineStrict3) || !Integer.toString(this.valueCount).equals(utf8LineStrict4) || !BuildConfig.FLAVOR.equals(utf8LineStrict5)) {
                throw new IOException("unexpected journal header: [" + utf8LineStrict + ", " + utf8LineStrict2 + ", " + utf8LineStrict4 + ", " + utf8LineStrict5 + "]");
            }
            int i = 0;
            while (true) {
                try {
                    readJournalLine(bufferedSourceBuffer.readUtf8LineStrict());
                    i++;
                } catch (EOFException e) {
                    this.redundantOpCount = i - this.lruEntries.size();
                    if (bufferedSourceBuffer.exhausted()) {
                        this.journalWriter = newJournalWriter();
                    } else {
                        rebuildJournal();
                    }
                    Util.closeQuietly(bufferedSourceBuffer);
                    return;
                }
            }
        } catch (Throwable th) {
            Util.closeQuietly(bufferedSourceBuffer);
            throw th;
        }
    }

    private void readJournalLine(String str) throws IOException {
        String strSubstring;
        int iIndexOf = str.indexOf(32);
        if (iIndexOf == -1) {
            throw new IOException("unexpected journal line: " + str);
        }
        int i = iIndexOf + 1;
        int iIndexOf2 = str.indexOf(32, i);
        if (iIndexOf2 == -1) {
            String strSubstring2 = str.substring(i);
            strSubstring = strSubstring2;
            if (iIndexOf == 6) {
                strSubstring = strSubstring2;
                if (str.startsWith(REMOVE)) {
                    this.lruEntries.remove(strSubstring2);
                    return;
                }
            }
        } else {
            strSubstring = str.substring(i, iIndexOf2);
        }
        Entry entry = this.lruEntries.get(strSubstring);
        Entry entry2 = entry;
        if (entry == null) {
            entry2 = new Entry(this, strSubstring);
            this.lruEntries.put(strSubstring, entry2);
        }
        if (iIndexOf2 != -1 && iIndexOf == 5 && str.startsWith(CLEAN)) {
            String[] strArrSplit = str.substring(iIndexOf2 + 1).split(" ");
            entry2.readable = true;
            entry2.currentEditor = null;
            entry2.setLengths(strArrSplit);
            return;
        }
        if (iIndexOf2 == -1 && iIndexOf == 5 && str.startsWith(DIRTY)) {
            entry2.currentEditor = new Editor(this, entry2);
        } else {
            if (iIndexOf2 == -1 && iIndexOf == 4 && str.startsWith(READ)) {
                return;
            }
            throw new IOException("unexpected journal line: " + str);
        }
    }

    private void validateKey(String str) {
        if (LEGAL_KEY_PATTERN.matcher(str).matches()) {
            return;
        }
        throw new IllegalArgumentException("keys must match regex [a-z0-9_-]{1,120}: \"" + str + "\"");
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        synchronized (this) {
            if (this.initialized && !this.closed) {
                for (Entry entry : (Entry[]) this.lruEntries.values().toArray(new Entry[this.lruEntries.size()])) {
                    if (entry.currentEditor != null) {
                        entry.currentEditor.abort();
                    }
                }
                trimToSize();
                this.journalWriter.close();
                this.journalWriter = null;
                this.closed = true;
                return;
            }
            this.closed = true;
        }
    }

    void completeEdit(Editor editor, boolean z) throws IOException {
        synchronized (this) {
            Entry entry = editor.entry;
            if (entry.currentEditor != editor) {
                throw new IllegalStateException();
            }
            int i = 0;
            if (z) {
                i = 0;
                if (!entry.readable) {
                    int i2 = 0;
                    while (true) {
                        i = 0;
                        if (i2 >= this.valueCount) {
                            break;
                        }
                        if (!editor.written[i2]) {
                            editor.abort();
                            throw new IllegalStateException("Newly created entry didn't create value for index " + i2);
                        }
                        if (!this.fileSystem.exists(entry.dirtyFiles[i2])) {
                            editor.abort();
                            return;
                        }
                        i2++;
                    }
                }
            }
            while (i < this.valueCount) {
                File file = entry.dirtyFiles[i];
                if (!z) {
                    this.fileSystem.delete(file);
                } else if (this.fileSystem.exists(file)) {
                    File file2 = entry.cleanFiles[i];
                    this.fileSystem.rename(file, file2);
                    long j = entry.lengths[i];
                    long size = this.fileSystem.size(file2);
                    entry.lengths[i] = size;
                    this.size = (this.size - j) + size;
                }
                i++;
            }
            this.redundantOpCount++;
            entry.currentEditor = null;
            if (entry.readable || z) {
                entry.readable = true;
                this.journalWriter.writeUtf8(CLEAN).writeByte(32);
                this.journalWriter.writeUtf8(entry.key);
                entry.writeLengths(this.journalWriter);
                this.journalWriter.writeByte(10);
                if (z) {
                    long j2 = this.nextSequenceNumber;
                    this.nextSequenceNumber = 1 + j2;
                    entry.sequenceNumber = j2;
                }
            } else {
                this.lruEntries.remove(entry.key);
                this.journalWriter.writeUtf8(REMOVE).writeByte(32);
                this.journalWriter.writeUtf8(entry.key);
                this.journalWriter.writeByte(10);
            }
            this.journalWriter.flush();
            if (this.size > this.maxSize || journalRebuildRequired()) {
                this.executor.execute(this.cleanupRunnable);
            }
        }
    }

    public void delete() throws IOException {
        close();
        this.fileSystem.deleteContents(this.directory);
    }

    @Nullable
    public Editor edit(String str) throws IOException {
        return edit(str, -1L);
    }

    Editor edit(String str, long j) throws IOException {
        synchronized (this) {
            initialize();
            checkNotClosed();
            validateKey(str);
            Entry entry = this.lruEntries.get(str);
            if (j != -1 && (entry == null || entry.sequenceNumber != j)) {
                return null;
            }
            if (entry != null && entry.currentEditor != null) {
                return null;
            }
            if (!this.mostRecentTrimFailed && !this.mostRecentRebuildFailed) {
                this.journalWriter.writeUtf8(DIRTY).writeByte(32).writeUtf8(str).writeByte(10);
                this.journalWriter.flush();
                if (this.hasJournalErrors) {
                    return null;
                }
                Entry entry2 = entry;
                if (entry == null) {
                    entry2 = new Entry(this, str);
                    this.lruEntries.put(str, entry2);
                }
                Editor editor = new Editor(this, entry2);
                entry2.currentEditor = editor;
                return editor;
            }
            this.executor.execute(this.cleanupRunnable);
            return null;
        }
    }

    public void evictAll() throws IOException {
        synchronized (this) {
            initialize();
            for (Entry entry : (Entry[]) this.lruEntries.values().toArray(new Entry[this.lruEntries.size()])) {
                removeEntry(entry);
            }
            this.mostRecentTrimFailed = false;
        }
    }

    @Override // java.io.Flushable
    public void flush() throws IOException {
        synchronized (this) {
            if (this.initialized) {
                checkNotClosed();
                trimToSize();
                this.journalWriter.flush();
            }
        }
    }

    public Snapshot get(String str) throws IOException {
        synchronized (this) {
            initialize();
            checkNotClosed();
            validateKey(str);
            Entry entry = this.lruEntries.get(str);
            if (entry != null && entry.readable) {
                Snapshot snapshot = entry.snapshot();
                if (snapshot == null) {
                    return null;
                }
                this.redundantOpCount++;
                this.journalWriter.writeUtf8(READ).writeByte(32).writeUtf8(str).writeByte(10);
                if (journalRebuildRequired()) {
                    this.executor.execute(this.cleanupRunnable);
                }
                return snapshot;
            }
            return null;
        }
    }

    public File getDirectory() {
        return this.directory;
    }

    public long getMaxSize() {
        long j;
        synchronized (this) {
            j = this.maxSize;
        }
        return j;
    }

    public void initialize() throws IOException {
        synchronized (this) {
            if (this.initialized) {
                return;
            }
            if (this.fileSystem.exists(this.journalFileBackup)) {
                if (this.fileSystem.exists(this.journalFile)) {
                    this.fileSystem.delete(this.journalFileBackup);
                } else {
                    this.fileSystem.rename(this.journalFileBackup, this.journalFile);
                }
            }
            if (this.fileSystem.exists(this.journalFile)) {
                try {
                    readJournal();
                    processJournal();
                    this.initialized = true;
                    return;
                } catch (IOException e) {
                    Platform.get().log(5, "DiskLruCache " + this.directory + " is corrupt: " + e.getMessage() + ", removing", e);
                    try {
                        delete();
                        this.closed = false;
                    } catch (Throwable th) {
                        this.closed = false;
                        throw th;
                    }
                }
            }
            rebuildJournal();
            this.initialized = true;
        }
    }

    public boolean isClosed() {
        boolean z;
        synchronized (this) {
            z = this.closed;
        }
        return z;
    }

    boolean journalRebuildRequired() {
        int i = this.redundantOpCount;
        return i >= 2000 && i >= this.lruEntries.size();
    }

    void rebuildJournal() throws IOException {
        synchronized (this) {
            if (this.journalWriter != null) {
                this.journalWriter.close();
            }
            BufferedSink bufferedSinkBuffer = Okio.buffer(this.fileSystem.sink(this.journalFileTmp));
            try {
                bufferedSinkBuffer.writeUtf8(MAGIC).writeByte(10);
                bufferedSinkBuffer.writeUtf8(VERSION_1).writeByte(10);
                bufferedSinkBuffer.writeDecimalLong(this.appVersion).writeByte(10);
                bufferedSinkBuffer.writeDecimalLong(this.valueCount).writeByte(10);
                bufferedSinkBuffer.writeByte(10);
                for (Entry entry : this.lruEntries.values()) {
                    if (entry.currentEditor != null) {
                        bufferedSinkBuffer.writeUtf8(DIRTY).writeByte(32);
                        bufferedSinkBuffer.writeUtf8(entry.key);
                        bufferedSinkBuffer.writeByte(10);
                    } else {
                        bufferedSinkBuffer.writeUtf8(CLEAN).writeByte(32);
                        bufferedSinkBuffer.writeUtf8(entry.key);
                        entry.writeLengths(bufferedSinkBuffer);
                        bufferedSinkBuffer.writeByte(10);
                    }
                }
                bufferedSinkBuffer.close();
                if (this.fileSystem.exists(this.journalFile)) {
                    this.fileSystem.rename(this.journalFile, this.journalFileBackup);
                }
                this.fileSystem.rename(this.journalFileTmp, this.journalFile);
                this.fileSystem.delete(this.journalFileBackup);
                this.journalWriter = newJournalWriter();
                this.hasJournalErrors = false;
                this.mostRecentRebuildFailed = false;
            } catch (Throwable th) {
                bufferedSinkBuffer.close();
                throw th;
            }
        }
    }

    public boolean remove(String str) throws IOException {
        synchronized (this) {
            initialize();
            checkNotClosed();
            validateKey(str);
            Entry entry = this.lruEntries.get(str);
            if (entry == null) {
                return false;
            }
            boolean zRemoveEntry = removeEntry(entry);
            if (zRemoveEntry && this.size <= this.maxSize) {
                this.mostRecentTrimFailed = false;
            }
            return zRemoveEntry;
        }
    }

    boolean removeEntry(Entry entry) throws IOException {
        if (entry.currentEditor != null) {
            entry.currentEditor.detach();
        }
        for (int i = 0; i < this.valueCount; i++) {
            this.fileSystem.delete(entry.cleanFiles[i]);
            this.size -= entry.lengths[i];
            entry.lengths[i] = 0;
        }
        this.redundantOpCount++;
        this.journalWriter.writeUtf8(REMOVE).writeByte(32).writeUtf8(entry.key).writeByte(10);
        this.lruEntries.remove(entry.key);
        if (!journalRebuildRequired()) {
            return true;
        }
        this.executor.execute(this.cleanupRunnable);
        return true;
    }

    public void setMaxSize(long j) {
        synchronized (this) {
            this.maxSize = j;
            if (this.initialized) {
                this.executor.execute(this.cleanupRunnable);
            }
        }
    }

    public long size() throws IOException {
        long j;
        synchronized (this) {
            initialize();
            j = this.size;
        }
        return j;
    }

    public Iterator<Snapshot> snapshots() throws IOException {
        Iterator<Snapshot> it;
        synchronized (this) {
            initialize();
            it = new Iterator<Snapshot>(this) { // from class: okhttp3.internal.cache.DiskLruCache.3
                final Iterator<Entry> delegate;
                Snapshot nextSnapshot;
                Snapshot removeSnapshot;
                final DiskLruCache this$0;

                {
                    this.this$0 = this;
                    this.delegate = new ArrayList(this.this$0.lruEntries.values()).iterator();
                }

                @Override // java.util.Iterator
                public boolean hasNext() {
                    if (this.nextSnapshot != null) {
                        return true;
                    }
                    synchronized (this.this$0) {
                        try {
                            if (this.this$0.closed) {
                                return false;
                            }
                            while (this.delegate.hasNext()) {
                                Snapshot snapshot = this.delegate.next().snapshot();
                                if (snapshot != null) {
                                    this.nextSnapshot = snapshot;
                                    return true;
                                }
                            }
                            return false;
                        } catch (Throwable th) {
                            throw th;
                        }
                    }
                }

                @Override // java.util.Iterator
                public Snapshot next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    Snapshot snapshot = this.nextSnapshot;
                    this.removeSnapshot = snapshot;
                    this.nextSnapshot = null;
                    return snapshot;
                }

                @Override // java.util.Iterator
                public void remove() {
                    Snapshot snapshot = this.removeSnapshot;
                    if (snapshot == null) {
                        throw new IllegalStateException("remove() before next()");
                    }
                    try {
                        this.this$0.remove(snapshot.key);
                    } catch (IOException e) {
                    } catch (Throwable th) {
                        this.removeSnapshot = null;
                        throw th;
                    }
                    this.removeSnapshot = null;
                }
            };
        }
        return it;
    }

    void trimToSize() throws IOException {
        while (this.size > this.maxSize) {
            removeEntry(this.lruEntries.values().iterator().next());
        }
        this.mostRecentTrimFailed = false;
    }
}
