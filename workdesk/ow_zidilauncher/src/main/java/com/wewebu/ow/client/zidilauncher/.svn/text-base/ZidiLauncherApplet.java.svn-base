package com.wewebu.ow.client.zidilauncher;

import java.applet.Applet;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import com.wewebu.ow.client.zidilauncher.winregistry.PreferencesRegistry;
import com.wewebu.ow.client.zidilauncher.winregistry.RegistryNotAvailableException;
import com.wewebu.ow.client.zidilauncher.winregistry.WindowsRegistry;

/**
 *<p>
 * ZidiLauncherApplet.
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
public class ZidiLauncherApplet extends Applet
{

    private static final long serialVersionUID = -885368417030314782L;

    private OSType osType = OSType.UNKNOWN;

    private boolean debugMode = false;

    private boolean useDDE = false;

    public static final int RETURN_SUCCESS = 0;

    public static final int RETURN_ERR_INVALID_OS = 1;

    public static final int RETURN_ERR_MALFORMED_URL = 2;

    public static final int RETURN_ERR_REGISTRY_NOT_AVAILABLE = 3;

    public static final int RETURN_ERR_UNHANDLED_FILE_TYPE = 4;

    public static final int RETURN_ERR_REGISTRY_ACCESS = 5;

    public static final int RETURN_ERR_EXECUTE_COMMAND = 6;

    public static final int RETURN_ERR_WEBDAV_MOUNT = 7;

    public static final int RETURN_ERR_WEBDAV_MOUNT_TIMEOUT = 8;

    public static final int RETURN_ERR_OSX_START_OFFICE = 9;

    private List<String> WINDOWS_PROGID_WHITELIST = new ArrayList<String>();

    private List<String> OSX_FILEEXTENSION_WHITELIST = new ArrayList<String>();//new String[] { ".doc", ".docx", ".dot", ".dotx", ".xls", ".xlsx", ".xlsm", ".ppt", ".pptx" };

    private List<String> OSX_FILEEXTENSION_APPLICATIONS = new ArrayList<String>(); //new String[] { "Microsoft Word", "Microsoft Word", "Microsoft Word", "Microsoft Word", "Microsoft Excel", "Microsoft Excel", "Microsoft Excel", "Microsoft Powerpoint", "Microsoft Powerpoint" };

    private static final String PARAM_INITIAL_URL = "initialUrl";
    /**
     * Applet parameter that is expected to provide a path from which to get the configuration file.
     * The path is relative to the applet's code-base.
     */
    public static final String CFG_PROPS = "cfg.properties";
    public static final String PARAM_ZIDI_WINDOWS_PROG_IDS = "zidi.windows.prog.ids";
    public static final String PARAM_ZIDI_OSX_FILEEXTENSION_WHITELIST = "zidi.osx.fileextension.whitelist";
    public static final String PARAM_ZIDI_OSX_FILEEXTENSION_APPLICATIONS = "zidi.osx.fileextension.applications";

    @Override
    public void init()
    {
        super.init();
        System.out.println("Initializing zidi launcher applet...");
        // detect operating system
        String os = System.getProperty("os.name").toLowerCase();
        if (os.indexOf("win") >= 0)
        {
            osType = OSType.WINDOWS;
        }
        else if (os.indexOf("mac") >= 0)
        {
            osType = OSType.OSX;
        }
        System.out.println("Detected operating system: " + osType.toString());
        // read parameters
        String debugParam = getParameter("debug");
        debugMode = (debugParam != null) && debugParam.equalsIgnoreCase("true");
        String useDDEParam = getParameter("useDDE");
        useDDE = (useDDEParam != null) && useDDEParam.equalsIgnoreCase("true");
        System.out.println("debugMode=" + debugMode);
        System.out.println("useDDE=" + useDDE);

        try
        {
            readConfiguration();
        }
        catch (ZidiLauncherAppletInitException e)
        {
            e.printStackTrace();
            return;
        }

        // read initial URL
        String initialUrl = getParameter(PARAM_INITIAL_URL);
        if (initialUrl != null)
        {
            System.out.println("initialUrl=" + initialUrl);
            String initialUrlReadOnlyParam = getParameter("initialUrlReadOnly");
            boolean initialUrlReadOnly = (initialUrlReadOnlyParam != null) && initialUrlReadOnlyParam.equalsIgnoreCase("true");
            if (initialUrlReadOnly)
            {
                viewDocument(initialUrl);
            }
            else
            {
                editDocument(initialUrl);
            }
        }
    }

    /**
     * Read up the configuration property file from server.
     * @throws ZidiLauncherAppletInitException 
     */
    private void readConfiguration() throws ZidiLauncherAppletInitException
    {
        Properties cfg = new Properties();
        try
        {
            URL cfgUrl = ZidiLauncherApplet.class.getResource(CFG_PROPS);
            System.out.println("CfgUrl=" + cfgUrl);
            if (null != cfgUrl)
            {
                System.out.println("Loading configuration from cfgUrl=" + cfgUrl);
                cfg.load(cfgUrl.openStream());

                WINDOWS_PROGID_WHITELIST.addAll(readCommaSeparatedList(cfg, PARAM_ZIDI_WINDOWS_PROG_IDS));
                OSX_FILEEXTENSION_WHITELIST.addAll(readCommaSeparatedList(cfg, PARAM_ZIDI_OSX_FILEEXTENSION_WHITELIST));
                OSX_FILEEXTENSION_APPLICATIONS.addAll(readCommaSeparatedList(cfg, PARAM_ZIDI_OSX_FILEEXTENSION_APPLICATIONS));

                if (debugMode)
                {
                    logList(WINDOWS_PROGID_WHITELIST, "WINDOWS_PROGID_WHITELIST");
                    logList(OSX_FILEEXTENSION_WHITELIST, "OSX_FILEEXTENSION_WHITELIST");
                    logList(OSX_FILEEXTENSION_APPLICATIONS, "OSX_FILEEXTENSION_APPLICATIONS");
                }
            }
        }
        catch (IOException e)
        {
            throw new ZidiLauncherAppletInitException(e);
        }
    }

    private void logList(List<String> list, String listName)
    {
        StringBuffer sb = new StringBuffer();
        for (String aStr : list)
        {
            sb.append(aStr + "|");
        }
        System.err.println(listName + ":\t" + sb.toString());
    }

    private List<String> readCommaSeparatedList(Properties propertiesFile, String listPropertyName)
    {
        List<String> list = new ArrayList<String>();
        String listParamContent = propertiesFile.getProperty(listPropertyName);
        if (null != listParamContent)
        {
            StringTokenizer tokenizer = new StringTokenizer(listParamContent, ",");
            while (tokenizer.hasMoreTokens())
            {
                String token = tokenizer.nextToken();
                token = token.trim();
                if (!token.isEmpty())
                {
                    list.add(token);
                }
            }
        }

        return list;
    }

    public int editDocument(final String url)
    {
        if (debugMode)
        {
            JOptionPane.showMessageDialog(this, "ENTER: editDocument(url=" + url + ")");
        }
        // handle URL depending on operating system
        if (osType == OSType.WINDOWS)
        {
            return AccessController.doPrivileged(new PrivilegedAction<Integer>() {
                public Integer run()
                {
                    return Integer.valueOf(handleDocumentWindows(url, false));
                }
            }).intValue();
        }
        else if (osType == OSType.OSX)
        {
            return AccessController.doPrivileged(new PrivilegedAction<Integer>() {
                public Integer run()
                {
                    return Integer.valueOf(handleDocumentOSX(url, false));
                }
            }).intValue();
        }
        else
        {
            if (debugMode)
            {
                JOptionPane.showMessageDialog(this, "EXIT: error invalid operating system.");
            }
            return RETURN_ERR_INVALID_OS;
        }
    }

    public int viewDocument(final String url)
    {
        if (debugMode)
        {
            JOptionPane.showMessageDialog(this, "ENTER: viewDocument(url=" + url + ")");
        }
        // handle URL depending on operating system
        if (osType == OSType.WINDOWS)
        {
            return AccessController.doPrivileged(new PrivilegedAction<Integer>() {
                public Integer run()
                {
                    return Integer.valueOf(handleDocumentWindows(url, true));
                }
            }).intValue();
        }
        else if (osType == OSType.OSX)
        {
            return AccessController.doPrivileged(new PrivilegedAction<Integer>() {
                public Integer run()
                {
                    return Integer.valueOf(handleDocumentOSX(url, true));
                }
            }).intValue();
        }
        else
        {
            if (debugMode)
            {
                JOptionPane.showMessageDialog(this, "EXIT: error invalid operating system.");
            }
            return RETURN_ERR_INVALID_OS;
        }
    }

    private int handleDocumentWindows(String url, boolean readOnly)
    {
        // parse URL
        URL parsedUrl;
        try
        {
            parsedUrl = new URL(url);
        }
        catch (MalformedURLException mue)
        {
            if (debugMode)
            {
                JOptionPane.showMessageDialog(this, "EXIT: malformed URL.");
            }
            return RETURN_ERR_MALFORMED_URL;
        }
        // try to get access to the registry
        WindowsRegistry reg;
        try
        {
            reg = new PreferencesRegistry();
        }
        catch (RegistryNotAvailableException rnae)
        {
            if (debugMode)
            {
                JOptionPane.showMessageDialog(this, "EXIT: error registry not available:\n" + rnae.getCause().getMessage());
            }
            return RETURN_ERR_REGISTRY_NOT_AVAILABLE;
        }
        // get the file extension
        String file;
        try
        {
            file = URLDecoder.decode(parsedUrl.getPath(), "UTF8");
        }
        catch (UnsupportedEncodingException uee)
        {
            if (debugMode)
            {
                JOptionPane.showMessageDialog(this, "EXIT: unsupported encoding UTF8.");
            }
            return RETURN_ERR_MALFORMED_URL;
        }
        int pos = file.lastIndexOf('/');
        if (pos > 0)
        {
            file = file.substring(pos + 1);
        }
        pos = file.lastIndexOf('.');
        String fileExtension = (pos >= 0) ? file.substring(pos) : null;
        // find handler for file type
        if (fileExtension == null)
        {
            if (debugMode)
            {
                JOptionPane.showMessageDialog(this, "EXIT: unhandled file type: file has no extension");
            }
            return RETURN_ERR_UNHANDLED_FILE_TYPE;
        }
        if (debugMode)
        {
            JOptionPane.showMessageDialog(this, "Found fileExtension=" + fileExtension);
        }
        String progId = null;
        try
        {
            progId = reg.read(WindowsRegistry.HKEY_CLASSES_ROOT, fileExtension, null);
            if (progId != null)
            {
                String curVer = reg.read(WindowsRegistry.HKEY_CLASSES_ROOT, progId + "\\CurVer", null);
                if (curVer != null)
                {
                    progId = curVer;
                }
            }
        }
        catch (Exception e)
        {
            if (debugMode)
            {
                JOptionPane.showMessageDialog(this, "EXIT: error accessing registry:\n" + e.getMessage());
            }
            return RETURN_ERR_REGISTRY_ACCESS;
        }
        if (progId == null)
        {
            if (debugMode)
            {
                JOptionPane.showMessageDialog(this, "EXIT: unhandled file type: No program ID registered for file extension");
            }
            return RETURN_ERR_UNHANDLED_FILE_TYPE;
        }
        if (debugMode)
        {
            JOptionPane.showMessageDialog(this, "Detected ProgID=" + progId);
        }
        boolean progIdOk = checkProgId(progId);
        if (!progIdOk)
        {
            if (debugMode)
            {
                JOptionPane.showMessageDialog(this, "EXIT: unhandled file type: The registered progId is not in the whitelist of allowed progIds");
            }
            return RETURN_ERR_UNHANDLED_FILE_TYPE;
        }
        // get shell command and ddeexec String
        String shellCommand = null;
        String shellDDEExec = null;
        try
        {
            String subKey = readOnly ? "OpenAsReadOnly" : "Open";
            shellCommand = reg.read(WindowsRegistry.HKEY_CLASSES_ROOT, progId + "\\shell\\" + subKey + "\\command", null);
            shellDDEExec = reg.read(WindowsRegistry.HKEY_CLASSES_ROOT, progId + "\\shell\\" + subKey + "\\ddeexec", null);
        }
        catch (Exception e)
        {
            if (debugMode)
            {
                JOptionPane.showMessageDialog(this, "EXIT: error accessing registry:\n" + e.getMessage());
            }
            return RETURN_ERR_REGISTRY_ACCESS;
        }
        if (shellCommand == null)
        {
            if (debugMode)
            {
                JOptionPane.showMessageDialog(this, "EXIT: no shell command found in registry for this program ID");
            }
            return RETURN_ERR_UNHANDLED_FILE_TYPE;
        }
        if (debugMode)
        {
            JOptionPane.showMessageDialog(this, "INFO: command=" + shellCommand);
            JOptionPane.showMessageDialog(this, "INFO: ddeexec=" + shellDDEExec);
        }
        // run the command
        if ((shellDDEExec == null) || (!useDDE))
        {
            String[] commandParts = parseRegistryCommandString(shellCommand);
            if (commandParts.length < 1)
            {
                if (debugMode)
                {
                    JOptionPane.showMessageDialog(this, "EXIT: shell command string found in registry is empty");
                }
                return RETURN_ERR_UNHANDLED_FILE_TYPE;
            }
            if (shellDDEExec == null)
            {
                // there is no DDE code registered for this programId. So we just need to replace the "%1" in the
                // command by the URL
                for (int i = 0; i < commandParts.length; i++)
                {
                    if (commandParts[i].indexOf("%1") >= 0)
                    {
                        commandParts[i] = commandParts[i].replace("%1", url);
                    }
                }
            }
            else
            {
                // this programId is registered to open documents by DDE, but this applet is not configured to use DDE.
                // So we try to run the program registered as DDE server without the DDE arguments and add the URL as
                // single argument. This does work in many cases and we just need to support Office applications.
                String[] newCommandParts = new String[2];
                newCommandParts[0] = commandParts[0];
                newCommandParts[1] = url;
                commandParts = newCommandParts;
            }
            try
            {
                Runtime.getRuntime().exec(commandParts);
            }
            catch (IOException e)
            {
                if (debugMode)
                {
                    JOptionPane.showMessageDialog(this, "EXIT: error executing command:\n" + e.getMessage());
                }
                return RETURN_ERR_EXECUTE_COMMAND;
            }
        }
        else
        {
            JOptionPane.showMessageDialog(this, "EXIT: Opening files by DDE is not yet supported.");
            return RETURN_ERR_EXECUTE_COMMAND;
        }
        return RETURN_SUCCESS;
    }

    /**
     * @param progId
     * @return true if progId was recognized.
     */
    private boolean checkProgId(String progId)
    {
        // check program ID against white-list
        boolean progIdOk = false;
        for (String p : WINDOWS_PROGID_WHITELIST)
        {
            if (progId.startsWith(p))
            {
                progIdOk = true;
            }
        }
        return progIdOk;
    }

    private String[] parseRegistryCommandString(String command)
    {
        ArrayList<String> commandParts = new ArrayList<String>();
        StringBuffer temp = new StringBuffer();
        boolean quotedState = false;
        for (int i = 0; i < command.length(); i++)
        {
            char c = command.charAt(i);
            if (c == '"')
            {
                quotedState = !quotedState;
            }
            else if (c == ' ')
            {
                if (quotedState)
                {
                    temp.append(c);
                }
                else
                {
                    commandParts.add(temp.toString());
                    temp.setLength(0);
                }
            }
            else
            {
                temp.append(c);
            }
        }
        if (temp.length() > 0)
        {
            commandParts.add(temp.toString());
        }
        return commandParts.toArray(new String[commandParts.size()]);
    }

    private int handleDocumentOSX(String url, boolean readOnly)
    {
        String applicationName = null;
        int dotPos = url.lastIndexOf('.');
        if (dotPos > 0)
        {
            String fileExtension = url.substring(dotPos);
            for (int i = 0; i < OSX_FILEEXTENSION_WHITELIST.size(); i++)
            {
                String testExt = OSX_FILEEXTENSION_WHITELIST.get(i);
                if (fileExtension.equalsIgnoreCase(testExt))
                {
                    applicationName = OSX_FILEEXTENSION_APPLICATIONS.get(i);
                }
            }
            if (applicationName == null)
            {
                if (debugMode)
                {
                    JOptionPane.showMessageDialog(this, "EXIT: unhandled file type: The file extension is not in the whitelist of allowed file extensions");
                }
                return RETURN_ERR_UNHANDLED_FILE_TYPE;
            }
        }
        else
        {
            if (debugMode)
            {
                JOptionPane.showMessageDialog(this, "EXIT: unhandled file type: File name has no file extsnion.");
            }
            return RETURN_ERR_UNHANDLED_FILE_TYPE;
        }
        if (debugMode)
        {
            JOptionPane.showMessageDialog(this, "Opening " + url + "\nwith " + applicationName);
        }
        try
        {
            String[] cmdMountWebdav = new String[7];
            cmdMountWebdav[0] = "osascript";
            cmdMountWebdav[1] = "-e";
            cmdMountWebdav[2] = "tell application \"" + applicationName + "\"";
            cmdMountWebdav[3] = "-e";
            cmdMountWebdav[4] = "open \"" + url + "\"";
            cmdMountWebdav[5] = "-e";
            cmdMountWebdav[6] = "end tell";
            Runtime.getRuntime().exec(cmdMountWebdav);
            /*
            BufferedReader brInputStream = new BufferedReader(new InputStreamReader(p.getInputStream()));
            try
            {
                StringBuffer message = new StringBuffer();
                String line = null;
                while ((line = brInputStream.readLine()) != null)
                {
                    if (line != null)
                    {
                        message.append(line);
                    }
                }
                int exitValueMountWebdav = p.waitFor();
                if (exitValueMountWebdav != 0)
                {
                    if (debugMode)
                    {
                        JOptionPane.showMessageDialog(this, "mount_webdav exit code " + exitValueMountWebdav + "\nConsole output:" + message.toString());
                    }
                }
            }
            finally
            {
                try
                {
                    brInputStream.close();
                }
                catch (IOException ioe)
                {
                    ; // just ignore it and make FindBugs happy with a non-empty catch clasue
                }
            }
            */
        }
        catch (Exception e)
        {
            if (debugMode)
            {
                JOptionPane.showMessageDialog(this, "EXIT: Error starting " + applicationName + " : " + e.getMessage());
            }
            return RETURN_ERR_OSX_START_OFFICE;
        }
        return RETURN_SUCCESS;
    }

    private int handleDocumentOSX_Legacy(String url, boolean readOnly)
    {
        // parse URL
        URL parsedUrl;
        try
        {
            parsedUrl = new URL(url);
        }
        catch (MalformedURLException mue)
        {
            if (debugMode)
            {
                JOptionPane.showMessageDialog(this, "EXIT: malformed URL.");
            }
            return RETURN_ERR_MALFORMED_URL;
        }
        // check file extension against whitelist
        String fileExtension = parsedUrl.getPath();
        if (fileExtension.length() >= 0)
        {
            int i = fileExtension.lastIndexOf('/');
            if (i > 0)
            {
                fileExtension = fileExtension.substring(i + 1);
            }
            i = fileExtension.lastIndexOf('.');
            if (i > 0)
            {
                fileExtension = fileExtension.substring(i);
                boolean fileExtensionOk = false;
                for (String p : OSX_FILEEXTENSION_WHITELIST)
                {
                    if (fileExtension.equalsIgnoreCase(p))
                    {
                        fileExtensionOk = true;
                    }
                }
                if (!fileExtensionOk)
                {
                    if (debugMode)
                    {
                        JOptionPane.showMessageDialog(this, "EXIT: unhandled file type: The file extension is not in the whitelist of allowed file extensions");
                    }
                    return RETURN_ERR_UNHANDLED_FILE_TYPE;
                }
            }
            else
            {
                if (debugMode)
                {
                    JOptionPane.showMessageDialog(this, "EXIT: unhandled file type: File name has no file extsnion.");
                }
                return RETURN_ERR_UNHANDLED_FILE_TYPE;
            }
        }
        else
        {
            if (debugMode)
            {
                JOptionPane.showMessageDialog(this, "EXIT: unhandled file type: There is no path at all.");
            }
            return RETURN_ERR_UNHANDLED_FILE_TYPE;
        }
        // build url and path
        String serverUrl = parsedUrl.getProtocol() + "://" + parsedUrl.getHost();
        if (parsedUrl.getPort() != -1)
        {
            serverUrl += ":" + parsedUrl.getPort();
        }
        String path;
        try
        {
            path = URLDecoder.decode(parsedUrl.getPath(), "UTF8");
        }
        catch (UnsupportedEncodingException uee)
        {
            if (debugMode)
            {
                JOptionPane.showMessageDialog(this, "EXIT: unsupported encoding UTF8.");
            }
            return RETURN_ERR_MALFORMED_URL;
        }
        String contextPath;
        String filePathInContext;
        if (path.length() > 0)
        {
            path = path.substring(1);
            int i = path.indexOf('/');
            if (i > 0)
            {
                contextPath = "/" + path.substring(0, i);
                filePathInContext = path.substring(i);
            }
            else
            {
                if (debugMode)
                {
                    JOptionPane.showMessageDialog(this, "EXIT: URL has to consist of at least the context path and the path to the file within that context.");
                }
                return RETURN_ERR_MALFORMED_URL;
            }
        }
        else
        {
            if (debugMode)
            {
                JOptionPane.showMessageDialog(this, "EXIT: URL has to consist of at least the context path and the path to the file within that context.");
            }
            return RETURN_ERR_MALFORMED_URL;
        }
        if (debugMode)
        {
            JOptionPane.showMessageDialog(this, "serverURL: " + serverUrl + "\ncontextPath: " + contextPath + "\nfilePathInContext: " + filePathInContext);
        }
        /*
        filePathInContext = ZioiDafFileNameEncoder.encodePath(filePathInContext, '/');
        if (debugMode)
        {
            JOptionPane.showMessageDialog(this, "ZIDI DAF encoded filePathInContext: " + filePathInContext);
        }
        */
        // mount the webdav share

        // 2012-04-06: removed old WebDAV mount

        /*
        try
        {
            String[] cmdCreateWebdavVolume = new String[2];
            cmdCreateWebdavVolume[0] = "mkdir";
            cmdCreateWebdavVolume[1] = "/Volumes" + contextPath;
            if (debugMode)
            {
                JOptionPane.showMessageDialog(this, "Executing: " + cmdCreateWebdavVolume[0] + " " + cmdCreateWebdavVolume[1]);
            }
            Runtime.getRuntime().exec(cmdCreateWebdavVolume);
            String[] cmdMountWebdav = new String[3];
            cmdMountWebdav[0] = "mount_webdav";
            cmdMountWebdav[1] = serverUrl + contextPath + "/";
            cmdMountWebdav[2] = "/Volumes" + contextPath + "/";
            if (debugMode)
            {
                JOptionPane.showMessageDialog(this, "Executing: " + cmdMountWebdav[0] + " " + cmdMountWebdav[1] + " " + cmdMountWebdav[2]);
            }
            Process p = Runtime.getRuntime().exec(cmdMountWebdav);
            BufferedReader brInputStream = new BufferedReader(new InputStreamReader(p.getInputStream()));
            try
            {
                StringBuffer message = new StringBuffer();
                String line = null;
                while ((line = brInputStream.readLine()) != null)
                {
                    if (line != null)
                    {
                        message.append(line);
                    }
                }
                int exitValueMountWebdav = p.waitFor();
                if (exitValueMountWebdav != 0)
                {
                    if (debugMode)
                    {
                        JOptionPane.showMessageDialog(this, "mount_webdav exit code " + exitValueMountWebdav + "\nConsole output:" + message.toString());
                    }
                }
            }
            finally
            {
                try
                {
                    brInputStream.close();
                }
                catch (IOException ioe)
                {
                    ; // just ignore it and make FindBugs happy with a non-empty catch clasue
                }
            }
        }
        catch (Exception e)
        {
            if (debugMode)
            {
                JOptionPane.showMessageDialog(this, "EXIT: Error running webdav mount command: " + e.getMessage());
            }
            return RETURN_ERR_WEBDAV_MOUNT;
        }
        // check existance of volumes directory
        File volumesDirectory = new File("/Volumes" + contextPath);
        if ((!volumesDirectory.exists()) || (!volumesDirectory.isDirectory()))
        {
            if (debugMode)
            {
                JOptionPane.showMessageDialog(this, "EXIT: Volume directory does not exist.");
            }
            return RETURN_ERR_WEBDAV_MOUNT;
        }
        */

        // 2012-04-06: new WebDAV mount

        // check if already mounted
        File mountPointFile = new File("/Volumes" + contextPath);
        if (mountPointFile.exists())
        {
            if (debugMode)
            {
                JOptionPane.showMessageDialog(this, "ZIDI WebDAV share already mounted.");
            }
        }
        else
        {
            /*
            String appleScriptMount = "try\nmount volume \"" + serverUrl + contextPath + "\"\nend try";
            if (debugMode)
            {
                JOptionPane.showMessageDialog(this, "Running AppleScript:\n"+appleScriptMount);
            }
            ScriptEngineManager seManager = new ScriptEngineManager();
            ScriptEngine engine = seManager.getEngineByName("AppleScript");
            try
            {
                engine.eval(appleScriptMount);
            }
            catch(Exception e)
            {
                if (debugMode)
                {
                    JOptionPane.showMessageDialog(this, "EXIT: Error mounting zidi volume: " + e.getMessage());
                }
                return RETURN_ERR_WEBDAV_MOUNT;
            }
            */
            try
            {
                String[] cmdMountWebdav = new String[7];
                cmdMountWebdav[0] = "osascript";
                cmdMountWebdav[1] = "-e";
                cmdMountWebdav[2] = "try";
                cmdMountWebdav[3] = "-e";
                cmdMountWebdav[4] = "mount volume \"" + serverUrl + contextPath + "\"";
                cmdMountWebdav[5] = "-e";
                cmdMountWebdav[6] = "end try";
                Process p = Runtime.getRuntime().exec(cmdMountWebdav);
                BufferedReader brInputStream = new BufferedReader(new InputStreamReader(p.getInputStream()));
                try
                {
                    StringBuffer message = new StringBuffer();
                    String line = null;
                    while ((line = brInputStream.readLine()) != null)
                    {
                        if (line != null)
                        {
                            message.append(line);
                        }
                    }
                    int exitValueMountWebdav = p.waitFor();
                    if (exitValueMountWebdav != 0)
                    {
                        if (debugMode)
                        {
                            JOptionPane.showMessageDialog(this, "mount_webdav exit code " + exitValueMountWebdav + "\nConsole output:" + message.toString());
                        }
                    }
                }
                finally
                {
                    try
                    {
                        brInputStream.close();
                    }
                    catch (IOException ioe)
                    {
                        ; // just ignore it and make FindBugs happy with a non-empty catch clasue
                    }
                }
            }
            catch (Exception e)
            {
                if (debugMode)
                {
                    JOptionPane.showMessageDialog(this, "EXIT: Error running webdav mount command: " + e.getMessage());
                }
                return RETURN_ERR_WEBDAV_MOUNT;
            }
        }

        // wait until zidi is available
        long start = System.currentTimeMillis();
        int timeout = 45 * 1000; // 45 seconds
        boolean mounted = false;
        while ((!mounted) && ((System.currentTimeMillis() - start) < timeout))
        {
            String[] children = mountPointFile.list();
            if ((children != null) && (children.length > 0))
            {
                mounted = true;
            }
            else
            {
                try
                {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e)
                {
                    // no problem. just continue with the next iteration
                }
            }
        }
        if (!mounted)
        {
            if (debugMode)
            {
                JOptionPane.showMessageDialog(this, "EXIT: Unable to mount webdav share within timeout.");
            }
            return RETURN_ERR_WEBDAV_MOUNT_TIMEOUT;
        }
        // launch Office via launcher service
        try
        {
            String cmdOpen[] = new String[2];
            cmdOpen[0] = "open";
            cmdOpen[1] = "/Volumes" + contextPath + filePathInContext;
            if (debugMode)
            {
                JOptionPane.showMessageDialog(this, "Executing: " + cmdOpen[0] + " " + cmdOpen[1]);
            }
            Runtime.getRuntime().exec(cmdOpen);
        }
        catch (IOException e)
        {
            if (debugMode)
            {
                JOptionPane.showMessageDialog(this, "EXIT: error executing command:\n" + e.getMessage());
            }
            return RETURN_ERR_EXECUTE_COMMAND;
        }
        return RETURN_SUCCESS;
    }

}
