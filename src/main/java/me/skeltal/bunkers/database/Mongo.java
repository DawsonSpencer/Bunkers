package me.skeltal.bunkers.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.skeltal.bunkers.Bunkers;
import me.skeltal.bunkers.profile.GameProfile;
import me.skeltal.bunkers.util.config.ConfigCursor;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.io.Closeable;
import java.util.Collections;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class Mongo implements Closeable {

    private Bunkers plugin;
    private MongoClient client;
    private MongoDatabase mongoDatabase;
    private MongoCollection profiles;

    public Mongo(Bunkers plugin) {
        this.plugin = plugin;
        this.init();
    }

    public void init() {
        ConfigCursor cursor = new ConfigCursor(this.plugin.getRootConfig(), "database.mongo");
        if (!cursor.exists("host")
                || !cursor.exists("port")
                || !cursor.exists("database")
                || !cursor.exists("authentication.enabled")
                || !cursor.exists("authentication.username")
                || !cursor.exists("authentication.password")
                || !cursor.exists("authentication.database")) {
            throw new RuntimeException("Missing MongoDB configuration option");
        }

        if (cursor.getBoolean("authentication.enabled")) {
            MongoCredential credential = MongoCredential.createCredential(
                    cursor.getString("authentication.username"),
                    cursor.getString("authentication.database"),
                    cursor.getString("authentication.password").toCharArray()
            );
            this.client = new MongoClient(new ServerAddress(cursor.getString("host"), cursor.getInt("port")), Collections.singletonList(credential));
        } else {
            this.client = new MongoClient(new ServerAddress(cursor.getString("host"), cursor.getInt("port")));
        }

        this.mongoDatabase = this.client.getDatabase(cursor.getString("database"));
        this.profiles = this.mongoDatabase.getCollection("profiles");
    }

    public void disable() {
        for (Player player : this.plugin.getServer().getOnlinePlayers()) {
            GameProfile.getByUuid(player.getUniqueId()).save();
        }
        this.close();
    }

    public void dropProfiles() {
        this.profiles.drop();
    }

    public Document getProfile(UUID uuid) {
        return (Document) this.profiles.find(Filters.eq("uuid", uuid.toString())).first();
    }

    public void replacePlayer(GameProfile gameProfile, Document document) {
        this.profiles.replaceOne(Filters.eq("uuid", gameProfile.getUuid().toString()), document, new ReplaceOptions().upsert(true));
    }

    @Override
    public void close() {
        if(this.client != null) {
            this.client.close();
        }
    }

}
