package com.siemion.booklibrary;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@NonFinal
public class Book {
    Integer id;
    @NonNull String title;
    @NonNull Integer year;
    @NonNull Person author;

    public Book(Integer id, @NonNull String title, @NonNull Integer year,
                @NonNull Person author) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.author = author;
    }

    public Book(@NonNull String title, @NonNull Integer year, @NonNull Person author) {
        this.id = null;
        this.title = title;
        this.year = year;
        this.author = author;
    }
}
