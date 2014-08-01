package com.wewebu.ow.server.fieldctrlimpl.dynamic;

import java.util.List;

import com.wewebu.ow.server.app.OwComboItem;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.field.OwFieldProvider;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Data handler interface for dependent combo box field control.
 * Implementing classes can be used to control the value of the combo box field,
 * and are responsible for retrieving the data from 3rd party systems.
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
 *@since 4.2.0.0
 */
public interface OwDependentComboboxDataHandler
{
    /**
     * Init method called when OwFieldControl is successfully initialized
     * and attached to context.
     * @param context OwAppContext where OwFieldControl is attached
     * @param configuration OwXMLUtil configuration information
     * @throws OwException
     */
    void init(OwAppContext context, OwXMLUtil configuration) throws OwException;

    /**
     * Provide Combobox results which should be available as options in UI.
     * @param fieldProvider OwFieldProvider current context object who define fields
     * @return List of OwComboItem elements, or empty List
     * @throws OwException
     */
    List<OwComboItem> getData(OwFieldProvider fieldProvider) throws OwException;

    /**
     * Return a list of Id's on which fields of the field provider the handler is dependent. 
     * @return List of Strings, or empty list
     */
    List<String> getDependentFieldIds();

}
