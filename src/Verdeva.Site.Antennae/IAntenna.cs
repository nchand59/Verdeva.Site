using System;

namespace Verdeva.Sites.Antennae
{
    public interface IAntenna {
        IObservable<Transponder> Signals { get; }

        int Id { get; }

    }
}
