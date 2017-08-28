package id.jeffersonsetiawan.aem.core;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Component(service = JobConsumer.class,
        immediate = true,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Demo to listen on changes in the resource tree",
                JobConsumer.PROPERTY_TOPICS + "=id/jeffersonsetiawan/aem/core/replicationjob"
        })

public class ReplicationLogger implements JobConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReplicationLogger.class);
    public static final String TOPIC = "id/jeffersonsetiawan/aem/core/replicationjob";
    static final String myTopic = JobConsumer.PROPERTY_TOPICS;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Override
    public JobResult process(final Job job) {
        final String pagePath = job.getProperty("PAGE_PATH").toString();

        ResourceResolver resourceResolver = null;
        try {
            Map<String, Object> serviceParams = new HashMap<String, Object>();
            serviceParams.put(ResourceResolverFactory.SUBSERVICE, "training");
            resourceResolver = resourceResolverFactory.getServiceResourceResolver(serviceParams);
        } catch (LoginException e) {
            e.printStackTrace();
//            return JobResult.FAILED;
        }

        final PageManager pm = resourceResolver.adaptTo(PageManager.class);
        final Page page = pm.getContainingPage(pagePath);
        if (page != null) {
            LOGGER.info("******************* ACTIVATION OF PAGE : {}", page.getTitle());
        }

        return JobResult.OK;
    }
}