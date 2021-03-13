package node;

public class IP {
    private final String type;
    private final String ip;

    public IP(String type, String ip) {
        this.type = type;
        this.ip = ip;
    }

    public String getType() {
        return type;
    }

    public String getIp() {
        return ip;
    }
}
