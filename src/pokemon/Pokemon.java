package pokemon;

public class Pokemon {
    private int DEFAULT_IV = 15;

    private String name;
    private Move[] moves;
    private String[] type;
    private int level;
    private int hp, atk, def, spAtk, spDef, speed;

    public Pokemon(Pokedex dex, String name, Move[] moves, int level) {
        Pokemon checkPkmn = dex.getPkmn(name);
        if (checkPkmn == null) {
            throw new IllegalArgumentException("Invalid Pokemon name");
        }

        if (moves.length < 1 || moves.length > 4) {
            throw new IllegalArgumentException("Pokemon can't know less than 1 or more than 4 moves");
        }

        if (level < 1 || level > 100) {
            throw new IllegalArgumentException("Pokemon level must be between 1 and 100 inclusive");
        }

        this.name = name;
        this.moves = moves;
        type = checkPkmn.type;
        this.level = level;

        hp = calculateHp(checkPkmn.hp);
        atk = calculateStat(checkPkmn.atk);
        def = calculateStat(checkPkmn.def);
        spAtk = calculateStat(checkPkmn.spAtk);
        spDef = calculateStat(checkPkmn.spDef);
        speed = calculateStat(checkPkmn.speed);
    }

    public Pokemon(String name, String[] type, int hp, int atk, int def, int spAtk, int spDef, int speed) {
        this.name = name;
        this.type = type;
        this.hp = hp;
        this.atk = atk;
        this.def = def;
        this.spAtk = spAtk;
        this.spDef = spDef;
        this.speed = speed;

        moves = null;
        level = 1;
    }

    private int calculateHp(int baseHp) {
        // integer division is okay here, since the original formula uses a floor function after the division
        return (2 * baseHp + DEFAULT_IV) * level / 100 + level + 10;
    }

    private int calculateStat(int baseStat) {
        // integer division is okay here, same as above
        return (2 * baseStat + DEFAULT_IV) * level / 100 + 5;
    }

    @Override
    public String toString() {
        String toReturn = "";
        toReturn += name + " lvl " + level + "\n";
        toReturn += type[0];
        if (!type[1].equals("")) {
            toReturn += ", " + type[1];
        }
        toReturn += "\n\n";

        toReturn += "HP: " + hp + "\n";
        toReturn += "Attack: " + atk + "\n";
        toReturn += "Defense: " + def + "\n";
        toReturn += "Special Attack: " + spAtk + "\n";
        toReturn += "Special Defense: " + spDef + "\n";
        toReturn += "Speed: " + speed;

        return toReturn;
    }
}