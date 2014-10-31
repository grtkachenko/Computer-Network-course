package model;

import java.util.Set;
import java.util.TreeSet;

/**
 * User: gtkachenko
 * Time: 21:36
 */
public class ServerInfos {
    private Set<ServerInfo> serverInfoList = new TreeSet<ServerInfo>();

    public void addServerInfoIfNeeded(ServerInfo serverInfo) {
        serverInfoList.add(serverInfo);
    }

}
