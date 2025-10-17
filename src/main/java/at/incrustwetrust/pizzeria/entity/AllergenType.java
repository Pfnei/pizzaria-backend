package at.incrustwetrust.pizzeria.entity;

import java.util.Map;

//This enum would be an Alternative to the existing version (abbreviation = char)
public enum AllergenType {
    A, B, C, D, E, F, G, H, L, M, N, O, P, R;


    private static final Map<AllergenType, String> description = Map.ofEntries(
            Map.entry(A, "Glutenhaltiges Getreide"),
            Map.entry(B, "Krebstiere"),
            Map.entry(C, "Eier"),
            Map.entry(D, "Fisch"),
            Map.entry(E, "Erdnüsse"),
            Map.entry(F, "Soja"),
            Map.entry(G, "Milch (inkl. Laktose)"),
            Map.entry(H, "Schalenfrüchte (Nüsse)"),
            Map.entry(L, "Sellerie"),
            Map.entry(M, "Senf"),
            Map.entry(N, "Sesam"),
            Map.entry(O, "Schwefeldioxid und Sulfite"),
            Map.entry(P, "Lupinen"),
            Map.entry(R, "Weichtiere")
    );

    public String getDescription() {
        return description.getOrDefault(this, this.name());
    }




}
