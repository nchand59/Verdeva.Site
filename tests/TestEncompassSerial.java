import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.paybycar.site.encompass.*;

import java.io.IOException;
import java.util.TooManyListenersException;
import java.util.concurrent.BlockingQueue;

public class TestEncompassSerial {

    @Test
    public void testDiscover() {
        EncompassSerial antenna = new EncompassSerial();
        try {
            Assertions.assertNotNull(antenna.discover());
        } catch (NoDeviceFoundException e) {
            Assertions.fail("No Serial Device Found");
            e.printStackTrace();
        } catch (ToManySerialDevicesFound toManySerialDevicesFound) {
            Assertions.fail(("Not clear which device I should use"));
            toManySerialDevicesFound.printStackTrace();
        }
    }

    @Test void runBench() throws UnsupportedCommOperationException, TooManyListenersException, IOException, ToManySerialDevicesFound, PortInUseException, NoDeviceFoundException, InterruptedException {
        EncompassSerial antenna = new EncompassSerial();
        ITagReader reader = antenna.open();
        reader.start();

        BlockingQueue<String> tags = reader.tags();
        for(int ii = 0; ii < 3; ii++)
            System.out.println(tags.take());
    }
}
