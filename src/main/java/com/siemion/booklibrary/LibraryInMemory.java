package com.siemion.booklibrary;

import lombok.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

class LibraryInMemory implements Library {
    private static final int ID_INITIAL_VALUE = 1;

    private final Map<Integer, BookEntity> bookMap = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(ID_INITIAL_VALUE);
    private final BookMapper bookMapper;

    LibraryInMemory(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    @Override
    public Book add(@NonNull Book book) {
        int id = idGenerator.getAndIncrement();
        BookEntity entity = bookMapper.toEntity(id, book);
        bookMap.put(id, entity);
        return bookMapper.toDto(entity);
    }

    @Override
    public Book remove(@NonNull Integer bookId) throws BookNotFoundException, BookIsLentException {
        synchronized (getExistingNotLentBook(bookId)) {
            BookEntity book = getExistingNotLentBook(bookId);
            bookMap.remove(bookId);
            return bookMapper.toDto(book);
        }
    }

    @Override
    public BookList list() {
        Collection<BookEntity> values = bookMap.values();
        List<Book> bookList = values.stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
        long availableBooks = values.stream()
                .filter(BookEntity::isAvailable)
                .count();
        long lentBooks = values.stream()
                .filter(BookEntity::isLent)
                .count();
        return new BookList(bookList, availableBooks, lentBooks);
    }

    @Override
    public List<Book> search(@NonNull Filter filter) {
        return bookMap.values().stream()
                .filter(book -> matches(book, filter))
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void lend(@NonNull Integer bookId, @NonNull Person lender) throws BookNotFoundException, BookIsLentException {
        synchronized (getExistingNotLentBook(bookId)) {
            BookEntity book = getExistingNotLentBook(bookId);
            book.lend(lender);
        }
    }

    @Override
    public BookWithLender details(@NonNull Integer bookId) throws BookNotFoundException {
        BookEntity book = getExistingBook(bookId);
        return bookMapper.toDtoWithLender(book);
    }

    private BookEntity getExistingNotLentBook(Integer bookId) throws BookNotFoundException, BookIsLentException {
        BookEntity book = getExistingBook(bookId);
        if (book.isLent()) {
            throw new BookIsLentException();
        }
        return book;
    }

    private BookEntity getExistingBook(Integer bookId) throws BookNotFoundException {
        BookEntity book = bookMap.get(bookId);
        if (book == null) {
            throw new BookNotFoundException();
        }
        return book;
    }

    private boolean matches(BookEntity book, Filter filter) {
        return isNullOrEqual(filter.getTitle(), book.getTitle())
                && isNullOrEqual(filter.getAuthor(), book.getAuthor())
                && isNullOrEqual(filter.getYear(), book.getYear());
    }

    private <T> boolean isNullOrEqual(T filterValue, T bookValue) {
        return Objects.isNull(filterValue) || Objects.equals(filterValue, bookValue);
    }
}
