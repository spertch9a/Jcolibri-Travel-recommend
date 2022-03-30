/**
 * InitGate.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 23/06/2007
 */
package com.demo.app.jcolibri.extensions.textual.IE.gate.gateinit;

import gate.util.GateException;

import java.io.File;

/**
 * Initalizes GATE with the configuration files included in this package.
 * See GATE's tutorial for details.
 * @author Juan A. Recio-Garcia
 * @version 1.0
 */
public class InitGate
{
    private static boolean initialized = false;

    public static void initGate()
    {
	if (initialized)
	    return;
	try
	{
	    File gateHome = new File(jcolibri.util.FileIO.findFile("jcolibri/extensions/textual/IE/gate/gateinit").getFile());
	    gate.Gate.setGateHome(gateHome);
	    gate.Gate.setUserConfigFile(new File(gateHome, "user-gate.xml"));
	    gate.Gate.init();
	    gate.Gate.getCreoleRegister().registerDirectories(jcolibri.util.FileIO.findFile("jcolibri/extensions/textual/IE/gate/gateinit/plugins/ANNIE"));
	    initialized = true;
	} catch (GateException e)
	{
	    org.apache.commons.logging.LogFactory.getLog(InitGate.class).error(e);
	}
    }
}
