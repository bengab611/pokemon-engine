package pokemon;

public class Action {
    private ActionType type;
    private Move move;
    private int switchIndex;

    public Action(Move move) {
        type = ActionType.ATTACK;
        this.move = move;
    }

    public Action(int switchIndex) {
        type = ActionType.SWITCH;
        this.switchIndex = switchIndex;
    }

    public ActionType getType() { return type; }
    public Move getMove() { return move; }
    public int getSwitchIndex() { return switchIndex; }
}