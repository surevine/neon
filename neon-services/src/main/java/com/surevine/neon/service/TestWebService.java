package com.surevine.neon.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;

import com.surevine.neon.ServletBase;

@WebServlet(value="/s/test", loadOnStartup=1)
public class TestWebService extends ServletBase {


	private static final long serialVersionUID = -1598226494736457846L;

	@SuppressWarnings("unchecked")
	protected JSONObject get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	JSONObject rV = new JSONObject();
    	rV.put("hello", "world");
    	return rV;
    }

}