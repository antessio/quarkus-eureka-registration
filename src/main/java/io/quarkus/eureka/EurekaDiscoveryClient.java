package io.quarkus.eureka;

import io.quarkus.eureka.client.DataCenterInfo;
import io.quarkus.eureka.client.EurekaService;
import io.quarkus.eureka.client.Instance;
import io.quarkus.eureka.client.InstanceBuilder;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.vertx.core.Vertx;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class EurekaDiscoveryClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(EurekaDiscoveryClient.class);

    @Inject
    Vertx vertx;

    @Inject
    @ConfigProperty(name="eureka.client.enabled")
    Boolean eurekaClientEnabled;

    @Inject
    @ConfigProperty(name = "quarkus.http.port")
    Integer httpPort;

    @Inject
    @ConfigProperty(name = "eureka.instance.appname")
    String appname;

    @Inject
    @ConfigProperty(name = "eureka.instance.leaseRenewalIntervalInSeconds")
    Long leaseRenewalIntervalInSeconds;

    @Inject
    EurekaService eurekaService;

    @Inject
    @ConfigProperty(name = "eureka.instance.healthCheckUrl")
    String healthCheckUrl;

    @Inject
    @ConfigProperty(name = "eureka.instance.statusPageUrl")
    String statusPageUrl;

    private String instanceId;


    void onShutdown(@Observes ShutdownEvent shutdownEvent){
        eurekaService.cancel(appname,instanceId);
    }

    void onStart(@Observes StartupEvent event){
        LOGGER.info("Application Started");
        if(eurekaClientEnabled){
            LOGGER.info("Eureka Client enabled. Application name is {}",appname);
            try {
                InetAddress ip = InetAddress.getLocalHost();
                String basePath = "http://"+ip.getHostAddress() + ":" + httpPort;

                Instance instance = new InstanceBuilder()
                        .app(appname)
                        .healthCheckUrl(basePath + healthCheckUrl)
                        .statusPageUrl(basePath + statusPageUrl)
                        .homePageUrl(basePath+"/")
                        .hostName(ip.getHostAddress())
                        .instanceId(appname+":"+ UUID.randomUUID().toString())
                        .ipAddr(ip.getHostAddress())
                        .port(httpPort)
                        .secureVipAddress(appname.toLowerCase())
                        .vipAddress(appname.toLowerCase())
                        .status(Instance.InstanceStatus.STARTING.name())
                        .dataCenterInfo(new DataCenterInfo(DataCenterInfo.Name.MyOwn))
                        .createInstance();
                instanceId = eurekaService.register(instance);
                LOGGER.info("Client registered with {} insance id",instanceId);
                Map<String,String> metadata = new HashMap<>();

                Instance instanceUpdated = new InstanceBuilder()
                        .app(appname)
                        .healthCheckUrl(basePath + "/management/health")
                        .statusPageUrl(basePath + "/management/info")
                        .homePageUrl(basePath+"/")
                        .hostName(ip.getHostAddress())
                        .instanceId(instanceId)
                        .ipAddr(ip.getHostAddress())
                        .port(httpPort)
                        .secureVipAddress(appname.toLowerCase())
                        .vipAddress(appname.toLowerCase())
                        .status(Instance.InstanceStatus.UP.name())
                        .dataCenterInfo(new DataCenterInfo(DataCenterInfo.Name.MyOwn))
                        .metadata(metadata)
                        .createInstance();
                eurekaService.register(instanceUpdated);

                vertx.setPeriodic(leaseRenewalIntervalInSeconds.longValue()*1000,x->{
                    LOGGER.info("Sending heartbeat");
                    eurekaService.heartBeat(appname,instanceId);
                });
            }catch(IOException e){
                LOGGER.error("Failed to load the application",e);
                throw new RuntimeException("Failed to load the application",e);
            }
        }
    }


    public String getInstanceId() {
        return instanceId;
    }


}
