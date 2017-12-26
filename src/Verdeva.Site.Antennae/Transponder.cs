using System;

namespace Verdeva.Sites.Antennae {
    public sealed class Transponder {
        public string Id { get; set; }
        public TransponderType Type { get; set; }

        public override string ToString() {
            return String.Format("Transponder: {{ id: {0}, type:{1} }}", Id, Enum.GetName(typeof(TransponderType), Type));
        }
    }
}