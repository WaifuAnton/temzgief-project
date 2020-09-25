package com.shop.service;

import com.shop.config.Constants;
import com.shop.dao.ProductDao;
import com.shop.dao.factory.DaoFactory;
import com.shop.entity.Product;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

public class ProductsWomenService implements Service {
    private static final Logger logger = LogManager.getLogger(ProductsWomenService.class);

    private final ProductDao productDao = DaoFactory.getDaoFactory().getProductDao();

    @Override
    public String execute(HttpServletRequest request) {
        int page;
        String pageParam = request.getParameter("page");
        if (pageParam == null)
            page = 1;
        else
            page = Integer.parseInt(pageParam);
        long pagesCount;
        try {
            Long pagesReq = (Long) request.getAttribute("pagesCount");
            if (pagesReq == null) {
                pagesCount = (long) Math.ceil((double) productDao.count() / Constants.PRODUCT_LIMIT);
                request.setAttribute("pagesCount", pagesCount);
            }
            else
                pagesCount = pagesReq;
            List<Product> products = productDao.findLimitedByCategoryName("women", page - 1);
            request.setAttribute("productsLim", products);
            return "women.jsp";
        }
        catch (SQLException throwables) {
            logger.error("Unpredictable SQL exception has occurred", throwables);
            return null;
        }
    }
}
