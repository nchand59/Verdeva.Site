using System;
using Verdeva.Sites.Antennae;

namespace Verdeva.Cloud.API.Models.Antennae
{
    public class AntennaDetectionModel
    {
        public int AntennaId { get; set; }
        public string TransponderId { get; set; }
        public TransponderType TransponderType { get; set; }
        public DateTime TimeStamp { get; set; }
    }
}