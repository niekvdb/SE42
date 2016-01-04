/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auction.dao;

import auction.domain.Item;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author Niek
 */
public class ItemDAOJPAImpl implements ItemDAO {

    private EntityManager em;

    public ItemDAOJPAImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public int count() {
        Query q = em.createNamedQuery("Item.count", Item.class);
        return ((Long) q.getSingleResult()).intValue();
    }

    @Override
    public void create(Item item) {
        em.persist(item);
    }

    @Override
    public void edit(Item item) {
        if (findByDescription(item.getDescription()) == null) {
            throw new IllegalArgumentException();
        }
        em.merge(item);
    }

    @Override
    public Item find(Long id) {

        System.out.println("---Searching for item with id: " + id);

        Query q = em.createNamedQuery("Item.find", Item.class);
        q.setParameter("id", id);
        Item item = null;
        try {
            item = (Item) q.getSingleResult();
            System.out.println("---Item found");
        } catch (NoResultException e) {
            System.out.println("---Item not found");
            e.printStackTrace();
            item = null;
        }
        return item;
    }

    @Override
    public List<Item> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Item.class));
        return em.createQuery(cq).getResultList();
    }

    @Override
    public List<Item> findByDescription(String description) {
        Query q = em.createNamedQuery("Item.findByDescription", Item.class);
        q.setParameter("description", description);
        List<Item> items;
        try {
            items = (List<Item>) q.getResultList();
        } catch (NoResultException e) {
            e.printStackTrace();
            items = new ArrayList();
        }
        return items;
    }

    @Override
    public void remove(Item item) {
        em.remove(em.merge(item));
    }

}
