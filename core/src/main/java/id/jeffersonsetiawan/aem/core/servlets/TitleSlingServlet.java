package id.jeffersonsetiawan.aem.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Simple Demo Servlet",
//                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.resourceTypes="+ "/apps/geometrixx/components/title",
                "sling.servlet.extensions=" + "html",
                "sling.servlet.paths=/bin/company/titleservlet"
        })
public class TitleSlingServlet extends SlingSafeMethodsServlet {
    private static final long serialVersionUID = -3960692666512058118L;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        response.getWriter().print("<h1>Sling Servlet injected this title</h1>");
    }
}
