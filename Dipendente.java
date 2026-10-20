package pasticceria;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;


public class Dipendente {
	
	private String nome;

	private String cognome;
	private String codiceFiscale;
	
	private String id;
	
	private double oreLavoro;
	private double oreStraordinario;
	
	private boolean riposo; // è true se ha riposato almeno un giorno della settimana
	private boolean domenica; // è true se ha lavorato da domenica precedente
	
	private ArrayList<RichiestaSpeciale> richieste = new ArrayList();
	private ArrayList<Turno> turni = new ArrayList(); 
	
	private int richiesteSettimana = 0;
	

	@Override
	public boolean equals(Object obj) {
		Dipendente d = (Dipendente) obj;
		if(this.id.equals(d.id)) return true;
		else return false;
	}

	public ArrayList<Turno> getTurni() {
		return turni;
	}

	public Dipendente(String nome, String cognome, String codiceFiscale, String id) {
		this.nome = nome;
		this.cognome = cognome;
		this.codiceFiscale = codiceFiscale;
		this.id = id;
		this.oreLavoro = 0.0;
		this.oreStraordinario = 0.0;
		this.riposo = false;
		this.domenica = false;
	}	

	@Override
	public String toString() {
		return nome + ", " + cognome + ", " + codiceFiscale + ", codice: " + id
				+ ", oreLavoro:" + oreLavoro + ", oreLavoroConsecutive:" + ", oreStraordinario:"
				+ oreStraordinario + ", riposo:" + riposo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getOreLavoro() {
		return oreLavoro;
	}

	public void setOreLavoro(double oreLavoro) {
		this.oreLavoro = oreLavoro;
	}


	public double getOreStraordinario() {
		return oreStraordinario;
	}

	public boolean isDomenica() {
		return domenica;
	}

	public void setDomenica(boolean domenica) {
		this.domenica = domenica;
	}

	public void setOreStraordinario(double oreStraordinario) {
		this.oreStraordinario = oreStraordinario;
	}

	public boolean isRiposo() {
		return riposo;
	}

	public void setRiposo(boolean riposo) {
		this.riposo = riposo;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	
	public int getStraordinari() {
		if(this.oreLavoro > 40) {
			int ol = (int) this.oreLavoro;
			this.setOreStraordinario(ol);
			return ol - 40;
		}
		else {
			return 0;
		}
	}

	public ArrayList<RichiestaSpeciale> getRichieste() {
		return richieste;
	}
	
	// tutti i controlli per verificare se un dipendente può lavorare o no
	
	/*
	public boolean controlloDomenica() { 
		if(this.domenica) return false; // se domenica è true vuol dire che ha lavorato la scorsa e dunque non può lavorare questa
		else return true;
	}
	*/
	
	public boolean controlloOreDisponibili(String tipoTurno, int limiteOre) { // il limite ore senrve a gestire lo straordinario
		if(tipoTurno.equals("M")) {
			if(this.oreLavoro + 7 > limiteOre) return false;
			else return true;
		}
		else {
			if(this.oreLavoro + 6 > limiteOre)return false;
			else return true;
		}
	}
	
	public String calcolaDataPrecedente(String dataStringa) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
	    LocalDate data = LocalDate.parse(dataStringa, formatter);
	    return data.minusDays(1).format(formatter);
	}
	
	// metodo per far si che se un dipendente lavora la mattina in una sede il pomeriggio non si sposti all'altra sede
	public boolean controlloStessaSede(String data, int edificio) {
	    // Ciclo sui turni già assegnati al dipendente
	    for (Turno t : this.getTurni()) {
	        //dipendente lavora già in questa data
	        if (t.getData().equals(data)) {
	            // Se la sede del turno già assegnato è diversa da quella richiesta
	            if (t.getEdificio() != edificio) {
	                return false; //false = non può cambiare sede!
	            }
	        }
	    }
	    // Se non lavora in quel giorno o se lavora nella stessa sede è Ok
	    return true;
	}
	
	public boolean controlloOreConsecutive(SlotOrario s) {
		// due controlli:
		// 1) se dip ha fatto ieri M e ieri P non può fare oggi M
		// 2) se dip ha fatto ieri P e oggi M non può fare oggi P
		String dataOggi = s.getData();
		String tipoOggi = s.getTipo();
		String dataIeri = calcolaDataPrecedente(dataOggi);
		
		// possibili turni effettuati
		boolean mattinaIeri = false;
		boolean pomeriggioIeri = false;
		boolean mattinaOggi = false;
		
		for(Turno t : this.getTurni()) {
			if(t.getData().equals(dataOggi) && t.getTipo().equals("M")) {
				mattinaOggi = true;
			}
			if(t.getData().equals(dataIeri) && t.getTipo().equals("M")) {
				mattinaIeri = true;
			}
			if(t.getData().equals(dataIeri) && t.getTipo().equals("P")) {
				pomeriggioIeri = true;
			}
		}
		
		// ora analizzo i due casi
		if(tipoOggi.equals("M")) {
			if(mattinaIeri && pomeriggioIeri) return false;
		}
		if(tipoOggi.equals("P")) {
			if(pomeriggioIeri && mattinaOggi) return false;
		}
		
		return true;
	}
	
	public boolean controlloStessoOrario(String data,String tipoTurno) {
		for(Turno t : this.turni) {
			if(data.equals(t.getData()) && tipoTurno.equals(t.getTipo())) return false;
		}
		return true;
	}
	
	public boolean controlloRichieste(SlotOrario so) {
		for(Turno t : so.getTurni()) {
			for(RichiestaSpeciale rs : t.getRichieste()) {
				if(rs.getDipendente().getId().equals(this.getId())) return false;
			}
		}
		return true;
	}
	
	// per calcolare il numero di richieste in una settimaa
	public void richiesteSettimana(Settimana settimana) {
		int conteggio = 0;
		for(SlotOrario so : settimana.getSlot()) {
			for(Turno t : so.getTurni()) {
				for(RichiestaSpeciale rs : this.richieste) {
					if(t.getRichieste().contains(rs)) conteggio++;
				}
			}
		}
		this.richiesteSettimana = conteggio;
	}

	public int getRichiesteSettimana() {
		return richiesteSettimana;
	}

}

class ComparareDipendentiOreLavoro implements Comparator<Dipendente> {

	@Override
	public int compare(Dipendente d1, Dipendente d2) {
		int o1 = (int) d1.getOreLavoro();
		int o2 = (int) d2.getOreLavoro();
		return o2 - o1;
	}
	
}

class ComparareDipendentiRichieste implements Comparator<Dipendente> {

	@Override
	public int compare(Dipendente d1, Dipendente d2) {
		int r1 = d1.getRichiesteSettimana();
		int r2 = d2.getRichiesteSettimana();
		return r2 - r1;
	}
	
}
