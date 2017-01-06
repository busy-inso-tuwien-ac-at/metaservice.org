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

package org.metaservice.manager.injection;

import com.google.inject.Provider;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import javax.inject.Inject;

/**
 * Created by ilo on 12.02.14.
 */
public class SchedulerProvider implements Provider<Scheduler> {
    private final Scheduler scheduler;



    @Inject
    public SchedulerProvider(InjectionJobFactory injectionJobFactory) throws SchedulerException {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        scheduler = schedulerFactory.getScheduler();
        scheduler.start();
        scheduler.setJobFactory(injectionJobFactory);
    }

    @Override
    public Scheduler get() {
        return scheduler;
    }
}
