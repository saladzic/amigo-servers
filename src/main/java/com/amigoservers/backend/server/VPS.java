package com.amigoservers.backend.server;

import com.amigoservers.backend.util.exception.ResourceException;
import com.amigoservers.backend.util.exception.ServerException;

public class VPS extends Server {
    private int id;
    private OS os;
    private int cpu;
    private int ram;

    /*
    ToDo: Get OS, CPU, RAM by "Server-VPS-ID"
    public VPS(int id);*/

    public VPS(OS os, int cpu, int ram) {
        super(os, cpu, ram);
        this.os = os;
        this.cpu = cpu;
        this.ram = ram;
    }

    public Server upgradeRam() throws ResourceException {
        // ToDo: Upgrade, Dowmgrade Ram and CPU
        ram++;
        return this;
    }

    @Override
    public Server create() throws ResourceException {
        /*
           Proxmox: https://github.com/Corsinvest/cv4pve-api-java
           https://pve.proxmox.com/pve-docs/api-viewer/#/nodes/{node}/qemu
        */

        /*
        this.id = ?;
        this.username = ?;
        this.password = ?;
        */
        return this;
    }

    @Override
    public void delete() {

    }

    @Override
    public void start() throws ServerException {

    }

    @Override
    public void stop() throws ServerException {

    }

    public String toString() {
        return "[VPS-Server credentials]\n" +
                "Username: " + this.getUsername() +
                "\n" +
                "Password: " + this.getPassword() +
                "\n";
    }
}
