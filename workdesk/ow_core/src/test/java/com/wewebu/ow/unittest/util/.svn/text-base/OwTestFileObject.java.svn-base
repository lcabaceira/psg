package com.wewebu.ow.unittest.util;

import java.io.File;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwFileObject;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.unittest.util.log.OwLog;

/**
 *<p>
 * OwTestFileObject.
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
public class OwTestFileObject extends OwFileObject
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwTestFileObject.class);

    private String m_dmsid;

    public OwTestFileObject(OwNetwork network_p, File file_p) throws Exception
    {
        super(network_p, file_p);
    }

    /** construct File Object
     * @param locale_p 
     * @param eventmanager_p 
     * @param file_p reference to the file, the object is working on
     * @param dmsID_p String DMSID 
     * @throws Exception 
     */
    public OwTestFileObject(Locale locale_p, OwEventManager eventmanager_p, java.io.File file_p, String dmsID_p) throws Exception
    {
        super();

        m_dmsid = dmsID_p;

        m_eventmanager = eventmanager_p;
        m_locale = locale_p;

        // store reference to the file
        m_File = file_p;

        //check if file exists
        if (!m_File.exists())
        {
            LOG.debug("The file doesn't exist:" + m_File.getAbsolutePath());
        }

        // load the properties
        loadProperties();
    }

    /** get the ECM specific ID of the Object. 
     *  The DMSID is not interpreted by the Workdesk, nor does the Workdesk need to know the syntax.
     *  However, it must hold enough information, so that the ECM Adapter is able to reconstruct the Object.
     *  The reconstruction is done through OwNetwork.createObjectFromDMSID(...)
     *  The Workdesk uses the DMSID to store ObjectReferences as Strings. E.g.: in the task databases.
     *
     *  The syntax of the ID is up to the ECM Adapter,
     *  but would usually be made up like the following:
     *
     */
    public String getDMSID() throws Exception
    {
        return m_dmsid;
    }

    /** overridable factory method
     * 
     * @param file_p
     * @return OwFileObject
     * @throws Exception 
     */
    protected OwFileObject createFileObject(File file_p) throws Exception
    {
        return new OwTestFileObject(m_locale, m_eventmanager, file_p, m_dmsid + "/" + getName());
    }
}
