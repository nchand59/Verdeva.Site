package com.paybycar.site.encompass;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SimpleTagReader implements ITagReader, Runnable {
    private BlockingQueue<String> queue;
    private InputStream data;
    private Thread thread;

    private static final char CR = (char)13;
    private static final char LF = (char)10;
    private static final char SOM = '#';

    public SimpleTagReader(InputStream data){
        queue = new LinkedBlockingQueue();
        this.data = data;
        this.thread = new Thread(this);
    }


    public void stop() {
        this.thread.interrupt();
    }

    public void start() {
        this.thread.start();
    }

    @Override
    public BlockingQueue<String> tags() {
        return queue;
    }

    @Override
    public void run() {
        try {
            StringBuffer tag = null;
            byte[] buffer = new byte[1024];

            char last = 0;
            char datum;
            int len;
            while ((len = this.data.read(buffer)) > -1){
                for(int ii = 0; ii < len; ii ++) {
                    datum = (char)buffer[ii];
                    switch (datum) {
                        case SOM:
                            System.out.println("Tag Found");
                            tag = new StringBuffer();
                            break;
                        case CR:
                            break;
                        case LF:
                            if (last == CR && tag != null) {
                                queue.add(tag.toString());
                                tag = null;
                            }
                        default:
                            if (tag == null)
                                tag = new StringBuffer();
                            tag.append((char) datum);
                            break;
                    }
                    last = datum;
                }
            }
        } catch (IOException e) {
            System.out.println("error reading tag");
            e.printStackTrace(System.out);
        }
    }
}
