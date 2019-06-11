package io.quarkus.eureka.client;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import java.util.Map;
import java.util.Objects;

public class Instance {
    private final int defaultSecurePort=443;
    private final int defaultPort=80;

    private final String hostName; //="WKS-SOF-L011",
    private final String instanceId;
    private final String app; //"com.automationrhapsody.eureka.app",
    private final String vipAddress; //"com.automationrhapsody.eureka.app",
    private final String secureVipAddress; //": "com.automationrhapsody.eureka.app"
    private final String ipAddr; //": "10.0.0.10",
    private final String status; //"STARTING",
    private final Integer port; //": {"$": "8080", "@enabled": "true"},
    private final Integer securePort; //": {"$": "8443", "@enabled": "true"},
    private final String healthCheckUrl; //": "http://WKS-SOF-L011:8080/healthcheck",
    private final String statusPageUrl; //": "http://WKS-SOF-L011:8080/status",
    private final String homePageUrl; //": "http://WKS-SOF-L011:8080",
    private final DataCenterInfo dataCenterInfo;
    private Map<String,String> metadata;

    public Instance(String hostName, String instanceId, String app, String vipAddress, String secureVipAddress, String ipAddr, String status, Integer port, Integer securePort, String healthCheckUrl, String statusPageUrl, String homePageUrl,DataCenterInfo dataCenterInfo) {
        this.hostName = hostName;
        this.instanceId = instanceId;
        this.app = app;
        this.vipAddress = vipAddress;
        this.secureVipAddress = secureVipAddress;
        this.ipAddr = ipAddr;
        this.status = status;
        this.port = port;
        this.securePort = securePort;
        this.healthCheckUrl = healthCheckUrl;
        this.statusPageUrl = statusPageUrl;
        this.homePageUrl = homePageUrl;
        this.dataCenterInfo = dataCenterInfo;
    }

    /**
     * {@link Instance} JSON and XML format for port information does not follow the usual conventions, which
     * makes its mapping complicated. This class represents the wire format for port information.
     */
    public static class PortWrapper {
        private final boolean enabled;
        private final int port;


        public PortWrapper(boolean enabled, int port) {
            this.enabled = enabled;
            this.port = port;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public int getPort() {
            return port;
        }
    }

    public String getHostName() {
        return hostName;
    }

    public String getApp() {
        return app;
    }

    public String getVipAddress() {
        return vipAddress;
    }

    public String getSecureVipAddress() {
        return secureVipAddress;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public String getStatus() {
        return status;
    }

    @JsonbTransient
    public Integer getPort() {
        return port;
    }

    @JsonbProperty("port")
    public PortWrapper getPortWrapper(){
        if(this.port!=null) {
            return new PortWrapper(true, this.port);
        }else{
            return new PortWrapper(false, defaultPort);
        }
    }
    @JsonbTransient
    public Integer getSecurePort() {
        return securePort;
    }

    @JsonbProperty("securePort")
    public PortWrapper getSecurePortWrapper(){
        if(this.securePort!=null) {
            return new PortWrapper(true, this.securePort);
        }else{
            return new PortWrapper(false, defaultSecurePort);
        }
    }
    public String getHealthCheckUrl() {
        return healthCheckUrl;
    }

    public String getStatusPageUrl() {
        return statusPageUrl;
    }

    public String getHomePageUrl() {
        return homePageUrl;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public DataCenterInfo getDataCenterInfo() {
        return dataCenterInfo;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Instance instance = (Instance) o;
        return Objects.equals(hostName, instance.hostName) &&
                Objects.equals(app, instance.app) &&
                Objects.equals(vipAddress, instance.vipAddress) &&
                Objects.equals(secureVipAddress, instance.secureVipAddress) &&
                Objects.equals(ipAddr, instance.ipAddr) &&
                Objects.equals(status, instance.status) &&
                Objects.equals(port, instance.port) &&
                Objects.equals(securePort, instance.securePort) &&
                Objects.equals(healthCheckUrl, instance.healthCheckUrl) &&
                Objects.equals(statusPageUrl, instance.statusPageUrl) &&
                Objects.equals(homePageUrl, instance.homePageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hostName, app, vipAddress, secureVipAddress, ipAddr, status, port, securePort, healthCheckUrl, statusPageUrl, homePageUrl);
    }

    @Override
    public String toString() {
        return "Instance{" +
                "hostName='" + hostName + '\'' +
                ", app='" + app + '\'' +
                ", vipAddress='" + vipAddress + '\'' +
                ", secureVipAddress='" + secureVipAddress + '\'' +
                ", ipAddr='" + ipAddr + '\'' +
                ", status='" + status + '\'' +
                ", port=" + port +
                ", securePort=" + securePort +
                ", healthCheckUrl='" + healthCheckUrl + '\'' +
                ", statusPageUrl='" + statusPageUrl + '\'' +
                ", homePageUrl='" + homePageUrl + '\'' +
                '}';
    }

    public enum InstanceStatus {
        UP, // Ready to receive traffic
        DOWN, // Do not send traffic- healthcheck callback failed
        STARTING, // Just about starting- initializations to be done - do not
        // send traffic
        OUT_OF_SERVICE, // Intentionally shutdown for traffic
        UNKNOWN;

        public static InstanceStatus toEnum(String s) {
            if (s != null) {
                try {
                    return InstanceStatus.valueOf(s.toUpperCase());
                }finally {

                }
            }
            return UNKNOWN;
        }
    }


}