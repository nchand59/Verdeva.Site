using System;
using System.Collections.Generic;
using System.Text;

namespace Verdeva.Sites.Antennae.Transcore
{
    public class Message {
        byte[] raw;
        public Message(byte[] raw) {
            this.raw = raw;
        }

        public override string ToString() {
            return $"{{{Encoding.ASCII.GetString(raw)}}}";
        }

        public string Data {
            get {
                var m = Encoding.ASCII.GetString(raw);

                return m.Substring(0, m.IndexOf('&'));
            }
        }
    }
}
