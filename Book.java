package Library;

import java.io.Serializable;

public class Book implements Serializable {
    public String isbn;
    public String title;
    public String author;
    public String content;

    public Book(String isbn, String title, String author, String content) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.content = content;
    }
}
