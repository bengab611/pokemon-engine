package pokemon;

public class Pokemon {
    private final int DEFAULT_IV = 15;

    private String name;
    private Move[] moves;
    private String[] type;
    private int level;
    private int maxHp, hp, atk, def, spAtk, spDef, speed;

    public Pokemon(Pokedex dex, String name, int level) {
        Pokemon checkPkmn = dex.getPkmn(name);
        if (checkPkmn == null) {
            throw new IllegalArgumentException("Invalid Pokemon name");
        }

        if (level < 1 || level > 100) {
            throw new IllegalArgumentException("Pokemon level must be between 1 and 100 inclusive");
        }

        this.name = name;
        moves = new Move[4];
        type = checkPkmn.type;
        this.level = level;

        calculateStats(checkPkmn.hp, checkPkmn.atk, checkPkmn.def, checkPkmn.spAtk, checkPkmn.spDef, checkPkmn.speed);
    }

    public Pokemon(Pokedex dex, String name) {
        this(dex, name, 1);
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

        moves = new Move[4];
        level = 1;
    }

    public void takeDamage(int amount) {
        hp -= amount;
        System.out.println(name + " took " + amount + " damage.");
    }

    public void addMove(Move newMove) {
        for (int i = 0; i < 4; i++) {
            if (moves[i] == null) {
                moves[i] = newMove;
                return;
            }
        }

        System.out.println(name + " already knows 4 moves.");
    }

    public void replaceMove(Move newMove, int moveToReplace) {
        moves[moveToReplace] = newMove;
    }

    public String movesToString() {
        String toReturn = "";
        for (Move move : moves) {
            if (move != null) {
                toReturn += move.toString() + "\n";
            }
        }

        return toReturn;
    }

    public String getName() { return name; }
    public String[] getType() { return type; }
    public int getLevel() { return level; }
    public int getHp() { return hp; }
    public int getAtk() { return atk; }
    public int getDef() {return def; }
    public int getSpAtk() { return spAtk; }
    public int getSpDef() { return spDef; }
    public int getSpeed() { return speed; }
    public Move getMove(int index) { return moves[index]; }
    public Move[] getMoves() { return moves; }

    private void calculateStats(int baseHp, int baseAtk, int baseDef, int baseSpAtk, int baseSpDef, int baseSpeed) {
        // Intger division is fine here, since the original formulas use the floor function after division
        maxHp = (2 * baseHp + DEFAULT_IV) * level / 100 + level + 10;
        hp = maxHp;

        atk = (2 * baseAtk + DEFAULT_IV) * level / 100 + 5;
        def = (2 * baseDef + DEFAULT_IV) * level / 100 + 5;
        spAtk = (2 * baseSpDef + DEFAULT_IV) * level / 100 + 5;
        spDef = (2 * baseSpAtk + DEFAULT_IV) * level / 100 + 5;
        speed = (2 * baseSpeed + DEFAULT_IV) * level / 100 + 5;
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

        toReturn += "HP: " + hp + "/" + maxHp + "\n";
        toReturn += "Attack: " + atk + "\n";
        toReturn += "Defense: " + def + "\n";
        toReturn += "Special Attack: " + spAtk + "\n";
        toReturn += "Special Defense: " + spDef + "\n";
        toReturn += "Speed: " + speed + "\n";

        return toReturn;
    }
}