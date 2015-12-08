package auction.service;

import org.junit.Ignore;
import javax.persistence.*;
import util.DatabaseCleaner;
import auction.domain.Category;
import auction.domain.Item;
import auction.domain.User;
import java.util.Iterator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ItemsFromSellerTest {

    final EntityManagerFactory emf = Persistence.createEntityManagerFactory("auctionPU");
    final EntityManager em = emf.createEntityManager();
    private AuctionMgr auctionMgr;
    private RegistrationMgr registrationMgr;
    private SellerMgr sellerMgr;

    public ItemsFromSellerTest() {
    }

    @Before
    public void setUp() throws Exception {
        registrationMgr = new RegistrationMgr();
        auctionMgr = new AuctionMgr();
        sellerMgr = new SellerMgr();
        new DatabaseCleaner(em).clean();
    }

    @Test
    //   @Ignore
    public void numberOfOfferdItems() {

        String email = "ifu1@nl";
        String omsch1 = "omsch_ifu1";
        String omsch2 = "omsch_ifu2";

        User user1 = registrationMgr.registerUser(email);
        assertEquals(0, user1.numberOfOfferdItems());

        Category cat = new Category("cat2");
        Item item1 = sellerMgr.offerItem(user1, cat, omsch1);

        // test number of items belonging to user1
        //assertEquals(0, user1.numberOfOfferdItems());
        assertEquals(1, user1.numberOfOfferdItems());

        /*
         *  expected: which one of te above two assertions do you expect to be true?
         *  QUESTION:
         *    Explain the result in terms of entity manager and persistance context.
         ANSWER:
         The bottom assertion is expected to be true. One Item is created and commited in the entity manager after which it 
         is automaticly added to the offerdItems list of the item's seller (User).
         This means there the number of offeredItems should be 1.
         */
        assertEquals(1, item1.getSeller().numberOfOfferdItems());

        User user2 = registrationMgr.getUser(email);
        assertEquals(1, user2.numberOfOfferdItems());
        Item item2 = sellerMgr.offerItem(user2, cat, omsch2);
        assertEquals(2, user2.numberOfOfferdItems());

        User user3 = registrationMgr.getUser(email);
        assertEquals(2, user3.numberOfOfferdItems());

        User userWithItem = item2.getSeller();
        assertEquals(2, userWithItem.numberOfOfferdItems());
        // assertEquals(3, userWithItem.numberOfOfferdItems());
        /*
         *  expected: which one of te above two assertions do you expect to be true?
         *  QUESTION:
         *    Explain the result in terms of entity manager and persistance context.
        
         ANSWER:
         The top assertion is to be expected.
         The seller of item2 is same user as user3, so the number of offerdItems should still be 2.
         */

        assertNotSame(user3, userWithItem);
        assertEquals(user3, userWithItem);

    }

    @Test
//    @Ignore
    public void getItemsFromSeller() {
        String email = "ifu1@nl";
        String omsch1 = "omsch_ifu1";
        String omsch2 = "omsch_ifu2";

        Category cat = new Category("cat2");

        User user10 = registrationMgr.registerUser(email);
        Item item10 = sellerMgr.offerItem(user10, cat, omsch1);
        Iterator<Item> it = user10.getOfferedItems();
        // testing number of items of java object
        assertTrue(it.hasNext());

        // now testing number of items for same user fetched from db.
        User user11 = registrationMgr.getUser(email);
        Iterator<Item> it11 = user11.getOfferedItems();
        assertTrue(it11.hasNext());
        it11.next();
        assertFalse(it11.hasNext());

        // Explain difference in above two tests for te iterator of 'same' user
        //ANSWER
        //item10 is added to user10 so user10 has 1 item in it's offerdItems list.
        //user10's email is used for user11, which makes it the same persistant user
        //it11 is user11's iterator.
        //When it11.hasNext() is called first, the offerdItems list contains one item so it returns true
        //When it11.next() is called, the index of the iterator is increased by
        //This means that there is no next item in the offerdItems list, so it returns false
        User user20 = registrationMgr.getUser(email);
        Item item20 = sellerMgr.offerItem(user20, cat, omsch2);
        Iterator<Item> it20 = user20.getOfferedItems();
        assertTrue(it20.hasNext());
        it20.next();
        assertTrue(it20.hasNext());

        User user30 = item20.getSeller();
        Iterator<Item> it30 = user30.getOfferedItems();
        assertTrue(it30.hasNext());
        it30.next();
        assertTrue(it30.hasNext());

    }
}
