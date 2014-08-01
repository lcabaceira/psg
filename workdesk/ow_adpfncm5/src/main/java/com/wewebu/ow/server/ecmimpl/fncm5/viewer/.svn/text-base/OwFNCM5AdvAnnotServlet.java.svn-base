package com.wewebu.ow.server.ecmimpl.fncm5.viewer;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *<p>
 * Annotation servlet for Advanced Viewing.
 * Return different response in case of an error during processing requests.
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
public class OwFNCM5AdvAnnotServlet extends OwFNCM5DaejaAnnotServlet
{
    /**Generated serial version UID*/
    private static final long serialVersionUID = -4285273391453865019L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        HttpSession session = req.getSession(false);
        if (session != null)
        {
            processRequest(req, resp);
        }
        resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    @Override
    protected void sendErrorMessage(HttpServletResponse resp, String msg, int httpStatus) throws IOException
    {
        resp.setStatus(httpStatus);
        StringBuilder msgToSend = new StringBuilder("<STATUS>FAILED<MSGTEXT>");
        String respMsg = msgToSend.append(msg).toString();
        byte[] msgResp = respMsg.getBytes("UTF-8");
        resp.setContentType("text/plain");
        resp.setContentLength(respMsg.length());
        resp.setCharacterEncoding("UTF-8");
        OutputStream out = resp.getOutputStream();
        out.write(msgResp);
        out.flush();
        out.close();
    }

}