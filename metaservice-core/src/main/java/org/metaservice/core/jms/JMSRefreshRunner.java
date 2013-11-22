package org.metaservice.core.jms;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.metaservice.api.provider.Provider;
import org.metaservice.core.Config;
import org.metaservice.core.MetaserviceModule;
import org.metaservice.core.rdf.BufferedSparql;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

public class JMSRefreshRunner extends AbstractJMSRunner {
    private final Logger LOGGER = LoggerFactory.getLogger(JMSRefreshRunner.class);
    private final Provider provider;
    private final BufferedSparql bufferedSparql;

    public static void main(String[] args) throws JMSException {
        Injector injector = Guice.createInjector(new MetaserviceModule());
        AbstractJMSRunner runner = injector.getInstance(JMSRefreshRunner.class);
        runner.run();
    }

    @Inject
    private JMSRefreshRunner(
            Provider provider,
            BufferedSparql bufferedSparql,
            Config config
    ) throws JMSException, RepositoryException {
        super("Refresh",config);
        this.bufferedSparql = bufferedSparql;
        this.provider = provider;
    }

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage m = (TextMessage) message;
            provider.refresh(bufferedSparql.createURI(m.getText()));
            bufferedSparql.flushModel();
        } catch (JMSException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
