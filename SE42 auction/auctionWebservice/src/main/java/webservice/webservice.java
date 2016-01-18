/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice;

import javax.xml.ws.Endpoint;

/**
 *
 * @author Niek
 */
public class webservice {

    private static final String auctionURL = "http://localhost:9090/Auction";
    private static final String registrationURL = "http://localhost:9091/Registration";
    private static final String databaseCleaner = "http://localhost:9092/DatabaseCleaner";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Starting webservices");
        Endpoint.publish(auctionURL, new Auction());
        System.out.println("-- AuctionService deployed");
        Endpoint.publish(registrationURL, new Registration());
        System.out.println("-- RegistrationService deployed");
        Endpoint.publish(databaseCleaner, new WebDatabaseClean());
        System.out.println("-- WebDatabaseClean deployed");

    }

}
