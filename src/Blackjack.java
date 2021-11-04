import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Blackjack {
    static int puntenSpeler;
    static int puntenBank;
    static Deque<Kaart> spelerDeck;
    static Deque<Kaart> bankDeck;
    static int aantalAzenSpeler;
    static int aantalAzenBank;
    static int kapitaal = 1000;
    static int wedBedrag;
    static boolean beginSpeler = true;
    static boolean beginBank = true;

    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        System.out.println("Welkom bij Blackjack! Type 'n' voor een nieuw spel. Type 'q' om het programma af te sluiten.");
        while (true) {
            String invoer = reader.readLine();
            ifBreak:
            if (invoer.equals("n")) {
                if (!speelSpel()) {
                    System.out.println("Je eindsaldo is " + kapitaal);
                    System.out.println("Type 'n' voor een nieuw spel. Type 'q' om het programma af te sluiten.");
                    if (kapitaal == 0) {
                        kapitaal = 1000;
                        break ifBreak;
                    }
                    return;
                }
            } else if (invoer.equals("q")) {
                return;
            }
        }
    }

    static boolean speelSpel() throws IOException {
        spelerDeck = createDeck();
        bankDeck = createDeck();
        puntenSpeler = 0;
        puntenBank = 0;
        aantalAzenSpeler = 0;
        aantalAzenBank = 0;
        beginSpeler = true;
        beginBank = true;

        System.out.println("Je saldo is " + kapitaal + ". Geef een rond bedrag op waarvoor je wil spelen of type 'q' om het spel af te sluiten.");

        if (!geefBedragOp()) {
            return false;
        }
        if (!spelerSpeelt()) {
            return false;
        }

        if (kapitaal < 1) {
            System.out.println("Je saldo is 0. Helaas, je bent blut.");
            return false;
        }

        if (kapitaal > 0) {
            speelSpel();
        }
        return true;
    }

    static boolean spelerSpeelt() throws IOException {
        boolean resultaat;
        if (beginSpeler) {
            trekKaart(spelerDeck, "Speler");
            trekKaart(spelerDeck, "Speler");
            resultaat = checkResultSpeler();
        } else {
            trekKaart(spelerDeck, "Speler");
            resultaat = checkResultSpeler();
        }
        if (!resultaat) {
            System.out.println("Je hebt " + puntenSpeler + " punten. Type 'k' voor een nieuwe kaart, 'p' om te passen of 'q' om te stoppen.");
            while (true) {
                String invoer = reader.readLine();

                if (invoer.equalsIgnoreCase("k")) {
                    if (!spelerSpeelt()) {
                        return false;
                    }
                    break;
                } else if (invoer.equalsIgnoreCase("p")) {
                    bankSpeelt();
                    break;
                } else if (invoer.equalsIgnoreCase("q")) {
                    kapitaal -= wedBedrag;
                    return false;
                } else {
                    System.out.println("Type 'k' voor een nieuwe kaart, 'p' om te passen of 'q' om te stoppen.");
                }

            }
        }
        return true;
    }

    static boolean checkResultSpeler() {
        if (puntenSpeler == 21 && beginSpeler) {
            kapitaal = kapitaal + wedBedrag * 2;
            System.out.println("Blackjack! Je hebt gewonnen. Je krijgt 2x je inzet terug.");
            return true;
        }
        beginSpeler = false;

        if (puntenSpeler > 21 && aantalAzenSpeler > 0) {
            puntenSpeler -= 10;
            aantalAzenSpeler--;
        }

        if (puntenSpeler > 21) {
            kapitaal -= wedBedrag;
            System.out.println("Helaas. Je hebt " + puntenSpeler + " punten. Je hebt verloren.");
            return true;
        }
        return false;
    }

    static void bankSpeelt() {
        if (beginBank) {
            trekKaart(bankDeck, "Bank");
            trekKaart(bankDeck, "Bank");
            beginBank = false;
        } else {
            trekKaart(bankDeck, "Bank");
        }
        if (!resultaatBank()) {
            bankSpeelt();
        }
    }

    static boolean resultaatBank() {
        if (puntenBank > 21 && aantalAzenBank > 1) {
            puntenBank -= 10;
            aantalAzenBank--;
        }
        if (puntenBank > 21) {
            kapitaal += wedBedrag;
            System.out.println("De bank heeft " + puntenBank + " punten. Je hebt gewonnen!");
            return true;
        }
        if (puntenBank > puntenSpeler) {
            kapitaal -= wedBedrag;
            System.out.println("De bank heeft " + puntenBank + " punten. Je hebt verloren.");
            return true;
        }
        if (puntenBank >= 17 && puntenBank == puntenSpeler) {
            System.out.println("De bank heeft " + puntenBank + " punten. Gelijkspel!");
            return true;
        }
        if (puntenBank >= 17 && puntenBank < puntenSpeler) {
            kapitaal += wedBedrag;
            System.out.println("De bank heeft " + puntenBank + " punten. Je hebt gewonnen!");
            return true;
        }
        return false;
    }


    static Deque<Kaart> createDeck() {
        List<String> kaartTypes = Arrays.asList("Harten", "Ruiten", "Klaver", "Schoppen");
        Kaart twee = new Kaart("2", 2);
        Kaart drie = new Kaart("3", 3);
        Kaart vier = new Kaart("4", 4);
        Kaart vijf = new Kaart("5", 5);
        Kaart zes = new Kaart("6", 6);
        Kaart zeven = new Kaart("7", 7);
        Kaart acht = new Kaart("8", 8);
        Kaart negen = new Kaart("9", 9);
        Kaart tien = new Kaart("10", 10);
        Kaart boer = new Kaart("Boer", 10);
        Kaart vrouw = new Kaart("Vrouw", 10);
        Kaart heer = new Kaart("Heer", 10);
        Kaart aas = new Kaart("Aas", 11);
        List<Kaart> kaartenZonderType = Arrays.asList(twee, drie, vier, vijf, zes, zeven, acht, negen, tien, boer, vrouw, heer, aas);
        List<Kaart> deckGeordend = new ArrayList<>();

        for (String kaartType : kaartTypes) {
            for (Kaart kaartZonderType : kaartenZonderType) {
                deckGeordend.add(new Kaart(kaartType + " " + kaartZonderType.kaartRang, kaartZonderType.kaartPunten));
            }
        }

        Collections.shuffle(deckGeordend);

        return new ArrayDeque<>(deckGeordend);
    }

    static Kaart trekKaart(Deque<Kaart> deck, String typeSpeler) {
        Kaart kaart = deck.pop();

        if (typeSpeler.equals("Speler")) {
            puntenSpeler += kaart.kaartPunten;
            System.out.println("Je hebt een " + kaart.kaartRang + " getrokken!");
            if (kaart.kaartRang.contains("Aas")) {
                aantalAzenSpeler++;
            }
        } else if (typeSpeler.equals("Bank")) {
            System.out.println("De bank trok een " + kaart.kaartRang);
            puntenBank += kaart.kaartPunten;
            if (kaart.kaartRang.contains("Aas")) {
                aantalAzenBank++;
            }
        }
        return kaart;
    }

    static boolean geefBedragOp() throws IOException {
        String invoer = reader.readLine();

        if (invoer.equalsIgnoreCase("q")) {
            return false;
        }
        try {
            wedBedrag = Integer.parseInt(invoer);
            if (wedBedrag < 1 || wedBedrag > kapitaal) {
                throw new RuntimeException();
            }
        } catch (RuntimeException e) {
            System.out.println("Geef een geldig rond getal op tussen de 0 en " + kapitaal);
            geefBedragOp();
        }
        return true;
    }
}

class Kaart {
    Kaart(String kaartRang, int kaartPunten) {
        this.kaartRang = kaartRang;
        this.kaartPunten = kaartPunten;
    }

    String kaartRang;
    int kaartPunten;
}