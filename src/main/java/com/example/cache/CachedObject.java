package com.example.cache;

/**
 * Created by sergey on 07.10.2018.
 */
public class CachedObject<V> {

    private long timestamp;
    private V object;

    public CachedObject(long timestamp, V object) {
        this.timestamp = timestamp;
        this.object = object;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public V getObject() {
        return object;
    }

    public void setObject(V object) {
        this.object = object;
    }
}
