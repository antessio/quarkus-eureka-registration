package io.quarkus.eureka.client;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.Base64;

@ApplicationScoped
public class EurekaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EurekaService.class);

    @Inject
    @ConfigProperty(name = "eureka.client.username")
    String eurekaClientUsername;

    @Inject
    @ConfigProperty(name = "eureka.client.password")
    String eurekaClientPassword;

    @Inject
    @RestClient
    EurekaClient eurekaClient;



    public void heartBeat(String appname, String instanceId){
        Response.Status responseStatus = eurekaClient.heartBeat(getAuthorization(),appname,instanceId);
        if(responseStatus.getStatusCode()==404){
            throw new RuntimeException("Instance "+instanceId+" with appName "+appname+" not registered");
        }else if(responseStatus.getStatusCode()==500){
            throw new RuntimeException("Instance "+instanceId+" with appName "+appname+" failure");
        }
    }
    public String register(Instance instanceInfo){
        RegistrationDTO registrationDTO = new RegistrationDTO(instanceInfo);
        Entity<?> registrationDTOEntity = Entity.json(registrationDTO);
        LOGGER.info("Entity {}", registrationDTOEntity.toString());
        JsonbConfig config = new JsonbConfig()
                .withAdapters(new DataCenterInfoJsonbAdapter(), new PortWrapperJsonbAdapter());
        Jsonb jsonb = JsonbBuilder.create(config);
        String authorization = getAuthorization();
        Response.Status responseStatus = eurekaClient.register(authorization,instanceInfo.getApp(),jsonb.toJson(registrationDTO));
        if(responseStatus.getStatusCode()==204){
            return instanceInfo.getInstanceId();
        }else {
            return null;
        }
    }

    private String getAuthorization() {
        return "Basic "+Base64.getEncoder().encodeToString((eurekaClientUsername+":"+eurekaClientPassword).getBytes());
    }

    public void cancel(String appname, String instanceId) {
        eurekaClient.cancel(getAuthorization(),appname,instanceId);
    }
}
