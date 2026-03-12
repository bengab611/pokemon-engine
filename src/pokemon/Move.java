package pokemon;

import java.util.Random;

import org.json.simple.JSONObject;

public class Move {
    private String name, category, type;
    private int power, pp, maxPp;
    private double accuracy;

    private JSONObject effects;
    private JSONObject condition;

    private final double[] MULTISTRIKE_ARRAY = {0.35, 0.35, 0.15, 0.15};

    private Random random;
    private final int RANDOM_MIN = 85;
    private final int RANDOM_MAX = 100;

    private boolean critical;
    private double typeMultiplier;

    public Move(Movedex dex, String name) {
        Move checkMove = dex.getMove(name);
        if (checkMove == null) {
            throw new IllegalArgumentException("Invalid move name");
        }

        this.name = name;
        this.category = checkMove.category;
        this.type = checkMove.type;
        this.power = checkMove.power;
        this.pp = checkMove.pp;
        maxPp = pp;
        this.accuracy = checkMove.accuracy;

        this.effects = checkMove.effects;
        this.condition = checkMove.condition;

        random = new Random();
    }

    public Move(String name, String category, String type, int power, int pp, double accuracy, JSONObject effects, JSONObject condition) {
        this.name = name;
        this.category = category;
        this.type = type;
        this.power = power;
        this.pp = pp;
        maxPp = pp;
        this.accuracy = accuracy;

        this.effects = effects;
        this.condition = condition;

        random = new Random();

        critical = false;
    }

    public void execute(TypeChart typeChart, Pokemon user, Pokemon target) {
        System.out.println(user.getName() + " used " + name + ".");
        pp--;

        double accCalc = random.nextDouble();
        if (accCalc > accuracy) {
            System.out.println(user.getName() + "'s attack missed!");
            return;
        }

        int hits = 1;
        if (effects != null && effects.get("multistrike") != null) {
            if (((Long) effects.get("multistrike")).intValue() == 5) {
                double multistrikeRoll = random.nextDouble();
                double chanceTotal = 0.0;
                for (int i = 0; i < MULTISTRIKE_ARRAY.length; i++) {
                    chanceTotal += MULTISTRIKE_ARRAY[i];
                    if (multistrikeRoll < chanceTotal) {
                        hits = (i + 2);
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < hits; i++) {
            int damage = calculateDamage(typeChart, user, target);

            if (typeMultiplier == 0.0) {
                System.out.println("It doesn't affect " + target.getName() + "...");
                return;
            }

            target.takeDamage(damage);

            if (critical) {
                System.out.println("A critical hit!");
                critical = false;
            }

            if (target.isFainted()) {
                break;
            }
        }

        if (typeMultiplier > 0.0 && typeMultiplier < 1.0) {
            System.out.println("It's not very effective...");
        }

        if (typeMultiplier > 1.0) {
            System.out.println("It's super effective!");
        }

        if (!target.isFainted()) {
            inflictConditions(target);
        }
    }

    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getType() { return type; }
    public int getPower() { return power; }
    public int getPp() { return pp; }
    public double getAccuracy() { return accuracy; }

    public boolean isOutOfPp() { return pp == 0; }

    private int calculateDamage(TypeChart typeChart, Pokemon user, Pokemon target) {
        if (category.equals("Status")) {
            return 0;
        }

        double damage = (2.0 * user.getLevel() / 5 + 2) * power;

        if (category.equals("Physical")) {
            damage = damage * user.getAtk() / target.getDef();
        }
        else if (category.equals("Special")) {
            damage = damage * user.getSpAtk() / target.getSpDef();
        }

        damage = damage / 50 + 2;

        int critStage = user.getCritStage();
        if (effects != null && effects.get("increased_crit") != null) {
            critStage += ((Long) effects.get("increased_crit")).intValue();
        }
        if (critStage > 3) {
            critStage = 3;
        }

        int critMax;
        switch (critStage) {
            case 0: 
                critMax = 24;
                break;
            case 1: 
                critMax = 8;
                break;
            case 2: 
                critMax = 2;
                break;
            case 3: 
                critMax = 1;
                break;
            default:
                critMax = 24;
        }

        double critCalc = random.nextInt(critMax) + 1;
        if (critCalc == 1) {
            critical = true;
            damage *= 1.5;
        }

        double variability = (random.nextInt(RANDOM_MAX - RANDOM_MIN + 1) + RANDOM_MIN) / 100.0;
        damage *= variability;

        if (type.equals(user.getType()[0]) || type.equals(user.getType()[1])) {
            damage *= 1.5;
        }

        typeMultiplier = typeChart.getEffectiveness(type, target.getType()[0]);
        if (!target.getType()[1].equals("")) {
            typeMultiplier *= typeChart.getEffectiveness(type, target.getType()[1]);
        }
        damage *= typeMultiplier;

        if (user.isBurned() && category == "Physical") {
            damage *= 0.5;
        }

        if (Double.compare(damage % 1, 0.5) < 1E-9) {
            return (int) Math.floor(damage);
        }
        return (int) Math.round(damage);
    }

    private void inflictConditions(Pokemon target) {
        if (condition == null || target.hasCondition()) {
            return;
        }

        double conditionRoll = random.nextDouble();

        if (condition.get("burn") != null) {
            double burnChance = (Double) condition.get("burn");
            if (conditionRoll < burnChance) {
                target.setCondition(true);
                target.setBurn(true);
                System.out.println(target.getName() + " was burned!");
            }
        }

        if (condition.get("freeze") != null) {
            double freezeChance = (Double) condition.get("freeze");
            if (conditionRoll < freezeChance) {
                target.setCondition(true);
                target.setFrozen(true);
                System.out.println(target.getName() + " was frozen!");
            }
        }
    }

    @Override
    public String toString() {
        String toReturn = name + "\n";
        toReturn += "\tPP: " + pp + "/" + maxPp + "\n";
        toReturn += "\t" + category + " " + type + "\n";
        
        if (!category.equals("Status")) {
            toReturn += "\t" + power + " power";
        }

        return toReturn;
    }
}