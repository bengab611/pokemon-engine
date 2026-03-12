package pokemon;

import java.io.FileReader;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Movedex {
    private final String JSON_FILE_PATH = "resources/moves.json";

    private HashMap<String, Move> moveMap;

    public Movedex() {
        moveMap = new HashMap<>();
        loadJson();
    }

    public Move getMove(String name) {
        if (!moveMap.containsKey(name)) {
            return null;
        }

        return moveMap.get(name);
    }

    private void loadJson() {
        try {
            JSONParser parser = new JSONParser();
            JSONObject allMoves = (JSONObject) parser.parse(new FileReader(JSON_FILE_PATH));

            JSONObject moveStats;

            String name, category, type;
            int power, pp;
            double accuracy;
            
            JSONObject effects;
            JSONObject condition;

            for (Object key : allMoves.keySet()) {
                // TODO: Remove conditional after comment is removed
                if (((String) key).equals("_comment") || ((String) key).equals("_comment2")) {
                    continue;
                }

                moveStats = (JSONObject) allMoves.get(key);

                name = (String) key;
                category = (String) moveStats.get("category");
                type = (String) moveStats.get("type");
                
                power = ((Long) moveStats.get("power")).intValue();
                pp = ((Long) moveStats.get("pp")).intValue();

                accuracy = (Double) moveStats.get("accuracy");

                effects = (JSONObject) moveStats.get("effects");
                condition = (JSONObject) moveStats.get("condition");

                moveMap.put(name, new Move(name, category, type, power, pp, accuracy, effects, condition));
            }
        }
        catch (Exception e) {
                e.printStackTrace();
            }
    }
}