package fr.noeldupuis.hdoapi.common.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class PagedResponse<T> extends RepresentationModel<PagedResponse<T>> {
    
    private List<T> content;
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
    public static class PageMetadata {
        private int page;
        private int size;
        private long totalElements;
        private int totalPages;
        private boolean first;
        private boolean last;
        private boolean hasNext;
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