package pokemon;

import java.util.Scanner;

public class Game {
    private Pokedex pokedex;
    private Movedex movedex;

    private Trainer trainerOne, trainerTwo;
    
    Scanner scan;

    public Game() {
        scan = new Scanner(System.in);
        
        pokedex = new Pokedex();
        movedex = new Movedex();

        System.out.println("Trainer One: ");
        initializeTrainer(trainerOne);
        System.out.println("Trainer Two: ");
        initializeTrainer(trainerTwo);
    }

    private void initializeTrainer(Trainer trainer) {
        System.out.println("Enter your name:");
        trainerOne = new Trainer(scan.nextLine());
        System.out.println(trainerOne);
        initializePokemon(trainerOne);
    }

    private void initializePokemon(Trainer trainer) {
        String name;
        int level;

        for (int i = 0; i < 6; i++) {
            System.out.println("What will Pokemon #" + (i + 1) + " be?");
            if (i > 0) {
                System.out.println("To continue with " +  i + " Pokemon, enter \"done\".");
            }

            name = scan.nextLine();
            if (name.equals("done") && i > 0) {
                break;
            }

            System.out.println("What level will " + name + " be?");
            level = scan.nextInt();
            scan.nextLine();

            try {
                trainer.addPokemon(new Pokemon(pokedex, name, level));

                System.out.println(trainer.getPokemon(i));
                initializeMoves(trainer.getPokemon(i));
            }
            catch (IllegalArgumentException e) {
                System.out.println("Invalid Pokemon name and/or level, try again.");
                i--;
            }
        }
    }

    private void initializeMoves(Pokemon pkmn) {
        String name;
        System.out.println("What moves will " + pkmn.getName() + " know?");

        for (int i = 0; i < 4; i++) {
            System.out.println("Enter move #" + (i + 1));
            if (i > 0) {
                System.out.println("To continue with " + i + " moves, enter \"done\"");
            }

            name = scan.nextLine();
            if (name.equals("done") && i > 0) {
                break;
            }

            try {
                pkmn.addMove(new Move(movedex, name));
            }
            catch (IllegalArgumentException e) {
                System.out.println("Invalid move name, try again.");
                i--;
            }
        }
    }
}