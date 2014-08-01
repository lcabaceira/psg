package com.wewebu.ow.server.plug.owrecordext;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;

/**
 *<p>
 * Property view based on a JSP formular for an eFile object.
 * Properties involved in creation of the unique key must be read-only in this view.
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
 *@since 3.1.0.0
 */
public class OwContractPropertyFormView extends OwObjectPropertyFormularView
{
    /**properties set in previous view*/
    private OwPropertyCollection m_previousProperties;

    /**
     * Constructor
     */
    public OwContractPropertyFormView()
    {
        m_previousProperties = new OwStandardPropertyCollection();
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ui.OwView#onActivate(int, java.lang.Object)
     */
    protected void onActivate(int index_p, Object reason_p) throws Exception
    {
        super.onActivate(index_p, reason_p);
        mergeProperties(((OwContractDocument) getDocument()).getGeneratedProperties());
    }

    /**
     * Set the value for properties changed in previous view.
     * @param changedProperties_p 
     */
    public void mergeProperties(OwPropertyCollection changedProperties_p)
    {
        m_previousProperties = changedProperties_p;
        if (changedProperties_p != null)
        {
            m_properties.putAll(changedProperties_p);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView#update(javax.servlet.http.HttpServletRequest)
     */
    protected OwPropertyCollection update(HttpServletRequest request_p) throws Exception
    {
        OwPropertyCollection result = new OwStandardPropertyCollection();
        result.putAll(m_previousProperties);
        OwPropertyCollection update = super.update(request_p);
        if (update != null)
        {
            result.putAll(update);
        }
        //not all mandatory fields are updated!
        if (!m_theFieldManager.getUpdateStatus())
        {
            result = null;
        }
        return result;
    }
}
