package com.siemion.booklibrary;

public class LibraryFactory {
    public Library newInstance() {
        return new LibraryInMemory(new BookMapper());
    }
}
