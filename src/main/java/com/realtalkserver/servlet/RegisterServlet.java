package com.realtalkserver.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.realtalkserver.util.Datastore;

@SuppressWarnings("serial")
public class RegisterServlet extends BaseServlet {
    private static final String PARAMETER_REG_ID = "PARAMETER_REG_ID";
    private static final String PARAMETER_USER = "PARAMETER_USER";
    private static final String PARAMETER_PWORD = "PARAMENTER_PWORD";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        String stRegId = getParameter(req, PARAMETER_REG_ID);
        String stUser = getParameter(req, PARAMETER_USER);
        String stPwd = getParameter(req, PARAMETER_PWORD);
        Datastore.register(stRegId);
        setSuccess(resp);
    }
}
