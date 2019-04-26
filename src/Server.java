import java.io.Serializable;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Server implements ServerInterface, Runnable, Serializable {

    List<String> routerList;
    List<String> members;
    int port;
    Registry registry;
    String name;
    int routerPort;
    int nodePort;
    int index;
    int client;
    boolean leader;
    long id;
    String leaderName;
    String twophaserouter;
    String twophasenode;
    public Server(int port) {
        routerList = new ArrayList<>();
        members = new ArrayList<>();
        name = "server" + port;
        this.port = port;
        this.leader = false;
        this.client = 0;
        this.index = 0;
        this.routerPort = 8000;
        this.nodePort = 8080;
        this.leaderName = "";

        id = System.currentTimeMillis();

        try {

            registry = LocateRegistry.getRegistry(1099);
            ServerInterface stub = (ServerInterface) UnicastRemoteObject.exportObject(this, port);
            registry.rebind(name, stub);



        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        long cur = System.currentTimeMillis();
        while(System.currentTimeMillis() - cur > 20000){
            broadcast();
            cur = System.currentTimeMillis();
        }
    }

    @Override
    public boolean receiveBroadcast(){
        return true;
    }


    public boolean broadcast(){
        for(String name : members){
            try {
                registry = LocateRegistry.getRegistry(1099);
                ServerInterface serverInterface = (ServerInterface) registry.lookup(name);
                boolean back = serverInterface.receiveBroadcast();
                if(this.leaderName.equals(name)){
                    this.bullyelect();
                    return false;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }

        }
        return true;

    }
    @Override
    public boolean registerRouter(String name) throws RemoteException{
        Router router = new Router(name, routerPort++);
        registry = LocateRegistry.getRegistry(1099);

        Node node = new Node("dummy");
        this.prepare(name, "");
        this.commit();
        routerList.add(name);
        return true;
    }


    public boolean registerNode(String name){

        this.client++;
        try {
            registry = LocateRegistry.getRegistry(1099);
            int size = this.routerList.size();
            String router = routerList.get(index++ % size);
            Node node = new Node(name, router, nodePort++);
            this.prepare(router, name);
            this.commit();

            RouterInterface routerInterface = (RouterInterface) registry.lookup(router);
            routerInterface.registerUser(name);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public void prepare(String routername, String nodename){
        Set<String> visited = new HashSet<>();
        for(String n : members){
            if(n.equals(this.name)) continue;
            try {
                registry = LocateRegistry.getRegistry(1099);
                ServerInterface serverInterface = (ServerInterface) registry.lookup(n);


                boolean back = serverInterface.receivePrepare(routername, nodename);

                if(back){
                    visited.add(n);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();

            }
        }
        if(visited.size() == members.size() - 1){
            commit();
        }else{
            prepare(routername, nodename);
        }
    }

//    @Override
//    public void prepare(Router router, Node node){
//        Set<String> visited = new HashSet<>();
//        for(String n : members){
//            if(n.equals(this.name)) continue;
//            try {
//                registry = LocateRegistry.getRegistry(1099);
//                ServerInterface serverInterface = (ServerInterface) registry.lookup(n);
//                System.out.println(router);
//                System.out.println(node);
//                boolean back = serverInterface.receivePrepare(router, node);
//
//                if(back){
//                    visited.add(n);
//                }
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            } catch (NotBoundException e) {
//                e.printStackTrace();
//
//            }
//        }
//        if(visited.size() == members.size() - 1){
//            commit();
//        }else{
//            prepare(router, node);
//        }
//    }



    @Override
    public boolean receivePrepare(String router, String node)  {
        System.out.println(router);
        System.out.println(node);
        this.twophasenode = node;
        this.twophaserouter = router;

        return true;
    }

    @Override
    public void commit()  {
        Set<String> visited = new HashSet<>();
        for(String n : members){
            if(n.equals(this.name)) continue;
            try {
                registry = LocateRegistry.getRegistry(1099);
                ServerInterface serverInterface = (ServerInterface) registry.lookup(n);
                boolean back = serverInterface.receiveCommit();
                if(back){
                    visited.add(n);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }

        }
        if(visited.size() == members.size() - 1){
            return;
        }else{
            commit();
        }
    }

    @Override
    public boolean receiveCommit() {

        if(this.twophasenode.equals("")){
            //register node
            int size = routerList.size();

            System.out.println(twophaserouter);
            try {
                RouterInterface routerInterface = (RouterInterface) registry.lookup(this.twophaserouter);
                routerInterface.registerUser(this.twophasenode);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        }else {
            this.routerList.add(this.twophaserouter);

        }
        return true;
    }



    public void bullyelect() {

        for(String name : members){

            System.out.println(name);
            try {
                registry = LocateRegistry.getRegistry(1099);
                ServerInterface serverInterface = (ServerInterface) registry.lookup(name);
                if(serverInterface.sendId()){
                    serverInterface.leaderNotify(name);
                    break;
                }
            } catch (NotBoundException e) {
                e.printStackTrace();
            } catch (AccessException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void updateLeader(String name){
        this.leaderName = name;
    }

    @Override
    public boolean sendId() {
        Set<String> visited = new HashSet<>();
        System.out.println("230");
        System.out.println(members);
        for(String name : members){
            if(name.equals(this.name)) continue;
            try {
                registry = LocateRegistry.getRegistry(1099);
                ServerInterface serverInterface = (ServerInterface) registry.lookup(name);
                boolean back = serverInterface.receiveId(this.id);
                if(back){
                    System.out.println(name);
                    visited.add(name);
                }
            } catch (NotBoundException e) {
                e.printStackTrace();
            } catch (AccessException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        System.out.println(visited);
        if(visited.size() == members.size() - 1){
            System.out.println("expected 248");
            return true;
        }
        return false;
    }

    @Override
    public boolean receiveId(long id) {
        if(id < this.id){
            return true;
        }
        return false;
    }

    @Override
    public void leaderNotify(String name) {
        this.leaderName = name;
        this.leader = true;
        for(String m : members){
            if(!m.equals(name)){
                try {
                    registry = LocateRegistry.getRegistry(1099);
                    ServerInterface serverInterface = (ServerInterface) registry.lookup(m);
                    serverInterface.updateLeader(name);
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (NotBoundException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
}
