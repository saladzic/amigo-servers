package com.amigoservers.backend.server;

import com.amigoservers.backend.log.SystemLog;
import com.amigoservers.backend.log.UserLog;
import com.amigoservers.backend.util.exception.ResourceException;
import com.amigoservers.backend.util.exception.ServiceNotFoundException;

public class OrderItem extends Order {
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
            UserLog userLog = new UserLog();
            userLog.error(getUser(), "no_resources", "no_resources_available");
            SystemLog systemLog = new SystemLog();
            systemLog.error("no_resources", "no_resources_available_system");
        }
        throw new ServiceNotFoundException("Service could not be found.");
    }
}
