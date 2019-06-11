package io.quarkus.eureka.client;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.bind.adapter.JsonbAdapter;

public class DataCenterInfoJsonbAdapter implements JsonbAdapter<DataCenterInfo, JsonObject> {

    /*
    "dataCenterInfo": {
            "@class": "com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo",
            "name": "MyOwn"
        },
     */
    @Override
    public JsonObject adaptToJson(DataCenterInfo dataCenterInfo) throws Exception {
        String id = "com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo";
        if(dataCenterInfo.getName().equals(DataCenterInfo.Name.Amazon)){
            id = "com.netflix.appinfo.AmazonInfo";
        }
        return Json.createObjectBuilder()
                .add("@class",id)
                .add("name",dataCenterInfo.getName().name())
                .build();
    }

    @Override
    public DataCenterInfo adaptFromJson(JsonObject jsonObject) throws Exception {
        return new DataCenterInfo(DataCenterInfo.Name.valueOf(jsonObject.getString("name")));
    }
}
