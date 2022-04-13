/**
 * WindowUtils.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 24/10/2007
 */
package com.demo.jcolibri.method.gui.utils;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * Utility class to manage windows
 * @author Juan A. Recio-Garcia
 * @version 1.0
 *
 */
public class WindowUtils
{

    public static void centerWindow(JFrame frame)
    {
	Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	frame.setBounds((screenSize.width - frame.getWidth()) / 2,
		(screenSize.height - frame.getHeight()) / 2, 
		frame.getWidth(),
		frame.getHeight());
    }
    public static void centerWindow(JDialog dialog)
    {
	Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	dialog.setBounds((screenSize.width - dialog.getWidth()) / 2,
		(screenSize.height - dialog.getHeight()) / 2, 
		dialog.getWidth(),
		dialog.getHeight());
    }
}
