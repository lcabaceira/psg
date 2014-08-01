<%@page import="java.net.JarURLConnection"%>
<%@page import="org.bouncycastle.cert.X509CertificateHolder"%>
<%@page autoFlush="true" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8" language="java"%>
<%@page import="java.util.Enumeration"%>
<%@page import="java.net.URL"%>
<%@page import="java.util.Properties"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.io.IOException"%>
<%@page import="java.util.jar.Manifest"%>
<%@page import="java.util.jar.Attributes"%>
<%@page import="java.util.jar.JarFile"%>
<%@page import="java.io.File"%>
<%@page import="java.security.cert.X509Certificate"%>
<%@page import="java.io.FilenameFilter"%>
<%@page import="java.util.jar.JarEntry"%>
<%@page import="javax.servlet.jsp.JspWriter"%>
<%@page import="com.wewebu.ow.server.util.jar.OwJarVerifier"%>
<%
    if (request.getMethod().equals("GET"))
    {
        response.setStatus(404);
    }
    else
    {
        response.setStatus(200);
    }
%><%!private Class loadClass(String className)
    {
        try
        {
            return (Class.forName(className));
        }
        catch (ClassNotFoundException e)
        {
            return (null);
        }
    }

    private Class checkClass(JspWriter out, String libName, String className)
    {
        StringBuffer sb = new StringBuffer();

        boolean hasBeenReported = false;
        boolean hasError = false;

        Class testClass = null;
        try
        {
            String notFoundErrorMessage = "Lib " + libName + " or similar not found in your environment!";
            sb.append("Checking for <b>" + libName + "</b><br/>");

            testClass = loadClass(className);
            if (testClass == null)
            {
                sb.append(notFoundErrorMessage);
                hasError = true;
            }
            else
            {
                String location = "an unknown location.";
                try
                {
                    java.net.URL url = testClass.getProtectionDomain().getCodeSource().getLocation();
                    location = url.toString();
                    if (location.startsWith("jar"))
                    {
                        url = ((java.net.JarURLConnection) url.openConnection()).getJarFileURL();
                        location = url.toString();
                    }
                    if (location.startsWith("file"))
                    {
                        java.io.File file = new java.io.File(url.getFile());
                        location = file.getAbsolutePath();
                    }
                }
                catch (Throwable t)
                {
                }
                sb.append("Found at ");
                sb.append(location);
                sb.append("<br/>\n");
                java.lang.Package docPackage = testClass.getPackage();
                if (docPackage == null)
                {
                    out.print("Class is not located in a package. Can not verify version.<br/>\n");
                }
                else
                {
                    sb.append("Product: " + docPackage.getImplementationTitle() + "<br/>");
                    sb.append("Vendor: " + docPackage.getImplementationVendor() + "<br/>");
                    sb.append("Version: " + docPackage.getImplementationVersion() + "<br/>");
                    sb.append("ClassLoader: " + (testClass.getClassLoader() == null ? "null" : testClass.getClassLoader().toString()) + "<br/>");
                }
            }
        }
        catch (NoClassDefFoundError e)
        {
            try
            {
                hasError = true;
                sb.append("Is present but failed loading due to a missing dependency.<br>\n");
                sb.append("Error Message: " + e.getMessage());
                sb.append("<br/>\n");
                StackTraceElement[] trace = e.getStackTrace();
                for (int i = 0; i < trace.length; i++)
                {
                    sb.append(trace[i].toString() + "<br>\n");
                }
            }
            catch (Exception ioe)
            {
            }
            hasBeenReported = true;
        }
        catch (Throwable t)
        {
            if (!hasBeenReported)
            {
                try
                {
                    hasError = true;
                    sb.append("Might be present but failed loading due to an unknown error.<br>\n");
                    sb.append("Error Message: " + t.getMessage());
                    sb.append("<br/>\n");
                    StackTraceElement[] trace = t.getStackTrace();
                    for (int i = 0; i < trace.length; i++)
                    {
                        sb.append(trace[i].toString() + "<br/>\n");
                    }
                }
                catch (Exception ioe)
                {
                }
            }
        }
        try
        {
            String cssClass = (hasError) ? "error" : "";
            String style = (hasError) ? "color:red" : "";
            out.write("<p class='" + cssClass + "' style='" + style + "'>\n");
            out.write(sb.toString());
            out.write("<p>\n");
        }
        catch (IOException ex)
        {

        }
        return (testClass);
    }

    private static final String NEEDS = "needs.";

    private void displayDetails(JspWriter out, URL ddUrl)
    {
        try
        {
            Properties properties = new Properties();
            properties.load(ddUrl.openStream());
            Set keys = properties.keySet();
            Iterator it = keys.iterator();
            while (it.hasNext())
            {
                String aKey = (String) it.next();
                if (aKey.startsWith(NEEDS))
                {
                    String aKeyValue = properties.getProperty(aKey);
                    String jarName = aKey.substring(NEEDS.length());
                    checkClass(out, jarName, aKeyValue);
                }
            }
        }
        catch (Exception e)
        {
        }
    }

    private void checkModuleDependency(JspWriter out, URL ddUrl, PageContext pageContext)
    {
        String ddExternalForm = ddUrl.toExternalForm();
        String moduleInfo = "??";
        String moduleName = ddExternalForm;

        boolean isVerifiedOK = true;
        boolean isSigned = false;
        if (ddExternalForm.startsWith("jar:file:"))
        {
            try
            {
                URL fileUrl = new URL(ddUrl.getFile());
                String[] parts = fileUrl.getFile().split("!");
                ddExternalForm = parts[0];

                File file = new File(ddExternalForm);
                moduleName = file.getName();

                System.err.println("Jar file: " + ddExternalForm);
                JarFile jarFile = new JarFile(ddExternalForm);
                OwJarVerifier verifier = new OwJarVerifier(ddUrl);

                isSigned = verifier.isSigned();
                String errorMessage = "";
                try
                {
                    X509Certificate cert = OwJarVerifier.getWeWebUCertificate();
                    verifier.verify(cert);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    isVerifiedOK = false;
                    errorMessage = e.getMessage();
                }

                Manifest manifest = jarFile.getManifest();
                if (null != manifest)
                {
                    Attributes mainAttributes = manifest.getMainAttributes();

                    String implementationVendor = mainAttributes.getValue("Implementation-Vendor");
                    String implementationTitle = mainAttributes.getValue("Implementation-Title");
                    String implementationVersion = mainAttributes.getValue("Implementation-Version");
                    String specificationVersion = mainAttributes.getValue("Specification-Version");

                    moduleInfo = "<br/>Product: " + ((null != implementationTitle) ? implementationTitle : "??") + "<br/>Version: " + ((null != implementationVersion) ? implementationVersion : "??") + "<br/>Vendor: "
                            + ((null != implementationVendor) ? implementationVendor : "") + "<br/>Is signed: " + isSigned;

                    moduleInfo += "<br/>Signature verification: " + ((isVerifiedOK) ? "OK" : "failed <br/>" + errorMessage);

                    if (isSigned && isVerifiedOK)
                    {
                        X509CertificateHolder[] certificates = verifier.getSignatureCertificates();
                        String signers = "";
                        for (int i = 0; i < certificates.length; i++)
                        {
                            signers += "<br/>&nbsp;&nbsp;" + certificates[i].getSubject();
                        }
                        moduleInfo += "<br/>Signature file certificates: " + signers;
                    }
                }
            }
            catch (Throwable th)
            {
                moduleInfo = "<br/><span style=\"color: red;\">Could not read manifest!</span>";
                pageContext.getServletContext().log("Could not read manifest!", th);
            }
        }

        try
        {
            out.write("<div class='module" + ((isSigned && !isVerifiedOK) ? ", error" : "") + "' " + ((isSigned && !isVerifiedOK) ? "style='color:red'" : "") + ">\n");
            out.write("<H4>Module: " + moduleName + "<br/>" + moduleInfo + "</H4>\n");
            out.write("Loaded from " + ddUrl.getFile() + "<br/>");
            out.write("<a href=\"javascript:toggleElementDisplay('" + moduleName + "');\">Check required libraries</a><br/>");
            out.write("<span style=\"display: none;\" id=\"" + moduleName + "\">");
            displayDetails(out, ddUrl);
            out.write("</span>");
            out.write("</div>\n");
        }
        catch (IOException ioEx)
        {
            pageContext.getServletContext().log("Could not read manifest!", ioEx);
        }
    }%>

<H4>Current class path</H4>
<%
    java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(System.getProperty("java.class.path"), java.io.File.pathSeparator);
    while (tokenizer.hasMoreTokens())
    {

        out.print(tokenizer.nextToken());
        out.print("<br>\n");
    }
%>
<p>&nbsp;</p>

<%
    String[] jars = new String[] {};
    String libURL = application.getRealPath("/WEB-INF/lib");
    if (null != libURL)
    {
        File libFolder = new File(libURL);
        if (libFolder.isDirectory())
        {
            jars = libFolder.list(new FilenameFilter() {
                public boolean accept(File dir, String name)
                {
                    return name.endsWith(".jar");
                }
            });
        }

        out.write("<script language=\"JavaScript\" type=\"text/javascript\">\n");
        out.write("function toggleElementDisplay(id)\n");
        out.write("{\n");
        out.write("   var details = document.getElementById(id);\n");
        out.write("   if ( details )\n");
        out.write("   {\n");
        out.write("       if (details.style.display == 'none')\n");
        out.write("       {\n");
        out.write("           details.style.display = '';\n");
        out.write("       }\n");
        out.write("       else\n");
        out.write("       {\n");
        out.write("           details.style.display = 'none';\n");
        out.write("       }\n");
        out.write("   }\n");
        out.write("}\n");
        out.write("</script>\n");
        if (0 != jars.length)
        {
            out.write("<table border='1' cellpadding='4' cellspacing='0' bordercolor='#000000' width='100%')>");
            for (int i = 0; i < jars.length; i++)
            {

                File aFile = new File(libFolder, jars[i]);
                JarFile jarFile = new JarFile(aFile);
                JarEntry owDependencyEntry = jarFile.getJarEntry("ow_dependency.properties");
                if (null != owDependencyEntry)
                {
                    out.write("<tr><td width='800' valign='top'>");
                    URL aDD = new URL("jar:" + aFile.toURI() + "!/ow_dependency.properties");
                    checkModuleDependency(out, aDD, pageContext);
                    out.write("</td></tr>");
                }
            }
            out.write("</table><br><br>\n");
        }
        else
        {
            Enumeration dependencyDescriptors = this.getClass().getClassLoader().getResources("ow_dependency.properties");
            out.write("<table border='1' cellpadding='4' cellspacing='0' bordercolor='#000000' width='100%')>");
            while (dependencyDescriptors.hasMoreElements())
            {
                out.write("<tr><td width='800' valign='top'>");
                URL aDD = (URL) dependencyDescriptors.nextElement();
                checkModuleDependency(out, aDD, pageContext);
                out.write("</td></tr>");
            }
            out.write("</table><br><br>\n");
        }
    } else {
        %><p>Could not read content of the /WEB-INF/lib folder. Probably this is served from an ear/war archive.</p><%
    }
    out.flush();
%>