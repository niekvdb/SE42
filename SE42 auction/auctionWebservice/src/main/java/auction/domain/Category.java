package auction.domain;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Category implements Serializable {

    private String description;

    private Category() {
        description = "undefined";
    }

    public Category(String description) {
        this.description = description;
    }

    public String getDiscription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
