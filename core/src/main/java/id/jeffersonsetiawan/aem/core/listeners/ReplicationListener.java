package id.jeffersonsetiawan.aem.core.listeners;

import com.day.cq.replication.ReplicationAction;
import com.day.cq.replication.ReplicationActionType;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

@Component(service = EventHandler.class,
        immediate = true,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Demo to listen on changes in the resource tree",
                EventConstants.EVENT_TOPIC + "=" + ReplicationAction.EVENT_TOPIC
        })
public class ReplicationListener implements EventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReplicationListener.class);
    private static final String TOPIC = "id/jeffersonsetiawan/aem/core/replicationjob";

    @Reference
    private JobManager jobManager;


    @Override
    public void handleEvent(final Event event) {
        ReplicationAction action = ReplicationAction.fromEvent(event);

        if (action.getType().equals(ReplicationActionType.ACTIVATE)) {

            if (action.getPath() != null) {
                try {
                    // Create a properties map that contains things we want to pass through the job
                    HashMap<String, Object> jobprops = new HashMap<String, Object>();
                    jobprops.put("PAGE_PATH", action.getPath());
                    jobManager.addJob(TOPIC, jobprops);

                } catch (Exception e) {
                    LOGGER.error("********************************* ERROR CREATING JOB : NO PAYLOAD WAS DEFINED");
                    e.printStackTrace();
                }
            }

        }
    }
}