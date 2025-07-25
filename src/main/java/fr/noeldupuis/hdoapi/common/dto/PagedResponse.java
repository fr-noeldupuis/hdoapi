package fr.noeldupuis.hdoapi.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "Paginated response with HATEOAS links")
public class PagedResponse<T> extends RepresentationModel<PagedResponse<T>> {
    
    @Schema(description = "List of items in the current page")
    private List<T> content;
    
    @Schema(description = "Pagination metadata")
    private PageMetadata pageMetadata;
    
    public PagedResponse(List<T> content, Page<?> page, String baseUrl) {
        this.content = content;
        this.pageMetadata = new PageMetadata(page);
        addPaginationLinks(page, baseUrl);
    }
    
    private void addPaginationLinks(Page<?> page, String baseUrl) {
        // Self link
        add(Link.of(baseUrl + "?page=" + page.getNumber() + "&size=" + page.getSize(), "self"));
        
        // First page
        if (page.hasPrevious()) {
            add(Link.of(baseUrl + "?page=0&size=" + page.getSize(), "first"));
        }
        
        // Previous page
        if (page.hasPrevious()) {
            add(Link.of(baseUrl + "?page=" + (page.getNumber() - 1) + "&size=" + page.getSize(), "prev"));
        }
        
        // Next page
        if (page.hasNext()) {
            add(Link.of(baseUrl + "?page=" + (page.getNumber() + 1) + "&size=" + page.getSize(), "next"));
        }
        
        // Last page
        if (page.hasNext()) {
            add(Link.of(baseUrl + "?page=" + (page.getTotalPages() - 1) + "&size=" + page.getSize(), "last"));
        }
    }
    
    @Data
    @Schema(description = "Pagination metadata information")
    public static class PageMetadata {
        @Schema(description = "Current page number (0-based)", example = "0")
        private int page;
        
        @Schema(description = "Number of items per page", example = "10")
        private int size;
        
        @Schema(description = "Total number of items across all pages", example = "25")
        private long totalElements;
        
        @Schema(description = "Total number of pages", example = "3")
        private int totalPages;
        
        @Schema(description = "Whether this is the first page", example = "true")
        private boolean first;
        
        @Schema(description = "Whether this is the last page", example = "false")
        private boolean last;
        
        @Schema(description = "Whether there is a next page", example = "true")
        private boolean hasNext;
        
        @Schema(description = "Whether there is a previous page", example = "false")
        private boolean hasPrevious;
        
        public PageMetadata(Page<?> page) {
            this.page = page.getNumber();
            this.size = page.getSize();
            this.totalElements = page.getTotalElements();
            this.totalPages = page.getTotalPages();
            this.first = page.isFirst();
            this.last = page.isLast();
            this.hasNext = page.hasNext();
            this.hasPrevious = page.hasPrevious();
        }
    }
} 