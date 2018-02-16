package org.entando.entando.web.model.common;

import com.agiletec.aps.system.common.FieldSearchFilter;
import org.apache.commons.lang.StringEscapeUtils;


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


    public Filter() {
        //
    }

    public Filter(String attribute, String value) {
        this.attribute = attribute;
        this.value = value;
    }

    public Filter(String attribute, String value, String operator) {
        this(attribute, value);
        this.operator = operator;
    }

    public String getAttributeName() {
        return this.getAttribute();
    }

    @SuppressWarnings("rawtypes")
    public FieldSearchFilter getFieldSearchFilter() {
        FieldSearchFilter filter = new FieldSearchFilter(StringEscapeUtils.escapeSql(this.getAttributeName()), StringEscapeUtils.escapeSql(this.getValue()), true);
        return filter;
    }

}
