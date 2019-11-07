package com.siemion.booklibrary;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BookWithLender extends Book {
    Person lender;

    BookWithLender(Integer id, String title, Integer year, Person author, Person lender) {
        super(id, title, year, author);
        this.lender = lender;
    }

    public boolean isAvailable() {
        return lender == null;
    }

    public boolean isLent() {
        return lender != null;
    }
}
