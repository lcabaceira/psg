package com.wewebu.ow.server.ecmimpl.opencmis.cross;

import java.util.Collection;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.ecm.OwStatusContextException;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNetwork;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISAbstractDomainFolder;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISDomainFolderClass;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISDomainFolderClassImpl;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Root folder of standard CMIS repositories in a cross adapter scenario.
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
public class OwCMISCrossDomainFolder extends OwCMISAbstractDomainFolder
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwCMISCrossDomainFolder.class);

    private OwCMISCrossNetwork crossNetwork;

    public OwCMISCrossDomainFolder(OwCMISCrossNetwork crossNetwork_p) throws OwException
    {
        this(crossNetwork_p, new OwCMISDomainFolderClassImpl());
    }

    public OwCMISCrossDomainFolder(OwCMISCrossNetwork crossNetwork_p, OwCMISDomainFolderClass class_p) throws OwException
    {
        super(class_p);
        this.crossNetwork = crossNetwork_p;
    }

    public int getChildCount(int[] objectTypes_p, int context_p)
    {
        return 2;
    }

    public OwObjectCollection getChilds(int[] objectTypes_p, Collection propertyNames_p, OwSort sort_p, int maxSize_p, int versionSelection_p, OwSearchNode filterCriteria_p) throws OwException
    {
        try
        {
            for (int iType = 0; iType < objectTypes_p.length; iType++)
            {
                switch (objectTypes_p[iType])
                {
                    case OwObjectReference.OBJECT_TYPE_ALL_CONTAINER_OBJECTS:
                    case OwObjectReference.OBJECT_TYPE_FOLDER:
                    {
                        OwStandardObjectCollection ret = new OwStandardObjectCollection();
                        OwNetwork internalNetwork = crossNetwork.getInternalNetwork();
                        OwObject internalDomainFolder = internalNetwork.getObjectFromPath("/", true);

                        ret.addAll(internalDomainFolder.getChilds(objectTypes_p, propertyNames_p, sort_p, maxSize_p, versionSelection_p, filterCriteria_p));

                        OwNetwork externalNetwork = crossNetwork.getExternalNetwork();
                        OwObject externalDomainFolder = externalNetwork.getObjectFromPath("/", true);
                        String defaultExternalResourceID = externalNetwork.getResource(null).getID();
                        ret.add(OwCMISCrossInvocationHandler.createCrossNetworkObject(crossNetwork, crossNetwork.getXMapping(), externalDomainFolder, defaultExternalResourceID));

                        return ret;
                    }
                }
            }
            LOG.info("OwCMISCrossDomainFolder.getChilds(...) returns null...");
            return null;
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwServerException("Could not retrieve cross domain folder children.", e);
        }

    }

    public boolean hasChilds(int[] objectTypes_p, int context_p) throws OwStatusContextException
    {
        //omits child count display through exception handling
        throw new OwStatusContextException("");
        //TODO: implement child count
    }

    public String getMIMEParameter()
    {
        //TODO : should this be object-class responsibility ?
        return "";
    }

    public String getMIMEType()
    {
        //TODO : should this be object-class responsibility ?
        return "ow_root/cmis_domain_cross";
    }

    public String getName()
    {
        OwCMISNetwork internalNetwork = crossNetwork.getInternalNetwork();
        return OwString.localize(internalNetwork.getLocale(), "cmis.OwCMISCrossDomainFolder.name", "CMIS Cross Network");
    }

    @Override
    public Object getNativeObject() throws OwException
    {
        return crossNetwork;
    }

}
