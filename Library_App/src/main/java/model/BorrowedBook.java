package model;

import java.time.LocalDate;

public class BorrowedBook {
    private int transactionId;
    private String bookTitle;
    private int bookId;
    private int userId;
    private String username;
    private String status;
    private LocalDate dueDate;

    // Constructors
    public BorrowedBook() {
    }

    public BorrowedBook(int transactionId, int bookId, int userId, String username, String status, LocalDate dueDate) {
        this.transactionId = transactionId;
        this.bookId = bookId;
        this.userId = userId;
        this.username = username;
        this.status = status;
        this.dueDate = dueDate;
    }

    // Getters and Setters
    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
