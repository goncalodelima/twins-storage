package com.twins.storage.model.service;

import com.twins.storage.model.Storage;

import java.util.List;

public interface StorageFoundationService {

    void put(Storage storage);

    void update(Storage storage);

    void remove(Storage storage);

    Storage get(String id);

    List<Storage> getAll();

}
