package esempi;

import pasticceria.Dipendente;
import pasticceria.Pasticceria;

public class EsempioFinale {

	public static void main(String[] args) {
		
		// creazione della pasticceria
		Pasticceria past = new Pasticceria("Zichella");
		
		System.out.println("Pasticceria: " + past.getNome());
		System.out.println();
		
		///////////////////DIPENDENTI
		
		Dipendente d1 = past.nuovoDipendente("Dipendente","Uno","LCCRSS03E48L238S"); // DIPUNO101
		Dipendente d2 = past.nuovoDipendente("Dipendente","Due","NOAGLL05E44L248F"); // DIPDUE102
		Dipendente d3 = past.nuovoDipendente("Dipendente","Tre","ABBBLU03E48L488S"); // DIPTRE103
		Dipendente d4 = past.nuovoDipendente("Dipendente","Quattro","ROSROS32E48L333S"); // DIPQUA104
		Dipendente d5 = past.nuovoDipendente("Dipendente","Cinque","LCCNCL04E06L219D"); // DIPCIN105
		Dipendente d6 = past.nuovoDipendente("Dipendente","Sei","LCCRSS03E48L111S"); // DIPSEI106
		Dipendente d7 = past.nuovoDipendente("Dipendente","Sette","GNLMRE93F30L219D"); // DIPSET107
		Dipendente d8 = past.nuovoDipendente("Dipendente","Otto","CLDFRZ33E83H938S"); // DIPOTT108
		Dipendente d9 = past.nuovoDipendente("Dipendente","Nove","VNSARN55E33F838E"); // DIPNOV109
		Dipendente d10 = past.nuovoDipendente("Dipendente","Dieci","LCCRSS44F55L219D"); // DIPDIE110
		Dipendente d11 = past.nuovoDipendente("Dipendente", "Undici", "MRCNRI88E10L219C"); // DIPUND111
		Dipendente d12 = past.nuovoDipendente("Dipendente", "Dodici", "ELNBNC92A41L219Z"); // DIPDOD112 
		
		///////////////////TURNI
		
		past.creaTurni("20260223", "20260315"); // 3 settimane da testare
		
		///////////////////RICHIESTE SPECIALI
		
		// Settimana 1
		past.creaRichiesta("20260223", "Visita medica", "DIPUNO101", 5, "m");
		past.creaRichiesta("20260225", "Udienza tribunale", "DIPDUE102", 5, "m");
		past.creaRichiesta("20260227", "Intervento idraulico", "DIPTRE103", 4, "m");
		past.creaRichiesta("20260228", "Permesso Sabato", "DIPQUA104", 5, "m");
		past.creaRichiesta("20260228", "Permesso Sabato", "DIPUNO101", 5, "m");
		past.creaRichiesta("20260225", "Medico", "DIPUND111", 5, "m");
		past.creaRichiesta("20260227", "Permesso", "DIPOTT108", 5, "m");

		// Settimana 2 
		past.creaRichiesta("20260302", "Rinnovo patente", "DIPCIN105", 4, "m");
		past.creaRichiesta("20260304", "Esami del sangue", "DIPSEI106", 5, "m");
		past.creaRichiesta("20260307", "Cena di classe", "DIPSET107", 3, "p");
		past.creaRichiesta("20260304", "Visita pediatra figli", "DIPDIE110", 2, "p");
		past.creaRichiesta("20260303", "Fisioterapia alla spalla", "DIPUNO101", 3, "p");
		past.creaRichiesta("20260304", "Visita dal dentista", "DIPDUE102", 4, "m");
		past.creaRichiesta("20260308", "Consegna auto dal meccanico", "DIPTRE103", 2, "m");
		past.creaRichiesta("20260307", "Impegno familiare importante", "DIPQUA104", 5, "p");
		
		// Settimana 3
		past.creaRichiesta("20260309", "Visita di controllo medica", "DIPUNO101", 4, "m");
		past.creaRichiesta("20260310", "Rinnovamento documenti passaporto", "DIPDUE102", 3, "m");
		past.creaRichiesta("20260311", "Esami clinici del sangue", "DIPTRE103", 5, "m");
		past.creaRichiesta("20260311", "Udienza tribunale come testimone", "DIPQUA104", 5, "p");
		past.creaRichiesta("20260312", "Riparazione guasto auto officina", "DIPCIN105", 3, "m");
		past.creaRichiesta("20260313", "Incontro con professori scuola figli", "DIPSEI106", 2, "p");
		past.creaRichiesta("20260313", "Visita specialistica cardiologica", "DIPSET107", 5, "m");
		past.creaRichiesta("20260314", "Impegno associativo/scout", "DIPOTT108", 3, "p");
		past.creaRichiesta("20260314", "Trasloco nuova abitazione", "DIPNOV109", 4, "m");

		///////////////////SETTIMANE
		
		past.creaSettimane();
		
		///////////////////ASSEGNAZIONE SETTIMANE
		
		past.assegnaSettimane(past.getSettimane());
		
		

	}

}
