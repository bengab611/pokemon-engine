package pokemon;

import java.util.Scanner;
import java.util.Random;

public class Game {
    private Pokedex pokedex;
    private Movedex movedex;
    private TypeChart typeChart;

    private Trainer[] trainers;
    private Pokemon[] activePkmn;
    private boolean[] fainted;
    private Move[] nextMoves;
    private int[] movePriority;

    private boolean winFlag;
    private int indexOfWinner;
    
    Scanner scan;
    Random rand;

    public Game() {
        scan = new Scanner(System.in);
        rand = new Random();
        
        pokedex = new Pokedex();
        movedex = new Movedex();
        typeChart = new TypeChart();

        trainers = new Trainer[2];
        nextMoves = new Move[2];
        movePriority = new int[2];
        fainted = new boolean[2];
        
        for (int i = 0; i < fainted.length; i++) {
            fainted[i] = false;
        }

        winFlag = false;

        System.out.println("Trainer One: \n");
        initializeTrainer(0);
        System.out.println("Trainer Two: ");
        initializeTrainer(1);
        
        initializeBattle();

        while (!winFlag) {
            cycle();
            for (int i = 0; i < fainted.length; i++) {
                if (fainted[i]) {
                    if (trainers[i].isOutOfPkmn()) {
                        System.out.println(trainers[i].getName() + " is out of usable Pokemon.\n");
                        winFlag = true;
                        indexOfWinner = (i + 1) % 2;
                        break;
                    }

                    sendPkmn(i);
                    fainted[i] = false;
                }
            }
        }

        System.out.println(trainers[indexOfWinner].getName() + " wins!");
    }

    private void cycle() {
        for (int i = 0; i < trainers.length; i++) {
            System.out.print(trainers[i].getName() + "'s " + activePkmn[i].getName());
            System.out.println(" " + activePkmn[i].getHp() + "/" + activePkmn[i].getMaxHp() + " HP");
        }
        System.out.println();

        chooseAction(0);
        chooseAction(1);
        calculateMovePriority();
        executeActions();
    }

    private void chooseAction(int indexOfTrainer) {
        System.out.println("What will " + trainers[indexOfTrainer].getName() + " do?");
        System.out.println("1. Attack");
        System.out.println("2. Switch Pokemon");
        System.out.println("3. Use Item");

        String action = scan.nextLine();
        System.out.println();
        if (action.equals("1") || action.toLowerCase().equals("attack")) {
            nextMoves[indexOfTrainer] = chooseMove(activePkmn[indexOfTrainer]);
        }
    }

    private Move chooseMove(Pokemon pkmn) {
        System.out.println("Select a move: \n");
        System.out.println(pkmn.movesToString());

        String move = scan.nextLine();
        System.out.println();

        for (int i = 0; i < pkmn.getMoves().length; i++) {
            if (pkmn.getMove(i) != null) {
                if (move.equals(String.valueOf(i + 1)) || move.toLowerCase().equals(pkmn.getMove(i).getName().toLowerCase())) {
                    return pkmn.getMove(i);
                }
            }
        }

        System.out.println("Invalid move. Enter number or name of move.");
        return chooseMove(pkmn);
    }

    private void calculateMovePriority() {
        if (activePkmn[0].getSpeed() > activePkmn[1].getSpeed()) {
            movePriority[0] = 1;
            movePriority[1] = 0;
        }

        if (activePkmn[0].getSpeed() < activePkmn[1].getSpeed()) {
            movePriority[0] = 0;
            movePriority[1] = 1;
        }

        else {
            movePriority[0] = rand.nextInt() % 2;
            movePriority[1] = (movePriority[0] + 1) % 2;
        }
    }
    
    private void executeActions() {
        if (movePriority[0] < movePriority[1]) {
            Move temp = nextMoves[0];
            nextMoves[0] = nextMoves[1];
            nextMoves[1] = temp;
        }

        for (int i = 0; i < nextMoves.length; i++) {
            nextMoves[i].execute(typeChart, activePkmn[i], activePkmn[(i + 1) % 2]);
            System.out.println();
            if (activePkmn[(i + 1) % 2].getHp() == 0) {
                System.out.println(activePkmn[(i + 1) % 2].getName() + " fainted!");
                fainted[(i + 1) % 2] = true;
                break;
            }
        }
    }

    private void initializeTrainer(int indexOfTrainer) {
        System.out.println("Enter your name:");
        trainers[indexOfTrainer] = new Trainer(scan.nextLine());
        System.out.println();
        initializePokemon(indexOfTrainer);
    }

    private void initializePokemon(int indexOfTrainer) {
        String name;
        int level;

        for (int i = 0; i < trainers[indexOfTrainer].getTeam().length; i++) {
            System.out.println("What will Pokemon #" + (i + 1) + " be?");
            if (i > 0) {
                System.out.println("To continue with " +  i + " Pokemon, enter \"done\".");
            }

            name = scan.nextLine();
            System.out.println();
            if (name.equals("done") && i > 0) {
                break;
            }

            System.out.println("What level will " + name + " be?");
            level = scan.nextInt();
            scan.nextLine();
            System.out.println();

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
        System.out.println("What moves will " + pkmn.getName() + " know?\n");

        for (int i = 0; i < pkmn.getMoves().length; i++) {
            System.out.println("Enter move #" + (i + 1));
            if (i > 0) {
                System.out.println("To continue with " + i + " moves, enter \"done\"");
            }

            name = scan.nextLine();
            System.out.println();
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
        System.out.println();
    }

    private void sendPkmn(int indexOfTrainer) {
        System.out.println("What pokemon would " + trainers[indexOfTrainer].getName() + " like to send out?");
        System.out.println(trainers[indexOfTrainer]);

        // TODO: Handle having more than one of the same pokemon in team
        String nextPkmn = scan.nextLine();
        for (int i = 0; i < trainers[indexOfTrainer].getTeam().length; i++) {
            if (nextPkmn.equals(String.valueOf(i + 1)) || nextPkmn.toLowerCase().equals(trainers[indexOfTrainer].getPkmn(i).getName().toLowerCase())) {
                activePkmn[indexOfTrainer] = trainers[indexOfTrainer].getPkmn(i);
                System.out.println(trainers[indexOfTrainer].getName() + " sent out " + activePkmn[indexOfTrainer].getName() + ".");
                return;
            }
        }
    }

    private void sendPkmn(int indexOfTrainer, int indexOfPkmn) {
        activePkmn[indexOfTrainer] = trainers[indexOfTrainer].getPkmn(indexOfPkmn);
        System.out.println(trainers[indexOfTrainer].getName() + " sent out " + activePkmn[indexOfTrainer].getName() + ".");
    }
}