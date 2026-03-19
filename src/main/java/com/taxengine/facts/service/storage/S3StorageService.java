package com.taxengine.facts.service.storage;

public interface S3StorageService {
    String putObject(String key, byte[] content, String contentType);
    byte[] getObject(String key);
}
