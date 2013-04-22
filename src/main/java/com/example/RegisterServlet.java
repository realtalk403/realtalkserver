package com.example;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class RegisterServlet extends BaseServlet {
    private static final String PARAMETER_REG_ID = "regId";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        String regId = getParameter(req, PARAMETER_REG_ID);
        Datastore.register(regId);
        setSuccess(resp);
    }
}
