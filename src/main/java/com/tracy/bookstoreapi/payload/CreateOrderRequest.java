package com.tracy.bookstoreapi.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public class CreateOrderRequest {
    @NotBlank
    @Size(min = 1, max = 50)
    private List<Long> bookIds;

    public CreateOrderRequest() {
    }

    public CreateOrderRequest(List<Long> bookIds) {
        this.bookIds = bookIds;
    }

    public List<Long> getBookIds() {
        return bookIds;
    }

    public void setBookIds(List<Long> bookIds) {
        this.bookIds = bookIds;
    }
}
