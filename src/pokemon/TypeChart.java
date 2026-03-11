package pokemon;

import java.util.HashMap;
import java.util.ArrayList;
import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class TypeChart {
    private final String JSON_FILE_PATH = "resources/typeChart.json";

    private HashMap<String, HashMap<String, Double>> typeMap;
    private ArrayList<String> types;

    public TypeChart() {
        initializeTypes();
        initializeMap();
        addEffectiveness();
    }

    public double getEffectiveness(String attackType, String defenseType) {
        return typeMap.get(attackType).get(defenseType);
    }

    private void initializeTypes() {
        types = new ArrayList<>();

        types.add("Normal");
        types.add("Fighting");
        types.add("Flying");
        types.add("Poison");
        types.add("Ground");
        types.add("Rock");
        types.add("Bug");
        types.add("Ghost");
        types.add("Steel");
        types.add("Fire");
        types.add("Water");
        types.add("Grass");
        types.add("Electric");
        types.add("Psychic");
        types.add("Ice");
        types.add("Dragon");
        types.add("Dark");
        types.add("Fairy");
    }

    private void initializeMap() {
        typeMap = new HashMap<>();

        for (String attackType : types) {
            HashMap<String, Double> innerMap = new HashMap<>();

            for (String defenseType : types) {
                innerMap.put(defenseType, 1.0);
            }

            typeMap.put(attackType, innerMap);
        }
    }

    private void addEffectiveness() {
        try {
            JSONParser parser = new JSONParser();
            JSONObject matchups = (JSONObject) parser.parse(new FileReader(JSON_FILE_PATH));

            JSONObject defenseTypes;
            String attackType, defenseType;
            double effectiveness;

            for (Object key : matchups.keySet()) {
                defenseTypes = (JSONObject) matchups.get(key);

                attackType = (String) key;
                for (Object innerKey : defenseTypes.keySet()) {
                    defenseType = (String) innerKey;
                    effectiveness = (Double) defenseTypes.get(innerKey);

                    typeMap.get(attackType).put(defenseType, effectiveness);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}