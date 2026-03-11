package pokemon;

import java.util.Scanner;

public class Game {
    private Pokedex pokedex;
    private Movedex movedex;

    private Trainer[] trainers;
    private Pokemon[] activePkmn;
    private Move[] nextMoves;
    private int[] nextMovesPriority;

    private boolean winFlag;
    
    Scanner scan;

    public Game() {
        scan = new Scanner(System.in);
        
        pokedex = new Pokedex();
        movedex = new Movedex();

        trainers = new Trainer[2];
        nextMoves = new Move[2];
        nextMovesPriority = new int[2];

        winFlag = false;

        System.out.println("Trainer One: ");
        initializeTrainer(0);
        System.out.println("Trainer Two: ");
        initializeTrainer(1);
        
        initializeBattle();

        while (!winFlag) {
            chooseAction(0);
            chooseAction(1);
        }
    }

    private void chooseAction(int indexOfTrainer) {
        System.out.println("What will " + trainers[indexOfTrainer].getName() + " do?");
        System.out.println("1. Attack");
        System.out.println("2. Switch Pokemon");
        System.out.println("3. Use Item");

        String action = scan.nextLine();
        if (action.equals("1") || action.toLowerCase().equals("attack")) {
            nextMoves[indexOfTrainer] = chooseMove(activePkmn[indexOfTrainer]);
        }
    }

    private Move chooseMove(Pokemon pkmn) {
        System.out.println("Select a move: \n");
        System.out.println(pkmn.movesToString());

        String move = scan.nextLine();

        for (int i = 0; i < 4; i++) {
            if (pkmn.getMove(i) != null) {
                if (move.equals(String.valueOf(i + 1)) || move.toLowerCase().equals(pkmn.getMove(i).getName().toLowerCase())) {
                    return pkmn.getMove(i);
                }
            }
        }

        System.out.println("Invalid move. Enter number or name of move.");
        return chooseMove(pkmn);
    }

    private void initializeTrainer(int indexOfTrainer) {
        System.out.println("Enter your name:");
        trainers[indexOfTrainer] = new Trainer(scan.nextLine());
        initializePokemon(indexOfTrainer);
    }

    private void initializePokemon(int indexOfTrainer) {
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
                trainers[indexOfTrainer].addPkmn(new Pokemon(pokedex, name, level));

                System.out.println(trainers[indexOfTrainer].getPkmn(i));
                initializeMoves(trainers[indexOfTrainer].getPkmn(i));
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