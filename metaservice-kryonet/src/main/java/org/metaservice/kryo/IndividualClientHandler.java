package org.metaservice.kryo;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import org.metaservice.kryo.beans.AbstractMessage;
import org.metaservice.kryo.beans.PostProcessorMessage;
import org.metaservice.kryo.beans.ResponseMessage;

import javax.xml.ws.Response;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by ilo on 08.06.2014.
 */
public class IndividualClientHandler<T extends AbstractMessage> extends Listener {
    private final Queue<T> queue;
    private final Set<T> inFlight = Collections.synchronizedSet(new HashSet<T>());
    private final int flightCount;
    private final ExecutorService submitExecutorService = Executors.newSingleThreadExecutor();
    private final ExecutorService responseExecutorService = Executors.newSingleThreadExecutor();

    public IndividualClientHandler(Queue<T> queue,int flightCount) {
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
        }
    }

    @Override
    public void disconnected(Connection connection) {
        System.err.println("disconnect " + connection.getID());

        submitExecutorService.shutdownNow();
        List<Runnable> runnables = responseExecutorService.shutdownNow();
        for(Runnable r :runnables){
            r.run();
        }
        try {
            responseExecutorService.awaitTermination(10, TimeUnit.SECONDS);
            submitExecutorService.awaitTermination(10,TimeUnit.SECONDS);
        } catch (InterruptedException e) {

        }
        synchronized (inFlight) {
            for (T t : inFlight) {
                queue.pushBack(t);
                System.err.println(t.get_id() + " no result received -> pushback");
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
        synchronized (inFlight) {
            System.err.println(o.getAboutMessage().get_id() +" returned");

            Iterator<T> iter = inFlight.iterator();
            while (iter.hasNext()) {
                T t = iter.next();
                if (t.get_id().equals((o.getAboutMessage().get_id()))) {
                    if(o.getStatus() == ResponseMessage.Status.OK) {
                        System.err.println(o.getAboutMessage().get_id() + " successfull");
                    }else{
                        queue.markAsFailed(o);
                    }
                    iter.remove();
                    break;
                }
            }
        }
    }
}
