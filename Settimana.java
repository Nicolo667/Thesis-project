package pasticceria;

import java.util.ArrayList;

public class Settimana {

	private ArrayList<SlotOrario> slots = new ArrayList();
	private ArrayList<String> date = new ArrayList();
	// devono esserci sia i turni di via saluzzo che quelli di via madama
	
	private ArrayList<Dipendente> straordinari = new ArrayList();

	public ArrayList<Dipendente> getStraordinari() {
		return straordinari;
	}

	public ArrayList<String> getDate() {
		return date;
	}

	public ArrayList<SlotOrario> getSlot() {
		return this.slots;
	}

	@Override
	public String toString() {
		String elenco = "";
		for(SlotOrario s : this.slots) {
			elenco = elenco + "  - slot -  " + s.getCodiceSlot() + "\n";
			for(Turno t : s.getTurni()) {
				String edificio = "";
				if(t.getEdificio() == 0) edificio = "Via Saluzzo";
				else edificio = "Via Madama";
				elenco = elenco +"    - turno -    "+ t.getData() + " " + t.getTipo() + " " + edificio + "\n";
				for(Dipendente d : t.getDipendenti()) {
					elenco = elenco + "        - dipendente -        " + d.getNome() + " " + d.getCognome() + " " + d.getId() + "\n";
				}
			}
			elenco += "\n";
		}
		
		
		return elenco;
	}
	
	
}
