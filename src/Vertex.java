import java.io.Serializable;

public class Vertex implements Serializable {

    String name;

    public Vertex(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "name='" + name + '\'' +
                '}';
    }
}

