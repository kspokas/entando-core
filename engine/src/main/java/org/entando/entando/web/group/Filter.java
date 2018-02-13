package org.entando.entando.web.group;

import com.agiletec.aps.system.common.FieldSearchFilter;

public class Filter {

    private String attribute;
    private String operator;
    private String value;

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public FieldSearchFilter getFieldSearchFilter() {
        FieldSearchFilter filter = new FieldSearchFilter(this.getAttribute(), this.getValue(), true);
        return filter;
    }
}
