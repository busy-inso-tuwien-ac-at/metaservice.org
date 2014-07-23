package org.metaservice.core.postprocessor;

import cc.ilo.cc.ilo.pipeline.builder.CompositePipeBuilder;
import cc.ilo.cc.ilo.pipeline.pipes.AbstractPipe;
import info.aduna.iteration.Iterations;
import org.metaservice.api.MetaserviceException;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.messaging.*;
import org.metaservice.api.messaging.descriptors.DescriptorHelper;
import org.metaservice.api.postprocessor.PostProcessor;
import org.metaservice.api.postprocessor.PostProcessorException;
import org.metaservice.core.AbstractDispatcher;
import org.metaservice.core.dispatcher.NotifyPipe;
import org.metaservice.core.dispatcher.SendDataPipe;
import org.metaservice.core.dispatcher.postprocessor.*;
import org.openrdf.model.*;
import org.openrdf.query.*;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.*;

/**
 * Created by ilo on 17.01.14.
 */
public class PostProcessorDispatcher extends AbstractDispatcher implements org.metaservice.api.messaging.dispatcher.PostProcessorDispatcher {
    private final Logger LOGGER = LoggerFactory.getLogger(PostProcessorDispatcher.class);

    private final PostProcessor postProcessor;
    private final MetaserviceDescriptor metaserviceDescriptor;
    private final MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor;
    private final RepositoryConnection repositoryConnection;
    private final ValueFactory valueFactory;
    private final Set<Statement> loadedStatements;
    private final Debug debug;
    private final DescriptorHelper descriptorHelper;
    private final MessageHandler messageHandler;
    private final Config config;
    private final AbstractPipe<Context,Context> pipeline;

    @Inject
    private PostProcessorDispatcher(
            RepositoryConnection repositoryConnection,
            Config config,
            MessageHandler messageHandler,
            MetaserviceDescriptor metaserviceDescriptor, ValueFactory valueFactory,
            PostProcessor postProcessor,
            MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor,
            Debug debug,
            DescriptorHelper descriptorHelper
    ) throws MalformedQueryException, RepositoryException, MetaserviceException {
        super(config,messageHandler);
        this.metaserviceDescriptor = metaserviceDescriptor;
        this.postProcessor = postProcessor;
        this.postProcessorDescriptor = postProcessorDescriptor;
        this.repositoryConnection = repositoryConnection;
        this.valueFactory = valueFactory;
        this.debug = debug;
        this.descriptorHelper = descriptorHelper;
        this.messageHandler = messageHandler;
        this.config = config;
        loadedStatements = calculatePreloadedStatements();
        pipeline = getPipeline();

    }
    private Set<Statement> calculatePreloadedStatements() throws RepositoryException, MetaserviceException {
        Repository resultRepository = createTempRepository(true);
        RepositoryConnection resultConnection = resultRepository.getConnection();

        loadNamespaces(resultConnection,postProcessorDescriptor.getNamespaceList());
        loadOntologies(resultConnection,postProcessorDescriptor.getLoadList());
        resultConnection.commit();
        HashSet<Statement> result  =new HashSet<>();
        Iterations.addAll(resultConnection.getStatements(null, null, null, true, NO_CONTEXT),result);
        resultConnection.close();
        resultRepository.shutDown();
        return Collections.unmodifiableSet(result);
    }


    public static class Context{
        public PostProcessingTask task;
        public long messagingTimestamp;
        public Set<URI> objects;
        public Set<URI> subjects;
        public Set<URI> processableSubjects;
        public URI metadata;
        public Repository resultRepository;
        public RepositoryConnection resultConnection;
        public List<Statement> generatedStatements;
        public Set<URI> existingGraphs;
    }


    public  AbstractPipe<Context,Context> getPipeline() throws PostProcessorException {

        try {
            return CompositePipeBuilder
                    //Filter
                    .create(new DebugFilter(LOGGER, debug))
                    .pipe(new AlreadyProcessedFilter(LOGGER, postProcessorDescriptor))
                    .pipe(new AbortEarlyFilter(postProcessor, LOGGER))
                    .pipe(new RequestToOldFilter(LOGGER, repositoryConnection, valueFactory, descriptorHelper, metaserviceDescriptor, postProcessorDescriptor))

                            //setup
                    .pipe(new CreateTempRepositoryPipe())

                            // run the processor
                    .pipe(new RunPostprocessorPipe(postProcessor, loadedStatements, LOGGER))

                            //check the statements
                    .pipe(new DetermineGeneratedStatementsPipe(postProcessor, valueFactory, LOGGER, loadedStatements))

                            //check existing graphs
                    .pipe(new CalculateExistingGraphsForSubjectsPipe(repositoryConnection, valueFactory, LOGGER, descriptorHelper, postProcessorDescriptor, metaserviceDescriptor))

                            //only update last checked date, if no changes
                    .pipe(new NoChangeFilter(repositoryConnection, valueFactory, LOGGER))

                            //drop existing
                    .pipe(new DropExistingGraphsPipe(repositoryConnection, LOGGER))

                            //create metadata in repository
                    .pipe(new MetadataPipe(repositoryConnection, postProcessor, valueFactory, descriptorHelper, metaserviceDescriptor, postProcessorDescriptor, LOGGER))

                            //send to repository
                    .pipe(new SendDataPipe<PostProcessor>(LOGGER, repositoryConnection, valueFactory, config, postProcessor))

                            //clear resources
                    .pipe(new CloseTempRepositoryPipe())

                            // Notfiy postprocessors
                    .pipe(new NotifyPipe(postProcessorDescriptor, LOGGER, messageHandler))
                    .build()
                    ;
        } catch (MalformedQueryException | RepositoryException e) {
            e.printStackTrace();
            throw new PostProcessorException("Could not create Pipeline",e);
        }
    }

    @Override
    public void process(PostProcessingTask task, long messagingTimestamp) throws PostProcessorException{
        Context context = new Context();
        context.task =task;
        context.messagingTimestamp =messagingTimestamp;
        try{
            /*
                    //try to process into defined state in case anything broke
            //todo put this in pipes
            if(repositoryConnection.isActive()){
                try {
                    repositoryConnection.rollback();
                }catch (Exception e){
                    throw new PostProcessorException("failed to recover",e);
                }
            }

            */

            pipeline.process(context);

        } catch (RepositoryException | MetaserviceException e) {
            LOGGER.error("Couldn't create {}",context.task,e);
            throw new PostProcessorException(e);
        } catch (Exception e) {
            throw new PostProcessorException(e);
        }
    }

    @Override
    @Deprecated
    public boolean isOkCheapCheck(PostProcessingTask task, long jmsTimestamp) throws PostProcessorException {
        return true; //intermediate solution
    }

}
