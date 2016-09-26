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

package org.metaservice.core.runner;

import org.metaservice.api.messaging.MessagingException;
import org.metaservice.core.injection.InjectorFactory;
import org.metaservice.api.messaging.ProviderMessagingLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ilo on 08.06.2014.
 */
public class ProviderRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProviderRunner.class);

    public static void main(String[] args) {
        if(args.length != 1)
        {
            LOGGER.error("Need provider id");
            System.exit(-1);
        }
        String id=  args[0];
        ProviderMessagingLoop runner = InjectorFactory.getInjectorForProvider(id).getInstance(ProviderMessagingLoop.class);
        try {
            runner.run();
        } catch (MessagingException e) {
            LOGGER.error("failed",e);
        }
    }
}
