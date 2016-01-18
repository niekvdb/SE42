/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import web.webservicemethodes;
import webservice.User;

/**
 *
 * @author Niek
 */
public class RegistrationMgrTest {
    
     @Before
    public void setUp() throws Exception {
        webservicemethodes.cleanDatabase();
    }

    @Test
    public void registerUser() {
        User user1 = webservicemethodes.registerUser("jj@PP.nl");
        assertTrue(user1.getEmail().equals("jj@PP.nl"));
        
        User user2 = webservicemethodes.registerUser("pp@jj");
        assertTrue(user2.getEmail().equals("pp@jj"));
    }

    @Test
    public void getUser() {
        User user1 = webservicemethodes.registerUser("jj@pp");
        User getUser = webservicemethodes.getUser("jj@pp");
        
        assertEquals(user1.getEmail(), getUser.getEmail());
        assertNull(webservicemethodes.getUser("test123"));
        webservicemethodes.registerUser("abc");
        assertNull(webservicemethodes.getUser("abc"));
    }


    
}
