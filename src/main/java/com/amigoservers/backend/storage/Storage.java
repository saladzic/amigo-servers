package com.amigoservers.backend.storage;

import com.amigoservers.backend.node.Node;
import com.amigoservers.backend.server.Server;
import com.amigoservers.backend.util.exception.ResourceException;
import com.amigoservers.backend.util.exception.ServerException;

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
