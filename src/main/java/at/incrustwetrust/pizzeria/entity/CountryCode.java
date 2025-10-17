package at.incrustwetrust.pizzeria.entity;

import java.util.Map;

public enum CountryCode {

    AT, DE, CH,


    AL, AD, AM, AZ, BY, BE, BA, BG, CY, CZ, DK, EE, FI, FR, GE,
    GR, HR, HU, IE, IS, IT, LI, LT, LU, LV, MC, MD, ME, MK, MT,
    NL, NO, PL, PT, RO, RS, RU, SE, SI, SK, SM, ES, TR, UA, VA,
    GB_EN, GB_NI, GB_SCT, GB_WLS, XK;

    private static final Map<CountryCode, String> displayNames = Map.ofEntries(
            Map.entry(AT, "Österreich"),
            Map.entry(DE, "Deutschland"),
            Map.entry(CH, "Schweiz"),
            Map.entry(AD, "Andorra"),
            Map.entry(AL, "Albanien"),
            Map.entry(AM, "Armenien"),
            Map.entry(AZ, "Aserbaidschan"),
            Map.entry(BA, "Bosnien und Herzegowina"),
            Map.entry(BE, "Belgien"),
            Map.entry(BG, "Bulgarien"),
            Map.entry(BY, "Belarus"),
            Map.entry(CY, "Zypern"),
            Map.entry(CZ, "Tschechien"),
            Map.entry(DK, "Dänemark"),
            Map.entry(EE, "Estland"),
            Map.entry(ES, "Spanien"),
            Map.entry(FI, "Finnland"),
            Map.entry(FR, "Frankreich"),
            Map.entry(GE, "Georgien"),
            Map.entry(GB_EN, "England"),
            Map.entry(GB_NI, "Nordirland"),
            Map.entry(GB_SCT, "Schottland"),
            Map.entry(GB_WLS, "Wales"),
            Map.entry(GR, "Griechenland"),
            Map.entry(HR, "Kroatien"),
            Map.entry(HU, "Ungarn"),
            Map.entry(IE, "Irland"),
            Map.entry(IS, "Island"),
            Map.entry(IT, "Italien"),
            Map.entry(LI, "Liechtenstein"),
            Map.entry(LT, "Litauen"),
            Map.entry(LU, "Luxemburg"),
            Map.entry(LV, "Lettland"),
            Map.entry(MC, "Monaco"),
            Map.entry(MD, "Moldawien"),
            Map.entry(ME, "Montenegro"),
            Map.entry(MK, "Nordmazedonien"),
            Map.entry(MT, "Malta"),
            Map.entry(NL, "Niederlande"),
            Map.entry(NO, "Norwegen"),
            Map.entry(PL, "Polen"),
            Map.entry(PT, "Portugal"),
            Map.entry(RO, "Rumänien"),
            Map.entry(RS, "Serbien"),
            Map.entry(RU, "Russland"),
            Map.entry(SE, "Schweden"),
            Map.entry(SI, "Slowenien"),
            Map.entry(SK, "Slowakei"),
            Map.entry(SM, "San Marino"),
            Map.entry(TR, "Türkei"),
            Map.entry(UA, "Ukraine"),
            Map.entry(VA, "Vatikanstadt"),
            Map.entry(XK, "Kosovo")
    );

    public String getDisplayName() {
        return displayNames.getOrDefault(this, this.name());
    }


}
