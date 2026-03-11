package pokemon;

import java.util.Scanner;

public class Game {
    private Pokedex pokedex;
    private Movedex movedex;

    private Trainer[] trainers;
    private Pokemon[] activePkmn;

    private boolean isActive;
    
    Scanner scan;

    public Game() {
        scan = new Scanner(System.in);
        
        pokedex = new Pokedex();
        movedex = new Movedex();

        trainers = new Trainer[2];

        System.out.println("Trainer One: ");
        initializeTrainer(trainers[0]);
        System.out.println("Trainer Two: ");
        initializeTrainer(trainers[1]);
        
        initializeBattle();
    }

    private void initializeTrainer(Trainer trainer) {
        System.out.println("Enter your name:");
        trainer = new Trainer(scan.nextLine());
        initializePokemon(trainer);
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
                trainer.addPkmn(new Pokemon(pokedex, name, level));

                System.out.println(trainer.getPkmn(i));
                initializeMoves(trainer.getPkmn(i));
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

    private void initializeBattle() {
        System.out.println(trainers[0].getName() + " vs. " + trainers[1].getName());
        System.out.println("Battle!\n");
        
        activePkmn = new Pokemon[2];
        sendPkmn(0, 0);
        sendPkmn(1, 0);
    }

    private void sendPkmn(int indexOfTrainer, int indexOfPkmn) {
        activePkmn[indexOfTrainer] = trainers[indexOfTrainer].getPkmn(indexOfPkmn);
        System.out.println(trainers[indexOfTrainer].getName() + " sent out " + activePkmn[indexOfTrainer].getName() + ".");
    }
}