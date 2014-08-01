package com.wewebu.ow.server.ui.preview;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ui.OwView;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * An {@link OwIntegratedPreview} container.
 * The containee integrated preview is change accordingly to the object set through {@link #setObject(OwObject)}.
 * The integrated preview configuration is passed through the constructor.    
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
public class OwPreview extends OwView
{
    private static final String CLASS_ATTRIBUTE = "class";
    private OwXMLUtil previewConfiguration;
    private OwIntegratedPreview integratedPreview;
    private OwObject object;
    private Class<?> previewClass;

    public OwPreview(OwXMLUtil previewConfiguration, OwObject object)
    {
        this.previewConfiguration = previewConfiguration;
        this.object = object;
    }

    @Override
    protected void init() throws Exception
    {
        super.init();

        String previewClassName = previewConfiguration.getSafeStringAttributeValue(CLASS_ATTRIBUTE, "com.wewebu.ow.server.ui.preview.OwSimpleIntegratedPreview");
        if (previewClassName != null)
        {
            previewClass = Class.forName(previewClassName);
            if (object != null)
            {
                setObject(object);
            }
        }
    }

    public synchronized void setObject(OwObject object) throws Exception
    {
        if (previewClass != null)
        {
            if (integratedPreview != null)
            {
                integratedPreview.detach();
                getViewList().remove(integratedPreview);
            }

            integratedPreview = (OwIntegratedPreview) previewClass.newInstance();
            integratedPreview.setObject(object);
            integratedPreview.setConfiguration(previewConfiguration);

            addView(integratedPreview, null);

            this.object = object;
        }
    }

}
