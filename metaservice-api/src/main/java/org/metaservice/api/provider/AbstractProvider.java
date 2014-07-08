package org.metaservice.api.provider;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Created by ilo on 04.03.14.
 */
public abstract class AbstractProvider<T> implements Provider<T> {
    protected final ValueFactory valueFactory;

    protected AbstractProvider(ValueFactory valueFactory) {
        this.valueFactory = valueFactory;
    }

    protected void addIfNotNull(@NotNull RepositoryConnection resultConnection, @NotNull Resource subject, @NotNull URI predicate,@Nullable String text,@Nullable String language) throws RepositoryException {
        if(text != null){
            if(language != null)
                resultConnection.add(subject, predicate, valueFactory.createLiteral(text,language));
            else
                resultConnection.add(subject, predicate, valueFactory.createLiteral(text));
        }

    }

    protected void addIfNotNull(@NotNull RepositoryConnection resultConnection, @NotNull Resource subject, @NotNull URI predicate,@Nullable String object) throws RepositoryException {
        if(object != null)
            resultConnection.add(subject, predicate, valueFactory.createLiteral(object));
    }
    protected void addIfNotNull(@NotNull RepositoryConnection resultConnection, @NotNull Resource subject, @NotNull URI predicate,@Nullable XMLGregorianCalendar object) throws RepositoryException {
        if(object != null)
            resultConnection.add(subject, predicate, valueFactory.createLiteral(object));
    }
    protected void addIfNotNull(@NotNull RepositoryConnection resultConnection, @NotNull Resource subject, @NotNull URI predicate,@Nullable Integer object) throws RepositoryException {
        if(object != null)
            resultConnection.add(subject, predicate, valueFactory.createLiteral(object));
    }
    protected void addIfNotNull(@NotNull RepositoryConnection resultConnection, @NotNull Resource subject, @NotNull URI predicate,@Nullable Boolean object) throws RepositoryException {
        if(object != null)
            resultConnection.add(subject, predicate, valueFactory.createLiteral(object));
    }

    protected void addIfNotEmpty(@NotNull RepositoryConnection resultConnection,@NotNull Resource uri,@NotNull URI relation,@NotNull URI object) throws RepositoryException {
        if(resultConnection.getStatements(object,null,null,false).hasNext())
            resultConnection.add(uri,relation,object);
    }
}
