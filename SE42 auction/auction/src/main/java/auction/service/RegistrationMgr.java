package auction.service;

import java.util.*;
import auction.domain.User;
import auction.dao.UserDAO;
import auction.dao.UserDAOJPAImpl;
import javax.persistence.*;

public class RegistrationMgr {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("auctionPU");

    public RegistrationMgr() {
    }

    /**
     * Registreert een gebruiker met het als parameter gegeven e-mailadres, mits
     * zo'n gebruiker nog niet bestaat.
     * @param email
     * @return Een Userobject dat geïdentificeerd wordt door het gegeven
     * e-mailadres (nieuw aangemaakt of reeds bestaand). Als het e-mailadres
     * onjuist is ( het bevat geen '@'-teken) wordt null teruggegeven.
     */
    public User registerUser(String email) {
        if (!email.contains("@")) {
            return null;
        }
        
        EntityManager em = emf.createEntityManager();
        UserDAO userDAO = new UserDAOJPAImpl(em);
        
        User user = null;
        
        em.getTransaction().begin();
        try {
            user = userDAO.findByEmail(email);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        
        if (user != null) {
            return user;
        }
        
        if (user != null) {
            return user;
        }
        
        
        user = new User(email);
        
        em = emf.createEntityManager();
        userDAO = new UserDAOJPAImpl(em);
        
        em.getTransaction().begin();
        try {
            userDAO.create(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        
        return user;
    }

    /**
     *
     * @param email een e-mailadres
     * @return Het Userobject dat geïdentificeerd wordt door het gegeven
     * e-mailadres of null als zo'n User niet bestaat.
     */
    public User getUser(String email) {     
        EntityManager em = emf.createEntityManager();
        UserDAO userDAO = new UserDAOJPAImpl(em);
        User user = null;
        em.getTransaction().begin();
        try {
            user = userDAO.findByEmail(email);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        
        return user;
    }

    /**
     * @return Een iterator over alle geregistreerde gebruikers
     */
    public List<User> getUsers() {
        EntityManager em = emf.createEntityManager();
        UserDAO userDAO = new UserDAOJPAImpl(em);
        List<User> users = new ArrayList();
        em.getTransaction().begin();
        try {
            users = userDAO.findAll();
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        
        return users;
    }
}
