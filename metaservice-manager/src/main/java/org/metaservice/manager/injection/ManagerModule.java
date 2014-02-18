package org.metaservice.manager.injection;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;
import org.quartz.Scheduler;

/**
 * Created by ilo on 18.02.14.
 */
public class ManagerModule implements Module {
    @Override
    public void configure(Binder binder) {
        binder.bind(Scheduler.class).toProvider(SchedulerProvider.class).in(Scopes.SINGLETON);
    }
}
