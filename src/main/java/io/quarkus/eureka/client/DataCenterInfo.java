package io.quarkus.eureka.client;


public class DataCenterInfo {
    public enum Name {Netflix, Amazon, MyOwn};

    private final Name name;

    public DataCenterInfo(Name name) {
        this.name = name;
    }

    public Name getName(){
        return this.name;
    };

}
