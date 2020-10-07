/*==========================================================================
  
  debezium-hello

  App.java

  A trivially simple example of using Camels' Debezium support to consume
  change records from a relational database -- PostgreSQL in this
  case.

  Use as you see fit.

==========================================================================*/
package me.kevinboone.apacheintegration.debezium_hello;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.spi.PropertiesComponent;

public class App
  {
  public static void main (String args[])
      throws Exception
    {
    CamelContext camelContext = new DefaultCamelContext();

    camelContext.addRoutes (new RouteBuilder()
      {
      public void configure()
        {
	// For convenience, read database connection properties from
	//  the file application.properties. There's nothing significant
	//  about this name -- this isn't Spring Boot.
	PropertiesComponent pc = camelContext.getPropertiesComponent();
        pc.setLocation("classpath:application.properties");

        // Define the Debezium consumer endpoint
	from ("debezium-postgres:{{database.hostname}}?"
          + "databaseHostname={{database.hostname}}"
          + "&databasePort={{database.port}}"
          + "&databaseUser={{database.user}}"
          + "&databasePassword={{database.password}}"
          + "&databaseDbname={{database.dbname}}"
          + "&databaseServerName={{database.hostname}}"
          + "&schemaWhitelist={{database.schema}}"
          + "&tableWhitelist={{database.schema}}.{{database.table}}"
          + "&offsetStorageFileName=/tmp/offset.dat"
          + "&offsetFlushIntervalMs=10000"
          + "&pluginName=pgoutput")
	  .to ("log://foo"); // Just log each message.
        }
      });

    // Start the routes
    camelContext.start()e;

    // Consume messages forever
    while (true)
      {
      Thread.sleep (1000);
      }

    // We should stop the Camel context if the program ever stops.
    //camelContext.stop();
    }
  }

