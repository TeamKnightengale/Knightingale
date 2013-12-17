package edu.allegheny.knightingale.test;

import org.dbunit.*;
import org.dbunit.database.*;
import org.dbunit.dataset.xml.*;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.operation.DatabaseOperation;
import org.dbunit.dataset.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Description :  this will be used as the base class for all the dbunit test cases for DCC
 *
 */

public class DBUnitTestCase extends DBTestCase {
 
    /**
     * Tell the XML parser there is no DTD.
     */
    protected boolean dtdMetadata = false;
 
    /**
     * Tell the DataSet creator to parse defensively.
     */
    protected boolean enableColumnSensing = true;
 
    /**
     * Enforce a case sensitive DataSet
     */
    protected boolean caseSensitiveDataSet = true;
 
    protected String moduleFolder;
    protected String driver;
    protected String connectionURL;
    protected String userName;
    protected String password;
    protected String schema;
 
    protected String datasetTestData;
    protected DataSource dataSource;
 
    public DataSource getDataSource() {
        return dataSource;  
    }
 
    public DBUnitTestCase() {
    }
 
    public DBUnitTestCase(String folder, String testDBFileName, String propertiesFile) {
        Properties props = new Properties();
        moduleFolder = folder;
        try {
     props.load(new FileInputStream(new File(folder, propertiesFile)));
            driver = props.getProperty("dbUnitJDBCDriver");
            connectionURL = props.getProperty("dbUnitConnectionURL");
            schema = props.getProperty("dbUnitSchema");
 
            System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, driver);
            System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, connectionURL);
            
            this.datasetTestData = testDBFileName;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    @Override
    public void setUp() throws Exception {
        super.setUp();
        
    }
 
    // protected void initDataSource() throws SQLException {
    //     dataSource = new SingleConnectionDataSource(connectionURL, userName, password, true);        
    // }
 
    protected DatabaseOperation getSetUpOperation() throws Exception {
        return DatabaseOperation.CLEAN_INSERT;
    }
 
    protected IDataSet getDataSet() throws Exception {
        FlatXmlDataSetBuilder fdsb = new FlatXmlDataSetBuilder();
        fdsb.setCaseSensitiveTableNames(caseSensitiveDataSet);
        fdsb.setColumnSensing(enableColumnSensing);
        fdsb.setDtdMetadata(dtdMetadata);
        return fdsb.build(new File(moduleFolder + datasetTestData));
    }
 
    @Override
    public void tearDown() throws Exception {
        Thread.sleep(1000); // added this because I got the same listener error otherwise when trying to do tear down
        super.tearDown();
        Thread.sleep(1000);
        dataSource.getConnection().close();
    }
}