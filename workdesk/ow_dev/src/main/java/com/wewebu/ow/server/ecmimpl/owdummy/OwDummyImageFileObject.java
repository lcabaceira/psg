package com.wewebu.ow.server.ecmimpl.owdummy;

import com.wewebu.ow.server.ecm.OwFileObject;
import com.wewebu.ow.server.ecm.OwNetwork;

/**
 *<p>
 * Implements OwFileObject for dummy Image files on the file system.
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
public class OwDummyImageFileObject extends OwFileObject
{

    /** Creates a new instance of OwDummyImageFileObject */
    public OwDummyImageFileObject(OwNetwork network_p, String strImageName_p) throws Exception
    {
        super(network_p, new java.io.File(((OwDummyNetwork) network_p).getContext().getBasePath() + ((OwDummyNetwork) network_p).getApplicationObjectBaseDir("/WEB-INF/appobj/reference/dummy") + "/adressen/" + strImageName_p));
    }

    public String getDMSID() throws Exception
    {
        // return the directory path to the object as a DMSID
        return OwDummyNetwork.DMS_PREFIX + "," + OwDummyFileObject.DMS_PREFIX + "," + m_File.getPath();
    }
}