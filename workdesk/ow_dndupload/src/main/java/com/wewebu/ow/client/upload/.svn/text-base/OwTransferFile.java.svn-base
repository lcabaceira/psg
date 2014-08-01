package com.wewebu.ow.client.upload;

import java.io.File;

/**
 *<p>
 * Just a wrapper over a {@link File} transfered through a D&D operation. to add support for storing the root path.
 * The root path is considered to be the path from where the user has started the D&D action.
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
public class OwTransferFile
{

    public File file;
    public File rootPath;

    /**
     * @param file_p the wrapped {@link File}.
     * @param rootPath_p the path considered the root path for this transfer item.
     */
    public OwTransferFile(File file_p, File rootPath_p)
    {
        this.file = file_p;
        this.rootPath = rootPath_p;
    }

    public File getFile()
    {
        return file;
    }

    public File getRootPath()
    {
        return rootPath;
    }
}
