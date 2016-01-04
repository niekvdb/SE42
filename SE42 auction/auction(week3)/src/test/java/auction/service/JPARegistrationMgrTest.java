package auction.service;

import java.util.List;
import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;

import auction.domain.User;
import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import util.DatabaseCleaner;

public class JPARegistrationMgrTest {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("auctionPU");
    private EntityManager em;
    private RegistrationMgr registrationMgr;
    
    @Before
    public void setUp() throws Exception {
        registrationMgr = new RegistrationMgr();
        
        em = emf.createEntityManager();
        DatabaseCleaner dc = new DatabaseCleaner(em);
        
        try
        {
            dc.clean();
        }
        catch(SQLException ex)
        {
            System.out.println("SQLException: " + ex.getMessage());
        }
    }

    @Test
    public void registerUser() {
        User user1 = registrationMgr.registerUser("xxx1@yyy");
        assertTrue(user1.getEmail().equals("xxx1@yyy"));
        User user2 = registrationMgr.registerUser("xxx2@yyy2");
        assertTrue(user2.getEmail().equals("xxx2@yyy2"));
        User user2bis = registrationMgr.registerUser("xxx2@yyy2");
        assertEquals(user2bis, user2);
        //geen @ in het adres
        assertNull(registrationMgr.registerUser("abc"));
    }

    @Test
    public void getUser() {
        User user1 = registrationMgr.registerUser("xxx5@yyy5");
        User userGet = registrationMgr.getUser("xxx5@yyy5");
        assertEquals(userGet, user1);
        assertNull(registrationMgr.getUser("aaa4@bb5"));
        registrationMgr.registerUser("abc");
        assertNull(registrationMgr.getUser("abc"));
    }

    @Test
    public void getUsers() {
        List<User> users = registrationMgr.getUsers();
        assertEquals(0, users.size());

        User user1 = registrationMgr.registerUser("xxx8@yyy");
        users = registrationMgr.getUsers();
        assertEquals(1, users.size());
        assertEquals(users.get(0), user1);


        User user2 = registrationMgr.registerUser("xxx9@yyy");
        users = registrationMgr.getUsers();
        assertEquals(2, users.size());

        registrationMgr.registerUser("abc");
        //geen nieuwe user toegevoegd, dus gedrag hetzelfde als hiervoor
        users = registrationMgr.getUsers();
        assertEquals(2, users.size());
    }
}
