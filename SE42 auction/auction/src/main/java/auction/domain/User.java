package auction.domain;

import javax.persistence.*;

@Entity
@NamedQueries({
    @NamedQuery(name = "User.getAll", query = "select u from User as u"),
    @NamedQuery(name = "User.count", query = "select count(u) from User as u"),
    @NamedQuery(name = "User.findByEmail", query = "select u from User as u where u.email = :userEmail")
})
public class User {

    @Id
    private String email;

    public User(String email) {
        this.email = email;

    }

    public String getEmail() {
        return email;
    }
}
