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
