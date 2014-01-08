package org.metaservice.core.rdf;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.Repository;

public class ValueFactoryProvider implements Provider<ValueFactory> {
    private final Repository repository;

    @Inject
    ValueFactoryProvider( Repository repository){
        this.repository = repository;
    }

    @Override
    public ValueFactory get() {
        return repository.getValueFactory();
    }
}
