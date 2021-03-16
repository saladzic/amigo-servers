package storage;

import util.exception.ResourceException;
import util.exception.ServerException;
import node.Node;
import server.Server;

public class Storage implements Node {
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
