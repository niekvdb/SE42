package auction.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import nl.fontys.util.FontysTime;
import nl.fontys.util.Money;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Bid implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private FontysTime time;
    private User buyer;
    private Money amount;
    
     private Item item;

    public Bid(User buyer, Money amount) {
        this.buyer = buyer;
        this.amount = amount;
        this.item = item;
        this.time = FontysTime.now();
    }

    public Bid() {
    }

    public FontysTime getTime() {
        return time;
    }

    public User getBuyer() {
        return buyer;
    }

    public Money getAmount() {
        return amount;
    }
}
