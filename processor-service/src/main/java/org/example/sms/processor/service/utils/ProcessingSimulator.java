package org.example.sms.processor.service.utils;

import org.example.sms.core.entities.exceptions.QueueFullException;
import org.jboss.logging.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class ProcessingSimulator<T> {
    private static final Logger logger = Logger.getLogger(ProcessingSimulator.class);
    private static final int MAX_QUEUE_SIZE = 100;

    private final BlockingQueue<T> queue = new LinkedBlockingQueue<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public ProcessingSimulator() {
        executor.submit(this::processQueue);
    }

    public void enqueue(T item) {
        if (queue.size() > MAX_QUEUE_SIZE) {
            throw new QueueFullException("Queue is full.");
        }
        try {
            queue.put(item);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void processQueue() {
        while (true) {
            try {
                T item = queue.take();
                process(item);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    protected abstract void process(T item);
}
