using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Text;
using Verdeva.Cloud.API.Models.Antennae;
using Verdeva.Sites.Antennae;

namespace Verdeva.Sites
{
    public class Site {


        int id;
        string apiRoot;
        List<IAntenna> antennae;

        public Site(int id, string apiRoot, List<IAntenna> antennae) {
            this.id = id;
            this.apiRoot = apiRoot;
            this.antennae = antennae;
        }

        public void Start() {
            Console.WriteLine("Starting Site");

            antennae[0].Signals.Subscribe(async s => {
                Console.WriteLine("antenna {0}: {1}", id, s);
                try {


                    using (var http = new HttpClient()) {
                        Console.WriteLine("sending message");
                        var r = await http.PostAsJsonAsync(
                            apiRoot + "/api/transponders",
                            new AntennaDetectionModel {
                                AntennaId = antennae[0].Id,
                                TimeStamp = DateTime.UtcNow,
                                TransponderId = s.Id,
                                TransponderType = s.Type
                            }
                        );
                        Console.WriteLine("responce: {0}", r.StatusCode);
                    }
                } catch {
                    Console.WriteLine($"Issue sending message");
                }

            });
        }
    }
}
