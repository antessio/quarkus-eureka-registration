package io.quarkus.eureka.client;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.bind.adapter.JsonbAdapter;

public class PortWrapperJsonbAdapter implements JsonbAdapter<Instance.PortWrapper, JsonObject> {

    /*
    "dataCenterInfo": {
            "@class": "com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo",
            "name": "MyOwn"
        },
     */
    @Override
    public JsonObject adaptToJson(Instance.PortWrapper port) throws Exception {

        return Json.createObjectBuilder()
                .add("@enabled",port.isEnabled())
                .add("$", port.getPort())
                .build();
    }

    @Override
    public Instance.PortWrapper adaptFromJson(JsonObject jsonObject) throws Exception {
        return new Instance.PortWrapper(jsonObject.getBoolean("@enabled"),jsonObject.getInt("$"));
    }
}
