package auction.domain;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import nl.fontys.util.Money;

@Entity
@NamedQueries({
    @NamedQuery(name = "Item.count", query = "select count(i) from Item as i"),
    @NamedQuery(name = "Item.find", query = "select i from Item as i where i.id = :id"),
    @NamedQuery(name = "Item.findByDescription", query = "select i from Item as i where i.description = :description")
})
public class Item implements Comparable, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User seller;

    private Category category;

    private String description;

    @OneToOne(mappedBy = "item")
    private Bid highest;

    public Item() {
    }

    public Item(User seller, Category category, String description) {
        this.seller = seller;
        this.category = category;
        this.description = description;
        this.highest = null;

        this.seller.addItem(this);
    }

    public Long getId() {
        return id;
    }

    public User getSeller() {
        return seller;
    }

    public Category getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public Bid getHighestBid() {
        return highest;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setHighestBid(Bid highest) {
        this.highest = highest;
    }

    public Bid newBid(User buyer, Money amount) {
        if (highest != null && highest.getAmount().compareTo(amount) >= 0) {
            return null;
        }
        highest = new Bid(buyer, amount, this);
        return highest;
    }

    public int compareTo(Object arg0) {
        //TODO
        return -1;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof Item)) {
            return false;
        }

        Item item = (Item) o;

        // aangezien id pas aangemaakt wordt als deze toegevoegd wordt aan de database
        // en de andere attributen buiten de constructor geset worden (dus niet persee
        // geinitialiseerd zijn) alleen de attributen binnen de constructor gebruikt
        return (this.seller.equals(item.getSeller())
                && this.category.equals(item.getCategory())
                && this.description.equals(item.getDescription()));
    }

    public int hashCode() {
        return this.seller.hashCode()
                * this.category.hashCode()
                * this.description.hashCode();
    }
}
