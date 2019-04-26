import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RouterInterface extends Remote {
    public void sendMessage(String sender, String receiver, String msg) throws RemoteException;
    public void propagateMessage(String sender, String receiver, int index, List<String> path, String msg) throws RemoteException;
    public void broadcastMessage(String sender, String msg) throws RemoteException;
    public boolean registerUser(String user) throws RemoteException;
    public void receiveandpropogate(String user) throws RemoteException;
    public List<String>  getUsers() throws RemoteException;
    public void getmembers() throws RemoteException;

}
