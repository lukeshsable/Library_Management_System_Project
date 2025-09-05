package model;

import java.io.InputStream;

public class AdminBook {
    private int id;
    private String title;
    private String author;
    private String isbn;
    private InputStream imageStream;  // For storing image data (BLOB)
    private String image;
    private int quantity;// URL or path of the image (if using URL/path storage)

    // Getters and Setters for all fields

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public InputStream getImageStream() {
        return imageStream;
    }

    public void setImageStream(InputStream imageStream) {
        this.imageStream = imageStream;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    

    public int getQuantity() {               
        return quantity;
    }

    public void setQuantity(int quantity) {  
        this.quantity = quantity;
    }
}
