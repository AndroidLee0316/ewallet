package com.pasc.business.ewallet.common.event;

import android.os.Handler;
import android.os.Looper;

import com.pasc.business.ewallet.NotProguard;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @date 2019/8/6
 * @des
 * @modify
 **/
@NotProguard
public class EventBusManager {
    private static class Single {
        private static final EventBusManager instance = new EventBusManager ();
    }
    private static final Handler mHandler = new Handler (Looper.getMainLooper ());
    private EventBusManager() {
    }
    public static EventBusManager getDefault(){
        return Single.instance;
    }

    private List<EventBusListener> busObservers = new CopyOnWriteArrayList<> ();

    public synchronized void register(EventBusListener observer) {
        if (observer != null) {
            if (!busObservers.contains (observer)) {
                busObservers.add (observer);
            }
        }
    }

    public synchronized void unregister(EventBusListener observer) {
        if (observer != null) {
            int index = busObservers.indexOf (observer);
            if (index != -1) {
                busObservers.remove (index);
            }
        }
    }

    /******注销所有观察者*******/
    public synchronized void unregisterrAll() {
        busObservers.clear ();
    }
    public synchronized void post(BaseEventType item) {
        for (final EventBusListener observer : busObservers) {
            post (item, observer);

        }
    }

    private void post(final BaseEventType item, final EventBusListener observer) {
        if (observer == null) {
            return;
        }
        if (Looper.myLooper () == Looper.getMainLooper ()) {
            observer.handleMessage (item);
        } else {
            mHandler.post (new Runnable () {
                @Override
                public void run() {
                    observer.handleMessage (item);
                }
            });
        }
    }
}
