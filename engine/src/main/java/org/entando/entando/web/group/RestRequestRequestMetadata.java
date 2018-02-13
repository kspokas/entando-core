package org.entando.entando.web.group;

import com.agiletec.aps.system.common.FieldSearchFilter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class RestRequestRequestMetadata {

    private String sort;
    private String direction;

    private int pageNum;
    private int pageSize = 5;

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

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
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

        return filters;
    }

}
