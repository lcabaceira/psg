package com.wewebu.ow.server.command;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwClipboardException;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.command.OwMultipleObjectsProcessCollector.OwObjectCollectData;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.log.OwLogCore;

/**
 *<p>
 * Execute a specific command for a collection of objects.
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
public abstract class OwCommand
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwCommand.class);
    /**checker for processability state of an object.*/
    private OwProcessableObjectStrategy m_processableObjectStrategy;
    /**the application context*/
    protected OwMainAppContext m_appContext;
    /**object collector*/
    protected OwMultipleObjectsProcessCollector m_collector;
    /**the objects on which the command is executed.*/
    protected Collection m_objects;

    /**command state: executed or not*/
    private boolean m_isExecuted;

    /**
     * Constructs a command where the given object is verified if it can be processed by this command, using <code>processableStrategy_p</code>.
     * @param object_p -  the object to be processed.
     * @param appContext_p - the application context.
     * @param processableObjectStrategy_p - checker for processability state of an object. 
     */
    public OwCommand(OwObject object_p, OwMainAppContext appContext_p, OwProcessableObjectStrategy processableObjectStrategy_p)
    {
        Collection objects = new LinkedList();
        objects.add(object_p);
        initialize(objects, appContext_p, processableObjectStrategy_p);
    }

    /**
     * Constructs a command where all given object are processable.
     * @param objects_p -  the collection of objects to be processed.
     * @param appContext_p - the application context.
     */
    public OwCommand(Collection objects_p, OwMainAppContext appContext_p)
    {
        this(objects_p, appContext_p, OwProcessableObjectStrategy.ALWAYS_PROCESSABLE_STRATEGY);
    }

    /**
     * Constructs a command where all given object are verified if they can be processed by this command, using <code>processableStrategy_p</code>.
     * @param objects_p -  the collection of objects to be processed.
     * @param appContext_p - the application context.
     * @param processableObjectStrategy_p - checker for processability state of an object. 
     */
    public OwCommand(Collection objects_p, OwMainAppContext appContext_p, OwProcessableObjectStrategy processableObjectStrategy_p)
    {
        initialize(objects_p, appContext_p, processableObjectStrategy_p);
    }

    /**
     * Initialize this command.
     * @param objects_p - collection of objects
     * @param appContext_p - application context
     * @param processableObjectStrategy_p - checker for processability state of an object. 
     */
    private void initialize(Collection objects_p, OwMainAppContext appContext_p, OwProcessableObjectStrategy processableObjectStrategy_p)
    {
        this.m_objects = objects_p;
        this.m_appContext = appContext_p;
        this.m_collector = new OwMultipleObjectsProcessCollector();
        this.m_processableObjectStrategy = processableObjectStrategy_p;
        this.m_isExecuted = false;
    }

    /**
     * Execute the command. All given objects are processed.<br>
     * In case that an object cannot be processed, depending of implementation 
     * of {@link OwProcessableObjectStrategy#canBeProcessed(OwObject)}, it is 
     * ignored (no exception is thrown) or added to the fail_to_process object list
     * (method {@link OwProcessableObjectStrategy#canBeProcessed(OwObject)} thrown an exception) . <br>
     * In case that an object is correctly processed, it is retained, 
     * and it will available for further interrogations.<br>
     * In case that an object fails to be processed, the reason of failure,
     * and the object is retained for further interrogations. 
     */
    public final void execute()
    {
        m_collector.clear();
        Iterator it = m_objects.iterator();
        while (it.hasNext())
        {
            OwObject obj = (OwObject) it.next();
            OwObjectCollectData collectData = null;
            try
            {
                collectData = m_collector.createCollectData(obj);

                if (!m_processableObjectStrategy.canBeProcessed(obj))
                {
                    m_collector.addUnprocessedObjectData(collectData);
                    continue;
                }

                processObject(obj);
                m_collector.addProcessedObjectData(collectData);
            }
            catch (Exception e)
            {
                String localizedMessage = e.getLocalizedMessage();
                if (localizedMessage == null || localizedMessage.equals(""))
                {
                    localizedMessage = e.getMessage();
                    if (localizedMessage == null || localizedMessage.equals(""))
                    {
                        localizedMessage = e.toString();
                    }
                }
                m_collector.addProcessedObjectFailureMessages(localizedMessage);
                if (e instanceof OwClipboardException)
                {
                    LOG.debug(localizedMessage, e); // Clipboard is full, is not a real error
                }
                else
                {
                    LOG.error(localizedMessage, e);
                }
                if (collectData != null)
                {
                    collectData.setFailureReason(e);
                    m_collector.addFailedObjectData(collectData);
                }
            }
        }
        m_isExecuted = true;
    }

    /**
     * Process an object. Each command must implement its specific way of processing.
     * @param object_p - the object that need to be processed.
     * @throws Exception - thrown when object fails to be processed.
     */
    protected abstract void processObject(OwObject object_p) throws Exception;

    /**
     * Check if the command execution ended with errors.<br>
     * Must be called after the {@link #execute()} method was called 
     * (otherwise a runtime exception is thrown).
     * @return -<code>true</code> in case the command execution ended with errors.
     */
    public boolean hasErrors()
    {
        checkIfIsExecuted();
        return m_collector.hasErrors();
    }

    /**
     * Get a String containing all error messages.<br>
     * Must be called after the {@link #execute()} method was called 
     * (otherwise a runtime exception is thrown).
     * @return -a <code>String</code> containing all error messages.
     */
    public String getAllErrorMessages()
    {
        checkIfIsExecuted();
        return m_collector.getAllErrorMessages();
    }

    /**
     * Check if the command successfully processed some objects.<br>
     * Must be called after the {@link #execute()} method was called 
     * (otherwise a runtime exception is thrown).
     * @return -<code>true</code> in case the command execution successfully processed some objects.
     */
    public boolean hasProcessedObjects()
    {
        checkIfIsExecuted();
        return m_collector.hasProcessedObjects();
    }

    /**
     * Get a list with successfully processed object names.<br>
     * Must be called after the {@link #execute()} method was called 
     * (otherwise a runtime exception is thrown).
     * @return -a <code>java.util.List</code> with names of successfully processed objects.
     */
    public List getProcessedObjectNames()
    {
        checkIfIsExecuted();
        return m_collector.getProcessedNames();
    }

    /**
     * Get a list with successfully processed objects.<br>
     * Must be called after the {@link #execute()} method was called 
     * (otherwise a runtime exception is thrown).
     * @return -a <code>java.util.List</code> with successfully processed {@link OwObject}s.
     */
    public List getProcessedObjects()
    {
        checkIfIsExecuted();
        return m_collector.getProcessedObjects();
    }

    /**
     * Get a set of DMSIDs of successfully processed objects.
     * Must be called after the {@link #execute()} method was called 
     * (otherwise a runtime exception is thrown).
     * @return - a <code>java.util.Set</code> with DMSIDs. 
     */
    public Set getProcessedDMSIDs()
    {
        checkIfIsExecuted();
        return m_collector.getProcessedDmsIds();
    }

    /**
     * Get a list with all names of objects that failed to be processed.
     * Must be called after the {@link #execute()} method was called 
     * (otherwise a runtime exception is thrown).
     * @return - a <code>java.util.List</code> of String (names of objects that cannot be processed.
     */
    public List getAllErrorNames()
    {
        checkIfIsExecuted();
        return m_collector.getAllErrorNames();
    }

    /**
     * Get a list with all objects that failed to be processed.
     * Must be called after the {@link #execute()} method was called 
     * (otherwise a runtime exception is thrown).
     * @return - a <code>java.util.List</code> of {@link OwObject} (objects that cannot be processed.
     */
    public List getAllErrorObjects()
    {
        checkIfIsExecuted();
        return m_collector.getAllErrorObjects();
    }

    /**
     * Verify if the current command is executed, throws a Runtime exception if not.
     */
    private void checkIfIsExecuted()
    {
        if (!m_isExecuted)
        {
            throw new RuntimeException("Command not executed! Please call method execute first!");
        }
    }

    /**
     * Get the list of objects that were unable to be processed because they were disabled.
     * @return list of disabled objects.
     */
    public List getDisabledObjects()
    {
        checkIfIsExecuted();
        return m_collector.getDisabledObjects();
    }

    /**
     * Check if this command has objects that were not processed and were disabled.
     * @return <code>true</code> if this command has disabled objects.
     */
    public boolean hasDisabledObjects()
    {
        checkIfIsExecuted();
        return m_collector.getDisabledObjects().size() > 0;
    }

    /**
     * Get the list of names of disabled objects.
     * @return list of names.
     */
    public List getDisabledObjectNames()
    {
        return m_collector.getDisabledObjectNames();
    }

    /**
     * Returns the list of {@link OwMultipleObjectsProcessCollector.OwObjectCollectData} objects that are not processed.
     * @return - a {@link List} of {@link OwMultipleObjectsProcessCollector.OwObjectCollectData} objects that are not processed.
     * @since 3.0.0.0
     */
    public List getFailedObjectsData()
    {
        checkIfIsExecuted();
        return m_collector.getFailedObjectsData();
    }
}