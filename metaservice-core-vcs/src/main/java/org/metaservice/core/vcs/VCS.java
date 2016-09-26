/*
 * Copyright 2015 Nikola Ilo
 * Research Group for Industrial Software, Vienna University of Technology, Austria
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

package org.metaservice.core.vcs;

import org.openrdf.model.URI;
import org.openrdf.model.impl.ValueFactoryImpl;

/**
 * Created by ilo on 01.06.2014.
 */
public class VCS {
    public static final String NAMESPACE = "http://metaservice.org/ns/vcs";

    /**
     * Classes
     */

    public final static URI REPOSITORY;

    public final static URI REVISION;

    public final static URI MERGE_REVISION;

    public final static URI TAG;
    public final static URI VCS_FILE;


    /**
     * Properties
     */
    public final static URI PART_OF_REPOSITORY;
    public final static URI REFERENCES_REVISION;
    public final static URI CONTAINS_REVISION;
    public final static URI CONTAINS_TAG;
    public final static URI CONTAINS_FILE;
    public final static URI SUCCESSOR_OF;
    public final static URI PREDECESSOR_OF;
    public final static URI REPOSITORY_PATH;




    static {
        ValueFactoryImpl valueFactory = ValueFactoryImpl.getInstance();
        REPOSITORY =valueFactory.createURI(NAMESPACE,"Repository");
        REVISION = valueFactory.createURI(NAMESPACE,"Revision");
        MERGE_REVISION = valueFactory.createURI(NAMESPACE,"MergeRevision");
        TAG =valueFactory.createURI(NAMESPACE,"Tag");
        VCS_FILE = valueFactory.createURI(NAMESPACE,"VcsFile");

        PART_OF_REPOSITORY = valueFactory.createURI(NAMESPACE,"partOfRepository");
        REFERENCES_REVISION = valueFactory.createURI(NAMESPACE,"referencesRevision");
        CONTAINS_REVISION =  valueFactory.createURI(NAMESPACE,"containsRevision");
        CONTAINS_TAG =  valueFactory.createURI(NAMESPACE,"containsTag");
        CONTAINS_FILE =  valueFactory.createURI(NAMESPACE,"containsFile");
        SUCCESSOR_OF = valueFactory.createURI(NAMESPACE,"successorOf");
        PREDECESSOR_OF = valueFactory.createURI(NAMESPACE,"predecessorOf");
        REPOSITORY_PATH = valueFactory.createURI(NAMESPACE,"repositoryPath");


    }

}
