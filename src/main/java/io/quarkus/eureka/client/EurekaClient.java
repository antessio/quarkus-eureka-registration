package io.quarkus.eureka.client;


import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;


@Path("/")
@RegisterRestClient
public interface EurekaClient {

    @POST
    @Path("/apps/{appId}")
    @Produces("application/json")
    @Consumes("application/json")
    Response.Status register(@HeaderParam("Authorization") String authorization, @PathParam("appId") String appId, String registrationDTO);

    @PUT
    @Path("/apps/{appId}/{instanceId}")
    @Produces("application/json")
    @Consumes("application/json")
    Response.Status heartBeat(@HeaderParam("Authorization") String authorization, @PathParam("appId") String appId, @PathParam("instanceId") String instanceId);

    @DELETE
    @Path("/apps/{appId}/{instanceId}")
    @Produces("application/json")
    @Consumes("application/json")
    void cancel(@HeaderParam("Authorization") String authorization, @PathParam("appId") String appId, @PathParam("instanceId") String instanceId);
}
