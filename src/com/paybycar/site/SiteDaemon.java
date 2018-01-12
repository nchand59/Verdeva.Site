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
    private Thread myThread;
    ITagReader reader = null;

    private boolean stopped = false;
    private Options options;

    String appUrl;
    boolean descover;
    String port;
    int antennaId;

    public SiteDaemon(){
        options = new Options();

        options.addOption("a", "api-url", true, "url of the api to send messages to");
        options.addOption("p", "port", true, "port to connect to the antenna on");
        options.addOption("d", "discover", false, "discover the port");
        options.addOption("id", "antenna-id", true, "Id of the antenna");
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

       if(cmd.hasOption("p"))
           port = cmd.getOptionValue("p");

       System.out.printf("%s, %n", appUrl, antennaId);
    }

    @Override
    public void start() throws Exception {
        EncompassSerial antenna = new EncompassSerial();
        API api = new API(this.appUrl);

        if(this.descover)
            reader = antenna.open();
        else
            reader = antenna.open(this.port);

        BlockingQueue<String> tags = reader.tags();

        myThread = new Thread(() -> {
            System.out.println("Searching for TDM tags...");
            while(!stopped){
                String tag = null;
                try {
                    tag = tags.poll(200, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
                if(tag != null)
                    try {
                        System.out.println(tag);
                        api.post(antennaId, tag.split("&")[0]);
                    } catch (Exception e){
                        System.err.println(e.getMessage());
                        e.printStackTrace(System.err);
                    }
            }
        });
        reader.start();
        myThread.start();
    }

    @Override
    public void stop() throws Exception {
        stopped = true;
        try{
            myThread.join(1000);
            reader.stop();
        }catch(InterruptedException e){
            System.err.println(e.getMessage());
            throw e;
        }
    }

    @Override
    public void destroy() {
        myThread = null;
    }
}
