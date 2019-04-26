import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Client {
    public static void main(String[] args){
        ServerCoordinator server = new ServerCoordinator();
        // 5 routers

        for(int i = 0; i < 5; i++) {
            try {
                server.registerRouter("router" + i);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }


        for(int i = 0; i < 15; i++){
            try {
                server.registerNode("node" + i);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        try{

            Registry registry = LocateRegistry.getRegistry(1099);
            NodeInterface nodeInterface0 = (NodeInterface) registry.lookup("node0");
            NodeInterface nodeInterface1 = (NodeInterface) registry.lookup("node1");
            nodeInterface0.refreshUsersList();
            nodeInterface1.refreshUsersList();


        }catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }


    }
}
