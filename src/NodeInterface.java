import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NodeInterface extends Remote {
    public void displayMessage(String sender, String msg) throws RemoteException;
    public void refreshUsersList() throws RemoteException;
}
