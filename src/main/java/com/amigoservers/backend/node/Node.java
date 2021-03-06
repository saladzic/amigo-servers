package com.amigoservers.backend.node;

import com.amigoservers.backend.server.Server;
import com.amigoservers.backend.util.exception.ResourceException;
import com.amigoservers.backend.util.exception.ServerException;

public interface Node {
    /**
     * Creates a new server
     * @return a new server
     * @throws ResourceException if no resources found for creating a new server
     */
    Server create() throws ResourceException;

    /**
     * Delete a node
     */
    void delete();

    /**
     * Starts the node
     * @throws ServerException if server could not be started
     */
    void start() throws ServerException;

    /**
     * Stops the service
     * @throws ServerException if server could not be stopped
     */
    void stop() throws ServerException;
}
