/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.dataobject;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.services.page.IPageManager;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;
import org.entando.entando.aps.system.services.dataobject.model.DataTypeDto;
import org.entando.entando.aps.system.services.dataobject.model.DataTypeDtoBuilder;
import org.entando.entando.aps.system.services.dataobjectmodel.DataObjectModel;
import org.entando.entando.aps.system.services.dataobjectmodel.IDataObjectModelManager;
import org.entando.entando.aps.system.services.entity.AbstractEntityService;
import org.entando.entando.aps.system.services.entity.model.EntityTypeShortDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.dataobject.model.DataTypeDtoRequest;
import org.entando.entando.web.dataobject.model.DataTypesBodyRequest;
import org.entando.entando.web.entity.model.EntityTypeDtoRequest;
import org.springframework.validation.BindingResult;

/**
 * @author E.Santoboni
 */
public class DataObjectService extends AbstractEntityService<DataObject, DataTypeDto> implements IDataObjectService {

    private IPageManager pageManager;
    private IDataObjectModelManager dataObjectModelManager;

    @Override
    public PagedMetadata<EntityTypeShortDto> getShortDataTypes(RestListRequest requestList) {
        return super.getShortEntityTypes(SystemConstants.DATA_OBJECT_MANAGER, requestList);
    }

    @Override
    protected IDtoBuilder<DataObject, DataTypeDto> getEntityTypeFullDtoBuilder(IEntityManager masterManager) {
        return new DataTypeDtoBuilder(masterManager.getAttributeRoles());
    }

    @Override
    public DataTypeDto getDataType(String dataTypeCode) {
        return (DataTypeDto) super.getFullEntityType(SystemConstants.DATA_OBJECT_MANAGER, dataTypeCode);
    }

    @Override
    public List<DataTypeDto> addDataTypes(DataTypesBodyRequest bodyRequest, BindingResult bindingResult) {
        return super.addEntityTypes(SystemConstants.DATA_OBJECT_MANAGER, bodyRequest, bindingResult);
    }

    @Override
    public DataTypeDto updateDataType(DataTypeDtoRequest request, BindingResult bindingResult) {
        return (DataTypeDto) super.updateEntityType(SystemConstants.DATA_OBJECT_MANAGER, request, bindingResult);
    }

    @Override
    protected DataObject createEntityType(IEntityManager entityManager, EntityTypeDtoRequest dto, BindingResult bindingResult) throws Throwable {
        DataObject dataObject = super.createEntityType(entityManager, dto, bindingResult);
        DataTypeDtoRequest dtr = (DataTypeDtoRequest) dto;
        if (this.checkModel(dataObject.getTypeCode(), dtr.getListModel(), bindingResult)) {
            dataObject.setListModel(dtr.getListModel());
        }
        if (this.checkModel(dataObject.getTypeCode(), dtr.getDefaultModel(), bindingResult)) {
            dataObject.setDefaultModel(dtr.getDefaultModel());
        }
        if (this.checkPage(dataObject.getTypeCode(), dtr.getViewPage(), bindingResult)) {
            dataObject.setViewPage(dtr.getViewPage());
        }
        return dataObject;
    }

    private boolean checkModel(String typeCode, String modelIdString, BindingResult bindingResult) {
        if (StringUtils.isEmpty(modelIdString)) {
            return false;
        }
        Long longId = null;
        try {
            longId = Long.parseLong(modelIdString);
        } catch (Exception e) {
            this.addError(bindingResult, new String[]{typeCode, modelIdString}, "dataType.modelId.invalid");
            return false;
        }
        DataObjectModel model = this.getDataObjectModelManager().getDataObjectModel(longId);
        if (null == model) {
            this.addError(bindingResult, new String[]{typeCode, modelIdString}, "dataType.modelId.doesNotExist");
            return false;
        } else if (model.getDataType().equals(typeCode)) {
            this.addError(bindingResult, new String[]{typeCode, modelIdString, model.getDataType()}, "dataType.modelId.mismatch");
            return false;
        }
        return true;
    }

    private boolean checkPage(String typeCode, String pageCode, BindingResult bindingResult) {
        if (StringUtils.isEmpty(pageCode)) {
            return false;
        }
        if (null == this.getPageManager().getOnlinePage(pageCode)) {
            this.addError(bindingResult, new String[]{typeCode, pageCode}, "dataType.pageCode.invalid");
            return false;
        }
        return true;
    }

    @Override
    public void deleteDataType(String entityTypeCode) {
        super.deleteEntityType(SystemConstants.DATA_OBJECT_MANAGER, entityTypeCode);
    }

    protected IPageManager getPageManager() {
        return pageManager;
    }

    public void setPageManager(IPageManager pageManager) {
        this.pageManager = pageManager;
    }

    protected IDataObjectModelManager getDataObjectModelManager() {
        return dataObjectModelManager;
    }

    public void setDataObjectModelManager(IDataObjectModelManager dataObjectModelManager) {
        this.dataObjectModelManager = dataObjectModelManager;
    }

}
