/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import web.webservicemethodes;
import webservice.Bid;
import webservice.Category;
import webservice.Item;
import webservice.Money;
import webservice.User;

/**
 *
 * @author Niek
 */
public class AuctionMgrTest {
    
     @Before
    public void setUp() throws Exception {
        webservicemethodes.cleanDatabase();
    }

    @Test
    public void getItem() {
        String email = "xx2@nl";
        String omsch = "omsch";

        User seller1 = webservicemethodes.registerUser(email);
        Category cat = new Category();
        cat.setDescription(omsch);
        Item item1 = webservicemethodes.offerItem(seller1, cat, omsch);
        Item item2 = webservicemethodes.getItem(item1.getId());

        assertEquals(omsch, item2.getDescription());
        assertEquals(email, item2.getSeller().getEmail());
    }

    @Test
    public void findItemByDescription() {
        String email3 = "xx3@nl";
        String omsch = "omsch";
        String email4 = "xx4@nl";
        String omsch2 = "omsch2";

        User seller3 = webservicemethodes.registerUser(email3);
        User seller4 = webservicemethodes.registerUser(email4);
        Category cat = new Category();
        cat.setDescription(omsch);
        Item item1 = webservicemethodes.offerItem(seller3, cat, omsch);
        Item item2 = webservicemethodes.offerItem(seller4, cat, omsch);

        ArrayList<Item> res = (ArrayList<Item>) webservicemethodes.findItemByDescription(omsch2);

        assertEquals(0, res.size());

        res = (ArrayList<Item>) webservicemethodes.findItemByDescription(omsch);
        assertEquals(2, res.size());
    }

    @Test
    public void newBid() {
        String email = "ss2@nl";
        String emailb = "bb@nl";
        String emailb2 = "bb2@nl";
        String omsch = "omsch_bb";

        User seller = webservicemethodes.registerUser(email);
        User buyer = webservicemethodes.registerUser(emailb);
        User buyer2 = webservicemethodes.registerUser(emailb2);

        Category cat = new Category();
        cat.setDescription(omsch);

        Item item1 = webservicemethodes.offerItem(seller, cat, omsch);
        Money money = new Money();
        money.setCents(100);
        money.setCurrency("eur");
        Bid bid1 = webservicemethodes.newBid(item1, buyer, money);

        assertEquals(emailb, bid1.getBuyer().getEmail());
        
        item1 = webservicemethodes.getItem(item1.getId());
        money.setCents(90);
        Bid bid2 = webservicemethodes.newBid(item1, buyer2, money);
      //  assertNull(bid2);

        item1 = webservicemethodes.getItem(item1.getId());
        money.setCents(110);
        Bid bid3 = webservicemethodes.newBid(item1, buyer2, money);
        assertEquals(emailb2, bid3.getBuyer().getEmail());
    }
}
