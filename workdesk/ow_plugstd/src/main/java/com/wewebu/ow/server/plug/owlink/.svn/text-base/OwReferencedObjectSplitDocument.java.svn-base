package com.wewebu.ow.server.plug.owlink;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.dmsdialogs.views.OwSplitObjectListDocument;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectLink;
import com.wewebu.ow.server.ecm.OwObjectLinkRelation;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.exceptions.OwRuntimeException;
import com.wewebu.ow.server.field.OwFieldColumnInfo;
import com.wewebu.ow.server.plug.std.log.OwLog;

/**
 *<p>
 * Special OwSplitObjectListDocument implementation.
 * Special document will resolves the links to specific OwObject representations.
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
 *@since 4.1.1.0
 */
public class OwReferencedObjectSplitDocument extends OwSplitObjectListDocument
{
    private static final Logger LOG = OwLog.getLogger(OwReferencedObjectSplitDocument.class);
    private OwObjectLinkRelation[] relations;

    public OwReferencedObjectSplitDocument(String[] splits, Collection<OwFieldColumnInfo>[] columnInfos, List<OwDocumentFunction> documentFunctions)
    {
        super(splits, columnInfos, documentFunctions);
    }

    @SuppressWarnings("unchecked")
    @Override
    public OwObjectCollection getSplit(int index)
    {
        OwObjectCollection col = super.getSplit(index);
        OwObjectCollection retCol = null;
        if (col != null)
        {
            retCol = new OwStandardObjectCollection();
            OwObjectLinkRelation relation = getRelations()[index];
            Iterator<?> it = col.iterator();
            while (it.hasNext())
            {
                OwObjectLink link = (OwObjectLink) it.next();
                OwObject retObj = getOwObject(link, relation);
                if (retObj != null)
                {
                    retCol.add(retObj);
                }
            }
        }
        return retCol;
    }

    public void setRelations(OwObjectLinkRelation[] relations)
    {
        this.relations = relations;
    }

    public OwObjectLinkRelation[] getRelations()
    {
        return this.relations;
    }

    /**
     * Extract object from link base on provided relation.
     * Can throw a OwProcessingRuntimeException in case retrieving relation object
     * throws an exception.
     * @param link OwObjectLink
     * @param relation OwObjectLinkRelation
     * @return OwObject
     */
    protected OwObject getOwObject(OwObjectLink link, OwObjectLinkRelation relation)
    {
        try
        {
            switch (relation)
            {
                case OUTBOUND:
                    return link.getTarget().getInstance();
                case INBOUND:
                default:
                    return link.getSource().getInstance();
            }
        }
        catch (Exception e)
        {
            StringBuilder msg = new StringBuilder("Faild to get instance from ");
            if (relation == OwObjectLinkRelation.OUTBOUND)
            {
                msg.append("target");
            }
            else
            {
                msg.append("source");
            }
            msg.append(" of link object");

            LOG.error(msg, e);
            throw new OwProcessingRuntimeException(msg.toString(), e);
        }
    }

    @SuppressWarnings("serial")
    protected static class OwProcessingRuntimeException extends OwRuntimeException
    {
        public OwProcessingRuntimeException(String message_p, Throwable cause_p)
        {
            super(message_p, cause_p);
        }

        @Override
        public String getModulName()
        {
            return "plug.std";
        }
    }
}
