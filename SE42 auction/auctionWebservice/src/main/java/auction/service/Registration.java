/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auction.service;

import auction.domain.User;
import java.sql.SQLException;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import nl.fontys.util.DatabaseCleaner;

/**
 *
 * @author Niek
 */
@WebService
public class Registration
{
    private final RegistrationMgr registrationMgr = new RegistrationMgr();
    @WebMethod
  
    public void cleanDatabase() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("auctionPU");
        EntityManager em = emf.createEntityManager();
        
        DatabaseCleaner dc = new DatabaseCleaner(em);
        try {
            dc.clean();
        }
        catch(SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }
    } 
    
    @WebMethod
    public User registerUser(String email) {
        return this.registrationMgr.registerUser(email);
    }
    
    @WebMethod
    public User getUser(String user) {
        return this.registrationMgr.getUser(user);
    }
    
    @WebMethod
    public boolean compareUser(User user1, User user2) {
        return user1.equals(user2);
    }
}
