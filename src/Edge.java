import java.io.Serializable;

public class Edge implements Serializable {
    String id;
    String src;
    String desst;
    int wieght;

    public Edge(String src, String desst, int wieght) {
        this.src = src;
        this.desst = desst;
        this.wieght = wieght;
    }


    public String getId() {
        return id;
    }

    public String getSrc() {
        return src;
    }

    public String getDesst() {
        return desst;
    }

    public int getWieght() {
        return wieght;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public void setDesst(String desst) {
        this.desst = desst;
    }

    public void setWieght(int wieght) {
        this.wieght = wieght;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "id='" + id + '\'' +
                ", src='" + src + '\'' +
                ", desst='" + desst + '\'' +
                ", wieght=" + wieght +
                '}';
    }
}
