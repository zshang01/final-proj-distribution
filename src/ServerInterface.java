import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
    public boolean sendId() throws RemoteException;
    public boolean receiveBroadcast() throws RemoteException;
    public boolean receiveId(long id) throws RemoteException;
    public void updateLeader(String name) throws RemoteException;
    public void leaderNotify(String name) throws RemoteException;
    public boolean registerRouter(String name) throws RemoteException;
    public boolean registerNode(String name) throws RemoteException;

    public void prepare(String router, String node) throws RemoteException;
    public boolean receivePrepare(String router, String node) throws RemoteException;
    public void commit() throws RemoteException;
    public boolean receiveCommit() throws RemoteException;



}
