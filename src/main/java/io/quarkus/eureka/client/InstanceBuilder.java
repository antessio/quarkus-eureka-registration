package io.quarkus.eureka.client;

import java.util.Map;

public class InstanceBuilder {

    private String hostName;
    private String instanceId;
    private String app;
    private String vipAddress;
    private String secureVipAddress;
    private String ipAddr;
    private String status;
    private Integer port;
    private Integer securePort;
    private String healthCheckUrl;
    private String statusPageUrl;
    private String homePageUrl;
    private DataCenterInfo dataCenterInfo;
    private Map<String,String> metadata;

    public InstanceBuilder hostName(String hostName) {
        this.hostName = hostName;
        return this;
    }

    public InstanceBuilder instanceId(String instanceId) {
        this.instanceId = instanceId;
        return this;
    }

    public InstanceBuilder app(String app) {
        this.app = app;
        return this;
    }

    public InstanceBuilder vipAddress(String vipAddress) {
        this.vipAddress = vipAddress;
        return this;
    }

    public InstanceBuilder secureVipAddress(String secureVipAddress) {
        this.secureVipAddress = secureVipAddress;
        return this;
    }

    public InstanceBuilder ipAddr(String ipAddr) {
        this.ipAddr = ipAddr;
        return this;
    }

    public InstanceBuilder status(String status) {
        this.status = status;
        return this;
    }

    public InstanceBuilder port(Integer port) {
        this.port = port;
        return this;
    }

    public InstanceBuilder securePort(Integer securePort) {
        this.securePort = securePort;
        return this;
    }

    public InstanceBuilder healthCheckUrl(String healthCheckUrl) {
        this.healthCheckUrl = healthCheckUrl;
        return this;
    }

    public InstanceBuilder statusPageUrl(String statusPageUrl) {
        this.statusPageUrl = statusPageUrl;
        return this;
    }

    public InstanceBuilder homePageUrl(String homePageUrl) {
        this.homePageUrl = homePageUrl;
        return this;
    }

    public InstanceBuilder dataCenterInfo(DataCenterInfo dataCenterInfo) {
        this.dataCenterInfo = dataCenterInfo;
        return this;
    }

    public InstanceBuilder metadata(Map<String,String> metadata){
        this.metadata = metadata;
        return this;
    }

    public Instance createInstance() {
        Instance instance = new Instance(hostName, instanceId, app, vipAddress, secureVipAddress, ipAddr, status, port, securePort, healthCheckUrl, statusPageUrl, homePageUrl,dataCenterInfo);
        if(metadata!=null){
            instance.setMetadata(metadata);
        }
        return instance;
    }
}