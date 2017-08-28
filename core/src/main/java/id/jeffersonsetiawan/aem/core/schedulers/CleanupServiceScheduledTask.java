package id.jeffersonsetiawan.aem.core.schedulers;

import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.Dictionary;

@Designate(ocd = CleanupServiceScheduledTask.Config.class)
@Component(service = Runnable.class,
        immediate = true
)
public class CleanupServiceScheduledTask implements Runnable {
    @ObjectClassDefinition(name = "Cleanup Service scheduled task",
            description = "Simple demo for cron-job like task with properties")
    public static @interface Config {

        @AttributeDefinition(name = "Cron-job expression")
        String scheduler_expression() default "*/5 * * * * ?";

        @AttributeDefinition(name = "Node to delete",
                description = "Can be configured in /system/console/configMgr")
        String cleanupPath() default "HAHAHAHAHA";
    }

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    @Reference
    private SlingRepository repository;
    private Config configuration;

    @Activate
    protected void activate(ComponentContext componentContext, final Config config) {
        this.configuration = config;
        configure(componentContext.getProperties());
    }

    protected void configure(Dictionary<?, ?> properties) {
        LOGGER.info("configure: cleanupPath='{}''", properties);
    }

    @Override
    public void run() {
        LOGGER.info(this.configuration.scheduler_expression());
        Session session = null;
        try {
            session = repository.loginService("training", null);
            if (session.itemExists(this.configuration.cleanupPath())) {
                session.removeItem(this.configuration.cleanupPath());
                LOGGER.info("node " + this.configuration.cleanupPath() + "deleted");
                session.save();
            }
        } catch (RepositoryException e) {
            LOGGER.error("exception during cleanup", e);
        } finally {
            if (session != null) {
                session.logout();
            }
        }
    }
}
