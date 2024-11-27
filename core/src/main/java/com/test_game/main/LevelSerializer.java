package com.test_game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.test_game.main.Levels.*;

public class LevelSerializer {
    private Json json;

    public LevelSerializer() {
        json = new Json();
    }

    // Save the Level to a JSON file
    public void save(Level level, String fileName) {
        String jsonData = json.prettyPrint(level); // Serialize to JSON string
        Gdx.files.local(fileName).writeString(jsonData, false);
    }

    // Load the Level from a JSON file
    public Level load(String fileName) {
        String jsonData = Gdx.files.local(fileName).readString(); // Read JSON string from file
        return json.fromJson(Level.class, jsonData); // Deserialize to Level object
    }
}
