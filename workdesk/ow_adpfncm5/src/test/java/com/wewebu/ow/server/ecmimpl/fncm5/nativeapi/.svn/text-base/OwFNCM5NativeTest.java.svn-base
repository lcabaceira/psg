package com.wewebu.ow.server.ecmimpl.fncm5.nativeapi;

import static junit.framework.Assert.fail;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextOutputCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;
import org.apache.log4j.NDC;
import org.junit.Ignore;

import com.filenet.api.collection.ClassDescriptionSet;
import com.filenet.api.collection.ContentElementList;
import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.collection.RepositoryRowSet;
import com.filenet.api.collection.StringList;
import com.filenet.api.core.Connection;
import com.filenet.api.core.ContentElement;
import com.filenet.api.core.ContentReference;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Document;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.IndependentObject;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.meta.ClassDescription;
import com.filenet.api.property.Properties;
import com.filenet.api.property.Property;
import com.filenet.api.query.RepositoryRow;
import com.filenet.api.util.UserContext;
import com.wewebu.ow.server.ecmimpl.fncm5.unittest.log.JUnitLogger;

/**
 *<p>
 * OwFNCM5NativeTest.
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
@Ignore
public abstract class OwFNCM5NativeTest
{
    private static final Logger LOG = JUnitLogger.getLogger(OwFNCM5NativeTest.class);

    protected Connection loginContextConnection(String uri_p, String jaasContext_p, URL jaasConfURL_p, final String userName_p, final String password_p) throws LoginException
    {
        String path = jaasConfURL_p.getPath();
        if (jaasConfURL_p.getPath() == null || jaasConfURL_p.getPath().length() == 0)
        {
            fail("Cannot find jaas.conf.*, url = " + jaasConfURL_p.toString());
        }
        else
        {
            LOG.info("Setting java.security.auth.login.config property to " + path);
            System.setProperty("java.security.auth.login.config", path);
        }

        // Make connection.
        Connection conn = Factory.Connection.getConnection(uri_p);
        CallbackHandler handler = new CallbackHandler() {

            public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException
            {
                if (callbacks.length > 0)
                {
                    NDC.push(userName_p);
                    for (int i = 0; i < callbacks.length; i++)
                    {
                        if (callbacks[i] instanceof NameCallback)
                        {
                            NameCallback nc = (NameCallback) callbacks[i];
                            nc.setName(userName_p);
                        }
                        else if (callbacks[i] instanceof PasswordCallback)
                        {
                            PasswordCallback pc = (PasswordCallback) callbacks[i];
                            pc.setPassword(password_p.toCharArray());
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
                            System.err.print("CH.handle: Unsupported callback " + callbacks[i]);
                            LOG.warn("CH.handle: Unsupported callback " + callbacks[i]);
                        }
                    }
                    NDC.pop();
                }

            }

        };

        // Get login context.
        LoginContext lc = null;
        lc = new LoginContext(jaasContext_p, handler);

        lc.login();
        UserContext.get().pushSubject(lc.getSubject());

        return conn;
    }

    protected Connection loginConnection(String uri_p, String jaasContext_p, final String userName_p, final String password_p) throws LoginException
    {
        LOG.info("OwFNCM5NativeTest.loginConnection :  java.security.auth.login.config = " + System.getProperty("java.security.auth.login.config"));

        Connection connection;
        connection = Factory.Connection.getConnection(uri_p);

        Subject subject = UserContext.createSubject(connection, userName_p, password_p, jaasContext_p);
        UserContext.get().pushSubject(subject);

        LOG.info("OwFNCM5NativeTest.loginConnection : logged in connection to " + uri_p + " for " + userName_p + " " + password_p);

        return connection;
    }

    protected void trace(IndependentObjectSet objects_p)
    {
        LOG.info("IndependentObjectSet " + (objects_p.isEmpty() ? "is empty." : "follows"));
        Iterator it = objects_p.iterator();
        int count = 0;
        while (it.hasNext())
        {
            count++;
            IndependentObject object = (IndependentObject) it.next();
            trace(count + "# ", object);
        }
    }

    protected void trace(String prefix_p, IndependentObject object)
    {
        LOG.info(prefix_p + " " + object);
    }

    protected void trace(RepositoryRowSet repositoryRowSet_p)
    {
        LOG.info("RepositoryRowSet " + (repositoryRowSet_p.isEmpty() ? "is empty." : "follows"));
        Iterator it = repositoryRowSet_p.iterator();
        int count = 0;
        while (it.hasNext())
        {
            count++;
            RepositoryRow row = (RepositoryRow) it.next();
            trace(count + "# ", row);
        }

    }

    protected void trace(String prefix_p, RepositoryRow repositoryRow_p)
    {
        Properties properties = repositoryRow_p.getProperties();
        Iterator pi = properties.iterator();

        while (pi.hasNext())
        {
            Property property = (Property) pi.next();
            StringBuilder trace = new StringBuilder();
            trace.append("{");
            trace.append(property.getObjectValue().getClass());
            trace.append("}");
            trace.append("=[");
            trace.append(property.getObjectValue());
            trace.append("] {");
            trace.append(property.getState());
            trace.append("}");

            LOG.info(prefix_p + "\t" + " " + trace.toString());
        }
    }

    protected void trace(ClassDescriptionSet classDescriptionSet_p)
    {
        trace(classDescriptionSet_p, false);
    }

    protected void trace(ClassDescriptionSet classDescriptionSet_p, boolean tree_p)
    {
        trace("", classDescriptionSet_p, tree_p);
    }

    protected void trace(String prefix_p, ClassDescriptionSet classDescriptionSet_p, boolean tree_p)
    {
        LOG.info(prefix_p + "ClassDescriptionSet " + (classDescriptionSet_p.isEmpty() ? "is empty." : "follows"));
        Iterator i = classDescriptionSet_p.iterator();

        while (i.hasNext())
        {

            ClassDescription cd = (ClassDescription) i.next();
            trace("\t" + prefix_p, cd, tree_p);
        }
    }

    protected void trace(String prefix_p, ClassDescription classDescription_p, boolean tree_p)
    {
        Boolean isDocument = classDescription_p.describedIsOfClass("Document");
        Boolean isFolder = classDescription_p.describedIsOfClass("Folder");
        Boolean isCustom = classDescription_p.describedIsOfClass("CustomObject");
        String classOf = " subclassOf " + (isDocument ? "Document" : (isFolder ? "Folder" : (isCustom ? "CustomObject" : "?")));
        LOG.info(prefix_p + "\tclass: " + classDescription_p.get_SymbolicName() + classOf);
        if (tree_p)
        {
            ClassDescriptionSet isc = classDescription_p.get_ImmediateSubclassDescriptions();
            if (!isc.isEmpty())
            {
                trace(prefix_p + "\t", isc, true);
            }

        }
    }

    protected void trace(Domain domain_p)
    {
        LOG.info("Domain name=" + domain_p.get_Name() + " id=" + domain_p.get_Id());
    }

    protected void trace(ObjectStore objectStore_p)
    {
        LOG.info("ObjectStore name=" + objectStore_p.get_Name() + " id=" + objectStore_p.get_Id());
    }

    protected void trace(Folder folder_p)
    {
        LOG.info("Folder " + folder_p.get_Name());
    }

    public static void traceContent(Document document_p)
    {
        LOG.info("Content of : " + document_p.get_Name());
        StringList present = document_p.get_ContentElementsPresent();
        if (present.isEmpty())
        {
            LOG.info("\t No content present !");
        }
        else
        {
            StringBuilder presentString = new StringBuilder();
            for (String contentPresent : (List<String>) present)
            {
                presentString.append(contentPresent);
                presentString.append("  ");
            }
            LOG.info("\t Present " + presentString.toString());
        }

        ContentElementList elements = document_p.get_ContentElements();
        if (elements == null)
        {
            LOG.info("\t null  elements found!");
        }
        else
        {
            LOG.info("\t " + elements.size() + " elements found!");
            for (ContentElement element : (List<ContentElement>) elements)
            {
                if (element instanceof ContentTransfer)
                {
                    ContentTransfer transfer = (ContentTransfer) element;
                    LOG.info("\t # Transfer : " + transfer.get_ContentType() + " - " + transfer.get_RetrievalName() + " - " + transfer.get_ContentSize());
                }
                else if (element instanceof ContentReference)
                {
                    ContentReference reference = (ContentReference) element;
                    LOG.info("\t # Reference : " + reference.get_ContentType() + " - " + reference.get_ContentLocation());
                }
            }
        }
    }
}
