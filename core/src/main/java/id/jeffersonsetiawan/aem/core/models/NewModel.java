package id.jeffersonsetiawan.aem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.settings.SlingSettingsService;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by jeffersonsetiawan on 7/5/17.
 */
@Model(adaptables=Resource.class)
public class NewModel {
    @Inject
    private SlingSettingsService settings;

    @Inject @Named("sling:resourceType") @Default(values="No resourceType")
    protected String resourceType;

    @Inject @Named("title") @Default(values="No Title")
    protected String javaTitle;

    private String message;

    @PostConstruct
    protected void init() {
        message = "";
//        message += "\tThis is instance: " + settings.getSlingId() + "\n";
        message += "\tResource type is: " + resourceType + "\n";
    }

    public String getMessage() {
        return message;
    }
    public String getJavaTitle() {
        return javaTitle;
    }
}
