package model;

public class Journal extends LibraryItem implements Borrowable {
    private String volume;
    private String fieldOfStudy;

    public Journal(String id, String title, String author, int year, String volume, String fieldOfStudy) {
        super(id, title, author, year);
        this.volume = volume;
        this.fieldOfStudy = fieldOfStudy;
    }

    @Override
    public String getCategory() { return "Journal"; }

    @Override
    public String display() {
        return String.format("[JOURNAL] %s Vol.%s (%d) | Field: %s | %s",
                title, volume, year, fieldOfStudy, available ? "Available" : "Borrowed");
    }

    @Override
    public boolean borrow(UserAccount user) {
        if (!available) return false;
        available = false;
        incrementTimesBorrowed();
        user.addToBorrowed(this);
        return true;
    }

    @Override
    public boolean returnItem(UserAccount user) {
        available = true;
        user.removeFromBorrowed(this);
        return true;
    }

    public String getVolume() { return volume; }
    public String getFieldOfStudy() { return fieldOfStudy; }
}
