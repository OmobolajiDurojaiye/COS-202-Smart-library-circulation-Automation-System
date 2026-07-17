package model;

public interface Borrowable {
    boolean borrow(UserAccount user);
    boolean returnItem(UserAccount user);
}
