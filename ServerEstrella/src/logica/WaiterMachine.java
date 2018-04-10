package logica;

import java.net.InetAddress;

public class WaiterMachine {

    private String status ;
    private String name;
    private int id;
    private InetAddress ipAddress;
    private int listeningPort;

    public WaiterMachine(String status, String name, int id) {
        this.status = status;
        this.name = name;
        this.id = id;
    }

    public WaiterMachine(String status, String name, int id, InetAddress ipAddress, int listeningPort) {
        this.status = status;
        this.name = name;
        this.id = id;
        this.ipAddress = ipAddress;
        this.listeningPort = listeningPort;
    }

    public int getListeningPort() {
        return listeningPort;
    }

    public void setListeningPort(int listeningPort) {
        this.listeningPort = listeningPort;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
