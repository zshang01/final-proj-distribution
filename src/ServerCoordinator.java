import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class ServerCoordinator {
    List<Server> serverList;
    ServerInterface serverInterface;
    Registry registry;
    int routerPort = 8000;
    public ServerCoordinator() {
        this.serverList = new ArrayList<>();
        List<String> names = new ArrayList<>();
        int start = 3000;
        try {
            registry = LocateRegistry.createRegistry(1099);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < 5; i++){
            Server server = new Server(start + i);
            serverList.add(server);
            names.add(server.name);
        }
        for(int i = 0; i < 5; i++){
            serverList.get(i).setMembers(names);
        }
        serverList.get(0).bullyelect();
        String leaderName = serverList.get(0).leaderName;
        System.out.println(leaderName);
        try {
            registry = LocateRegistry.getRegistry(1099);
            serverInterface = (ServerInterface) registry.lookup(leaderName);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }


    }

    public boolean registerRouter(String name) throws RemoteException{

        try {
            serverInterface.registerRouter(name);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return true;
    }


    public boolean registerNode(String name) throws RemoteException{


        try {
            serverInterface.registerNode(name);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return true;
    }



}
