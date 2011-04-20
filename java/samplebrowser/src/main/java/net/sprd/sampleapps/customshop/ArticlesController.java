package net.sprd.sampleapps.customshop;

import net.sprd.sampleapps.common.dataaccess.DataService;
import net.sprd.sampleapps.common.dataaccess.model.Article;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author mbs
 */
public class ArticlesController extends HttpServlet {
    private DataService dataAccess = DataService.getInstance();

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws ServletException, IOException {
        String uri = httpServletRequest.getRequestURI();
        if (uri.endsWith("articles")) {
            List<Article> articles = dataAccess.getArticles();
            httpServletResponse.setDateHeader("Expires", System.currentTimeMillis() + 36000000);
            httpServletRequest.setAttribute("articles", articles);
            httpServletRequest.getRequestDispatcher("/articles.jsp").forward(httpServletRequest, httpServletResponse);
        } else {
            String articleId = uri.substring(uri.lastIndexOf("/") + 1);
            Article article = dataAccess.getArticle(articleId);
            httpServletResponse.setDateHeader("Expires", System.currentTimeMillis() + 36000000);
            httpServletRequest.setAttribute("article", article);
            httpServletRequest.getRequestDispatcher("/articleDetails.jsp").forward(httpServletRequest, httpServletResponse);
        }
    }
}
