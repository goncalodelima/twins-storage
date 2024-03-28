package com.twins.storage.database;

import org.bukkit.configuration.ConfigurationSection;
import pt.gongas.database.Database;
import pt.gongas.database.DatabaseCredentials;
import pt.gongas.database.DatabaseFactory;

public class DatabaseProvider {

    public static Database create(ConfigurationSection section) {
        DatabaseCredentials credentials = DatabaseCredentials.builder()
                .type(section.getString("type"))
                .host(section.getString("host"))
                .database(section.getString("database"))
                .user(section.getString("user"))
                .password(section.getString("password"))
                .file(section.getString("file"))
                .build();

        return new DatabaseFactory().build(credentials);
    }

}
