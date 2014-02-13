package com.surevine.neon;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class ServletBase extends HttpServlet {

        private static final long serialVersionUID = 8526209559155963116L;

        protected final void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//                resp.setContentType("application/json");
//                JSONObject result = get(req, resp);
//                if (result!=null) {
//                	PrintWriter writer = resp.getWriter();
//                	writer.println(result.toJSONString());
//                	writer.close();
//                }
        }

//        protected abstract JSONObject get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;

}