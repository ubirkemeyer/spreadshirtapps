package net.sprd.sampleapps.common.mgmt;

import net.sprd.sampleapps.common.dataaccess.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * @author mbs
 */
public class AppManager extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(AppManager.class);

    @Override
    public void init()
            throws ServletException {
        log.info("Initializing application ...");
        DataService.getInstance().init();
    }

    @Override
    public void destroy() {
        log.info("Destroying application ...");
        DataService.getInstance().destroy();
    }
}
