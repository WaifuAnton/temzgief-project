package com.shop.service;

import com.shop.dao.UserDao;
import com.shop.dao.factory.DaoFactory;
import com.shop.entity.User;
import com.shop.security.UserSecurity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class RegisterService implements Service {
    private static final Logger logger = LogManager.getLogger(RegisterService.class);

    private final UserDao userDao = DaoFactory.getDaoFactory().getUserDao();

    @Override
    public String execute(HttpServletRequest request) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        try {
            User user = new User();
            user.setEmail(email);
            String salt = UserSecurity.generateSalt();
            user.setSalt(salt);
            user.setPasswordHash(UserSecurity.hashPassword(password, salt));
            userDao.insert(user);
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            return "index.jsp";
        }
        catch (SQLIntegrityConstraintViolationException e) {
            request.setAttribute("notAdded", "User already exists");
            return "controller?action=main";
        }
        catch (SQLException throwables) {
            logger.error("Unpredictable SQL exception has occurred", throwables);
            return null;
        }
    }
}
