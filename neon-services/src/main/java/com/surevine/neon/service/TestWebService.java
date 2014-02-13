package com.surevine.neon.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.HashMap;
import java.util.Map;

@Path("/testweb/")
@Produces("application/json")
public class TestWebService {
    @GET
    public Map<String,String> test() {
        Map<String,String> testMap = new HashMap<>();
        testMap.put("hello","world");
        return testMap;
    }
}