using System;
using System.Collections.Generic;
using System.IO.Ports;
using System.Reactive.Linq;
using System.Reactive.Subjects;
using System.Text;

namespace Verdeva.Sites.Antennae.Transcore
{
    public class Reader : IDisposable {
        SerialPort port;

        private const byte CR = 13;
        private const byte LF = 10;
        private const byte SOM = (byte)'#'; // start of message

        public Reader(string port) {
            this.port = new SerialPort(port, 9600, Parity.None, 8, StopBits.One);
        }

        private IObservable<byte[]> data;
        private Subject<byte[]> rawMessages = new Subject<byte[]>();

        public IObservable<Message> Messages => rawMessages.Select(raw => new Message(raw));

        public void Open() {
            port.Open();

            data = Observable.FromEvent<SerialDataReceivedEventHandler, SerialDataReceivedEventArgs>(
                h => (s, e) => h(e),
                h => port.DataReceived += h,
                h => port.DataReceived -= h
            ).Select(args => {
                var size = port.BytesToRead;
                var buffer = new byte[size];
                port.Read(buffer, 0, size);
                return buffer;
            });

            data.Subscribe(raw => ReadMessages(raw));
        }

        private void OnDataReceived(object sender, SerialDataReceivedEventArgs e) {
            throw new NotImplementedException();
        }

        List<byte> messageBuffer;
        byte last;
        private void ReadMessages(byte[] data) {

            foreach (var cur in data) {
                switch (cur) {

                    case SOM: // start of message
                        if (messageBuffer != null) // message starting in the middle of a messages, need to nck the partal message
                            Nak(messageBuffer.ToString());
                        messageBuffer = new List<byte>();
                        break;
                    case CR: break; // start of end of message (CRLF)
                    case LF: // end of message
                        if (last == CR) {
                            rawMessages.OnNext(messageBuffer.ToArray());
                            messageBuffer = null;
                        } // should we handle this error here?
                        break;
                    default:
                        if (messageBuffer != null) // append message to body
                            messageBuffer.Add(cur);
                        break;
                }
                last = cur;
            }
        }

        private void Nak(string partal) {

        }

        private void Ack(string message) {

        }

        #region IDisposable Support
        private bool disposedValue = false; // To detect redundant calls

        protected virtual void Dispose(bool disposing) {
            if (!disposedValue) {
                if (disposing) {
                    if (port != null)
                        port.Dispose();
                }

                disposedValue = true;
            }
        }

        public void Dispose() {
            Dispose(true);
        }
        #endregion
    }
}
