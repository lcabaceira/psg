package com.wewebu.ow.server.ui.viewer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Abstract OwAnnotationProvider.
 * Implements the default handling for annotation information requests,
 * and creating corresponding response.
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
public abstract class OwAbstractAnnotationInfoProvider<T> implements OwAnnotationInfoProvider
{
    public void handleRequest(OwInfoRequest req, OutputStream answer) throws OwException, IOException
    {
        StringBuilder msg = new StringBuilder();
        List<T> lst = getAnnotations(req.getParameter(OwInfoProvider.PARAM_DMSID));
        if (!lst.isEmpty())
        {
            String stag = "<", etag = "</", ctag = ">";
            msg.append(stag).append(OwAnnotationInfoProvider.PERMISSIONS).append(ctag);

            for (T annotation : lst)
            {
                msg.append(stag).append(OwAnnotationInfoProvider.PERMISSION).append(ctag);

                msg.append(stag).append(OwAnnotationInfoProvider.ID).append(ctag);
                msg.append(getId(annotation));
                msg.append(etag).append(OwAnnotationInfoProvider.ID).append(ctag);

                for (OwAnnotInfoEnum annoEnum : OwAnnotInfoEnum.values())
                {
                    msg.append(stag).append(annoEnum.getType()).append(ctag);
                    msg.append(getAnnotationInfo(annotation, annoEnum.getType()));
                    msg.append(etag).append(annoEnum.getType()).append(ctag);
                }

                msg.append(etag).append(OwAnnotationInfoProvider.PERMISSION).append(ctag);
            }

            msg.append(etag).append(OwAnnotationInfoProvider.PERMISSIONS).append(ctag);
            sendAnswer(answer, msg.toString());
        }
    }

    /**(overridable)
     * Called only if the object has annotation, and the process of
     * the message was correct.
     * @param answerStream OutputStream to send the answer
     * @param message String message to be send
     * @throws IOException If could not send data through output stream
     * @throws UnsupportedEncodingException if message could not be converted into UTF-8 representation
     */
    protected void sendAnswer(OutputStream answerStream, String message) throws UnsupportedEncodingException, IOException
    {
        answerStream.write(message.toString().getBytes("UTF-8"));
    }

    /**
     * Return the Id of provided Annotation object, or by default 
     * (see also {@link OwAnnotResultsEnum#DEFAULT_ID}) can be used.
     * @param annotObj T the current object representing the annotation
     * @return String representing the Id 
     */
    protected abstract String getId(T annotObj);

    /**
     * Get a list of Annotation object for current DMSID,
     * this id represents the specific content object of the ECM system.
     * @param objDMSID String DMSID
     * @return List of annotations, or empty list
     */
    protected abstract List<T> getAnnotations(String objDMSID);

    /**
     * Request the information of a specific type.
     * @param annotation T the annotation object for which information is requested
     * @param annotInfoType String representing the information type (see also {@link OwAnnotInfoEnum})
     * @return String representing the result (see also {@link OwAnnotResultsEnum}
     */
    protected abstract String getAnnotationInfo(T annotation, String annotInfoType);

}
