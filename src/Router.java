import java.io.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Router implements RouterInterface, Serializable {


    String name;
    // connect to other Router
    List<Link> neighbors = new ArrayList<>();

    List<String> nodes = new ArrayList<>();
    List<Edge> edges = new ArrayList<>();

    Graph g = new Graph(nodes, edges);

    List<String> users = new ArrayList<>();
    Set<String> alluser = new HashSet<>();
    int port;

    Registry registry;
    public Router(String name, int port) throws RemoteException{

        this.name = name;
        this.port = port;
        String fileName = "router-graph.txt";
        String line = null;
        List<String> vertexList = new ArrayList<>();
        List<Edge> edgeList = new ArrayList<>();

        try {
            registry = LocateRegistry.getRegistry(1099);
            RouterInterface stub = (RouterInterface) UnicastRemoteObject.exportObject(this, port);
            registry.rebind(name, stub);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try{
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            Set<String> vertexs = new HashSet<>();
            while((line = bufferedReader.readLine()) != null) {
                String[] st = line.split(" ");
                System.out.println(st);
                Vertex v1 = new Vertex(st[1]);
                Vertex v2 = new Vertex(st[2]);
                Edge e = new Edge(st[1], st[2], Integer.parseInt(st[3]));
                if(vertexs.add(st[1])){
                    vertexList.add(st[1]);
                }
                if(vertexs.add(st[2])){
                    vertexList.add(st[2]);
                }
                edgeList.add(e);
            }

            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.g = new Graph(vertexList, edgeList);

        //Dijkstra dijkstra = new Dijkstra(this.g);
        System.out.println(this.name);
        //dijkstra.find(this.name);
        //System.out.println(dijkstra.distance);

    }

    public void fillNeighbors(String linkname, String destRouter, int weight){
        Link link = new Link(linkname, this, destRouter, weight);
        System.out.println(link);
        this.neighbors.add(link);
        this.g.updateGraph(this, link);
    }

    public boolean registerUser(String name){
        Kruskal kruskal = new Kruskal(this.g);

        kruskal.process();
        if(users.add(name)){
            for(Edge edge : kruskal.getEdges()){
                if(edge.getSrc().equals(this.name)){
                    for(Link link : this.neighbors){
                        if(edge.getDesst().equals(link.dest)){
                            String next = link.dest;
                            try {
                                RouterInterface routerInterface = (RouterInterface) Naming.lookup(next);
                                routerInterface.receiveandpropogate(name);
                            } catch (NotBoundException e) {
                                e.printStackTrace();
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void receiveandpropogate(String incominguser){
        alluser.add(incominguser);
        Kruskal kruskal = new Kruskal(this.g);
        for(String user : users){
            String url = "rmi://localhost/" + user;

            try {
                NodeInterface nodeInterface = (NodeInterface) Naming.lookup(url);
                nodeInterface.refreshUsersList();
            } catch (NotBoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        for(Edge edge : kruskal.getEdges()){
            String src = edge.getSrc();
            String dest = edge.getDesst();
            if(src.equals(this.name)){
                for(Link link : this.neighbors){
                    String linkdest = link.getDest();
                    if(dest.equals(linkdest)){
                        try {
                            RouterInterface routerInterface = (RouterInterface) Naming.lookup(linkdest);
                            routerInterface.receiveandpropogate(incominguser);
                        } catch (NotBoundException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void broadcastMessage(String sender, String msg)  {
        Kruskal kruskal = new Kruskal(this.g);
        String url;
        for(String user : users){
            url = "rmi://localhost/" + user;
            try{
                NodeInterface stub = (NodeInterface) Naming.lookup(url);
                stub.displayMessage(sender, msg);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        for(Edge edge : kruskal.getEdges()){
            String src = edge.getSrc();
            String dest = edge.getDesst();
            url = "rmi://localhost/";
            if(this.name.equals(src)){
                for(Link link : neighbors){
                    String linksrc = link.src.name;
                    String linkdest = link.dest;
                    if(dest.equals(linkdest)){

                        try {
                            RouterInterface routerInterface = null;
                            try {
                                routerInterface = (RouterInterface) Naming.lookup(url + dest);
                            } catch (NotBoundException e) {
                                e.printStackTrace();
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                            routerInterface.broadcastMessage(sender, msg);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }

    }
    @Override
    public void sendMessage(String sender, String receiver, String msg) {
        Dijkstra dijkstra = new Dijkstra(this.g);
        dijkstra.find(this.name);
        List<String> path = dijkstra.getPath(receiver);

        propagateMessage(sender, receiver, 0, path, msg);

    }

    @Override
    public void propagateMessage(String sender, String receiver, int index, List<String> path, String msg) {
        if(index == path.size()  - 1){
            String name = path.get(index);
            String url = url = "rmi://localhost/" + receiver;
            try {
                NodeInterface stub = (NodeInterface) Naming.lookup(url);
                stub.displayMessage(sender, msg);
            } catch (NotBoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }else{
            String name = path.get(index);
            for(Link link : this.neighbors){
                if(link.dest.equals(name)){
                    try {
                        RouterInterface routerInterface = (RouterInterface) Naming.lookup(name);
                        routerInterface.propagateMessage(sender, receiver, index ++ , path, msg);
                    } catch (NotBoundException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public List<String> getUsers() {
        return this.users;
    }


    @Override
    public String toString() {
        return "Router{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public void getmembers() throws RemoteException {

    }
}
