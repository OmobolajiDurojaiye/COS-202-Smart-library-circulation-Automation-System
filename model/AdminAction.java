package model;

public class AdminAction {
    public enum ActionType { ADD, DELETE }

    private ActionType type;
    private LibraryItem item;

    public AdminAction(ActionType type, LibraryItem item) {
        this.type = type;
        this.item = item;
    }

    public ActionType getType() { return type; }
    public LibraryItem getItem() { return item; }
}
