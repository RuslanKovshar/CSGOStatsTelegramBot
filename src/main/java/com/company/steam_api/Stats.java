package com.company.steam_api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Stats {

    @JsonProperty("name")
    private String name;

    @JsonProperty("value")
    private Long value;

    public String getName() {
        return name;
    }

    public Long getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "com.company.steam_api.Stats{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
