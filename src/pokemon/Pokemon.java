package pokemon;

public class Pokemon {
    private final int DEFAULT_IV = 15;

    private String name;
    private Move[] moves;
    private String[] type;
    private int level;
    private int maxHp, hp, atk, def, spAtk, spDef, speed;

    public Pokemon(Pokedex dex, String name, Move[] moves, int level) {
        Pokemon checkPkmn = dex.getPkmn(name);
        if (checkPkmn == null) {
            throw new IllegalArgumentException("Invalid Pokemon name");
        }

        if (moves.length != 4) {
            throw new IllegalArgumentException("Invalid moveset");
        }

        if (level < 1 || level > 100) {
            throw new IllegalArgumentException("Pokemon level must be between 1 and 100 inclusive");
        }

        this.name = name;
        this.moves = moves;
        type = checkPkmn.type;
        this.level = level;

        maxHp = calculateHp(checkPkmn.hp);
        hp = maxHp;
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

    public void takeDamage(int amount) {
        hp -= amount;
        System.out.println(name + " took " + amount + " damage.");
    }

    public void addMove(Move newMove) {
        for (Move move : moves) {
            if (move == null) {
                move = newMove;
                break;
            }
        }
    }

    public void replaceMove(Move newMove, int moveToReplace) {
        moves[moveToReplace] = newMove;
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

        toReturn += "HP: " + hp + "/" + maxHp + "\n";
        toReturn += "Attack: " + atk + "\n";
        toReturn += "Defense: " + def + "\n";
        toReturn += "Special Attack: " + spAtk + "\n";
        toReturn += "Special Defense: " + spDef + "\n";
        toReturn += "Speed: " + speed + "\n";

        return toReturn;
    }
}