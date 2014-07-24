package org.metaservice.core.injection.providers;

import cc.ilo.cc.ilo.pipeline.pipes.AbstractPipe;
import cc.ilo.cc.ilo.pipeline.pipes.PipeProvider;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by ilo on 23.07.2014.
 */
@Singleton
public class GuicePipeProviderFactory {
    private final Injector injector;

    @Inject
    public GuicePipeProviderFactory(Injector injector) {
        this.injector = injector;
    }

    public <P extends AbstractPipe<I,O>,I,O> PipeProvider<I,O> createProvider(final Class<? extends AbstractPipe<I,O>> p){
        return new PipeProvider<I,O>() {
            @Override
            public AbstractPipe<I,O> createPipeInstance() {
                Injector child = injector.createChildInjector(new AbstractModule() {
                    @Override
                    protected void configure() {
                         bind(Logger.class).toInstance(LoggerFactory.getLogger(p));
                    }
                });
                return child.getInstance(p);
            }
        };
    }
}
