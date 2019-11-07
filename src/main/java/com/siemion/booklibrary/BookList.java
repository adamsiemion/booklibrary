package com.siemion.booklibrary;

import lombok.NonNull;
import lombok.Value;

import java.util.Collection;
import java.util.Collections;

@Value
public class BookList {

    Collection<Book> books;
    long availableBooks;
    long lentBooks;

    BookList(@NonNull Collection<Book> books, long availableBooks, long lentBooks) {
        this.books = Collections.unmodifiableCollection(books);
        this.availableBooks = availableBooks;
        this.lentBooks = lentBooks;
    }
}
