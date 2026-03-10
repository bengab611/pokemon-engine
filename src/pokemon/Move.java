package pokemon;

import java.util.Random;

public class Move {
    private String name;
    private String category;
    private String type;
    private int pp;
    private double accuracy;
    private int power;

    private Random random;
    private final int RANDOM_MIN = 85;
    private final int RANDOM_MAX = 100;

    private boolean critical;

    public Move(String name, String category, String type, int pp, double accuracy, int power) {
        this.name = name;
        this.category = category;
        this.type = type;
        this.pp = pp;
        this.accuracy = accuracy;
        this.power = power;

        random = new Random();

        critical = false;
    }

    public void execute(Pokemon user, Pokemon target) {
        System.out.println(user.getName() + " used " + name + ".");
        pp--;

        double accCalc = random.nextDouble();
        if (accCalc > accuracy) {
            System.out.println(user.getName() + "'s attack missed!");
            return;
        }

        int damage = calculateDamage(user, target);
        target.takeDamage(damage);

        if (critical) {
            System.out.println("A critical hit!");
        }
    }

    private int calculateDamage(Pokemon user, Pokemon target) {
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

        int critMax = 24;
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

        if (Double.compare(damage % 1, 0.5) < 1E-9) {
            return (int) Math.floor(damage);
        }
        return (int) Math.round(damage);
    }
}