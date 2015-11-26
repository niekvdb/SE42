package auction.dao;

import auction.domain.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

public class UserDAOJPAImpl implements UserDAO {

    private final EntityManager em;
    private final EntityTransaction tx;

    public UserDAOJPAImpl(EntityManager em) {
        this.tx = em.getTransaction();
        this.em = em;
    }

    @Override
    public int count() {
        Query q = em.createNamedQuery("User.count", User.class);
        return ((Long) q.getSingleResult()).intValue();
    }

    @Override
    public void create(User user) {
        if (findByEmail(user.getEmail()) != null) {
            throw new EntityExistsException();
        }
        tx.begin();
        try {
            em.persist(user);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        }
    }

    @Override
    public void edit(User user) {
        if (findByEmail(user.getEmail()) == null) {
            throw new IllegalArgumentException();
        }
        tx.begin();
        try {
            em.merge(user);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        }
    }

    @Override
    public List<User> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(User.class));
        return em.createQuery(cq).getResultList();
    }

    @Override
    public User findByEmail(String email) {
        Query q = em.createNamedQuery("User.findByEmail", User.class);
        q.setParameter("userEmail", email);
        try {
            User user = (User) q.getSingleResult();
            return user;
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public void remove(User user) {
        tx.begin();
        try {
            em.remove(em.merge(user));
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        }
    }
}
