package com.twins.storage.model.repository;

import com.twins.storage.model.Storage;

public interface StorageFoundationRepository {

    void setup();

    Storage findOne(String id);

    void insert(Storage storage);

    void update(Storage storage);

}
