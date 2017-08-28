package id.jeffersonsetiawan.aem.core;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

@Component(service = WorkflowProcess.class,
        immediate = true,
        property = {
                Constants.SERVICE_DESCRIPTION + "=A sample workflow process implementation.",
                Constants.SERVICE_VENDOR + "=Jefferson",
                "process.label=My Sample Workflow Process"
        }
)
public class MyProcess implements WorkflowProcess {

    private static final String TYPE_JCR_PATH = "JCR_PATH";
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public void execute(WorkItem item, WorkflowSession session, MetaDataMap args) throws WorkflowException {
        LOGGER.info("WORKFLOW START!!!");
        WorkflowData workflowData = item.getWorkflowData();
        if (workflowData.getPayloadType().equals(TYPE_JCR_PATH)) {
            String path = workflowData.getPayload().toString() + "/jcr:content";
            try {
                Node node = (Node) session.adaptTo(Session.class).getItem(path);
                LOGGER.info("WORKFLOW START!!!", node);
                if (node != null) {
                    node.setProperty("approved", readArgument(args));
                    session.adaptTo(Session.class).save();
                }
            } catch (RepositoryException e) {
                throw new WorkflowException(e.getMessage(), e);
            }
        }
        LOGGER.info("WORKFLOW END!!!");
    }

    private boolean readArgument(MetaDataMap args) {
        String argument = args.get("PROCESS_ARGS", "false");
        return argument.equalsIgnoreCase("true");
    }
}
