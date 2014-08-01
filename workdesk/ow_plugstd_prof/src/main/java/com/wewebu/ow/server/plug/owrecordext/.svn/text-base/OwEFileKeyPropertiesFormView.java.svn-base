package com.wewebu.ow.server.plug.owrecordext;

import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.ui.OwMultipanel;
import com.wewebu.ow.server.ui.OwView;

/**
 *<p>
 * Creates a view for properties involved in eFile key generation, using the provided JSP file for rendering.
 * In this view, the user has the possibility to set values for the ECM properties involved 
 * in eFile key generation.
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
public class OwEFileKeyPropertiesFormView extends OwObjectPropertyFormularView implements OwMultipanel
{
    /** the id for Next menu item*/
    private int m_iNextButtonIndex;
    private List<String> renderedProps;

    @Override
    protected void init() throws Exception
    {
        super.init();
        disableSaveButton();
    }

    /**
     * Disable the save button.
     */
    private void disableSaveButton()
    {
        if (getMenu() != null)
        {
            getMenu().enable(m_iSaveButton, false);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView#save(com.wewebu.ow.server.ecm.OwPropertyCollection)
     */
    @Override
    protected boolean save(OwPropertyCollection changedProperties_p) throws Exception
    {
        boolean result = false;
        if (getDocument() != null)
        {
            if (changedProperties_p == null)
            {
                changedProperties_p = new OwStandardPropertyCollection();
            }
            if (m_theFieldManager.getUpdateStatus())
            {
                result = ((OwContractDocument) getDocument()).saveGeneratedProperties(this.getObjectRef(), changedProperties_p);
            }
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ui.OwMultipanel#isValidated()
     */
    @SuppressWarnings("unchecked")
    public boolean isValidated() throws Exception
    {
        if (null == m_properties)
        {
            return false;
        }

        Iterator<OwProperty> it = m_properties.values().iterator();

        while (it.hasNext())
        {
            OwProperty prop = it.next();
            OwPropertyClass propClass = prop.getPropertyClass();

            if ((!propClass.isSystemProperty()) && propClass.isRequired() && ((prop.getValue() == null) || (prop.getValue().toString().length() == 0)))
            {
                return false;
            }
        }

        return true;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ui.OwMultipanel#setNextActivateView(com.wewebu.ow.server.ui.OwView)
     */
    public void setNextActivateView(OwView nextView_p) throws Exception
    {
        if (getMenu() != null)
        {
            // add next button
            m_iNextButtonIndex = getMenu().addFormMenuItem(this, getContext().localize("app.OwObjectPropertyView.next", "Weiter"), null, "Next", nextView_p, null);

            getMenu().enable(m_iNextButtonIndex, true);
        }

    }

    /**
     * Event called when user clicked Next button to switch to the next pane. 
     *   @param oReason_p Optional reason object submitted in addMenuItem
     *   @param request_p  HttpServletRequest
     */
    public void onNext(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        verifyRenderedProperties();
        if (onSaveInternal(request_p, oReason_p))
        {
            // activate the next view
            ((OwView) oReason_p).activate();
        }
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ui.OwMultipanel#setPrevActivateView(com.wewebu.ow.server.ui.OwView)
     */
    public void setPrevActivateView(OwView prevView_p) throws Exception
    {
        //do nothing at this moment
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView#renderProperty(java.io.Writer, com.wewebu.ow.server.ecm.OwProperty, boolean)
     */
    @Override
    protected void renderProperty(Writer w_p, OwProperty prop_p, boolean fReadOnly_p) throws Exception
    {
        super.renderProperty(w_p, prop_p, fReadOnly_p);
        if (prop_p != null)
        {
            renderedProps.add(prop_p.getPropertyClass().getClassName());
        }
    }

    public void renderMenuRegion(Writer w_p) throws Exception
    {
        disableSaveButton();
        super.renderMenuRegion(w_p);
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView#onRender(java.io.Writer)
     */
    @Override
    protected void onRender(Writer w_p) throws Exception
    {
        renderedProps = new LinkedList<String>();
        super.onRender(w_p);
    }

    /**
     * Check if the properties specified in the JSP file contains all properties needed for ID generation 
     * @throws OwConfigurationException - thrown when the JSP file doesn't contain all properties needed for ID generation.
     */
    protected void verifyRenderedProperties() throws OwConfigurationException
    {
        Set<String> mandatoryPropNames = ((OwContractDocument) getDocument()).getMandatoryPropertyNames();
        if (!renderedProps.containsAll(mandatoryPropNames))
        {
            mandatoryPropNames.removeAll(renderedProps);
            StringBuilder dynamicErrMessagePart = new StringBuilder();
            for (Iterator<String> iterator = mandatoryPropNames.iterator(); iterator.hasNext();)
            {
                String propName = iterator.next();
                dynamicErrMessagePart.append(propName);
                if (iterator.hasNext())
                {
                    dynamicErrMessagePart.append(", ");
                }
            }
            throw new OwConfigurationException(getContext().localize1("plug.owadddocument.OwAddObjectRecordFunction.missingProperties", "The following property names are not rendered: %1.", dynamicErrMessagePart.toString()));
        }
    }
}
