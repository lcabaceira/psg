package com.wewebu.ow.server.dmsdialogs.views;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.ui.OwDocument;

/**
 *<p>
 * Provide support for batch operations.
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
public class OwStandardBatchPropertiesHandler implements OwBatchPropertiesHandler
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwStandardBatchPropertiesHandler.class);

    /** contains the properties for batch-Import, set in owplugins.xml ==> <BatchPropertyList>  */
    protected Collection m_batchIndexProperties;
    /** the document*/
    protected OwDocument m_document;
    /** properties from the object */
    protected OwPropertyCollection m_properties;
    /** attribute bag name for the saved batch index properties stored in the document */
    protected static final String BATCH_INDEX_PROPERTIES_ATTRIBTUE_BAG_NAME = "OwObjectPropertyViewBatchIndexProperties";

    /**
     * Constructor.
     * @param document_p - the {@link OwDocument} object
     */
    public OwStandardBatchPropertiesHandler(OwDocument document_p)
    {
        m_document = document_p;
        m_properties = new OwStandardPropertyCollection();
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.views.OwBatchPropertiesHandler#setBatchIndexProperties(java.util.Collection)
     */
    public void setBatchIndexProperties(Collection batchIndexProperties_p)
    {
        m_batchIndexProperties = batchIndexProperties_p;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.views.OwBatchPropertiesHandler#saveBatchIndexProperties()
     */
    public void saveBatchIndexProperties() throws Exception
    {
        if (m_batchIndexProperties != null)
        {
            // batch indexing requested

            if (null != m_document)
            {
                //PropertyCollection to save the values for BatchIndex
                OwPropertyCollection savedBatchProperties = new OwStandardPropertyCollection();

                Iterator it = m_batchIndexProperties.iterator();
                while (it.hasNext())
                {
                    OwProperty prop = (OwProperty) m_properties.get(it.next());

                    if (null != prop)
                    {
                        savedBatchProperties.put(prop.getPropertyClass().getClassName(), prop);
                    }
                }

                m_document.setAttribute(BATCH_INDEX_PROPERTIES_ATTRIBTUE_BAG_NAME, savedBatchProperties);
            }
            else
            {
                String msg = "OwObjectPropertyView.safeBatchIndexProperties: Use OwPropViewDocument as the document class for batch indexing to work.";
                LOG.error(msg);
                throw new OwInvalidOperationException(msg);
            }
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.views.OwBatchPropertiesHandler#clearBatchIndex()
     */
    public void clearBatchIndex() throws OwInvalidOperationException
    {

        if (null != m_document)
        {
            // remove the batch index property so batch indizes are cleared
            m_document.remove(BATCH_INDEX_PROPERTIES_ATTRIBTUE_BAG_NAME);
        }
        else
        {
            String msg = "OwObjectPropertyView.clearBatchIndex: Use OwPropViewDocument as the document class for batch indexing to work.";
            LOG.error(msg);
            throw new OwInvalidOperationException(msg);
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.views.OwBatchPropertiesHandler#onBatchIndex()
     */
    public void onBatchIndex() throws Exception
    {
        // batch indexing requested
        if (null != m_document)
        {
            OwPropertyCollection propCollection = (OwPropertyCollection) m_document.getAttribute(BATCH_INDEX_PROPERTIES_ATTRIBTUE_BAG_NAME);

            if (null != propCollection)
            {
                // === batch index
                Iterator it = propCollection.keySet().iterator();
                while (it.hasNext())
                {
                    OwProperty propPrev = (OwProperty) propCollection.get(it.next());

                    OwProperty propCurrent = (OwProperty) m_properties.get(propPrev.getPropertyClass().getClassName());
                    if (null != propCurrent)
                    {
                        propCurrent.setValue(propPrev.getValue());
                    }
                }
            }

        }
        else
        {
            String msg = "OwObjectPropertyView.onBatchIndex: use OwPropViewDocument as the document class for batch indexing to work.";
            LOG.error(msg);
            throw new OwInvalidOperationException(msg);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.views.OwBatchPropertiesHandler#setProperties(com.wewebu.ow.server.ecm.OwPropertyCollection)
     */
    public void setProperties(OwPropertyCollection properties_p)
    {
        m_properties = properties_p;
    }
}
