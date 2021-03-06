/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.aps.system.services.entity.model;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import javax.validation.constraints.NotNull;

/**
 * @author E.Santoboni
 */
public class EntityTypeShortDto {

    @NotNull(message = "entityType.code.notBlank")
    private String code;
    @NotNull(message = "entityType.name.notBlank")
    private String name;

    public EntityTypeShortDto() {
    }

    public EntityTypeShortDto(IApsEntity entityType) {
        this.setCode(entityType.getTypeCode());
        this.setName(entityType.getTypeDescription());
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
