// package edu.allegheny.tweetanalyze.test;

// import org.dbunit.*;
// import org.dbunit.database.*;
// import org.dbunit.dataset.xml.*;
// import org.dbunit.PropertiesBasedJdbcDatabaseTester;
// import org.dbunit.operation.DatabaseOperation;
// import org.dbunit.dataset.*;
// import java.io.File;
// import java.io.FileOutputStream;
// import java.io.FileInputStream;
// import java.io.IOException;
// import java.sql.Connection;
// import java.sql.DriverManager;
// import javax.sql.DataSource;
// import java.sql.SQLException;
// import java.util.Properties;

// public class TestCompositionAnalyzer extends DBTestCase
// {
//     public TestCompositionAnalyzer(String name)
//     {
//         super( name );
//         System.setProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, "org.sqlite.JDBC" );
//         System.setProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, "jdbc:sqlite:tweets.db" );
//     }

//     protected IDataSet getDataSet() throws Exception
//     {
//         return new FlatXmlDataSetBuilder().build(new FileInputStream("full.xml"));
//     }

//     public void testme()
//     {
//         // Fetch database data after executing your code
//         IDataSet databaseDataSet = getConnection().createDataSet();
//         ITable actualTable = databaseDataSet.getTable("tweets");

//         // Load expected data from an XML dataset
//         IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new File("full.xml"));
//         ITable expectedTable = expectedDataSet.getTable("tweets");

//         // Assert actual database table match expected table
//         Assertion.assertEquals(expectedTable, actualTable);
//     }

        
// }