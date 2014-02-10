package com.surevine.neon;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;

public class TestWebService extends ServletBase {

    protected JSONObject get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	JSONObject rV = new JSONObject();
    	rV.put("hello", "world");
    	return rV;
    }

}
