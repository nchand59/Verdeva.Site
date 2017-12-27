using System;
using System.Collections.Generic;
using Verdeva.Sites.Antennae;

namespace Verdeva.Sites
{
    class Program
    {
        static void Main(string[] args)
        {
            var a = new Verdeva.Sites.Antennae.Transcore.Antenna(args[0]);

            var site = new Site(1, "https://paybycar-qa.nowintelligence.com", new List<IAntenna> { a });
            site.Start();
            a.Connect();

            Console.ReadKey();
        }
    }
}
