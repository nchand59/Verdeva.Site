using System;
using System.Reactive.Linq;

namespace Verdeva.Sites.Antennae.Transcore
{
    public class Antenna : IAntenna, IDisposable {

        Reader reader;

        public Antenna(string port) {
            reader = new Reader(port);

            Signals = reader.Messages.Select(m => new Transponder() {
                Id = m.Data,
                Type = TransponderType.TDM
            });
        }

        public IObservable<Transponder> Signals { get; private set; }

        public int Id { get; set; }

        public void Dispose() {
            ((IDisposable)reader).Dispose();
        }

        public void Connect() {
            reader.Open();
        }
    }
}
