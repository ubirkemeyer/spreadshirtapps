package net.sprd.sampleapps.designbrowser;

import net.sprd.sampleapps.common.dataaccess.DataService;
import net.sprd.sampleapps.common.dataaccess.model.Design;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author mbs
 */
public class DesignsController extends HttpServlet {
    private DataService dataAccess = DataService.getInstance();

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws ServletException, IOException {
        List<Design> designs = dataAccess.getDesigns();
        httpServletResponse.setDateHeader("Expires", System.currentTimeMillis() + 3600000);
        httpServletRequest.setAttribute("designs", designs);
        httpServletRequest.getRequestDispatcher("designs.jsp").forward(httpServletRequest, httpServletResponse);
    }
}