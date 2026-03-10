package pokemon;

public class Trainer {
    private String name;
    private Pokemon[] team;

    public Trainer(String name) {
        this.name = name;
        team = new Pokemon[6];
    }

    public void addPokemon(Pokemon newPkmn) {
        for (Pokemon pkmn : team) {
            if (pkmn == null) {
                pkmn = newPkmn;
                return;
            }
        }

        System.out.println("Team already has six Pokemon.");
    }

    public void replacePokemon(Pokemon newPkmn, int pkmnToReplace) {
        team[pkmnToReplace] = newPkmn;
    }

    public String getName() { return name; }
    public Pokemon getPokemon(int index) { return team[index]; }
    public Pokemon[] getTeam() { return team; }
}