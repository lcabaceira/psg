package com.wewebu.ow.server.ecmimpl.fncm5.login;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextOutputCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.log4j.Logger;
import org.apache.log4j.NDC;

import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;

/**
 *<p>
 * OwFNCM5CallbackHandler.
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
public class OwFNCM5CallbackHandler implements CallbackHandler
{
    private static final Logger LOG = OwLog.getLogger(OwFNCM5CallbackHandler.class);
    private String userName;
    private String password;

    public OwFNCM5CallbackHandler(String userName, String password)
    {
        this.userName = userName;
        this.password = password;
    }

    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException
    {
        try
        {
            NDC.push(this.userName);
            if (callbacks.length > 0)
            {
                for (int i = 0; i < callbacks.length; i++)
                {
                    if (callbacks[i] instanceof NameCallback)
                    {
                        NameCallback nc = (NameCallback) callbacks[i];
                        nc.setName(this.userName);
                    }
                    else if (callbacks[i] instanceof PasswordCallback)
                    {
                        PasswordCallback pc = (PasswordCallback) callbacks[i];
                        if (this.password != null)
                        {
                            pc.setPassword(this.password.toCharArray());
                        }
                        else
                        {
                            pc.setPassword(null);
                        }
                    }
                    else if (callbacks[i] instanceof TextOutputCallback)
                    {
                        TextOutputCallback toc = (TextOutputCallback) callbacks[i];
                        switch (toc.getMessageType())
                        {
                            case TextOutputCallback.ERROR:
                                LOG.error(toc.getMessage());
                                break;
                            case TextOutputCallback.WARNING:
                                LOG.warn(toc.getMessage());
                                break;
                            case TextOutputCallback.INFORMATION:
                                LOG.info(toc.getMessage());
                                break;
                            default:
                                LOG.debug("Unkonw msg type: " + toc.getMessage());

                        }
                    }
                    else
                    {
                        LOG.warn("OwFNCM5CallBackHandler.handle: Unsupported callback " + callbacks[i]);
                    }
                }
            }
        }
        finally
        {
            NDC.pop();
        }

    }

    /**
     * Simple getter for user name
     * @return String user name
     * @since 3.2.0.2
     */
    protected String getUserName()
    {
        return this.userName;
    }

    /**
     * Simple getter for password
     * @return String password
     * @since 3.2.0.2
     */
    protected String getPassword()
    {
        return this.password;
    }
}
