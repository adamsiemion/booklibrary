package com.siemion.booklibrary;

class BookMapper {

    Book toDto(BookEntity bookEntity) {
        return new Book(bookEntity.getId(), bookEntity.getTitle(),
                bookEntity.getYear(), bookEntity.getAuthor());
    }

    BookWithLender toDtoWithLender(BookEntity bookEntity) {
        return new BookWithLender(bookEntity.getId(), bookEntity.getTitle(),
                bookEntity.getYear(), bookEntity.getAuthor(), bookEntity.getLender());
    }

    BookEntity toEntity(Integer id, Book book) {
        return new BookEntity(id, book.getTitle(), book.getYear(), book.getAuthor());
    }
}
