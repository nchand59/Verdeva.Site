package com.paybycar.site;

import com.paybycar.site.encompass.EncompassSerial;
import com.paybycar.site.encompass.ITagReader;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class SiteDaemon implements Daemon {
    private Thread scanningThread;
    private Thread heardbeatThread;

    ITagReader reader = null;

    private boolean stopped = false;
    private Options options;

    String appUrl;
    boolean descover;
    String port;
    int antennaId;
    API api;
    String key;

    public SiteDaemon(){
        options = new Options();

        options.addOption("a", "api-url", true, "url of the api to send messages to");
        options.addOption("p", "port", true, "port to connect to the antenna on");
        options.addOption("d", "discover", false, "discover the port");
        options.addOption("id", "antenna-id", true, "Id of the antenna");
        options.addOption("k", "api-key", true, "api key to access the api");
    }

    @Override
    public void init(DaemonContext daemonContext) throws Exception {
        /*
         * Construct objects and initialize variables here.
         * You can access the command line arguments that would normally be passed to your main()
         * method as follows:
         */
        String[] args = daemonContext.getArguments();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        appUrl = cmd.getOptionValue("a");
        descover = cmd.hasOption("d");
        antennaId = Integer.parseInt(cmd.getOptionValue("id"));
        key = cmd.getOptionValue("k");

        if(cmd.hasOption("p"))
            port = cmd.getOptionValue("p");

       System.out.printf("url:%s antenna:%n key:%s", appUrl, antennaId, key );
    }

    @Override
    public void start() throws Exception {

        api = new API(this.appUrl, this.key);

        EncompassSerial antenna = new EncompassSerial();
        System.out.println("Creating API endpoint");

        System.out.println("Opening serial connection");

        if(this.descover)
            reader = antenna.open();
        else
            reader = antenna.open(this.port);

        BlockingQueue<String> tags = reader.tags();

        scanningThread = new Thread(() -> {
            System.out.println("Searching for TDM tags...");
            while(!stopped){
                String tag = null;
                try {
                    tag = tags.poll(200, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace(System.out);
                }
                if(tag != null)
                    try {
                        System.out.println(tag);
                        api.post(antennaId, tag.split("&")[0]);
                    } catch (Exception e){
                        System.out.println(e.getMessage());
                        e.printStackTrace(System.out);
                    }
            }
        });

        heardbeatThread = new Thread(() -> {
            System.out.println("Starting heartbeat thread");

            long lastPing = System.currentTimeMillis();

            try {
                System.out.println("start ping");
                api.heartbeat(antennaId, 1);
            } catch (Exception e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }

            while(!stopped){
                long now = System.currentTimeMillis();
                if( ((now - lastPing) / 60000.0) > 3){
                    try {
                        lastPing = now;
                        api.heartbeat(antennaId, 2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        reader.start();
        scanningThread.start();
        heardbeatThread.start();
    }

    @Override
    public void stop() throws Exception {
        stopped = true;
        try{
            try {
                System.out.println("stop ping");
                api.heartbeat(antennaId, 3);
            } catch (Exception e) {
                e.printStackTrace();
            }

            scanningThread.join(1000);
            heardbeatThread.join(1000);

            reader.stop();
        }catch(InterruptedException e){
            System.err.println(e.getMessage());
            throw e;
        }
    }

    @Override
    public void destroy() {
        scanningThread = null;
    }
}
