package pokemon;

import java.util.ArrayList;
import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class TypeChart {
    private final String JSON_FILE_PATH = "resources/typeChart.json";

    private ArrayList<String> types;
    private double[][] chart;

    public TypeChart() {
        initializeTypes();
        initializeChart();
        addEffectiveness();
    }

    public double getEffectiveness(String attackType, String defenseType) {
        return chart[types.indexOf(attackType)][types.indexOf(defenseType)];
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

    private void initializeChart() {
        chart = new double[types.size()][types.size()];

        for (int i = 0; i < types.size(); i++) {
            for (int j = 0; j < types.size(); j++) {
                chart[i][j] = 1.0;
            }
        }
    }

    private void addEffectiveness() {
        try {
            JSONParser parser = new JSONParser();
            JSONObject matchups = (JSONObject) parser.parse(new FileReader(JSON_FILE_PATH));

            JSONObject defenseTypes;

            for (Object key : matchups.keySet()) {
                defenseTypes = (JSONObject) matchups.get(key);

                for (Object innerKey : defenseTypes.keySet()) {
                    chart[types.indexOf((String) key)][types.indexOf((String) innerKey)] = (Double) defenseTypes.get(innerKey);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}