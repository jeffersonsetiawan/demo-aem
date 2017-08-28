package id.jeffersonsetiawan.aem.core;

import com.day.cq.commons.jcr.JcrUtil;
import com.day.cq.polling.importer.ImportException;
import com.day.cq.polling.importer.Importer;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.regex.Pattern;

@Component(service = Importer.class,
        immediate = true,
        property = {
                Importer.SCHEME_PROPERTY + "=stock",
                "propertyPrivate=false"
        }
)
public class StockDataImporter implements Importer {

    private final String SOURCE_URL = "http://download.finance.yahoo.com/d/quotes.csv?f=snd1l1yr&s=";

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Reference
    private SlingRepository repo;

    Session adminSession = null;

    @Override
    public void importData(final String scheme, final String dataSource, final Resource resource)
            throws ImportException {
        LOGGER.info("START POLLING STOCK!");
        try {
            // dataSource will be interpreted as the stock symbol
            URL sourceUrl = new URL(SOURCE_URL + dataSource);
            BufferedReader in = new BufferedReader(new InputStreamReader(sourceUrl.openStream()));
            String readLine = in.readLine(); // expecting only one line
            String lastTrade = Arrays.asList(Pattern.compile(",").split(readLine)).get(3);
            LOGGER.info("Last trade for stock symbol {} was {}", dataSource, lastTrade);
            in.close();

            //persist
            writeToRepository(dataSource, lastTrade, resource);
        } catch (MalformedURLException e) {
            LOGGER.error("MalformedURLException", e);
        } catch (IOException e) {
            LOGGER.error("IOException", e);
        } catch (RepositoryException e) {
            LOGGER.error("RepositoryException", e);
        }

    }

    private void writeToRepository(final String stockSymbol, final String lastTrade, final Resource resource) throws RepositoryException {
        adminSession = repo.loginService("training", null);
        Node parent = resource.adaptTo(Node.class);
        Node stockPageNode = JcrUtil.createPath(parent.getPath() + "/" + stockSymbol, "cq:Page",
                adminSession);
        Node lastTradeNode = JcrUtil.createPath(stockPageNode.getPath() + "/lastTrade", "nt:unstructured",
                adminSession);
        lastTradeNode.setProperty("lastTrade", lastTrade);
        lastTradeNode.setProperty("lastUpdate", Calendar.getInstance());
        adminSession.save();
        adminSession.logout();
    }

    @Override
    public void importData(String scheme, String dataSource, Resource target,
                           String login, String password) throws ImportException {
        importData(scheme, dataSource, target);

    }
}
