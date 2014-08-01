package com.wewebu.ow.server.plug.owdemo.owfax;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;

import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Demo send Fax function to simulate a fax device.
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
public class OwFaxSendDevice
{
    /** called when user clicked the send fax button
     * 
     */
    public static void onSendFax(String strFaxNumber_p, String strText_p, Collection objects_p) throws Exception
    {
        // check values
        if ((strFaxNumber_p == null) || (strFaxNumber_p.length() == 0))
        {
            // number is wrong formatted or empty
            throw new OwInvalidOperationException(new OwString("plug.owfax.OwSendFaxSendDevice.missingnumber", "Please insert a fax number."));
        }

        // send
        FileWriter faxBatch = new FileWriter("c:/owfax.txt");

        faxBatch.write("\r\n==========================================================\r\n");
        faxBatch.write("FAX SENDEBERICHT an Faxnummer: " + strFaxNumber_p);
        faxBatch.write("\r\n==========================================================\r\n");
        faxBatch.write("\r\nFREITEXT:");
        faxBatch.write("\r\n==========================================================\r\n");

        faxBatch.write(strText_p);

        faxBatch.write("\r\n");

        Iterator it = objects_p.iterator();
        while (it.hasNext())
        {
            OwObject obj = (OwObject) it.next();

            faxBatch.write("\r\n==========================================================\r\n");

            faxBatch.write("ANLAGE: " + obj.getName() + "\r\n");

            writeContent(obj, faxBatch);
        }

        faxBatch.flush();
        faxBatch.close();
    }

    /** get the content of a document as string 
     * @param obj_p OwObject to write the content of
     * @param w_p Writer object to write the content to
     */
    private static void writeContent(OwObject obj_p, Writer w_p) throws Exception
    {
        InputStreamReader in = null;
        try
        {
            in = new InputStreamReader(obj_p.getContentCollection().getContentElement(OwContentCollection.CONTENT_TYPE_DOCUMENT, 1).getContentStream(null));

            char[] cBuf = new char[10000];
            int iRead = 0;
            do
            {
                iRead = in.read(cBuf);
                if (iRead != -1)
                {
                    w_p.write(cBuf, 0, iRead);
                }
            } while (iRead != -1);
        }
        finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (IOException ex)
                {
                }
                in = null;
            }
        }
    }
}