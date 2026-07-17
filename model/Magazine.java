package model;

public class Magazine extends LibraryItem implements Borrowable {
    private String issueNumber;
    private String publisher;

    public Magazine(String id, String title, String author, int year, String issueNumber, String publisher) {
        super(id, title, author, year);
        this.issueNumber = issueNumber;
        this.publisher = publisher;
    }

    @Override
    public String getCategory() { return "Magazine"; }

    @Override
    public String display() {
        return String.format("[MAGAZINE] %s (%d) Issue: %s | Publisher: %s | %s",
                title, year, issueNumber, publisher, available ? "Available" : "Borrowed");
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

    public String getIssueNumber() { return issueNumber; }
    public String getPublisher() { return publisher; }
}
