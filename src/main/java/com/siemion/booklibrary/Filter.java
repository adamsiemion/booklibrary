package com.siemion.booklibrary;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Filter {
    String title;
    Person author;
    Integer year;
}
