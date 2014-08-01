package com.wewebu.ow.server.util.upload;

import java.util.Iterator;
import java.util.List;

import com.wewebu.ow.server.util.OwMimeTypes;

/**
 *<p>
 * Helper class for upload.
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
public class UploadHelper
{
    /**Default MIME type representing "application/octet-stream"
     * @since 4.2.0.0 */
    public static final String DEFAULT_MIME = "application/octet-stream";

    /**
     * determines the document format and returns it as a property object value
     * 
     * @param fileList_p
     * @return Object the document format
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public static Object getDocumentFormatProperty(List fileList_p) throws Exception
    {
        StringBuffer fileName = null;
        String tempType = null;
        boolean mimeTypeFlag = false;
        String mimeType = null;

        String mimeTypeList[] = new String[fileList_p.size()];
        Iterator it = fileList_p.iterator();
        int i = 0;
        while (it.hasNext())
        {
            String tempFileName = (String) it.next();
            int j = tempFileName.lastIndexOf(".");
            mimeTypeList[i] = tempFileName.substring(j + 1);

            if (mimeTypeList[i].equalsIgnoreCase("jpg") || mimeTypeList[i].equalsIgnoreCase("jpeg"))
            {
                mimeTypeList[i] = "jpeg";
            }
            if (mimeTypeList[i].equalsIgnoreCase("tif") || mimeTypeList[i].equalsIgnoreCase("tiff"))
            {
                mimeTypeList[i] = "tiff";
            }
            if (fileName != null)
            {
                fileName.append(",");
            }
            else
            {
                fileName = new StringBuffer();
            }
            fileName.append(tempFileName);
            i++;
        }// end of arrayFileNames for loop

        // check if all files have the same MIME types.
        if (mimeTypeList != null && mimeTypeList.length > 0)
        {
            tempType = mimeTypeList[0];
            for (int k = 0; k < mimeTypeList.length; k++)
            {
                if (mimeTypeList[k].equalsIgnoreCase(tempType))
                {
                    mimeTypeFlag = true;
                }
                else
                {
                    mimeTypeFlag = false;
                    break;
                }
            }
        }

        String propMimeType = null;
        if (mimeTypeFlag)
        {
            mimeType = tempType;
            propMimeType = OwMimeTypes.getMimeTypeFromExtension(mimeType);
        }

        if (propMimeType != null)
        {
            mimeType = propMimeType;
        }
        else
        {
            mimeType = DEFAULT_MIME;
        }

        if (fileName == null)
        {
            return mimeType;
        }
        else
        {
            return mimeType + ";name=" + fileName.toString();
        }
    }

    /**
     * Create a MIME parameter information, using following format:
     * <pre> &lt;MIME-Type&gt; + ";name=" + &lt;filename&gt; </pre>
     * If filename does not has MIME information (investigating extension)
     * a default MIME type is returned <i>application/octet-stream</i>, else the MIME type
     * is resolved through {@link OwMimeTypes#getMimeTypeFromExtension(String)}.<br />
     * In case that provided String is null the {@link #DEFAULT_MIME} is returned only.
     * @param filename String
     * @return String
     * @see OwMimeTypes
     * @since 4.2.0.0
     */
    public static String getMimeFormatProperty(String filename)
    {
        String mimeType = null;
        if (filename != null)
        {
            int dot = filename.lastIndexOf(".");
            if (dot > 0 && dot + 1 < filename.length())
            {
                mimeType = filename.substring(dot + 1);

                if (mimeType.equalsIgnoreCase("jpg") || mimeType.equalsIgnoreCase("jpeg"))
                {
                    mimeType = "jpeg";
                }
                if (mimeType.equalsIgnoreCase("tif") || mimeType.equalsIgnoreCase("tiff"))
                {
                    mimeType = "tiff";
                }
            }
        }

        String propMimeType = null;
        if (mimeType != null)
        {
            propMimeType = OwMimeTypes.getMimeTypeFromExtension(mimeType);
        }

        if (propMimeType != null)
        {
            mimeType = propMimeType;
        }
        else
        {
            mimeType = DEFAULT_MIME;
        }

        if (filename == null)
        {
            return mimeType;
        }
        else
        {
            return mimeType + ";name=" + filename;
        }
    }

}