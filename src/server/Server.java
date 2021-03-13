package server;

import node.IP;
import node.Node;

public abstract class Server implements Node {
    private int id;
    private IP ip;
    private String username;
    private String password;
    private OS os;
    private int cpu;
    private int ram;

    /**
     * ToDo: Get Server from id and create object
     */
    public Server(int id) {

    }

    public Server(OS os, int cpu, int ram) {
        this.os = os;
        this.cpu = cpu;
        this.ram = ram;
    }

    public int getId() {
        return id;
    }

    public int getRam() {
        return ram;
    }

    public void setRam(int ram) {
        this.ram = ram;
    }

    public int getCpu() {
        return cpu;
    }

    public void setCpu(int cpu) {
        this.cpu = cpu;
    }

    public OS getOs() {
        return os;
    }

    public void setOs(OS os) {
        this.os = os;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
