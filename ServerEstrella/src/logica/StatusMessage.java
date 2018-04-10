package logica;

import java.io.Serializable;


public class StatusMessage implements Serializable {

    private String status ;
    private String machineName;
    private int machineId;
    private int machinePort;

    public StatusMessage(){
    }

    public StatusMessage(String status, String machineName, int machineId, int machinePort) {
        this.status = status;
        this.machineName = machineName;
        this.machineId = machineId;
        this.machinePort = machinePort;
    }

    public int getMachinePort() {
        return machinePort;
    }

    public void setMachinePort(int machinePort) {
        this.machinePort = machinePort;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public int getMachineId() {
        return machineId;
    }

    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}
