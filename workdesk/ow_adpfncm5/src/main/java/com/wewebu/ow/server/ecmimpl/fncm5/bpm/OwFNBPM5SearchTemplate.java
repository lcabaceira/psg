package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import java.util.Collection;
import java.util.Vector;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.wewebu.ow.server.ecm.OwNetworkContext;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecmimpl.fncm5.search.OwFNCM5SearchTemplate;

/**
 *<p>
 * FileNet BPM Repository. <br/>
 * Overridden search template for Cross views. Scans additional information.
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
public class OwFNBPM5SearchTemplate extends OwFNCM5SearchTemplate
{
    /** retrieve list or container names that the items in this container can be sent to */
    private Collection m_containernames;

    public OwFNBPM5SearchTemplate(OwNetworkContext context_p, OwObject obj_p) throws Exception
    {
        super(context_p, obj_p, true);

    }

    /** (overridable) scan search template for additional custom info
     *  retrieve a list or container names that the items in this container can be sent to
     *  
     * @param searchTemplateNode_p Node
     */
    protected void scanCustomInfo(Node searchTemplateNode_p)
    {
        // === retrieve the options node
        NodeList containernames = ((org.w3c.dom.Document) searchTemplateNode_p).getElementsByTagName("BPMReassignContainerNames");
        if ((null != containernames) && (containernames.getLength() > 0))
        {
            Node containernameCollection = containernames.item(0);

            m_containernames = new Vector();

            for (Node n = containernameCollection.getFirstChild(); n != null; n = n.getNextSibling())
            {
                if (n.getNodeType() != Node.ELEMENT_NODE)
                {
                    continue;
                }

                m_containernames.add(n.getFirstChild().getNodeValue());
            }

            if (m_containernames.size() == 0)
            {
                m_containernames = null;
            }
        }
    }

    /** a list or container names that the items in this container can be sent to, or null to use default
     * 
     * @return Collection or null if nothing found
     */
    public Collection getReassignContainerNames()
    {
        return m_containernames;
    }
}
