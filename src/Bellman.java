import java.util.List;
import java.util.Map;

public class Bellman {
    Graph graph;
    Map<String, Integer> map;
    List<String> vertexList;
    List<Edge> edgeList;
    public Bellman(Graph graph) {
        this.graph = graph;
        vertexList = graph.getVertex();
        edgeList = graph.getEdges();
        String src = vertexList.get(0);
        for(String s : vertexList){
            map.put(s, Integer.MAX_VALUE);
        }
        map.put(src, 0);

    }

    public boolean hasCycle(){
        int n = this.vertexList.size();
        for(int i = 0; i < n - 1; i ++){
            for(int j = 0; j < edgeList.size(); j++){
                Edge e = edgeList.get(j);
                String src = e.getSrc();
                String dest = e.getDesst();
                int weight = e.getWieght();
                if(map.get(src) != Integer.MAX_VALUE && map.get(src) + weight < map.get(dest)){
                    map.put(dest, map.get(src) + weight);
                }
            }
        }

        for(int j = 0; j < edgeList.size(); j++){
            Edge e = edgeList.get(j);
            String src = e.getSrc();
            String dest = e.getDesst();
            int weight = e.getWieght();
            if(map.get(src) != Integer.MAX_VALUE && map.get(src) + weight < map.get(dest)){
                return true;
            }
        }
        return false;
    }
}
