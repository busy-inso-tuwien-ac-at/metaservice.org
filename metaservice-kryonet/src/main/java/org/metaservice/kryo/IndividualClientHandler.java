package org.metaservice.kryo;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.metaservice.kryo.beans.AbstractMessage;
import org.metaservice.kryo.beans.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by ilo on 08.06.2014.
 */
public class IndividualClientHandler<T extends AbstractMessage> extends Listener {
    private final Logger LOGGER  = LoggerFactory.getLogger(IndividualClientHandler.class);
    private final Queue<T> queue;
    private final Set<T> inFlight = Collections.synchronizedSet(new HashSet<T>());
    private final int flightCount;
    private final ExecutorService submitExecutorService;
    private final ExecutorService responseExecutorService;

    public IndividualClientHandler(Queue<T> queue,int flightCount) {
        ThreadFactoryBuilder builder = new ThreadFactoryBuilder()
                .setDaemon(true)
                .setNameFormat(queue.getName()+"-sumit-%d");
        submitExecutorService = Executors.newSingleThreadExecutor(builder.build());
        builder.setNameFormat(queue.getName()+"-response-%d");
        responseExecutorService = Executors.newSingleThreadExecutor(builder.build());
        this.queue = queue;
        this.flightCount = flightCount;
    }


    public void init(final Connection connection) {
        for(int i =0 ; i < flightCount;i++){
            submitExecutorService.execute(new Runnable() {
                @Override
                public void run() {
                    sendMsg(connection);
                }
            });
        }
    }

    private synchronized void sendMsg(Connection connection) {
        T msg = queue.get();
        if(msg != null) {
            synchronized (inFlight) {
                inFlight.add(msg);
            }
            connection.sendTCP(msg);
            LOGGER.trace("inflight {}",inFlight.size());
            LOGGER.trace("sent {}", msg.get_id());
        }else{
            LOGGER.warn("Queue return null - inFlight will decrease");
        }
    }

    @Override
    public void disconnected(Connection connection) {
        LOGGER.warn("disconnect " + connection.getID());

        submitExecutorService.shutdownNow();
        List<Runnable> runnables = responseExecutorService.shutdownNow();
        for(Runnable r :runnables){
            r.run();
        }
        try {
            responseExecutorService.awaitTermination(10, TimeUnit.SECONDS);
            submitExecutorService.awaitTermination(10,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOGGER.error("interrupted termination of executrservices", e);
        }
        synchronized (inFlight) {
            for (T t : inFlight) {
                LOGGER.info(t.get_id() + " no result received -> pushback");
                queue.pushBack(t);
            }
        }
    }

    @Override
    public void received(final Connection connection,final Object o) {
        if(o instanceof ResponseMessage){
            responseExecutorService.execute(new Runnable() {
                @Override
                public void run() {
                    handleResponse((ResponseMessage) o);
                }
            });
            submitExecutorService.execute(new Runnable() {
                @Override
                public void run() {
                    sendMsg(connection);
                }
            });
        }
    }

    private void handleResponse(ResponseMessage o) {
        T result = null;

        synchronized (inFlight) {
            LOGGER.trace(o.getAboutMessage().get_id() + " returned");
            Iterator<T> iter = inFlight.iterator();
            while (iter.hasNext()) {
                T t = iter.next();
                if (t.get_id().equals((o.getAboutMessage().get_id()))) {
                    result = t;
                    iter.remove();
                    break;
                }
            }

        }
        if(result != null){
            if(o.getStatus() == ResponseMessage.Status.OK) {
                LOGGER.trace("{} successfull",o.getAboutMessage().get_id());
            }else{
                LOGGER.warn("{} failed",o.getAboutMessage().get_id() );
                queue.markAsFailed(o);
            }
        }
    }
}
