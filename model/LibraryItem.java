package model;

import java.io.Serializable;

public abstract class LibraryItem implements Serializable, Comparable<LibraryItem> {
    protected String id;
    protected String title;
    protected String author;
    protected int year;
    protected boolean available;
    protected int timesBorrowed;

    public LibraryItem(String id, String title, String author, int year) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.available = true;
        this.timesBorrowed = 0;
    }

    public abstract String getCategory();
    public abstract String display();

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getYear() { return year; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public int getTimesBorrowed() { return timesBorrowed; }
    public void incrementTimesBorrowed() { timesBorrowed++; }

    @Override
    public int compareTo(LibraryItem other) {
        return this.title.compareToIgnoreCase(other.title);
    }

    @Override
    public String toString() { return display(); }
}
