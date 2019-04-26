import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph implements Serializable {
    List<String> vertex;
    List<Edge> edges;
    Map<String, List<String>> map;
    Map<String, Map<String, Integer>> costs;
    Router src;

    public Graph(List<String> vertex, List<Edge> edges) {
        this.vertex = vertex;
        this.edges = edges;
        map = new HashMap<>();
        costs = new HashMap<>();
        build();
    }

    private void build(){
        for(String v : vertex){
            map.putIfAbsent(v, new ArrayList<>());
            costs.putIfAbsent(v, new HashMap<>());
            for(Edge e : edges){
                String src = e.getSrc();
                String dest = e.getDesst();
                int weight = e.getWieght();
                if(v.equals(src)){
                    map.get(v).add(dest);
                    costs.get(v).put(dest, weight);
                }
            }
        }



    }

    public void updateGraph(Router src, List<Link> links){
        this.src = src;
        int n = links.size();
        for(int i = 0; i < n; i++){
            Link link = links.get(i);
            String dest = link.getDest();
            int weight = link.getWeight();
            this.vertex.add(dest);
            this.edges.add(new Edge(src.name, dest, weight));
        }
        build();
    }

    public void updateGraph(Router src, Link link){
        this.src = src;
        String dest = link.getDest();
        int weight = link.getWeight();
        this.vertex.add(dest);
        this.edges.add(new Edge(src.name, dest, weight));

        build();
    }


    public Map<String, List<String>> getMap() {
        return map;
    }

    public Map<String, Map<String, Integer>> getCosts() {
        return costs;
    }

    public List<String> getVertex() {
        return vertex;
    }

    public List<Edge> getEdges() {
        return edges;
    }


}

