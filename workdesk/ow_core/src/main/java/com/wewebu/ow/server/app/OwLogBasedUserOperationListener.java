package com.wewebu.ow.server.app;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwUserOperationEvent.OwUserOperationType;

/**
 *<p>
 * Listener for user operation.
 * Details about user operation are logged.
 * The logger can be a configured db logger. 
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
 * @since 3.1.0.3
 */
public class OwLogBasedUserOperationListener implements OwUserOperationListener
{
    private static final Logger LOG = Logger.getLogger(OwLogBasedUserOperationListener.class);

    private static final List<OwUserOperationType> TARGET_OPERATIONS = Arrays.asList(new OwUserOperationType[] { OwUserOperationType.LOGIN, OwUserOperationType.LOGOUT });

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwUserOperationListener#operationPerformed(com.wewebu.ow.server.app.OwUserOperationEvent)
     */
    public void operationPerformed(OwUserOperationEvent event_p)
    {
        if (TARGET_OPERATIONS.contains(event_p.getType()))
        {
            StringBuilder message = new StringBuilder();
            message.append(event_p.getUserInfo().getUserID());
            message.append("|");
            message.append(event_p.getType());
            message.append("|");
            message.append(event_p.getEventSource());
            message.append("|");
            message.append(event_p.getCreationTime());
            if (LOG.isInfoEnabled())
            {
                LOG.info(message);
            }
        }
    }

}
