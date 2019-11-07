package com.siemion.booklibrary;

import lombok.NonNull;
import lombok.Value;

@Value
public class Person {
    @NonNull String firstName;
    @NonNull String lastName;
}
