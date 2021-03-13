package server;

import exception.ResourceException;
import exception.ServiceNotFoundException;

public class Order {
    private String id;
    private ServerType type;
    private OS os;
    private int cpu;
    private int ram;

    public Server fulfillOrder() throws ServiceNotFoundException {
        try {
            if (type == ServerType.VPS) {
                // Ordered server is a VPS
                VPS vps = new VPS(os, cpu, ram);
                return vps.create();
            } else if (type == ServerType.Gameserver) {
                // Ordered server is a Gameserver
                Gameserver gameserver = new Gameserver(os, cpu, ram);
                return gameserver.create();
            }
        } catch (ResourceException e) {
            // Not enough resources are available ->
            //      reserve capacity and create warn log for user
        }
        throw new ServiceNotFoundException("Service could not be found.");
    }
}
