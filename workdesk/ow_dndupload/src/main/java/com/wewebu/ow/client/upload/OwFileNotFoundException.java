package com.wewebu.ow.client.upload;

import java.io.File;
import java.io.FileNotFoundException;

/**
 *<p>
 * Just a helper to carry the name of the not found file.
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
@SuppressWarnings("serial")
public class OwFileNotFoundException extends FileNotFoundException
{
    private File file;

    public OwFileNotFoundException(File file_p)
    {
        this.file = file_p;
    }

    public File getFile()
    {
        return file;
    }
}
