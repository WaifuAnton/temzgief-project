package com.shop.service;

import com.shop.config.Constants;
import com.shop.dao.ProductDao;
import com.shop.dao.factory.DaoFactory;
import com.shop.entity.Product;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

public class ProductsWomenService implements Service {
    private static final Logger logger = LogManager.getLogger(ProductsWomenService.class);

    private final ProductDao productDao = DaoFactory.getDaoFactory().getProductDao();

    @Override
    public String execute(HttpServletRequest request) {
        int page;
        HttpSession session = request.getSession();
        String sort = (String) session.getAttribute("sort");
        if (request.getParameter("sort") == null && sort == null) {
            sort = "name";
            session.setAttribute("sort", sort);
        }
        else if (request.getParameter("sort") != null && sort == null) {
            sort = request.getParameter("sort");
            session.setAttribute("sort", sort);
        }
        else if (request.getParameter("sort") != null && !request.getParameter("sort").equals(sort)) {
            sort = request.getParameter("sort");
            session.setAttribute("sort", sort);
        }
        String pageParam = request.getParameter("page");
        String order = (String) session.getAttribute("order");
        if (request.getParameter("order") == null && order == null) {
            order = "asc";
            session.setAttribute("order", "asc");
        }
        else if (request.getParameter("order") != null && order == null) {
            order = request.getParameter("order");
            session.setAttribute("order", order);
        }
        else if (request.getParameter("order") != null && !request.getParameter("order").equals(order)) {
            order = request.getParameter("order");
            session.setAttribute("order", order);
        }
        if (pageParam == null)
            page = 1;
        else
            page = Integer.parseInt(pageParam);
        request.setAttribute("page", page);
        long pagesCount;
        try {
            pagesCount = (long) Math.ceil((double) productDao.count() / Constants.PRODUCT_LIMIT);
            request.setAttribute("pagesCount", pagesCount);
            List<Product> products;
            products = productDao.findLimitedByCategoryNameSortBy("women", sort, "desc".equals(order), (page - 1) * Constants.PRODUCT_LIMIT);
            request.setAttribute("productsWomen", products);
            return "women.jsp";
        }
        catch (SQLException throwables) {
            logger.error("Unpredictable SQL exception has occurred", throwables);
            return null;
        }
    }
}
