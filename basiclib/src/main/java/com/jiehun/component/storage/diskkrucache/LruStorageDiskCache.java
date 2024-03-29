package com.jiehun.component.storage.diskkrucache;


import com.jiehun.component.utils.AbLazyLogger;
import com.robin.lazy.cache.disk.DiskCache;
import com.robin.lazy.cache.disk.naming.FileNameGenerator;
import com.robin.lazy.cache.disk.read.ReadFromDisk;
import com.robin.lazy.cache.disk.write.WriteInDisk;

import java.io.File;
import java.io.IOException;

/**
 * Lru算法的磁盘缓存实现
 *
 * @author jiangyufeng
 * @version [版本号, 2015年12月11日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class LruStorageDiskCache implements DiskCache {

    private static final String ERROR_ARG_NULL     = " argument must be not null";
    private static final String ERROR_ARG_NEGATIVE = " argument must be positive number";

    protected DiskStorageLruCache cache;
    private   File                reserveCacheDir;

    protected final FileNameGenerator fileNameGenerator;

    /**
     * @param cacheDir          Directory for file caching
     * @param fileNameGenerator Name generator} for cached files. Generated names must match
     *                          the regex <strong>[a-z0-9_-]{1,64}</strong>
     * @param cacheMaxSize      Max cache size in bytes. <b>0</b> means cache size is
     *                          unlimited.
     * @throws IOException if cache can't be initialized (e.g.
     *                     "No space left on device")
     */
    public LruStorageDiskCache(File cacheDir, FileNameGenerator fileNameGenerator,
                               long cacheMaxSize) throws IOException {
        this(cacheDir, null, fileNameGenerator, cacheMaxSize, 0);
    }

    /**
     * @param cacheDir          Directory for file caching
     * @param reserveCacheDir   null-ok; Reserve directory for file caching. It's used when
     *                          the primary directory isn't available.
     * @param fileNameGenerator Name generator} for cached files. Generated names must match
     *                          the regex <strong>[a-z0-9_-]{1,64}</strong>
     * @param cacheMaxSize      Max cache size in bytes. <b>0</b> means cache size is
     *                          unlimited.
     * @param cacheMaxFileCount Max file count in cache. <b>0</b> means file count is
     *                          unlimited.
     * @throws IOException if cache can't be initialized (e.g.
     *                     "No space left on device")
     */
    public LruStorageDiskCache(File cacheDir, File reserveCacheDir,
                               FileNameGenerator fileNameGenerator, long cacheMaxSize,
                               int cacheMaxFileCount) throws IOException {
        if (cacheDir == null) {
            throw new IllegalArgumentException("cacheDir" + ERROR_ARG_NULL);
        }
        if (cacheMaxSize < 0) {
            throw new IllegalArgumentException("cacheMaxSize"
                    + ERROR_ARG_NEGATIVE);
        }
        if (cacheMaxFileCount < 0) {
            throw new IllegalArgumentException("cacheMaxFileCount"
                    + ERROR_ARG_NEGATIVE);
        }
        if (fileNameGenerator == null) {
            throw new IllegalArgumentException("fileNameGenerator"
                    + ERROR_ARG_NULL);
        }

        if (cacheMaxSize == 0) {
            cacheMaxSize = Long.MAX_VALUE;
        }
        if (cacheMaxFileCount == 0) {
            cacheMaxFileCount = Integer.MAX_VALUE;
        }

        this.reserveCacheDir = reserveCacheDir;
        this.fileNameGenerator = fileNameGenerator;
        initCache(cacheDir, reserveCacheDir, cacheMaxSize, cacheMaxFileCount);
    }

    private void initCache(File cacheDir, File reserveCacheDir,
                           long cacheMaxSize, int cacheMaxFileCount) throws IOException {
        try {
            cache = DiskStorageLruCache.open(cacheDir, 1, 1, cacheMaxSize,
                    cacheMaxFileCount);
        } catch (IOException e) {
            AbLazyLogger.e(e, "");
            if (reserveCacheDir != null) {
                initCache(reserveCacheDir, null, cacheMaxSize,
                        cacheMaxFileCount);
            }
            if (cache == null) {
                throw e; // new RuntimeException("Can't initialize disk cache",
                // e);
            }
        }
    }

    @Override
    public long size() {
        return cache.size();
    }

    @Override
    public File getDirectory() {
        return cache.getDirectory();
    }

    @Override
    public File getFile(String key) {
        DiskStorageLruCache.Snapshot snapshot = null;
        try {
            snapshot = cache.get(getKey(key));
            return snapshot == null ? null : snapshot.getFile(0);
        } catch (IOException e) {
            AbLazyLogger.e(e, "");
            return null;
        } finally {
            if (snapshot != null) {
                snapshot.close();
            }
        }
    }

    @Override
    public <V> V get(String key, ReadFromDisk<V> readFromDisk) {
        File file = getFile(key);
        if (file == null || !file.exists()) {
            return null;
        }
        return readFromDisk.readOut(file);
    }

    @Override
    public <V> boolean put(String key, WriteInDisk<V> writeIn, V value)
            throws IOException {

        DiskStorageLruCache.Editor editor = cache.edit(getKey(key));
        if (editor == null) {
            return false;
        }
        boolean savedSuccessfully = false;
        if (writeIn != null) {
            savedSuccessfully = writeIn.writeIn(editor.newOutputStream(0),
                    value);
        }
        if (savedSuccessfully) {
            editor.commit();
        } else {
            editor.abort();
        }
        return savedSuccessfully;
    }

    @Override
    public <V> boolean put(String key, WriteInDisk<V> writeIn, V value,
                           long maxLimitTime) throws IOException {
        return put(key, writeIn, value);
    }

    @Override
    public boolean remove(String key) {
        try {
            return cache.remove(getKey(key));
        } catch (IOException e) {
            AbLazyLogger.e(e, "");
            return false;
        }
    }

    @Override
    public void close() {
        try {
            cache.close();
        } catch (IOException e) {
            AbLazyLogger.e(e, "");
        }
        cache = null;
    }

    @Override
    public void clear() {
        try {
            cache.delete();
        } catch (IOException e) {
            AbLazyLogger.e(e, "");
        }
        try {
            initCache(cache.getDirectory(), reserveCacheDir,
                    cache.getMaxSize(), cache.getMaxFileCount());
        } catch (IOException e) {
            AbLazyLogger.e(e, "");
        }
    }

    private String getKey(String key) {
        return fileNameGenerator.generate(key);
    }

}
