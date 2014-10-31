package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * User: gtkachenko
 * Time: 21:36
 */
public class ServerInfos {
    public interface ServerListChangeListener {
        public void onChanged(Set<ServerInfo> serverInfoList);
    }
    private List<ServerListChangeListener> listeners = new ArrayList<ServerListChangeListener>();

    private Set<ServerInfo> serverInfoList = new TreeSet<ServerInfo>();

    public void addServerInfoIfNeeded(ServerInfo serverInfo) {
        serverInfoList.add(serverInfo);
        for (ServerListChangeListener listener : listeners) {
            listener.onChanged(serverInfoList);
        }
    }

    public void addListener(ServerListChangeListener listener) {
        listeners.add(listener);
    }
}
