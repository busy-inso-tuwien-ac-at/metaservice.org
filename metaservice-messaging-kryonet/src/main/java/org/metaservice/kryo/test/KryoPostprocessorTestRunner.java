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
