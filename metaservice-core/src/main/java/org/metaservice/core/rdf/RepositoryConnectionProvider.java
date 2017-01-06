/*
 * Copyright 2015 Nikola Ilo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.metaservice.core.rdf;

import com.google.inject.Provider;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import javax.inject.Inject;

public class RepositoryConnectionProvider implements Provider<RepositoryConnection> {
    private final Repository repository;

    @Inject
    public RepositoryConnectionProvider(Repository repository) {
        this.repository = repository;
    }

    @Override
    public RepositoryConnection get() {
        try {
            return repository.getConnection();
            //TODO exception handling?
        } catch (RepositoryException e) {
            e.printStackTrace();
            return null;
        }
    }
}
