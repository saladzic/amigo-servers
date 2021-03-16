package server;

import util.exception.ResourceException;
import util.exception.ServerException;

public class Gameserver extends Server {
    public Gameserver(int id) {
        super(id);
    }

    public Gameserver(OS os, int cpu, int ram) {
        super(os, cpu, ram);
    }

    @Override
    public Server create() throws ResourceException {
        return null;
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
}
