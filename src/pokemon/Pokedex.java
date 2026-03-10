package pokemon;

import java.io.FileReader;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Pokedex {
    private final String JSON_FILE_PATH = "resources/baseStats.json";

    private HashMap<String, Pokemon> pkmnMap;

    public Pokedex() {
        pkmnMap = new HashMap<>();
        loadJson();
    }

    private void loadJson() {
        try {
            JSONParser parser = new JSONParser();
            JSONObject allPkmn = (JSONObject) parser.parse(new FileReader(JSON_FILE_PATH));

            JSONObject pkmnStats;

            String name;
            String[] type;
            int hp, atk, def, spAtk, spDef, speed;

            for (Object key : allPkmn.keySet()) {
                pkmnStats = (JSONObject) allPkmn.get(key);

                type = new String[2];

                name = (String) key;
                type[0] = (String) pkmnStats.get("typeOne");
                type[1] = (String) pkmnStats.get("typeTwo");

                hp = ((Long) pkmnStats.get("hp")).intValue();
                atk = ((Long) pkmnStats.get("atk")).intValue();
                def = ((Long) pkmnStats.get("def")).intValue();
                spAtk = ((Long) pkmnStats.get("spAtk")).intValue();
                spDef = ((Long) pkmnStats.get("spDef")).intValue();
                speed = ((Long) pkmnStats.get("speed")).intValue();

                pkmnMap.put(name, new Pokemon(name, type, hp, atk, def, spAtk, spDef, speed));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}