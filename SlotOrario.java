package pasticceria;

import java.util.*;

public class SlotOrario {

	private String codiceSlot;
	private ArrayList<Turno> turni;
	private String data;
	private String tipo;
	private ArrayList<Dipendente> dipendenti;
	
	public SlotOrario(String codiceSlot,String data, String tipo) {
		this.codiceSlot = codiceSlot;
		this.turni = new ArrayList();
		this.data = data;
		this.dipendenti = new ArrayList();
		this.tipo = tipo;
	}

	public String getTipo() {
		return tipo;
	}

	public ArrayList<Dipendente> getDipendenti() {
		return dipendenti;
	}

	public String getData() {
		return data;
	}

	public String getCodiceSlot() {
		return codiceSlot;
	}

	public ArrayList<Turno> getTurni() {
		return turni;
	}
	
	
	
}
