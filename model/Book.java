package model;

public class Book extends LibraryItem implements Borrowable {
    private String isbn;
    private String genre;

    public Book(String id, String title, String author, int year, String isbn, String genre) {
        super(id, title, author, year);
        this.isbn = isbn;
        this.genre = genre;
    }

    @Override
    public String getCategory() { return "Book"; }

    @Override
    public String display() {
        return String.format("[BOOK] %s by %s (%d) | ISBN: %s | Genre: %s | %s",
                title, author, year, isbn, genre, available ? "Available" : "Borrowed");
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

    public String getIsbn() { return isbn; }
    public String getGenre() { return genre; }
}
