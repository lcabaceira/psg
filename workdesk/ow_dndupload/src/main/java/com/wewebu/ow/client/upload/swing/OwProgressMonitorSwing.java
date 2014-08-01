package com.wewebu.ow.client.upload.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.wewebu.ow.client.upload.OwMessages;
import com.wewebu.ow.client.upload.OwProgressMonitor;

/**
 *<p>
 * OwProgressMonitorSwing.
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
@SuppressWarnings("serial")
public class OwProgressMonitorSwing extends JDialog implements OwProgressMonitor
{
    private static final Logger LOGGER = Logger.getLogger(OwProgressMonitorSwing.class.getName());

    private static final String NO_PROGRESS = "0%";

    private JLabel lblFile;
    private JLabel lblOverall;
    private JProgressBar fileProgressBar;
    private JProgressBar overallProgressBar;
    private JButton btnClose;
    private JButton btnCancel;

    //Time and progress estimates
    private int uploadedFiles = 0;
    private int fileCount = 0;

    private long fileSizes[];
    //The total size to be uploaded
    private long totalSize = 0;
    //The total size of uploaded files including the currently uploading file
    private long totalSizeUploaded = 0;
    //The cumulative size of fully uploaded files
    private long fullyUploadedFilesSize = 0;

    private long startTime; //measured in ms
    /**
     * in milliseconds since {@link #startTime}.
     */
    private long elapsedDeltaTime;
    /**
     * in milliseconds since {@link #startTime}.
     */
    private long estimatedTotalDeltaTime;

    private boolean canceled = false;
    private boolean hasErrors = false;
    private JLabel lblError;
    private Runnable onSuccess;
    private OwDNDPlatformSwing platform;

    public OwProgressMonitorSwing(Runnable onSuccess_p, OwDNDPlatformSwing owDNDPlatformSwing_p)
    {
        super(OwDNDPlatformSwing.getRootFrame());

        this.platform = owDNDPlatformSwing_p;
        this.onSuccess = onSuccess_p;

        this.setModal(true);

        String title = this.platform.getMessages().localize(OwMessages.UPLOAD_MESSAGES_TLTUPLOAD, "Upload");
        //        String pBarOverallStr = this.platform.getMessages().localize(OwMessages.UPLOAD_MESSAGES_LBLOVERALL, "Overall");
        String lblTimeEstimatesStr = this.platform.getMessages().localize(OwMessages.UPLOAD_MESSAGES_LBLTIMEESTIMATES, "Elapsed time / Remaining time: {0} / {1}", "..", "..");
        //        String pBarFileStr = this.platform.getMessages().localize(OwMessages.UPLOAD_MESSAGES_LBLUPLOADING, "Uploading");

        this.setTitle(title);
        this.getContentPane().setLayout(new BorderLayout());

        GridBagLayout centerLayout = new GridBagLayout();
        JPanel centerPanel = new JPanel(centerLayout);
        centerPanel.setPreferredSize(new Dimension(400, 200));
        centerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.gray));

        lblFile = new JLabel("--");

        fileProgressBar = new JProgressBar(0, 100);
        fileProgressBar.setValue(0);
        fileProgressBar.setStringPainted(true);
        fileProgressBar.setString(NO_PROGRESS);

        lblOverall = new JLabel(lblTimeEstimatesStr);

        overallProgressBar = new JProgressBar(0, 100);
        overallProgressBar.setValue(0);
        overallProgressBar.setStringPainted(true);
        overallProgressBar.setString(NO_PROGRESS);

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx = 1.0;
        c.weighty = 0.0;

        c.insets = new Insets(10, 10, 2, 10);
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 0;
        centerPanel.add(lblFile, c);

        c.insets = new Insets(2, 10, 2, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy++;
        centerPanel.add(fileProgressBar, c);

        c.insets = new Insets(10, 10, 2, 10);
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy++;
        centerPanel.add(lblOverall, c);

        c.insets = new Insets(2, 10, 2, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy++;
        centerPanel.add(overallProgressBar, c);

        c.weighty = 1.0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy++;
        lblError = new JLabel("Error");
        lblError.setForeground(Color.red);
        centerPanel.add(lblError, c);

        this.getContentPane().add(centerPanel, BorderLayout.CENTER);

        JPanel iconPanel = new JPanel(new GridBagLayout());
        iconPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray));

        URL uploadIconUrl = OwProgressMonitorSwing.class.getResource("add_documents.png");
        ImageIcon uploadIcon = new ImageIcon(uploadIconUrl);
        JLabel iconLabel = new JLabel(uploadIcon, SwingConstants.CENTER);

        c = new GridBagConstraints();
        c.insets = new Insets(20, 20, 20, 20);
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.weightx = 1.0;
        c.weighty = 1.0;

        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 0;
        iconPanel.add(iconLabel, c);

        this.getContentPane().add(iconPanel, BorderLayout.EAST);

        String btnCloseLabel = this.platform.getMessages().localize(OwMessages.UPLOAD_MESSAGES_BTNCLOSE, "Close");
        btnClose = new JButton(btnCloseLabel);

        String btnCancelLabel = this.platform.getMessages().localize(OwMessages.UPLOAD_MESSAGES_BTNCANCEL, "Cancel");
        btnCancel = new JButton(btnCancelLabel);

        JPanel controlsPanel = new JPanel(new GridBagLayout());

        c = new GridBagConstraints();
        c.insets = new Insets(4, 10, 4, 10);
        c.anchor = GridBagConstraints.FIRST_LINE_START;

        //Empty left space
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.3;
        c.weighty = 0;
        controlsPanel.add(new JLabel(), c);

        c.fill = GridBagConstraints.NONE;
        c.gridx++;
        c.gridy = 0;
        c.weightx = 0.2;
        c.weighty = 0;
        controlsPanel.add(btnClose, c);

        c.fill = GridBagConstraints.NONE;
        c.gridx++;
        c.gridy = 0;
        c.weightx = 0.2;
        c.weighty = 0;
        controlsPanel.add(btnCancel, c);

        //Empty right space
        c.fill = GridBagConstraints.NONE;
        c.gridx++;
        c.gridy = 0;
        c.weightx = 0.3;
        c.weighty = 0;
        controlsPanel.add(new JLabel(), c);

        this.getContentPane().add(controlsPanel, BorderLayout.SOUTH);

        this.btnCancel.setEnabled(true);
        this.btnClose.setEnabled(false);

        this.btnCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e_p)
            {
                OwProgressMonitorSwing.this.canceled = true;
                OwProgressMonitorSwing.this.btnCancel.setEnabled(false);
            }
        });
        this.btnClose.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e_p)
            {
                OwProgressMonitorSwing.this.close();
            }
        });

        this.pack();
        this.setLocationRelativeTo(null);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.client.upload.js.OwProgressMonitor#startingNextUpload(java.lang.String)
     */
    public void startingNextFileUpload(String fileName_p)
    {
        if (this.uploadedFiles >= this.fileCount)
        {
            throw new RuntimeException("You are trying to upload more files than previously advertised !!!");
        }
        this.lblFile.setText(fileName_p);
        this.fileProgressBar.setValue(0);
        this.fileProgressBar.setString(NO_PROGRESS);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.client.upload.js.OwProgressMonitor#fileUploaded()
     */
    public void fileUploaded()
    {
        this.uploadedFiles++;
        this.fullyUploadedFilesSize += this.fileSizes[this.uploadedFiles - 1];
        this.totalSizeUploaded = this.fullyUploadedFilesSize;
        updateEstimates();
    }

    private void updateEstimates()
    {
        this.elapsedDeltaTime = System.currentTimeMillis() - this.startTime;
        this.estimatedTotalDeltaTime = this.elapsedDeltaTime * this.totalSize / this.totalSizeUploaded;
        double elapsedTimeDbl = this.elapsedDeltaTime / 1000d;
        double estimatedRemainingTimeDbl = (this.estimatedTotalDeltaTime - this.elapsedDeltaTime) / 1000d;

        String elapsedTimeStr = String.format("%d:%02d:%02d", (int) (elapsedTimeDbl / 3600), (int) ((elapsedTimeDbl % 3600) / 60), (int) (elapsedTimeDbl % 60));
        String estimatedRemainingTimeStr = String.format("%d:%02d:%02d", (int) (estimatedRemainingTimeDbl / 3600), (int) ((estimatedRemainingTimeDbl % 3600) / 60), (int) (estimatedRemainingTimeDbl % 60));

        double totalSizeUploadedMB = OwProgressMonitorSwing.this.totalSizeUploaded / (1024d * 1024d);
        double speed = totalSizeUploadedMB / elapsedTimeDbl;
        final String timeEstimatesTxt = this.platform.getMessages().localize(OwMessages.UPLOAD_MESSAGES_LBLTIMEESTIMATES, "Elapsed time / Remaining time: {0} / {1} ({2} MB/s)", elapsedTimeStr, estimatedRemainingTimeStr, Math.round(speed));

        SwingUtilities.invokeLater(new Runnable() {

            public void run()
            {
                double overallProgress = (double) OwProgressMonitorSwing.this.totalSizeUploaded / (double) OwProgressMonitorSwing.this.totalSize * 100d;
                OwProgressMonitorSwing.this.overallProgressBar.setValue((int) overallProgress);
                OwProgressMonitorSwing.this.overallProgressBar.setString(Math.round(overallProgress) + "%");
                OwProgressMonitorSwing.this.lblOverall.setText(timeEstimatesTxt);
            }
        });
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.client.upload.js.OwProgressMonitor#finish()
     */
    public void finish()
    {
        SwingUtilities.invokeLater(new Runnable() {

            public void run()
            {
                OwProgressMonitorSwing.this.btnClose.setEnabled(true);
                if (OwProgressMonitorSwing.this.isCanceled())
                {
                    OwProgressMonitorSwing.this.close();
                }
                else if (OwProgressMonitorSwing.this.isAutoclose() && !OwProgressMonitorSwing.this.hasErrors())
                {
                    OwProgressMonitorSwing.this.close();
                }
                else
                {
                    OwProgressMonitorSwing.this.btnCancel.setEnabled(false);
                }
            }
        });
    }

    private boolean hasErrors()
    {
        return this.hasErrors;
    }

    /**
     * Auto close is always on.
     * @return boolean
     */
    private boolean isAutoclose()
    {
        return true;
    }

    private void close()
    {
        this.setVisible(false);
        this.dispose();

        if (!this.hasErrors() && !this.isCanceled())
        {
            //DND.onSuccess();
            LOGGER.info("Sending files to server done!");
            if (null != this.onSuccess)
            {
                this.onSuccess.run();
            }
        }
        //        DND.dropTarget().enable();
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.client.upload.js.OwProgressMonitor#fileUploadedProgress(double)
     */
    public void fileUploadedProgress(final double progress_p)
    {
        this.totalSizeUploaded = this.fullyUploadedFilesSize + (long) (progress_p * this.fileSizes[this.uploadedFiles]);
        updateEstimates();

        SwingUtilities.invokeLater(new Runnable() {

            public void run()
            {
                OwProgressMonitorSwing.this.fileProgressBar.setValue((int) (progress_p * 100));
                OwProgressMonitorSwing.this.fileProgressBar.setString(Math.round(progress_p * 100) + "%");
            }

        });
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.client.upload.js.OwProgressMonitor#error(java.lang.String)
     */
    public void error(final String message_p)
    {
        LOGGER.info("Upload error " + message_p);
        SwingUtilities.invokeLater(new Runnable() {

            public void run()
            {
                OwProgressMonitorSwing.this.hasErrors = true;
                OwProgressMonitorSwing.this.lblError.setText("<html><div>" + message_p + "</div></html>");
            }
        });
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.client.upload.js.OwProgressMonitor#start(int)
     */
    public void startingUpload(long fileSizes_p[])
    {
        LOGGER.info("Starting a new upload.");

        this.fileSizes = fileSizes_p;
        this.fileCount = fileSizes_p.length;
        this.totalSize = 0;
        for (long fSize : fileSizes_p)
        {
            this.totalSize += fSize;
        }

        this.lblError.setText("");
        this.uploadedFiles = 0;
        this.fileProgressBar.setValue(0);
        this.fileProgressBar.setString(NO_PROGRESS);
        this.overallProgressBar.setValue(0);
        this.overallProgressBar.setString(NO_PROGRESS);

        SwingUtilities.invokeLater(new Runnable() {

            public void run()
            {
                OwProgressMonitorSwing.this.setVisible(true);
            }
        });

        startTime = System.currentTimeMillis();
        elapsedDeltaTime = 0;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.client.upload.js.OwProgressMonitor#isCanceled()
     */
    public boolean isCanceled()
    {
        return this.canceled;
    }

}
