/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auction.service;

import auction.domain.Bid;
import auction.domain.Category;
import auction.domain.Item;
import auction.domain.User;
import java.sql.SQLException;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import nl.fontys.util.DatabaseCleaner;
import nl.fontys.util.Money;

/**
 *
 * @author Niek
 */
@WebService
public class Auction
{
    private final AuctionMgr auctionMgr = new AuctionMgr();
    private final SellerMgr  sellerMgr  = new SellerMgr();
    
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
    public Item getItem(long id) {
        return this.auctionMgr.getItem(id);
    }
    
    @WebMethod
    public List<Item> findItemByDescription(String description) {
        return this.auctionMgr.findItemByDescription(description);
    }
    
    @WebMethod
    public Bid newBid(Item item, User buyer, Money amount) {
        return this.auctionMgr.newBid(item, buyer, amount);
    }
    
    @WebMethod
    public Item offerItem(User seller, Category category, String description) {
        return this.sellerMgr.offerItem(seller, category, description);
    }
    
    @WebMethod
    public boolean revokeItem(Item item) {
        return this.sellerMgr.revokeItem(item);
    }

}