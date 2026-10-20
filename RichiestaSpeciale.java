package pasticceria;

public class RichiestaSpeciale {
	
	private String data;
	private String descrizione;
	private int importanza; //numero da 1 a 5
	private Dipendente dipendente;
	private String tipo;
	
	public RichiestaSpeciale(String data, String descrizione, Dipendente dipendente, int importanza) {
		this.data = data;
		this.descrizione = descrizione;
		this.importanza = importanza;
		this.dipendente = dipendente;
	}
	
	public Dipendente getDipendente() {
		return dipendente;
	}
	public String getData() {
		return this.data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public int getImportanza() {
		return importanza;
	}
	public void setImportanza(int importanza) {
		this.importanza = importanza;
	}

	@Override
	public String toString() {
		return "RichiestaSpeciale: " + this.descrizione + " " + this.data + " " + this.dipendente.getId(); 
	}


}
