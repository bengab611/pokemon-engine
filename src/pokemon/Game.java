package pokemon;

import java.util.Scanner;
import java.util.Random;

public class Game {
    private Pokedex pokedex;
    private Movedex movedex;
    private TypeChart typeChart;

    private Trainer[] trainers;
    private Pokemon[] activePkmn;
    private Action[] nextActions;
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
        activePkmn = new Pokemon[2];
        nextActions = new Action[2];

        movePriority = new int[2];
        for (int i = 0; i < movePriority.length; i++) {
            movePriority[i] = 0;
        }

        winFlag = false;

        System.out.println("Trainer One:\n");
        initializeTrainer(0);
        System.out.println("Trainer Two:\n");
        initializeTrainer(1);

        initializeBattle();

        while (!winFlag) {
            cycle();
        }

        System.out.println(trainers[indexOfWinner].getName() + " wins!");
    }

    private void cycle() {
        for (int i = 0; i < trainers.length; i++) {
            System.out.println(trainers[i].getName() + "'s " + activePkmn[i].getName());
            System.out.println(" " + activePkmn[i].getHp() + "/" + activePkmn[i].getMaxHp() + " HP");
        }
        System.out.println();

        for (int i = 0; i < trainers.length; i++) {
            nextActions[i] = chooseAction(i);
        }

        calculateMovePriority();

        executeActions();

        if (checkFainted()) {
            return;
        }

        checkConditions();

        if (checkFainted()) {
            return;
        }
    }

    private void executeActions() {
        int first;
        if (movePriority[0] < movePriority[1]) {
            first = 1;
        }
        else {
            first = 0;
        }

        for (int i = 0; i < nextActions.length; i++) {
            if (nextActions[first].getType() == ActionType.ATTACK) {
                if (!activePkmn[first].isFrozen()) {
                    nextActions[first].getMove().execute(typeChart, activePkmn[first], activePkmn[(first + 1) % 2]);
                }
                else {
                    System.out.println(activePkmn[first].getName() + " is frozen solid!");
                }

                if (activePkmn[0].isFainted() || activePkmn[1].isFainted()) {
                    break;
                }
            }

            else if (nextActions[first].getType() == ActionType.SWITCH) {
                sendPkmn(i, nextActions[i].getSwitchIndex());
            }

            if (first == 0) {
                first = 1;
            }
            else {
                first = 0;
            }

            System.out.println();
        }

        for (int i = 0; i < movePriority.length; i++) {
            movePriority[i] = 0;
        }
    }

    private boolean checkFainted() {
        for (int i = 0; i < activePkmn.length; i++) {
            if (activePkmn[i].isFainted()) {
                if (trainers[i].isOutOfPkmn()) {
                    System.out.println(trainers[i].getName() + " is out of usable Pokemon.");
                    winFlag = true;
                    indexOfWinner = (i + 1) % 2;
                    return true;
                }

                sendPkmn(i, choosePkmn(i));
            }
        }

        return false;
    }

    private void checkConditions() {
        for (int i = 0; i < activePkmn.length; i++) {
            if (!activePkmn[i].hasCondition()) {
                continue;
            }

            if (activePkmn[i].isBurned()) {
                System.out.println(activePkmn[i].getName() + " is hurt by its burn!");
                int burnDamage = activePkmn[i].getMaxHp() / 16;
                activePkmn[i].takeDamage(burnDamage);
            }

            if (activePkmn[i].isFrozen()) {
                double thawRoll = rand.nextDouble();
                if (thawRoll < 0.20) {
                    activePkmn[i].setFrozen(false);
                    activePkmn[i].setCondition(false);
                    System.out.println(activePkmn[i].getName() + " thawed out!");
                }
            }
        }
    }

    private Action chooseAction(int indexOfTrainer) {
        System.out.println("What will " + trainers[indexOfTrainer].getName() + " do?");
        System.out.println("1. Attack");
        System.out.println("2. Switch");
        System.out.println("3. Item");

        String action = scan.nextLine();
        System.out.println();

        if (action.equals("1") || action.toLowerCase().equals("attack")) {
            return new Action(chooseMove(activePkmn[indexOfTrainer]));
        }

        else if (action.equals("2") || action.toLowerCase().equals("switch")) {
            movePriority[indexOfTrainer] += 2;
            return new Action(choosePkmn(indexOfTrainer));
        }

        else if (action.equals("3") || action.toLowerCase().equals("item")) {
            System.out.println("TODO: Add Items");
            return chooseAction(indexOfTrainer);
        }

        else {
            System.out.println("Invalid choice, try again.");
            return chooseAction(indexOfTrainer);
        }
    }

    private Move chooseMove(Pokemon pkmn) {
        if (pkmn.isOutOfPp()) {
            return movedex.getMove("Struggle");
        }

        System.out.println("Select a move: \n");
        System.out.println(pkmn.movesToString());

        String move = scan.nextLine();
        System.out.println();

        for (int i = 0; i < pkmn.getMoves().length; i++) {
            if (pkmn.getMove(i) != null) {
                if (move.equals(String.valueOf(i + 1)) || move.toLowerCase().equals(pkmn.getMove(i).getName().toLowerCase())) {
                    if (pkmn.getMove(i).getPp() > 0) {
                        return pkmn.getMove(i);
                    }

                    else {
                        System.out.println(pkmn.getMove(i).getName() + " is out of pp! Choose another move.");
                        return chooseMove(pkmn);
                    }
                }
            }
        }

        System.out.println("Invalid move. Enter number or name of move.");
        return chooseMove(pkmn);
    }

    private int choosePkmn(int indexOfTrainer) {
        System.out.println("What pokemon would " + trainers[indexOfTrainer].getName() + " like to send out?");
        System.out.println(trainers[indexOfTrainer]);

        String nextPkmn = scan.nextLine();
        System.out.println();

        for (int i = 0; i < trainers[indexOfTrainer].getTeam().length; i++) {
            if (nextPkmn.equals(String.valueOf(i + 1)) || nextPkmn.toLowerCase().equals(trainers[indexOfTrainer].getPkmn(i).getName().toLowerCase())) {
                if (trainers[indexOfTrainer].getPkmn(i).isFainted()) {
                    System.out.println("You can't send out a fainted Pokemon.");
                    break;
                }

                return i;
            }
        }

        System.out.println("Invalid choice, try again");
        return choosePkmn(indexOfTrainer);
    }

    private void sendPkmn(int indexOfTrainer, int indexOfPkmn) {
        activePkmn[indexOfTrainer] = trainers[indexOfTrainer].getPkmn(indexOfPkmn);
        System.out.println(trainers[indexOfTrainer].getName() + " sent out " + activePkmn[indexOfTrainer].getName() + ".");
    }

    private void calculateMovePriority() {
        for (int i = 0; i < activePkmn.length; i++) {
            if (activePkmn[i].getSpeed() > activePkmn[(i + 1) % 2].getSpeed()) {
                movePriority[i]++;
                return;
            }
        }

        movePriority[rand.nextInt(2)]++;
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
            System.out.println("What will pokemon #" + (i + 1) + " be?");
            if (i > 0) {
                System.out.println("To continue with " + i + " Pokemon, enter \"done\".");
            }

            name = scan.nextLine();
            System.out.println();

            if (name.equals("done") && i > 0) {
                break;
            }

            if (pokedex.getPkmn(name) == null) {
                System.out.println("Invalid Pokemon name, try again.");
                i--;
                continue;
            }

            System.out.println("What level will " + name + " be?");
            level = scan.nextInt();
            scan.nextLine();
            System.out.println();

            while (level < 1 || level > 100) {
                System.out.println("Level must be between 1 and 100, try again.");
                level = scan.nextInt();
                scan.nextLine();
                System.out.println();
            }

            trainers[indexOfTrainer].addPkmn(new Pokemon(pokedex, name, level));
            System.out.println(trainers[indexOfTrainer].getPkmn(i));
            initializeMoves(trainers[indexOfTrainer].getPkmn(i));
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

            if (movedex.getMove(name) == null) {
                System.out.println("Invalid move name, try again.");
                i--;
                continue;
            }

            pkmn.addMove(new Move(movedex, name));
        }
    }

    private void initializeBattle() {
        System.out.println(trainers[0].getName() + " vs. " + trainers[1].getName());
        System.out.println("Battle!\n");

        sendPkmn(0, 0);
        sendPkmn(1, 0);
        System.out.println();
    }
}