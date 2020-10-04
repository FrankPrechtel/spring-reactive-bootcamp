package eu.prechtel.reciprocus;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;
import java.util.Random;

@Table
public class Gingerbread {
    public static final String[] flavors = {"chocolate", "cinnamon", "honey"};

    @Id
    private Integer id;

    private String flavor;

    public Gingerbread() {
        // get a random flavor
        this(flavors[new Random().nextInt(flavors.length)]);
    }

    public Gingerbread(String flavor) {
        this.flavor = flavor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFlavor() {
        return flavor;
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

    @Override
    public String toString() {
        return "Gingerbread{" +
                "id=" + id +
                ", flavor='" + flavor + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gingerbread that = (Gingerbread) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getFlavor(), that.getFlavor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFlavor());
    }
}
