import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Node implements NodeInterface, Serializable {
    String name;
    String connectedRouter;
    Registry registry;

    public Node(String name) throws RemoteException {
        this.name = name;
    }

    public Node(String name, String connectedRouter, int port) throws RemoteException  {
        this.name = name;
        this.connectedRouter = connectedRouter;
        try {
            registry = LocateRegistry.getRegistry(1099);
            NodeInterface stub = (NodeInterface) UnicastRemoteObject.exportObject(this, port);
            registry.rebind(name, stub);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    public String getName() {
        return name;
    }

    public String getConnectedRouter() {
        return connectedRouter;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setConnectedRouter(String connectedRouter) {
        this.connectedRouter = connectedRouter;
    }

    public boolean connectToRouter(){

        String url = null;
        url = "rmi://localhost/" + this.connectedRouter;

        try{
            RouterInterface routerInterface = (RouterInterface) Naming.lookup(url);
            return routerInterface.registerUser(this.name);
        }catch(Exception e){
           e.printStackTrace();
        }
        return false;
    }



    @Override
    public void displayMessage(String sender, String msg) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:s");
        Date date = new Date();
        System.out.println("time:" + dateFormat.format(date) + "receiver:" + this.name +  "sender:" + sender + "msg:" + msg);
    }

    @Override
    public void refreshUsersList(){
        try {
            RouterInterface routerInterface = (RouterInterface) Naming.lookup(this.connectedRouter);
            List<String> list = routerInterface.getUsers();
            System.out.println(list);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Node{" +
                "name='" + name + '\'' +
                '}';
    }
}
