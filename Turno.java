package pasticceria;

import java.util.ArrayList;

public class Turno {
	
	private String data;
	private String tipo; // il tipo rappresenta il turno di mattina 'M' o pomeriggio 'P'
	private int edificio; // 0 per edificio in 'via saluzzo' , 1 per edificio in 'via madama cristina'

	private String codice; // per identificare il turno è data + '-' + tipo + '-' + numeroEdificio
	
	@Override
	public boolean equals(Object obj) {
		Turno t = (Turno)obj;
		if(this.codice.equals(t.codice)) return true;
		else return false;
	}

	
	public int getEdificio() {
		return edificio;
	}


	private ArrayList<RichiestaSpeciale> richieste = new ArrayList();
	private ArrayList<Dipendente> dipendenti = new ArrayList();

	
	public ArrayList<Dipendente> getDipendenti() {
		return dipendenti;
	}

	public Turno(String data, String tipo, int edificio) {
		this.data = data;
		this.tipo = tipo;
		this.edificio = edificio;
		this.codice = data + "-" + tipo + "-" + edificio;
	}

	public ArrayList<RichiestaSpeciale> getRichieste() {
		return richieste;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}
}
