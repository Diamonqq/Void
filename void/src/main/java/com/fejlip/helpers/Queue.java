package com.fejlip.helpers;

import java.util.ArrayList;
import java.util.List;

public class Queue {
    private final List<QueueItem> queue = new ArrayList<>();
    private boolean running = false;

    private boolean clearTaskRunning = false;


    public void add(QueueItem item) {
        this.queue.add(item);
    }

    public QueueItem get() {
        QueueItem item = this.queue.get(0);
        queue.remove(0);
        return item;
    }

    public boolean isEmpty() {
        return this.queue.isEmpty();
    }

    public void setRunning(boolean isRunning) {
        this.running = isRunning;
    }

    public boolean isRunning() {
        return this.running;
    }

    public void clear() {
        this.queue.clear();
    }

    public void scheduleClear() {
        if (!this.clearTaskRunning) {
            this.clearTaskRunning = true;

            new Thread(() -> {
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.clear();
                this.setRunning(false);
                this.clearTaskRunning = false;
                Helpers.sendDebugMessage("Cleared queue.");
            }).start();
        }
    }
}
