package com.siemion.booklibrary;



import java.util.List;

public interface Library {
    Book add(Book book);
    Book remove(Integer bookId) throws BookNotFoundException, BookIsLentException;
    BookList list();
    List<Book> search(Filter filter);
    void lend(Integer bookId, Person lender) throws BookNotFoundException, BookIsLentException;
    BookWithLender details(Integer bookId) throws BookNotFoundException;
}
