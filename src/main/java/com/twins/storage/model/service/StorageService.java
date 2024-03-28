package com.twins.storage.model.service;

import com.twins.storage.model.Storage;
import com.twins.storage.model.repository.StorageFoundationRepository;
import com.twins.storage.model.repository.StorageRepository;
import pt.gongas.database.Database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StorageService implements StorageFoundationService {

    private final Map<String, Storage> cache;
    private final StorageFoundationRepository storageRepository;

    public StorageService(Database database){
        this.cache = new HashMap<>();
        this.storageRepository = new StorageRepository(database);
        this.storageRepository.setup();
    }

    @Override
    public void put(Storage storage) {
        this.cache.put(storage.getPlot().getId().toString(), storage);
        this.storageRepository.insert(storage);
    }

    @Override
    public void update(Storage storage) {
        this.storageRepository.update(storage);
    }

    @Override
    public void remove(Storage storage) {
        this.cache.remove(storage.getPlot().getId().toString());
    }

    @Override
    public List<Storage> getAll() {
        return this.cache
                .keySet()
                .stream()
                .map(this::get)
                .collect(Collectors.toList());
    }

    @Override
    public Storage get(String id) {

        if (this.cache.containsKey(id)) {
            return this.cache.get(id);
        }

        Storage storage = this.storageRepository.findOne(id);

        if (storage != null) {
            this.cache.put(storage.getPlot().getId().toString(), storage);
        }

        return storage;
    }

}
