package org.entando.entando.web.model.common;

import com.agiletec.aps.system.common.FieldSearchFilter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class RestListRequest {

    private String sort;
    private String direction;

    private Integer pageNum = 0;
    private Integer pageSize = 5;

    private Filter[] filter;

    public Filter[] getFilter() {
        return filter;
    }

    public void setFilter(Filter[] filter) {
        this.filter = filter;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }


    @SuppressWarnings("rawtypes")
    public FieldSearchFilter[] getFieldSearchFilters() {
        FieldSearchFilter[] filters = new FieldSearchFilter[0];
        if (null != filter && filter.length > 0) {
            for (Filter filter : filter) {
                filters = ArrayUtils.add(filters, filter.getFieldSearchFilter());
            }
        }
        if (StringUtils.isNotBlank(this.getSort())) {
            FieldSearchFilter sort = new FieldSearchFilter(this.getSort());
            if (StringUtils.isNotBlank(this.getDirection())) {
                sort.setOrder(FieldSearchFilter.Order.valueOf(this.getDirection()));
            }
            filters = ArrayUtils.add(filters, sort);
        }

        if (null != this.getPageSize()) {
            FieldSearchFilter pageFilter = new FieldSearchFilter(this.getPageSize(), this.getOffset());
            filters = ArrayUtils.add(filters, pageFilter);
        }

        return filters;
    }

    private Integer getOffset() {
        int page = this.getPageNum();
        if (null == this.getPageNum() || this.getPageNum() == 0) {
            return 0;
        }
        return this.getPageSize() * page;

    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

}