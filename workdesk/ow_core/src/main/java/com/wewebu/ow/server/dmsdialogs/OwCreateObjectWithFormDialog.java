package com.wewebu.ow.server.dmsdialogs;

import com.wewebu.ow.server.app.OwJspFormConfigurator;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView;
import com.wewebu.ow.server.dmsdialogs.views.classes.OwObjectClassSelectionCfg;
import com.wewebu.ow.server.ecm.OwObject;

/**
 *<p>
 * Create object dialog with JSP form based property view. 
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
 *@since 3.0.0.0
 */
public class OwCreateObjectWithFormDialog extends OwCreateObjectDialog
{
    /**
     * 
     * @param folderObject_p
     * @param strClassName_p
     * @param strObjectClassParent_p
     * @param openObject_p
     * @param jspForm_p
     * @deprecated will be replaced by {@link #OwCreateObjectWithFormDialog(OwObject, OwObjectClassSelectionCfg, boolean, String)}
     */
    public OwCreateObjectWithFormDialog(OwObject folderObject_p, String strClassName_p, String strObjectClassParent_p, boolean openObject_p, String jspForm_p)
    {
        super(folderObject_p, strClassName_p, strObjectClassParent_p, openObject_p);
        setJspConfigurator(new OwJspFormConfigurator(jspForm_p));
    }

    /**
     * 
     * @param folderObject_p
     * @param classSelectionCfg
     * @param openObject_p
     * @param jspForm_p
     * @since 4.1.0.0
     */
    public OwCreateObjectWithFormDialog(OwObject folderObject_p, OwObjectClassSelectionCfg classSelectionCfg, boolean openObject_p, String jspForm_p)
    {
        super(folderObject_p, classSelectionCfg, openObject_p);
        setJspConfigurator(new OwJspFormConfigurator(jspForm_p));
    }

    /**
     * 
     * @param folderObject_p
     * @param strClassName_p
     * @param strObjectClassParent_p
     * @param openObject_p
     * @param jspFormConfig_p
     * @since 3.1.0.0
     * @deprecated replaced by {@link #OwCreateObjectWithFormDialog(OwObject, OwObjectClassSelectionCfg, boolean, OwJspFormConfigurator)}
     */
    public OwCreateObjectWithFormDialog(OwObject folderObject_p, String strClassName_p, String strObjectClassParent_p, boolean openObject_p, OwJspFormConfigurator jspFormConfig_p)
    {
        super(folderObject_p, strClassName_p, strObjectClassParent_p, openObject_p);
        setJspConfigurator(jspFormConfig_p);
    }

    /**
     * 
     * @param folderObject_p
     * @param classSelectionCfg
     * @param openObject_p
     * @param jspFormConfig_p
     * @since 4.1.0.0
     */
    public OwCreateObjectWithFormDialog(OwObject folderObject_p, OwObjectClassSelectionCfg classSelectionCfg, boolean openObject_p, OwJspFormConfigurator jspFormConfig_p)
    {
        super(folderObject_p, classSelectionCfg, openObject_p);
        setJspConfigurator(jspFormConfig_p);
    }

    /**
     * 
     */
    protected OwPropertyViewBridge createPropertyViewBridge() throws Exception
    {
        OwObjectPropertyFormularView view = new OwObjectPropertyFormularView();
        view.setJspConfigurator(getJspConfigurator());

        return new OwFormPropertyViewBridge(view);
    }
}
