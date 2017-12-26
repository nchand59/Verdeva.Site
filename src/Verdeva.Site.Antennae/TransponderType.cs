namespace Verdeva.Sites.Antennae {
    public enum TransponderType : byte
    {
        Unknown = 0,

        // -- Passive

        /// <summary>
        /// Passive -- Passive off-the-shelf RFID Tags
        /// </summary>
        Gen2,

        // -- Active

        /// <summary>
        /// Active -- aka TDM or IAG, protocol used by MA toll transponder as well as the national standard
        /// </summary>
        TDM,

        /// <summary>
        /// Active -- Florida Toll Transponders
        /// </summary>
        SeGo,

        /// <summary>
        /// Active -- Older Florida Told Transponders
        /// </summary>
        eGo,

        /// <summary>
        /// Active -- Air Transport Association of America Protocol
        /// </summary>
        ATA,

        /// <summary>
        /// Active -- Air Transport Association of America Protocol
        /// </summary>
        eATA,

        /// <summary>
        /// Active -- CA Toll Transponder Protocols
        /// </summary>
        T21
    }
}
