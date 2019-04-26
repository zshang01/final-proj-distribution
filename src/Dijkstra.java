import java.util.*;

class Wrapper{
    String src;
    String dest;
    int weight;

    public Wrapper(String src, String dest, int weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }
}

public class Dijkstra {
    Graph graph;

    Set<String> visited;

    List<String> vs;
    List<Edge> es;
    Map<String, Integer> distance;
    Map<String, Map<String, Integer>> costs;
    Map<String, List<String>> map;
    Map<String, String> pres;

    public Dijkstra(Graph graph) {
        this.graph = graph;
        visited = new HashSet<>();
        this.vs = graph.getVertex();
        this.es = graph.getEdges();
        this.distance = new HashMap<>();
        this.map = graph.getMap();
        this.costs = graph.getCosts();
        pres = new HashMap<>();
        fill();
    }

    private void fill(){
        for(String s : vs){
            distance.put(s, Integer.MAX_VALUE);
        }
    }

    public void find(String src){

        distance.put(src, 0);
        PriorityQueue<Wrapper> pq = new PriorityQueue<>(new Comparator<Wrapper>() {
            @Override
            public int compare(Wrapper o1, Wrapper o2) {
                return o1.weight - o2.weight;
            }
        });
        System.out.println(map);
        List<String> starts = map.get(src);
        System.out.println(src);
        System.out.println(costs);
        for(String s : starts){
            Wrapper wrapper = new Wrapper(src, s, costs.get(src).get(s));
            pq.add(wrapper);
        }
        visited.add(src);
        while(!pq.isEmpty()){
            Wrapper cur = pq.poll();
            String s = cur.src;
            String dest = cur.dest;
            int cost = cur.weight;
            if(visited.add(dest)){
                System.out.println(dest);
                pres.put(dest, s);
                distance.put(dest, cost);
                for(String next : map.get(dest)){
                    Wrapper wrapper = new Wrapper(dest, next, cost + costs.get(dest).get(next));
                    pq.add(wrapper);
                }
            }
        }
    }



    public List<String> getPath(String dest){
        List<String> path = new ArrayList<>();
        while(pres.get(dest) != null){
            path.add(dest);
            dest = pres.get(dest);
        }
        Collections.reverse(path);
        return path;
    }
}
