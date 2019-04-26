import java.util.*;

public class Kruskal {

    Graph graph;
    List<Edge> edges;
    Set<String> nodes;
    Map<String, String> parent;
    List<Edge> mst;
    public Kruskal(Graph graph) {
        this.graph = graph;
        edges = new ArrayList<>(graph.getEdges());
        nodes = fill(graph.getEdges());
        parent = fillParent();
        mst = new ArrayList<>();

    }
    private Map<String, String>  fillParent(){
        Map<String, String> parent = new HashMap<>();

        for(String node : nodes){
            parent.put(node, node);
        }

        return parent;
    }
    private Set<String> fill(List<Edge> edges){
        Set<String> res = new HashSet<>();
        for(Edge e : edges){
            res.add(e.src);
            res.add(e.desst);
        }
        return res;
    }

    public void process(){
        Set<String> visited = new HashSet<>();
        PriorityQueue<Edge> pq = new PriorityQueue<>(new Comparator<Edge>() {
            @Override
            public int compare(Edge o1, Edge o2) {
                return o1.wieght - o2.wieght;
            }
        });
        pq.addAll(this.edges);
        int index = 0;
        while(index < nodes.size() - 1){
            Edge cur = pq.poll();
            String src = cur.getSrc();
            String dest = cur.getDesst();
            int weight = cur.getWieght();
            String srcparent = find(src);
            String destparent = find(dest);
            if(!srcparent.equals(destparent)){
                parent.put(destparent, srcparent);
                mst.add(cur);
                index++;
            }
        }

    }

    public List<Edge> getEdges(){
        return mst;
    }

    private String find(String src){
        if(!src.equals(parent.get(src))){
            parent.put(src, find(parent.get(src)));
        }
        return parent.get(src);
    }




}
