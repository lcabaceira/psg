package com.wewebu.ow.server.ecmimpl.fncm5.nativeapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.ConfirmationCallback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextOutputCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

/**
 *<p>
 * SimpleCallbackHandler.
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
public class SimpleCallbackHandler implements CallbackHandler
{

    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException
    {
        ConfirmationCallback confirmation = null;

        for (int i = 0; i < callbacks.length; i++)
        {
            if (callbacks[i] instanceof TextOutputCallback)
            {
                TextOutputCallback tc = (TextOutputCallback) callbacks[i];

                String text;
                switch (tc.getMessageType())
                {
                    case TextOutputCallback.INFORMATION:
                        text = "";
                        break;
                    case TextOutputCallback.WARNING:
                        text = "Warning: ";
                        break;
                    case TextOutputCallback.ERROR:
                        text = "Error: ";
                        break;
                    default:
                        throw new UnsupportedCallbackException(callbacks[i], "Unrecognized message type");
                }

                String message = tc.getMessage();
                if (message != null)
                {
                    text += message;
                }
                if (text != null)
                {
                    System.err.println(text);
                }

            }
            else if (callbacks[i] instanceof NameCallback)
            {
                NameCallback nc = (NameCallback) callbacks[i];

                if (nc.getDefaultName() == null)
                {
                    System.err.print(nc.getPrompt());
                }
                else
                {
                    System.err.print(nc.getPrompt() + " [" + nc.getDefaultName() + "] ");
                }
                System.err.flush();

                String result = readLine();
                if (result.equals(""))
                {
                    result = nc.getDefaultName();
                }

                nc.setName(result);

            }
            else if (callbacks[i] instanceof PasswordCallback)
            {
                PasswordCallback pc = (PasswordCallback) callbacks[i];

                System.err.print(pc.getPrompt());
                System.err.flush();

                //                pc.setPassword(Password.readPassword(System.in));

            }
            else if (callbacks[i] instanceof ConfirmationCallback)
            {
                confirmation = (ConfirmationCallback) callbacks[i];

            }
            else
            {
                throw new UnsupportedCallbackException(callbacks[i], "Unrecognized Callback");
            }
        }

        /* Do the confirmation callback last. */
        if (confirmation != null)
        {
            doConfirmation(confirmation);
        }
    }

    /* Reads a line of input */
    private String readLine() throws IOException
    {
        return new BufferedReader(new InputStreamReader(System.in)).readLine();
    }

    private void doConfirmation(ConfirmationCallback confirmation) throws IOException, UnsupportedCallbackException
    {
        String prefix;
        int messageType = confirmation.getMessageType();
        switch (messageType)
        {
            case ConfirmationCallback.WARNING:
                prefix = "Warning: ";
                break;
            case ConfirmationCallback.ERROR:
                prefix = "Error: ";
                break;
            case ConfirmationCallback.INFORMATION:
                prefix = "";
                break;
            default:
                throw new UnsupportedCallbackException(confirmation, "Unrecognized message type: " + messageType);
        }

        class OptionInfo
        {
            String name;
            int value;

            OptionInfo(String name, int value)
            {
                this.name = name;
                this.value = value;
            }
        }

        OptionInfo[] options;
        int optionType = confirmation.getOptionType();
        switch (optionType)
        {
            case ConfirmationCallback.YES_NO_OPTION:
                options = new OptionInfo[] { new OptionInfo("Yes", ConfirmationCallback.YES), new OptionInfo("No", ConfirmationCallback.NO) };
                break;
            case ConfirmationCallback.YES_NO_CANCEL_OPTION:
                options = new OptionInfo[] { new OptionInfo("Yes", ConfirmationCallback.YES), new OptionInfo("No", ConfirmationCallback.NO), new OptionInfo("Cancel", ConfirmationCallback.CANCEL) };
                break;
            case ConfirmationCallback.OK_CANCEL_OPTION:
                options = new OptionInfo[] { new OptionInfo("OK", ConfirmationCallback.OK), new OptionInfo("Cancel", ConfirmationCallback.CANCEL) };
                break;
            case ConfirmationCallback.UNSPECIFIED_OPTION:
                String[] optionStrings = confirmation.getOptions();
                options = new OptionInfo[optionStrings.length];
                for (int i = 0; i < options.length; i++)
                {
                    options[i].value = i;
                }
                break;
            default:
                throw new UnsupportedCallbackException(confirmation, "Unrecognized option type: " + optionType);
        }

        int defaultOption = confirmation.getDefaultOption();

        String prompt = confirmation.getPrompt();
        if (prompt == null)
        {
            prompt = "";
        }
        prompt = prefix + prompt;
        if (!prompt.equals(""))
        {
            System.err.println(prompt);
        }

        for (int i = 0; i < options.length; i++)
        {
            if (optionType == ConfirmationCallback.UNSPECIFIED_OPTION)
            {
                // defaultOption is an index into the options array
                System.err.println(i + ". " + options[i].name + (i == defaultOption ? " [default]" : ""));
            }
            else
            {
                // defaultOption is an option value
                System.err.println(i + ". " + options[i].name + (options[i].value == defaultOption ? " [default]" : ""));
            }
        }
        System.err.print("Enter a number: ");
        System.err.flush();
        int result;
        try
        {
            result = Integer.parseInt(readLine());
            if (result < 0 || result > (options.length - 1))
            {
                result = defaultOption;
            }
            result = options[result].value;
        }
        catch (NumberFormatException e)
        {
            result = defaultOption;
        }

        confirmation.setSelectedIndex(result);
    }

}
