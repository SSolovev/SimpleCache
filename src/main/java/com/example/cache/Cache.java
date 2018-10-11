package com.example.cache;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;


public class Cache<K, V> {
    private long maxCacheTime;
    private Map<K, CachedObject<V>> cachedObjectMap = new ConcurrentHashMap<>();
    private final Object lock = new Object();

    public Cache(long maxCacheTime) throws InterruptedException {
        this.maxCacheTime = maxCacheTime;

        Executors.newSingleThreadExecutor().submit(() -> {
                    try {
                        while (!Thread.currentThread().isInterrupted()) {

                            Thread.sleep(maxCacheTime);

                            synchronized (lock) {
                                cachedObjectMap.entrySet().parallelStream()
                                        .filter(e -> isExpired(e.getValue().getTimestamp()))
                                        .forEach(e -> cachedObjectMap.remove(e.getKey()));
                            }

                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    public Optional<V> getCachedValue(K key, V val) {
        CachedObject<V> fromCache = cachedObjectMap.get(key);

        if (fromCache != null && !isExpired(fromCache.getTimestamp())) {
            return Optional.of(fromCache.getObject());
        } else {
            return Optional.empty();
        }

    }

    public void setCachedValue(K key, V val) {
        CachedObject<V> cachedObject = cachedObjectMap.get(key);
        if (cachedObject != null) {
            synchronized (lock) {
                cachedObject.setObject(val);
                cachedObject.setTimestamp(System.currentTimeMillis());
            }
        } else {
            cachedObjectMap.put(key, new CachedObject<V>(System.currentTimeMillis(), val));
        }


//        Files.readAllBytes()
//
//                Files.write()

    }

    private boolean isExpired(long timestamp) {
        return (System.currentTimeMillis() - timestamp) >= maxCacheTime;
    }

//    private long getTimeOffset(long timestamp) {
//        return System.currentTimeMillis() - timestamp;
//    }
}
