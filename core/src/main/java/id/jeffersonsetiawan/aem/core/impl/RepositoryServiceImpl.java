package id.jeffersonsetiawan.aem.core.impl;

import id.jeffersonsetiawan.aem.core.RepositoryService;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Repository;

@Component(service=RepositoryService.class, property={
        Constants.SERVICE_DESCRIPTION + "=Simple Repository Service"
})
public class RepositoryServiceImpl implements RepositoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryServiceImpl.class);
    @Reference
    private Repository repository;
    public String getRepositoryName() {
        return repository.getDescriptor(Repository.REP_NAME_DESC);
    }
    @Activate
    protected void activate() {
        LOGGER.info("service activated" );
    }
    @Deactivate
    protected void deactivate() {
        LOGGER.info ("service deactivated");
    }
}
