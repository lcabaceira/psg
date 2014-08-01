package com.wewebu.ow.client.upload.js;

import java.io.File;

import com.wewebu.ow.client.upload.OwTransferFile;

/**
 *<p>
 * Java implementation of a JS File object.
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
 *@since 3.2.0.0
 */
public class OwJSTransferFile extends OwTransferFile implements OwJSObject
{
    public String name;
    public long size;

    /**
     * 
     * @param file_p Mjust be a regular file not a folder
     */
    public OwJSTransferFile(File file_p, File rootPath_p)
    {
        super(file_p, rootPath_p);
        if (!file_p.isFile())
        {
            throw new IllegalArgumentException("Folders are not accepted, only regular files.");
        }
        this.name = file_p.getName();
        this.size = file_p.length();
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.client.dnd.OwJSObject#asJavaScript()
     */
    public String asJavaScript()
    {
        //        Gson gson = new Gson();
        //        return gson.toJson(this);
        throw new RuntimeException("Do not call this method ...");
    }

    public String getName()
    {
        return name;
    }

    public long getSize()
    {
        return size;
    }
}
