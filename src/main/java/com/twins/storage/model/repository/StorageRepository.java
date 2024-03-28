package com.twins.storage.model.repository;

import com.twins.storage.model.Storage;
import com.twins.storage.model.adapter.StorageAdapter;
import pt.gongas.database.Database;

public class StorageRepository implements StorageFoundationRepository {

    private final Database database;
    private final StorageAdapter adapter;

    public StorageRepository(Database database) {
        this.database = database;
        this.adapter = new StorageAdapter();
    }

    @Override
    public void setup() {
        database
                .execute("CREATE TABLE IF NOT EXISTS storage (id VARCHAR(255), storage_amount INTEGER, storage_limit INTEGER, storage_level INTEGER)")
                .write();
    }

    @Override
    public Storage findOne(String id) {
        return database
                .execute("SELECT * FROM storage WHERE id = ?")
                .readOneWithAdapter(statement -> statement.set(1, id), this.adapter)
                .join();
    }

    @Override
    public void insert(Storage storage) {
        database.execute("INSERT INTO storage (id, storage_amount, storage_limit, storage_level) VALUES(?,?,?,?)")
                .write(statement -> {
                    statement.set(1, storage.getPlot().getId().toString());
                    statement.set(2, storage.getAmount());
                    statement.set(3, storage.getLimit());
                    statement.set(4, storage.getLevel());
                });
    }

    @Override
    public void update(Storage storage) {
        database
                .execute("UPDATE storage SET storage_amount = ?, storage_limit = ?, storage_level = ? WHERE id = ?")
                .write(statement -> {
                    statement.set(1, storage.getAmount());
                    statement.set(2, storage.getLimit());
                    statement.set(3, storage.getLevel());
                    statement.set(4, storage.getPlot().getId().toString());
                });
    }

}
