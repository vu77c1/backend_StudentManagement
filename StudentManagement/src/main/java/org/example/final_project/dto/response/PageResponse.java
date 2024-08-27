package org.example.final_project.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class PageResponse<T> implements Serializable {
    private int page;
    private int size;
    private long total;
    private long totalPages;
    private T items;
}
