package com.taxengine.facts.service.storage;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Primary
public class InMemoryS3StorageService implements S3StorageService {
    private final Map<String, byte[]> objects = new ConcurrentHashMap<>();

    @Override
    public String putObject(String key, byte[] content, String contentType) {
        objects.put(key, content);
        return key;
    }

    @Override
    public byte[] getObject(String key) {
        return objects.getOrDefault(key, new byte[0]);
    }
}
