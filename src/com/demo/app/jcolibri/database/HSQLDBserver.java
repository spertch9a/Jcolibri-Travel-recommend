/**
 * HSQLDBserver.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 04/07/2007
 */
package jcolibri.test.database;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;

import jcolibri.util.FileIO;

import org.hsqldb.Server;

/**
 * Creates a data base server with the tables for the examples/tests using the HSQLDB library.
 * @author Juan A. Recio-Garcia
 * @version 1.0
 */
public class HSQLDBserver
{
    static boolean initialized = false;

    private static Server server;

    /**
     * Initialize the server
     */
    public static void init()
    {
	if (initialized)
	    return;
        org.apache.commons.logging.LogFactory.getLog(HSQLDBserver.class).info("Creating data base ...");

	server = new Server();
	server.setDatabaseName(0, "travel");
	server.setDatabasePath(0, "mem:travel;sql.enforce_strict_size=true");
	
	server.setDatabaseName(1, "travelext");
	server.setDatabasePath(1, "mem:travelext;sql.enforce_strict_size=true");
	
	server.setLogWriter(null);
	server.setErrWriter(null);
	server.setSilent(true);
	server.start();

	initialized = true;
	try
	{
	    Class.forName("org.hsqldb.jdbcDriver");

	    PrintStream out = new PrintStream(new ByteArrayOutputStream());
	    Connection conn = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/travel", "sa", "");
	    SqlFile file = new SqlFile(new
	    File(FileIO.findFile("jcolibri/test/database/travel.sql").getFile()),false,new HashMap());
	    file.execute(conn,out,out, true);
	    
	    Connection connExt = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/travelext", "sa", "");
	    SqlFile fileExt = new SqlFile(new
	    File(FileIO.findFile("jcolibri/test/database/travelext.sql").getFile()),false,new HashMap());
	    fileExt.execute(connExt,out,out, true);

	    org.apache.commons.logging.LogFactory.getLog(HSQLDBserver.class).info("Data base generation finished");
	    
	} catch (Exception e)
	{
	    org.apache.commons.logging.LogFactory.getLog(HSQLDBserver.class).error(e);
	}

    }

    /**
     * Shutdown the server
     */
    public static void shutDown()
    {

	if (initialized)
	{
	    server.stop();
	    initialized = false;
	}
    }

    /**
     * Testing method
     */
    public static void main(String[] args)
    {
	HSQLDBserver.init();
	HSQLDBserver.shutDown();
	System.exit(0);
	
    }

}
