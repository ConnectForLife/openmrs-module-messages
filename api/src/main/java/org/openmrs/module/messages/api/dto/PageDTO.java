package org.openmrs.module.messages.api.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.openmrs.module.messages.domain.PagingInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent the DTO for the specific Page resource
 * @param <T> - related type of resource
 */
public class PageDTO<T> {

    private int pageIndex;

    private int pageSize;

    private int contentSize;

    private List<T> content = new ArrayList<T>();

    public PageDTO() { }

    public PageDTO(List<T> content, PagingInfo pagingInfo) {
        createContentInfo(content);
        createPageInfo(pagingInfo);
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public PageDTO<T> setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public PageDTO<T> setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public int getContentSize() {
        return contentSize;
    }

    public PageDTO<T> setContentSize(int contentSize) {
        this.contentSize = contentSize;
        return this;
    }

    public List<T> getContent() {
        return content;
    }

    public PageDTO<T> setContent(List<T> content) {
        this.content = content;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    private void createContentInfo(List<T> content) {
        if (content != null) {
            this.content = content;
            this.contentSize = content.size();
        }
    }

    private void createPageInfo(PagingInfo pagingInfo) {
        if (pagingInfo != null) {
            this.pageIndex = pagingInfo.getPage();
            this.pageSize = pagingInfo.getPageSize();
        }
    }

}
