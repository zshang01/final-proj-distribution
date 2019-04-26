import java.io.Serializable;

public class Link implements Serializable {
    String linkName;
    Router src;
    String dest;
    int weight;


    public Link(String linkName, Router src, String dest, int weight) {
        this.linkName = linkName;
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }

    public Router getSrc() {
        return src;
    }

    public String getDest() {
        return dest;
    }

    public int getWeight() {
        return weight;
    }

    public void setSrc(Router src) {
        this.src = src;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Link{" +
                "linkName='" + linkName + '\'' +
                ", src=" + src +
                ", dest='" + dest + '\'' +
                ", weight=" + weight +
                '}';
    }
}
