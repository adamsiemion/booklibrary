package com.siemion.booklibrary;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@RequiredArgsConstructor
class BookEntity {
    @NonNull Integer id;
    @NonNull String title;
    @NonNull Integer year;
    @NonNull Person author;
    @NonFinal Person lender;

    void lend(Person lender) {
        this.lender = lender;
    }

    boolean isLent() {
        return lender != null;
    }

    boolean isAvailable() {
        return lender == null;
    }
}
