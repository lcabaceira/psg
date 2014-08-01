package com.wewebu.ow.client.upload.swing;

import java.awt.BorderLayout;
import java.awt.Container;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.wewebu.ow.client.upload.OwMessages;

/**
 *<p>
 * Just a utility class to show some error messages.
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
 *@since 3.2.2.0
 */
public class OwErrorMessageBoxSwing
{
    public static void showError(final String message_p, final OwDNDPlatformSwing owDNDPlatformSwing_p)
    {
        SwingUtilities.invokeLater(new Runnable() {

            public void run()
            {
                String title = owDNDPlatformSwing_p.getMessages().localize(OwMessages.UPLOAD_MESSAGES_MSGERRGENERAL, "Error");

                final JOptionPane opane = new JOptionPane(message_p, JOptionPane.ERROR_MESSAGE);
                final JDialog dialog = new JDialog(OwDNDPlatformSwing.getRootFrame(), title, true);

                Container contentPane = dialog.getContentPane();
                contentPane.setLayout(new BorderLayout());
                contentPane.add(opane, BorderLayout.CENTER);

                opane.addPropertyChangeListener(new PropertyChangeListener() {

                    public void propertyChange(PropertyChangeEvent event_p)
                    {
                        if (dialog.isVisible() && event_p.getSource() == opane && (event_p.getPropertyName().equals(JOptionPane.VALUE_PROPERTY)) && event_p.getNewValue() != null && event_p.getNewValue() != JOptionPane.UNINITIALIZED_VALUE)
                        {
                            dialog.setVisible(false);
                            dialog.dispose();
                        }
                    }
                });

                dialog.setAlwaysOnTop(true);
                dialog.pack();
                dialog.setResizable(false);
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
            }
        });
    }
}
