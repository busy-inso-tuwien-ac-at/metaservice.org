package org.metaservice.kryo.test;

import org.metaservice.kryo.KryoClientUtil;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;

/**
 * Created by ilo on 07.06.2014.
 */
public class KryoPostprocessorTestRunner {
    public static final Logger LOGGER = LoggerFactory.getLogger(KryoPostprocessorTestRunner.class);

    public static void main(String[] args) throws IOException, RepositoryException, MalformedQueryException {
        KryoPostprocessorTestRunner kryoPostprocessorTestRunner = new KryoPostprocessorTestRunner(new PostProcessorListenerTest());
        kryoPostprocessorTestRunner.run();
        System.in.read();
    }

    private final PostProcessorListenerTest postProcessorListener;

    @Inject
    public KryoPostprocessorTestRunner(PostProcessorListenerTest postProcessorListener) {
        this.postProcessorListener = postProcessorListener;
    }

    private void run() {
        try {
            new KryoClientUtil().startClient(postProcessorListener);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
