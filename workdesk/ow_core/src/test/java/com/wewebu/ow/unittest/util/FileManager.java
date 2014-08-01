package com.wewebu.ow.unittest.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

/**
 *<p>
 * FileManager.
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
public class FileManager
{

    private static FileManager instance = null;

    private static Logger logger = Logger.getLogger("FileManager");

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private FileManager()
    {

    }

    public static FileManager getInstance()
    {

        if (instance == null)
        {
            instance = new FileManager();
        }
        return instance;
    }

    /**
     * Create Folder
     * 
     * @param folderName_p
     * @return true/false
     */
    public boolean createFolder(String folderName_p)
    {
        File file = new File(folderName_p);
        return file.mkdirs();
    }

    /**
     * Check if a file exists
     * 
     * @param fileName_p
     * @return true/false
     */
    public boolean isFileExists(String fileName_p)
    {
        File f = new File(fileName_p);
        boolean flag = (f.exists() && f.isFile() && f.canRead());
        return flag;
    }

    /**
     * Delete File
     * 
     * @param fileName_p
     * @return boolean
     */
    public boolean deleteFile(String fileName_p)
    {
        File file = new File(fileName_p);
        return file.delete();
    }

    /**
     * Reads a file and returns the content as String
     * 
     * @param fileName_p
     * @return File Content a String
     * @throws FileNotFoundException
     */
    public String getFileContentUTF8Encoded(String fileName_p) throws FileNotFoundException
    {
        String content = null;
        FileInputStream fileInputStream = null;
        try
        {
            fileInputStream = new FileInputStream(fileName_p);
            content = readFileUTF8encoded(fileName_p, fileInputStream);
        }
        finally
        {
            if (fileInputStream != null)
            {
                try
                {
                    fileInputStream.close();
                    fileInputStream = null;
                }
                catch (IOException ignore)
                {
                }
            }
        }
        return content;
    }

    /**
     * Read a file from InputStream, the fileName ist only required for Logging.
     * 
     * @param fileName_p
     *            Only required for logging
     * @param fis_p
     *            InputStream of file
     * @return String
     */
    public String readFile(String fileName_p, InputStream fis_p)
    {
        StringWriter stringWriter = new StringWriter();
        String content = "";
        int byteRead = 0;
        try
        {
            while ((byteRead = fis_p.read()) != -1)
            {
                stringWriter.write(byteRead);
            }
            stringWriter.flush();
            content = stringWriter.toString();
        }
        catch (NullPointerException ne)
        {
            System.out.println(">>> NullPointerException: FileManager.readFile():" + fileName_p);
            logger.error(">>> NullPointerException: FileManager.readFile():" + fileName_p, ne);
        }
        catch (IOException e)
        {
            System.out.println(">>> IOException: FileManager.readFile():" + fileName_p);
            logger.error(">>> IOException: FileManager.readFile():" + fileName_p, e);
        }
        finally
        {
            if (stringWriter != null)
            {
                try
                {
                    stringWriter.close();
                    stringWriter = null;
                }
                catch (IOException ignore)
                {
                }
            }
        }
        return content;
    }

    /**
     * Read an UTF8 Encoded File
     * 
     * @param fileName_p
     * @param fis_p
     * @return String
     */
    public static String readFileUTF8encoded(String fileName_p, InputStream fis_p)
    {
        StringWriter stringWriter = new StringWriter();
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        String content = "";
        try
        {
            inputStreamReader = new InputStreamReader(fis_p, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            int byteRead = 0;
            try
            {
                while ((byteRead = reader.read()) != -1)
                {
                    stringWriter.write(byteRead);
                }
                stringWriter.flush();
                content = stringWriter.toString();
            }
            catch (IOException e)
            {
                System.out.println(">>> IOException: FileManager.readFileUTF8encoded():" + fileName_p);
                logger.error(">>> IOException: FileManager.readFileUTF8encoded():" + fileName_p, e);
            }
            finally
            {
                if (stringWriter != null)
                {
                    try
                    {
                        stringWriter.close();
                        stringWriter = null;
                    }
                    catch (IOException ignore)
                    {
                    }
                }
                if (reader != null)
                {
                    try
                    {
                        reader.close();
                        reader = null;
                    }
                    catch (IOException ignore)
                    {
                    }
                }
                if (inputStreamReader != null)
                {
                    try
                    {
                        inputStreamReader.close();
                        inputStreamReader = null;
                    }
                    catch (IOException ignore)
                    {
                    }
                }
            }
        }
        catch (UnsupportedEncodingException e)
        {
            System.out.println(">>> UnsupportedEncodingException: FileManager.readFileUTF8encoded():" + fileName_p);
            logger.error("UnsupportedEncodingException: FileManager.readFileUTF8encoded():" + fileName_p, e);
        }
        return content;
    }

    /**
     * Save File
     * 
     * @param fileName_p
     * @param content_p
     */
    public void writeFile(String fileName_p, String content_p)
    {
        FileOutputStream fout = null; // declare a file output object
        PrintStream p = null; // declare a print stream object
        try
        {
            fout = new FileOutputStream(fileName_p);
            p = new PrintStream(fout);
            p.println(content_p);
        }
        catch (Exception e)
        {
            System.err.println("Error writing to file:" + fileName_p);
            logger.error("Error writing to file" + fileName_p, e);
        }
        finally
        {
            if (p != null)
            {
                p.flush();
                p.close();
                p = null;
            }
            if (fout != null)
            {
                try
                {
                    fout.flush();
                    fout.close();
                    fout = null;
                }
                catch (IOException ignore)
                {
                }
            }
        }
    }

    /**
     * Save file as UTF-8 Format
     * 
     * @param fileName_p
     * @param content_p
     */
    public void writeFileUTF8(String fileName_p, String content_p)
    {
        // System.out.println("file.encoding:"+System.getProperty("file.encoding"));
        // System.setProperty("file.encoding","ISO8859-1");
        FileOutputStream fout = null; // declare a file output object
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bufferedWriter = null;
        try
        {
            fout = new FileOutputStream(fileName_p);
            outputStreamWriter = new OutputStreamWriter(fout, "UTF-8");
            bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write(content_p);
            bufferedWriter.flush();
        }
        catch (UnsupportedEncodingException e)
        {
            System.err.println("Error writing to file (UnsupportedEncodingException):" + fileName_p);
            logger.error("Error writing to file (UnsupportedEncodingException)" + fileName_p, e);
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Error writing to file (FileNotFoundException):" + fileName_p);
            logger.error("Error writing to file" + fileName_p, e);
        }
        catch (IOException e)
        {
            System.err.println("Error writing to file (FileNotFoundException):" + fileName_p);
            logger.error("Error writing to file" + fileName_p, e);
        }
        finally
        {
            if (bufferedWriter != null)
            {
                try
                {
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    bufferedWriter = null;
                }
                catch (IOException ignore)
                {
                }
            }
            if (outputStreamWriter != null)
            {
                try
                {
                    outputStreamWriter.flush();
                    outputStreamWriter.close();
                    outputStreamWriter = null;
                }
                catch (IOException ignore)
                {
                }
            }
            if (fout != null)
            {
                try
                {
                    fout.flush();
                    fout.close();
                    fout = null;
                }
                catch (IOException ignore)
                {
                }
            }
        }
    }

    /**
     * Get String from an Input Stream
     * @param fileName_p 
     * @param encoding_p -
     *            encoding == null (default System encoding), encoding == UTF-8
     * @return String
     * @throws IOException
     */
    public String getStringFromInputStream(String fileName_p, String encoding_p) throws IOException
    {
        String line = null;
        InputStream fis = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader fin = null;
        StringBuffer readText = new StringBuffer("");
        try
        {
            fis = FileManager.class.getClassLoader().getResourceAsStream(fileName_p);
            if (encoding_p.equals("UTF-8"))
            {
                inputStreamReader = new InputStreamReader(fis, "UTF-8");
            }
            else
            {
                inputStreamReader = new InputStreamReader(fis);
            }
            fin = new BufferedReader(inputStreamReader);
            while ((line = fin.readLine()) != null)
            {
                readText.append(line + LINE_SEPARATOR);
            }
        }
        catch (Exception e)
        {
            System.err.println("Error at loading/reading document: " + fileName_p);
            logger.error("Error at loading/reading document: " + fileName_p, e);
        }
        finally
        {
            if (fin != null)
            {
                fin.close();
                fin = null;
            }
            if (inputStreamReader != null)
            {
                inputStreamReader.close();
                inputStreamReader = null;
            }
            if (fis != null)
            {
                fis.close();
                fis = null;
            }
        }
        return readText.toString();
    }
}
