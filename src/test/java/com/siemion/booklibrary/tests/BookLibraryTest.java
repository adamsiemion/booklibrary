package com.siemion.booklibrary.tests;

import com.siemion.booklibrary.Book;
import com.siemion.booklibrary.BookIsLentException;
import com.siemion.booklibrary.BookList;
import com.siemion.booklibrary.BookNotFoundException;
import com.siemion.booklibrary.BookWithLender;
import com.siemion.booklibrary.Filter;
import com.siemion.booklibrary.Library;
import com.siemion.booklibrary.LibraryFactory;
import com.siemion.booklibrary.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookLibraryTest {

    private static final int NON_EXISTING_BOOK_ID = 123;

    private Library library = new LibraryFactory().newInstance();
    private Person person = new Person("John", "Smith");
    private Book book1 = new Book("Book title 1", 2019, person);
    private Book book2 = new Book("Book title 2", 2018, person);

    @Test
    void list_shouldReturnEmptyResult_whenNoBooksHaveBeenAdded() {
        // when
        BookList bookList = library.list();
        // then
        assertEquals(0, bookList.getAvailableBooks());
        assertEquals(0, bookList.getLentBooks());
        assertTrue(bookList.getBooks().isEmpty());
    }

    @Test
    void add_shouldMakeListReturnAddedBook() {
        // given
        // when
        library.add(book1);
        // then
        BookList bookList = library.list();
        assertEquals(1, bookList.getAvailableBooks());
        assertEquals(0, bookList.getLentBooks());
        assertEquals(1, bookList.getBooks().size());
        assertEquals(book1.getAuthor(), first(bookList).getAuthor());
        assertEquals(book1.getTitle(), first(bookList).getTitle());
        assertEquals(book1.getYear(), first(bookList).getYear());
    }

    @Test
    void add_shouldGenerateIncreasingId() {
        // given
        Book addedBook1 = library.add(book1);
        // when
        Book addedBook2 = library.add(book2);
        // then
        assertTrue(addedBook1.getId() < addedBook2.getId());
    }

    @Test
    void remove_shouldRemoveBook() throws BookNotFoundException, BookIsLentException {
        // given
        Book addedBook = library.add(book1);
        // when
        Book removedBook = library.remove(addedBook.getId());
        // then
        assertEquals(addedBook, removedBook);
        BookList bookList = library.list();
        assertTrue(bookList.getBooks().isEmpty());
    }

    @Test
    void search_shouldReturnBookMatchingFilter_whenOneFilterCriteriaIsProvided() {
        // given
        library.add(book1);
        library.add(book2);
        // when
        List<Book> bookList = library.search(Filter.builder()
                .title(book1.getTitle())
                .build());
        // then
        assertEquals(1, bookList.size());
        assertEquals(book1.getTitle(), bookList.iterator().next().getTitle());
    }

    @Test
    void search_shouldReturnBookMatchingFilter_whenTwoFilterCriteriaIsProvided() {
        // given
        library.add(book1);
        library.add(book2);
        // when
        List<Book> bookList = library.search(Filter.builder()
                .title(book1.getTitle())
                .year(book1.getYear())
                .build());
        // then
        assertEquals(1, bookList.size());
        assertEquals(book1.getTitle(), bookList.iterator().next().getTitle());
    }

    @Test
    void search_shouldNotReturnAnyBook_whenNoBookMatchesProvidedCriteria() {
        // given
        library.add(book1);
        library.add(book2);
        // when
        List<Book> bookList = library.search(Filter.builder()
                .title(book1.getTitle())
                .year(book2.getYear())
                .build());
        // then
        assertTrue(bookList.isEmpty());
    }

    @Test
    void details_shouldFail_whenBookNotFound() {
        Assertions.assertThrows(BookNotFoundException.class,
                () -> library.details(NON_EXISTING_BOOK_ID));
    }

    @Test
    void details_shouldReturnAddedBook() throws BookNotFoundException {
        // given
        Book addedBook = library.add(book1);
        // when
        BookWithLender book = library.details(addedBook.getId());
        // then
        assertTrue(book.isAvailable());
        assertFalse(book.isLent());
        assertEquals(addedBook.getTitle(), book.getTitle());
        assertEquals(addedBook.getYear(), book.getYear());
        assertEquals(addedBook.getAuthor(), book.getAuthor());
    }

    @Test
    void details_shouldReturnLentBook() throws BookNotFoundException,
            BookIsLentException {
        // given
        Book addedBook = library.add(book1);
        library.lend(addedBook.getId(), person);
        // when
        BookWithLender book = library.details(addedBook.getId());
        // then
        assertFalse(book.isAvailable());
        assertTrue(book.isLent());
        assertEquals(person, book.getLender());
        assertEquals(addedBook.getTitle(), book.getTitle());
        assertEquals(addedBook.getYear(), book.getYear());
        assertEquals(addedBook.getAuthor(), book.getAuthor());
    }

    @Test
    void remove_shouldFail_whenBookNotFound() {
        Assertions.assertThrows(BookNotFoundException.class,
                () -> library.remove(NON_EXISTING_BOOK_ID));
    }

    @Test
    void remove_shouldFail_whenBookIsLent() throws BookNotFoundException,
            BookIsLentException {
        // given
        Book addedBook = library.add(book1);
        library.lend(addedBook.getId(), person);
        // when
        Assertions.assertThrows(BookIsLentException.class,
                () -> library.remove(addedBook.getId()));
    }

    @Test
    void lend_shouldFail_whenBookNotFound() {
        Assertions.assertThrows(BookNotFoundException.class,
                () -> library.lend(NON_EXISTING_BOOK_ID, person));
    }

    @Test
    void lend_shouldFail_whenBookIsLent() throws BookNotFoundException,
            BookIsLentException {
        // given
        Book addedBook = library.add(book1);
        library.lend(addedBook.getId(), person);
        // when
        Assertions.assertThrows(BookIsLentException.class,
                () -> library.lend(addedBook.getId(), person));
    }

    private Book first(BookList bookList) {
        return bookList.getBooks().iterator().next();
    }

}