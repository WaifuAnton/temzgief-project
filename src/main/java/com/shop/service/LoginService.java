package com.shop.service;

import com.shop.dao.UserDao;
import com.shop.dao.factory.DaoFactory;
import com.shop.entity.User;
import com.shop.security.UserSecurity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;

public class LoginService implements Service {
    private final Logger logger = LogManager.getLogger(LoginService.class);

    private final UserDao userDao = DaoFactory.getDaoFactory().getUserDao();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        try {
            Optional<User> optionalUser;
            if ((optionalUser = userDao.getByEmail(email)).isPresent()) {
                User user = optionalUser.get();
                String salt = user.getSalt();
                byte[] hash = user.getPasswordHash();
                byte[] expected = UserSecurity.hashPassword(password, salt);
                if (!Arrays.equals(expected, hash)) {
                    request.setAttribute("wrongEmailOrPassword", "Wrong email or password");
                    return "login.jsp";
                }
            }
            else {
                request.setAttribute("wrongEmailOrPassword", "Wrong email or password");
                return "login.jsp";
            }
            return "login.jsp";
        } catch (SQLException throwables) {
            logger.error("Unpredictable SQL exception has occurred", throwables);
            response.setStatus(500);
            return null;
        }
    }
}
