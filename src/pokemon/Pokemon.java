package pokemon;

public class Pokemon {
    private String name;
    private Move[] moves;
    private String[] type;
    private int level;
    private int hp, atk, def, spAtk, spDef, speed;

    public Pokemon(String name, Move[] moves, int level) {
        if (moves.length < 1 || moves.length > 4) {
            throw new IllegalArgumentException("Pokemon can't know less than 1 or more than 4 moves");
        }

        this.moves = moves;
        this.level = level;
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
}