package id.jeffersonsetiawan.aem.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.Repository;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Simple Demo Servlet",
//                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.resourceTypes=" + "/apps/geometrixx/components/lead",
                "sling.servlet.selectors=" + "lead-sling-servlet",
                "sling.servlet.extensions=" + "html"
        })
public class LeadSlingServlet extends SlingSafeMethodsServlet {
    private static final long serialVersionUID = 6165211752185245787L;

    @Reference
    private Repository repository;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Content-Type", "text/html");
        String[] keys = repository.getDescriptorKeys();
        response.getWriter().print("<br><table border='1'><th>Repository Descriptor</th><th>Value</th>");
        for (int i = 0; i < keys.length; i++) {
            try {
                response.getWriter().print("<tr><td>" + keys[i] + "</td><td>" + repository.getDescriptor(keys[i]) + "</td></tr>");
            } finally {
            }
        }
        response.getWriter().print("</table>");

    }
}
