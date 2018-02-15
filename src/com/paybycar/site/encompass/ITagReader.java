package com.paybycar.site.encompass;

import java.util.concurrent.BlockingQueue;

public interface ITagReader {
    BlockingQueue<String> tags();
    void start();
    void stop();
}
