package com.paybycar.site.encompass;

import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

public class EncompassSerial {

    public ITagReader open() throws ToManySerialDevicesFound, NoDeviceFoundException, PortInUseException, UnsupportedCommOperationException, TooManyListenersException, IOException {
        return this.open(this.discover());
    }

    public ITagReader open(String portName) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException, TooManyListenersException, NoDeviceFoundException {
        CommPortIdentifier port = CommPortIdentifier.getPortIdentifier(portName);
        return open(port);
    }

    private ITagReader open(CommPortIdentifier port) throws PortInUseException, UnsupportedCommOperationException, IOException, TooManyListenersException, NoDeviceFoundException {

        CommPort commPort = port.open(this.getClass().getName(), 2000);
        System.out.printf("Opening %s \n", commPort.getName());

        if (commPort instanceof SerialPort) {
            SerialPort serialPort = (SerialPort) commPort;
            serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

            InputStream in = serialPort.getInputStream();

            return new SimpleTagReader(in);
        }
        throw new NoDeviceFoundException();
    }

    public CommPortIdentifier discover() throws NoDeviceFoundException, ToManySerialDevicesFound {
        Enumeration<CommPortIdentifier> ports = CommPortIdentifier.getPortIdentifiers();

        CommPortIdentifier antenna = null;

        while(ports.hasMoreElements()){

            CommPortIdentifier port = ports.nextElement();
            if(port.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (antenna == null)
                    antenna = port;
                else
                    throw new ToManySerialDevicesFound();
            }
        }

        if(antenna == null)
            throw new NoDeviceFoundException();
        return antenna;
    }
}
