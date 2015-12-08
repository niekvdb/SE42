package auction.domain;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import javax.persistence.*;

@Entity
@NamedQueries({
    @NamedQuery(name = "User.count", query = "select count(u) from User as u"),
    @NamedQuery(name = "User.findByEmail", query = "select u from User as u where u.email = :email")
})
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "seller")
    private Set<Item> offeredItems;

    public User() {
    }

    public User(String email) {
        this.email = email;

    }

    public Long getId() {
        return Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Iterator getOfferedItems() {
        return this.offeredItems.iterator();
    }

    protected void addItem(Item item) {
        this.offeredItems.add(item);
    }

    public int numberOfOfferdItems() {
        return this.offeredItems.size();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }

        try {
            User user = (User) object;
            if (user.getEmail().equals(email)) {
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }
}
