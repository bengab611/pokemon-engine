package pokemon;

public class Trainer {
    private String name;
    private Pokemon[] team;

    public Trainer(String name) {
        this.name = name;
        team = new Pokemon[6];
    }

    public void addPkmn(Pokemon newPkmn) {
        for (int i = 0; i < 6; i++) {
            if (team[i] == null) {
                team[i] = newPkmn;
                return;
            }
        }

        System.out.println("Team already has six Pokemon.");
    }

    public void replacePkmn(Pokemon newPkmn, int pkmnToReplace) {
        team[pkmnToReplace] = newPkmn;
    }

    public boolean isOutOfPkmn() {
        for (int i = 0; i < team.length; i++) {
            if (team[i] != null && team[i].getHp() > 0) {
                return false;
            }
        }

        return true;
    }

    public String getName() { return name; }
    public Pokemon getPkmn(int index) { return team[index]; }
    public Pokemon[] getTeam() { return team; }

    @Override
    public String toString() {
        String toReturn = name + "\n";
        for (int i = 0; i < team.length; i++) {
            if (team[i] != null) {
                toReturn += team[i].toString();
            }
        }

        return toReturn;
    }
}