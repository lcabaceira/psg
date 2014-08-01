package com.alfresco.ow.contractmanagement;

import java.io.Writer;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.alfresco.ow.contractmanagement.fieldmanager.OwContractManagementProperty;
import com.alfresco.ow.contractmanagement.log.OwLog;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.history.OwStandardHistoryPropertyChangeEvent;
import com.wewebu.ow.server.plug.owrecordext.OwContractDocument;
import com.wewebu.ow.server.plug.owrecordext.OwContractPropertiesView;

/**
 *<p>
 * OwContractGroupPropertyView.<br/>
 * Specific wrapping of OwProperty for ContractManagement FieldManager functionality
 * and firing of property change event for auditing.
 * Also necessary for contract management key generation.
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
public class OwContractGroupPropertyView extends OwContractPropertiesView
{
    /**Class logger*/
    private static Logger LOG = OwLog.getLogger(OwContractGroupPropertyView.class);
    private OwPropertyCollection oldProps, changedProps;

    /**
     * Constructor
     * @param propertyGeneratorConfiguration_p 
     */
    public OwContractGroupPropertyView(Map<String, ? extends Object> propertyGeneratorConfiguration_p)
    {
        super(propertyGeneratorConfiguration_p);
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ui.OwView#onActivate(int, java.lang.Object)
     */
    @Override
    protected void onActivate(int index_p, Object reason_p) throws Exception
    {
        super.onActivate(index_p, reason_p);
        if (getDocument() instanceof OwContractDocument)
        {
            mergeProperties(getDocument().getGeneratedProperties());
        }
    }

    @Override
    protected void renderHorizontalProperty(Writer w_p, String strRowClassName_p, OwProperty property_p, OwPropertyClass propertyClass_p) throws Exception
    {
        OwProperty propWrapper = new OwContractManagementProperty(property_p, getObjectRef().getClassName());
        super.renderHorizontalProperty(w_p, strRowClassName_p, propWrapper, propertyClass_p);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyView#onApplyInternal(javax.servlet.http.HttpServletRequest, java.lang.Object)
     */
    @Override
    protected boolean onApplyInternal(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        boolean success = super.onApplyInternal(request_p, oReason_p);
        if (success)
        {
            addHistoryPropertyChangeEvent();
        }

        return success;
    }

    @Override
    protected boolean save(OwPropertyCollection changedProperties_p) throws Exception
    {
        if (changedProperties_p != null && !changedProperties_p.isEmpty())
        {
            this.changedProps = changedProperties_p;
            try
            {
                this.oldProps = getObjectRef().getClonedProperties(this.changedProps.keySet());
            }
            catch (OwException owEx)
            {
                if (LOG.isDebugEnabled())
                {
                    LOG.warn("Unable to retrieve current properties (old) values", owEx);
                }
                else
                {
                    LOG.warn("OwContractGroupPropertyView.save: Unable to retrieve old properties values, audit may be incorrect.");
                }
            }
        }
        return super.save(changedProperties_p);
    }

    protected void addHistoryPropertyChangeEvent() throws Exception
    {
        ((OwMainAppContext) this.getContext()).getHistoryManager().addEvent(OwEventManager.HISTORY_EVENT_TYPE_OBJECT, OwEventManager.HISTORY_EVENT_ID_OBJECT_MODIFY_PROPERTIES,
                new OwStandardHistoryPropertyChangeEvent(getObjectRef(), this.oldProps, this.changedProps), OwEventManager.HISTORY_STATUS_OK);
    }
}
