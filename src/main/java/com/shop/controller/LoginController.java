package com.shop.controller;

import com.shop.config.Constants;
import com.shop.service.Service;
import com.shop.service.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Service service = ServiceFactory.getService(Constants.LOGIN);
        service.execute(req, resp);
    }
}
