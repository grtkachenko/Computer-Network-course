package model;

import utils.Utils;

/**
 * User: gtkachenko
 * Date: 31/10/14
 */
public class ServerInfo implements Comparable<ServerInfo> {

    private final byte[] ip;
    private final String serverName;
    private int filesCount;
    private long lastModificationTimestamp;

    public ServerInfo(byte[] ip, String serverName, int filesCount, long lastModificationTimestamp) {
        this.ip = ip;
        this.serverName = serverName;
        this.filesCount = filesCount;
        this.lastModificationTimestamp = lastModificationTimestamp;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(serverName + " : ");
        stringBuilder.append(Utils.bytesIpToString(ip));
        stringBuilder.append("; files : " + filesCount);
        stringBuilder.append("; lastModification : " + Utils.convertLongTimeToString(lastModificationTimestamp));
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof ServerInfo) && (Utils.bytesIpToString(((ServerInfo) o).ip).equals(Utils.bytesIpToString(ip)));
    }

    @Override
    public int compareTo(ServerInfo serverInfo) {
        return serverName.compareTo(serverInfo.serverName);
    }
}
