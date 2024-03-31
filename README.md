This application allows to create an automatic floor plan for a house or an apartment based on inputs.



# Diskussion 10.3.2024
- Fläche 200m^2 - Treppenhaus
- Treppenhaus positionieren
- Parameter minimale Grösse Apartment
- Fehler: Fläche sollte 155m^2 sein (ist 140m^2)
- Badezimmer (blau) etc grob einzeichnen
- Variable Breite
- Constraints:
  - Minimale Fläche
  - Badezimmer mit Fenster
  - Abstellräume im Innern
  - Balkone

## Inputs
- Form in Breite und Länge (in m)
- [Minimale Wohnung. Z.B. 2.5 Zi]
- ...

# Idee
Entscheidungshilfe für den Architekten.
Varianten, um den Entscheidungsprozess zu vereinfachen
Zeichnen der Varianten von Hand bedeutet viel Aufwand



## Bruttogeschossfläche in Wohnungen aufteilen

Input:
- Bruttogeschossfläche. Z.B. 400m^2 pro Etage
- Erweiterung Höhe für Gebäude. Z.B. 3m pro Etage
- (Optional) Anzahl gewünschte Wohungen. Z.B. 3 2.5 Zimmerwohnungen, 3.5 ZiWo

Output:
- Plan mit Wohnungen
- Fünf verschiedene Varianten (Zufall)

Typische Wohnflächen:
- 2.5 Zimmerwohnungen = 65 - 75 m^2, 8-10%
- 3.5 Zimmerwohnungen = 90 - 100 m^2, 30-40%
- 4.5 Zimmerwohnungen = 115 - 125 m^2, 45-52%
- 5.5 Zimmerwohnungen = 125 - 135 m^2, 11%
- Treppenhaus mit Lift = 15 m^2 (in der Regel in der Mitte)

Ziel:
- Möglichst hohe Effizienz. D.h. viel Wohnraum/Nutzfläche. Z.B. wenig Platz für Treppenhaus
- Effizienz berechnen?
- Energieverbrauch? Nachhaltigkeit.

## Erweiterungen
Mögliche Rendite pro Variante berechnen.

