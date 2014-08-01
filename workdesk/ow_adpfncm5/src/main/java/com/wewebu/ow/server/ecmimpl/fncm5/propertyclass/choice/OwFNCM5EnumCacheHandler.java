package com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.choice;

import com.filenet.api.admin.ChoiceList;
import com.filenet.api.util.Id;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5CacheHandler;
import com.wewebu.ow.server.field.OwEnumCollection;

/**
 *<p>
 * Interface for caching for enum collections.
 * Extends the OwFNCM5CacheHandler for specific OwEnumCollection
 * handling.
 *</p>
 *
 *<p><font size="-2">
 * Alfresco Workdesk<br/>
 * Copyright (c) Alfresco Software, Inc.<br/>
 * All rights reserved.<br/>
 * <br/>
 * For licensing information read the license.txt file or<br/>
 * go to: http://wiki.alfresco.com<br/>
 *</font></p>
 */
public interface OwFNCM5EnumCacheHandler extends OwFNCM5CacheHandler
{
    /**
     * Return the OwEnumCollection which represents the
     * provided ChoiceList
     * @param lst com.filenet.api.admin.ChoiceList to be wrapped as OwEnumCollection
     * @return OwEnumCollection
     */
    OwEnumCollection getEnumCollection(ChoiceList lst);

    /**
     * Return a collection by Id. 
     * @param id com.filenet.api.util.Id which uniquely identifies the OwEnumCollection
     * @return OwEnumCollection if exist, else null is returned
     */
    OwEnumCollection getEnumCollection(Id id);

}