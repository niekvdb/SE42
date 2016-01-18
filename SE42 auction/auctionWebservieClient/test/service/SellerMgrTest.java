/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import web.webservicemethodes;
import webservice.Category;
import webservice.Item;
import webservice.Money;
import webservice.User;

/**
 *
 * @author Niek
 */
public class SellerMgrTest {
    
    @Before
    public void setUp() throws Exception {
        webservicemethodes.cleanDatabase();
    }

    /**
     * Test of offerItem method, of class SellerMgr.
     */
    @Test
    public void testOfferItem() {
        
        String omsch = "omsch";

        User user1 = webservicemethodes.registerUser("xx@nl");
        
        Category cat = new Category();
        cat.setDescription(omsch);
        Item item1 = webservicemethodes.offerItem(user1, cat, omsch);
        assertEquals(omsch, item1.getDescription());
        assertNotNull(item1.getId());
    }

    /**
     * Test of revokeItem method, of class SellerMgr.
     */
    @Test
    public void testRevokeItem() {
        
        String omsch = "omsch";
        String omsch2 = "omsch2";

        User seller = webservicemethodes.registerUser("sel@nl");
        User buyer = webservicemethodes.registerUser("buy@nl");
        Category cat = new Category();
        cat.setDescription("cat1");
        
        Item item1 = webservicemethodes.offerItem(seller, cat, omsch);
        boolean res = webservicemethodes.revokeItem(item1);
        assertTrue(res);
         
        int count = webservicemethodes.findItemByDescription(omsch).size();
        assertEquals(0,count);

        Item item2 = webservicemethodes.offerItem(seller, cat, omsch2);
        Money money = new Money();
        money.setCents(100);
        money.setCurrency("eur");
        webservicemethodes.newBid(item2, buyer, money);
        item2 = webservicemethodes.getItem(item2.getId());
        boolean res2 = webservicemethodes.revokeItem(item2);
        assertTrue(res2);
        int count2 = webservicemethodes.findItemByDescription(omsch2).size();
        assertEquals(0,count2);
    }
}
