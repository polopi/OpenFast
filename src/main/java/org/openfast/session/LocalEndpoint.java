/*
The contents of this file are subject to the Mozilla Public License
Version 1.1 (the "License"); you may not use this file except in
compliance with the License. You may obtain a copy of the License at
http://www.mozilla.org/MPL/

Software distributed under the License is distributed on an "AS IS"
basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
License for the specific language governing rights and limitations
under the License.

The Original Code is OpenFAST.

The Initial Developer of the Original Code is The LaSalle Technology
Group, LLC.  Portions created by The LaSalle Technology Group, LLC
are Copyright (C) The LaSalle Technology Group, LLC. All Rights Reserved.

Contributor(s): Jacob Northey <jacob@lasalletech.com>
                Craig Otis <cotis@lasalletech.com>
 */
package org.openfast.session;

import java.util.ArrayList;
import java.util.List;

public class LocalEndpoint implements Endpoint {
    private LocalEndpoint server;
    private ConnectionListener listener;
    private List<LocalConnection> connections;

    public LocalEndpoint() {
        connections = new ArrayList<>(3);
    }
    public LocalEndpoint(LocalEndpoint server) {
        this.server = server;
    }
    @Override
	public void accept() throws FastConnectionException {
        if (!connections.isEmpty()) {
            synchronized (this) {
                Connection connection = (Connection) connections.remove(0);
                listener.onConnect(connection);
            }
        }
    }
    @Override
	public Connection connect() throws FastConnectionException {
        LocalConnection localConnection = new LocalConnection(server, this);
        LocalConnection remoteConnection = new LocalConnection(localConnection);
        synchronized (server) {
            server.connections.add(remoteConnection);
        }
        return localConnection;
    }
    @Override
	public void setConnectionListener(ConnectionListener listener) {
        this.listener = listener;
    }
    @Override
	public void close() {}
}
