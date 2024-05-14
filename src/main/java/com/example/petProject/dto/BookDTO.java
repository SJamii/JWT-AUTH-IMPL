package com.example.petProject.dto;

import com.example.petProject.entity.Book;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDTO {
    private Integer id;
    private String title;
    private String author;
    private String publisher;


    public static BookDTO fromEntity(Book book) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setPublisher(book.getPublisher());
        return bookDTO;
    }

    public Book toEntity(BookDTO bookDTO) {
        Book book = new Book();
        book.setId(bookDTO.getId());
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setPublisher(bookDTO.getPublisher());
        return book;
    }


//    public BookDTO(Integer id, String title, String author, String publisher) {
//        this.id = id;
//        this.title = title;
//        this.author = author;
//        this.publisher = publisher;
//    }

    @Override
    public String toString() {
        return "BookDTO{" +
                "id=" + id +
                ", title_name='" + title + '\'' +
                ", author_name='" + author + '\'' +
                ", publisher_name='" + publisher + '\'' +
                '}';
    }
}
