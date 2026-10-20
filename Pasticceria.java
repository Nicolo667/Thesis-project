package pasticceria;

import java.util.*;


import java.time.*;
import java.time.format.*;


public class Pasticceria {
	
	private String nome;
	private int codiceId = 101;
	private int numeroSedi = 2;
	
	private TreeMap<String,Dipendente> dipendentiMappa = new TreeMap();
	private ArrayList<Dipendente> dipendenti = new ArrayList();
	private TreeMap<String,Dipendente> dipendentiCodici = new TreeMap();
	
	private TreeMap<String,Turno> turni = new TreeMap();
	private ArrayList<String> date = new ArrayList();
	
	private ArrayList<Settimana> settimane = new ArrayList();
	private TreeMap<String,SlotOrario> slots = new TreeMap();
	
	/// private ArrayList<Dipendente> dipendentiDomenica = new ArrayList();
	
	public ArrayList<String> getDate() {
		return date;
	}

	public Pasticceria(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}		

	public ArrayList<Dipendente> getDipendenti() {
		return dipendenti;
	}

	public TreeMap<String, Turno> getTurni() {
		return turni;
	}
	

	public ArrayList<Settimana> getSettimane() {
		return settimane;
	}

	public TreeMap<String, SlotOrario> getSlots() {
		return slots;
	}

	
	//////////////////////////////////////////////////////DIPENDENTI//////////////////////////////////////////////////////////////////////////
	
	// funzione per creare l'id del dipendente formato dalle prime 3 lettere del nome dalle prime 3 del cognome e dal numero che incrementa a ogni utente inserito.
	public String creaId(String nome, String cognome) {
		String codId = "";
		if(nome.length() <=3 && cognome.length()<=3) codId = nome + cognome + codiceId;
		if(nome.length() > 3 && cognome.length() <= 3) codId = nome.substring(0, 3) + cognome + codiceId;
		if(nome.length() <= 3 && cognome.length() > 3) codId = nome + cognome.substring(0,3) + codiceId;
		if(nome.length() > 3 && cognome.length() > 3) codId = nome.substring(0,3) + cognome.substring(0,3) + codiceId;
		codiceId++;
		return codId.toUpperCase();		
	}
	
	// funzione in grado di aggiungere un dipendente alla pasticceria
	public Dipendente nuovoDipendente(String nome, String cognome, String codiceFiscale) {
		if(dipendentiMappa.containsKey(codiceFiscale)) return null; // in interfaccia, messaggio di stampa 'errore: utente già definito'
		String id = creaId(nome, cognome);
		Dipendente dipendente = new Dipendente(nome,cognome,codiceFiscale,id);
		dipendentiMappa.put(codiceFiscale, dipendente);
		dipendenti.add(dipendente);
		dipendentiCodici.put(id, dipendente);
		return dipendente;
	}
	
	public Dipendente cercaDipendente(String codiceDipendente) {
		if(dipendentiCodici.containsKey(codiceDipendente)) return dipendentiCodici.get(codiceDipendente);
		else return null; // se non lo trova ritorna null 
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	//////////////////////////////////////////////////////TURNI//////////////////////////////////////////////////////////////////////////
	// vengono creati in autonomia tutti i turni di un determinato periodo
	// oltre che creare i turni vengono creati anche gli slot orari, che serviranno successivamente per l'assegnazione dei dipendenti ai turni.
	
	public boolean creaTurni(String dataInizio, String dataFine) {
		// da inserire correttamente un giorno d'inizio che sia lunedì e un giorno di fine che sia domenica.
		DateTimeFormatter mioFormato = DateTimeFormatter.ofPattern("yyyyMMdd");
		
		
		try {
	    	// utile perchè con i localDate possiamo evitare di gestire noi il calendario
	    	LocalDate inizio = LocalDate.parse(dataInizio,mioFormato);
	    	LocalDate fine = LocalDate.parse(dataFine,mioFormato);

	        // Controllo che la data di inizio sia precedente o uguale alla fine
	        if (inizio.isAfter(fine)) {
	            return false; //ritorno false ovvero qualcosa è andato storto
	        }
	        
	        // controllo che la data di inizio sia lunedì
	        if (inizio.getDayOfWeek() != java.time.DayOfWeek.MONDAY) {
	            return false;
	        }

	        // controllo che la fine sia una domenica
	        if (fine.getDayOfWeek() != java.time.DayOfWeek.SUNDAY) {
	            return false;
	        }

	        //Incrementiamo la data e creiamo i turni
	        LocalDate dataCorrente = inizio;
	        while (!dataCorrente.isAfter(fine)) {
	        	
	        	// cambio il localDate in stringa.
	            String dataStringa = dataCorrente.format(mioFormato);
	            date.add(dataStringa);
	            java.time.DayOfWeek giornoSettimana = dataCorrente.getDayOfWeek(); // serve perchè devo togliere le domeniche pomeriggio (a entrambe le sedi) e il lunedì alla sede di via madama
	            
	            for(int i=0;i<this.numeroSedi;i++) {
	            	           	
	            	// con il codice successivamente implementato riusciamo a soddisfare tutti i vincoli che riguardano i giorni di chiusura di entrambe le sedi della pasticceria
	            	// sede 0 ( via saluzzo ) chiude solo domenica pomeriggio, sede 1 ( via madama ) chiude domenica pomeriggio e tutto lunedì
	            	boolean saltaMattina = (i==1 && giornoSettimana == java.time.DayOfWeek.MONDAY);
	            	
	            	if(!saltaMattina) {
	            		Turno turnoMattina = new Turno(dataStringa, "M", i);
	            		if(slots.containsKey(dataStringa+"-M")) {
	            			SlotOrario so = slots.get(dataStringa+"-M");
	            			so.getTurni().add(turnoMattina);
	            		}
	            		else {
	            			SlotOrario slot = new SlotOrario(dataStringa+"-M",dataStringa,"M");
	            			slot.getTurni().add(turnoMattina);
	            			slots.put(dataStringa+"-M", slot);
	            		}
	            		turni.put(turnoMattina.getCodice(), turnoMattina);
	            	}
	            	
	            	boolean saltaPomeriggio = (giornoSettimana == java.time.DayOfWeek.SUNDAY) || (i == 1 && giornoSettimana == java.time.DayOfWeek.MONDAY);
	            	
	            	if(!saltaPomeriggio) {
	            		Turno turnoPomeriggio = new Turno(dataStringa, "P", i);
	            		if(slots.containsKey(dataStringa+"-P")) {
	            			SlotOrario so = slots.get(dataStringa+"-P");
	            			so.getTurni().add(turnoPomeriggio);
	            		}
	            		else {
	            			SlotOrario slot = new SlotOrario(dataStringa+"-P",dataStringa,"P");
	            			slot.getTurni().add(turnoPomeriggio);
	            			slots.put(dataStringa+"-P", slot);
	            		}
	            		turni.put(turnoPomeriggio.getCodice(), turnoPomeriggio);
	            	}
	            		            	
	            }

	            dataCorrente = dataCorrente.plusDays(1);
	        }

	        return true;
		}catch(DateTimeParseException e) {
	        // Se le stringhe in ingresso sono scritte male 
	        System.err.println("Errore nel formato delle date in ingresso: " + e.getMessage()); // stampa un messaggio di errore nella console
	        return false;
		}
	}
	
	public Turno cercaTurno(String data, String tipo, String luogo) {
		int numLuogo = -1;
		if(luogo.toUpperCase().equals("MADAMA")) numLuogo = 1;
		else if(luogo.toUpperCase().equals("SALUZZO")) numLuogo = 0;
		String codiceTurno = data + "-" + tipo.toUpperCase() + "-" + numLuogo;
		if(this.turni.containsKey(codiceTurno)) return this.turni.get(codiceTurno);
		else return null;
	}
	
	// creata ora una funzione in grado di rimuovere un turno (per qualsiasi ragione: feste, manutenzione...)
	
	public void rimuoviTurno(String data, String tipo, String luogo) {
		Turno turnoTemp = cercaTurno(data,tipo.toUpperCase(),luogo);
		if(turnoTemp != null) turni.remove(turnoTemp.getCodice());
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	//////////////////////////////////////////////////////RICHIESTE//////////////////////////////////////////////////////////////////////////
	
	// ATTENZION: molto importante è che non si possono fare richieste speciali per domenica! 
	public void creaRichiesta(String data, String descrizione, String codiceDipendente, int importanza, String tipoTurno) {
		// cerca tutti i turni con quella data e aggiunge la richiesta speciale a quel determinato turno
		
		/*
		// blocco per eliminare le richieste di domenica
		try {
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
	        LocalDate localDate = LocalDate.parse(data, formatter);
	        
	        if (localDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
	            // se la data è una domenica allora usciamo dal codice senza problemi 
	            return; 
	        }
	    } catch (Exception e) {
	        System.out.println("Formato data non valido: " + data);
	        return; // Evita di crashare se la data è formattata male
	    }
		// fine blocco per eliminare le richieste di domenica
		*/
		
		Dipendente dipendente = cercaDipendente(codiceDipendente);
		if(dipendente != null) {
			for(Turno turno : this.turni.values()) {
				if(turno.getData().equals(data) && tipoTurno.length() > 1) {
					if(importanza > 5) importanza = 5;
					if(importanza < 1) importanza = 1;
					RichiestaSpeciale richiesta = new RichiestaSpeciale(data,descrizione,dipendente,importanza);
					dipendente.getRichieste().add(richiesta);
					turno.getRichieste().add(richiesta);					
				}
				if(turno.getData().equals(data) && turno.getTipo().equals(tipoTurno.toUpperCase())) {
					if(importanza > 5) importanza = 5;
					if(importanza < 1) importanza = 1;
					RichiestaSpeciale richiesta = new RichiestaSpeciale(data,descrizione,dipendente,importanza);
					dipendente.getRichieste().add(richiesta);
					turno.getRichieste().add(richiesta);
				}
			}
		}		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	//////////////////////////////////////////////////////SCOMPOSIZIONE IN SETTIMANE//////////////////////////////////////////////////////////////////////////

	// può tornare utile per un controllo
	public ArrayList<SlotOrario> cercaSlotData(String data) {
		ArrayList<SlotOrario> slots = new ArrayList();
		
		for(SlotOrario so : this.slots.values()) {
			if(so.getData().equals(data)) slots.add(so);
		}
		
		return slots;
	}
	
	public void creaSettimane() {
		int cicli = this.date.size()/7;
		
		for(int i=0; i<cicli; i++) {
			// dentro i cicli creo le settimane su cui lavorare con tutti i turni.
			int indiceInizioSettimana = i * 7;
			
			Settimana sett = new Settimana();
			
			for(int j=0; j<7; j++) {
				String dataTemp = this.date.get(indiceInizioSettimana+j);
				sett.getDate().add(dataTemp);
				ArrayList<SlotOrario> slot = this.cercaSlotData(dataTemp);
				// ho tutti i turni di quella data, sia mattine che pomeriggi
				for(SlotOrario so : slot) {
					sett.getSlot().add(so);
				}
			}
			settimane.add(sett);
		}
		
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	//////////////////////////////////////////////////////ASSEGNAZIONE DEI TURNI//////////////////////////////////////////////////////////////////////////
	
	// serve a capire se un determinato giorno è una domenica (che deve essere gestita in modo differente)
	/*
		public boolean domenica(String dataStringa) {
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
	    
	    try {
	        LocalDate data = LocalDate.parse(dataStringa, formatter);
	        // controlla se il giorno della settimana è domenica 
	        return data.getDayOfWeek() == DayOfWeek.SUNDAY;
	        
	    } catch (DateTimeParseException e) {
	        System.err.println("Formato della data non valido " + dataStringa);
	        return false;
	    }
	}	
	*/
	
	// controlla se un dipendente ha riposato almeno un giorno a settimana
	public boolean verificaRiposoSettimanale(Settimana settimana) {
		ArrayList<SlotOrario> slots = settimana.getSlot();
		
		//ciclo su tutti i dipendenti della pasticceria
		for(Dipendente d: this.dipendenti) {
			boolean haGiornoLibero = false;
			d.setRiposo(false); // inizializzo nessuno con giorno libero
			
			// controllo ogni data della settimana (memorizzate nella settimana stessa)
			for(String data : settimana.getDate()) {
				if(!dipendenteLavoraInData(d,data,slots)) {
					haGiornoLibero = true;
					d.setRiposo(true);
					break;
				}
			}
			if(!haGiornoLibero) return false;
		}
		return true;
	}
	
	// controlla se un dipendente sta già lavorando in una determinata data 
	public boolean dipendenteLavoraInData(Dipendente d, String data, ArrayList<SlotOrario> slots) { // se dipendente non lavora in quella data allora vuol dire che ha un giorno di riposo
		for(SlotOrario s : slots) {
			if(s.getData().equals(data)) {
				if(s.getDipendenti().contains(d)) return true;
			}
		}
		return false;
	}
	
	// funzione che decide quali dipendenti (tra quelli che saranno possibili) assegnare allo slot (in modo randomico, ma con dei criteri) 
	public ArrayList<Dipendente> selezionaDipendenti(ArrayList<Dipendente> dipendentiPossibili, int numeroDipendenti){
		Random rand = new Random();
        int numeroCasuale = rand.nextInt(4); // genera un numero casuale: 0, 1, 2
        
        if(numeroCasuale == 0) { // scelgo i dipendenti in base alle ore di lavoro già effettuate in settimana
        	Collections.sort(dipendentiPossibili,new ComparareDipendentiOreLavoro());
        	ArrayList<Dipendente> selezionati = new ArrayList<>(dipendentiPossibili.subList(0, numeroDipendenti));
        	return selezionati;        	
        }
        
        else if(numeroCasuale == 1) { // modo completamente casuale 
        	Collections.shuffle(dipendentiPossibili);    
        	ArrayList<Dipendente> selezionati = new ArrayList<>(dipendentiPossibili.subList(0, numeroDipendenti));
        	return selezionati; 
        }
        
        else {
        	Collections.sort(dipendentiPossibili,new ComparareDipendentiRichieste());
        	ArrayList<Dipendente> selezionati = new ArrayList<>(dipendentiPossibili.subList(0, numeroDipendenti));
        	return selezionati;    
        }
        
	}
	
	// questa funzione è il cuore del programma, assegna i dipendenti alle varie settimane in modo da rispettare tutti i vincoli richiesti.
	public boolean assegnaDipendenteSettimana(Settimana settimana, int indiceSlot, int limiteOre) { // questo è il modo di assegnazione per una singola settimana
		// indiceSlot è la variabile che ci permette di capire a quale slot siamo della lista turni e in caso ci permette di 'tornare indietro', dovrà partire dunque da 0.		
		ArrayList<SlotOrario> slots = settimana.getSlot();
		
		if(indiceSlot == slots.size()) { // è l'ultimo slot
			// controlliamo che tutti i dipendenti abbiano riposato
			if(!verificaRiposoSettimanale(settimana)) return false;
			
			return true; // caso in cui tutti i turni della settimana vengono completati, RISULTATO CORRETTO E ALGORITMO CONCLUSO!
		}
		
		SlotOrario slotAttuale = slots.get(indiceSlot); // partirà per forza da 0, seleziono lo slot attuale.
		
		ArrayList<Dipendente> dipendentiCandidati = new ArrayList(); // qua dovrò aggiungere tutti i dipendenti che possono lavorare in quel determinato turno
		
		/// boolean check = this.domenica(slotAttuale.getData()); // se lo slot è di domenica
				
		// controlli sui singoli dipendenti, se possono lavorare o no in quel determinato slot 
		for(Dipendente d : this.dipendenti) {
			d.richiesteSettimana(settimana); // conto quante richieste hanno in questa settimana
			// tutti i controlli che si devono fare per ogni singolo dipendente se restituisce true allora vuol dire che quel dipendente può lavorare
			boolean checkDomenica = true;
			/// if(check)checkDomenica = d.controlloDomenica();
			boolean checkOreDisponibili = d.controlloOreDisponibili(slotAttuale.getTipo(), limiteOre);
			boolean checkOreConsecutive = d.controlloOreConsecutive(slotAttuale);
			
			// controllo del turno precedente (se è nella stessa sede o in sede diversa
			boolean checkSede = false;
			for(Turno t : slotAttuale.getTurni()) {
		        if(d.controlloStessaSede(slotAttuale.getData(), t.getEdificio())) {
		            checkSede = true; 
		            break; // esco appena un iterazione porta a true
		        }
		    }
			boolean checkRichiesta = d.controlloRichieste(slotAttuale);
			
			if(checkDomenica && checkOreDisponibili && checkOreConsecutive && checkRichiesta && checkSede) {
				// il dipendente va bene, lo possiamo dunque aggiungere ai dipendenti che vanno bene
				dipendentiCandidati.add(d);
			}
		}
			
		// ho bisogno di sapere il numero di dipendenti che mi serve selezionare
		int numDipendenti = slotAttuale.getTurni().size() * 3; // può variare ad esempio il lunedì sono di meno perchè una sede è chiusa al pomeriggio
		if(dipendentiCandidati.size() < numDipendenti) return false; // se non posso soddisfare il numero di dipendenti di cui ho bisogno c'è un problema e ritorno false
			
		// inizio un ciclo while che permette più tentativi quando 'torno indietro'
		int tentativo = 0;
		
		while(tentativo < 4) { //PIU AUMENTO PIU' TEMPO CI METTERA' ma più probabilità ci sara di trovare una soluzione senza l'utlilizzo di straordinari!
			// decido quali dipendenti prendere in base a casualità
			ArrayList<Dipendente> dipendentiSelezionati = selezionaDipendenti(dipendentiCandidati,numDipendenti);
			
			// ho la lista dei dipendenti selezionati per il turno e di quelli non selezionati
			if(dipendentiSelezionati.size() == 6) {
				// dobbiamo metterne 3 in un turno e 3 nell'altro 
				ArrayList<Dipendente> dipSel1 = new ArrayList<>();
				ArrayList<Dipendente> dipSel2 = new ArrayList<>();
				
				Turno t1 = slotAttuale.getTurni().get(0); // via saluzzo
			    Turno t2 = slotAttuale.getTurni().get(1); // via madama 
			    
			    // Distribuiamo i 6 dipendenti selezionati
			    for (Dipendente d : dipendentiSelezionati) {
			    	// controllo se possono lavorare
			    	boolean puoStareInTurno1 = d.controlloStessaSede(slotAttuale.getData(), t1.getEdificio());
			    	boolean puoStareInTurno2 = d.controlloStessaSede(slotAttuale.getData(), t2.getEdificio());;
			    	
			        // Se il dipendente può stare solo nella sede del primo turno (t1)
			    	if(puoStareInTurno1 && !puoStareInTurno2) {
			    		if (dipSel1.size() < 3) dipSel1.add(d);
				        else return false;
			    	}
			    	// Se può stare solo in T2
			        else if (!puoStareInTurno1 && puoStareInTurno2) {
			            if (dipSel2.size() < 3) dipSel2.add(d);
			            else return false; // troppa gente per T2
			        }
			        else {
			            if (dipSel1.size() < 3) dipSel1.add(d);
			            else if (dipSel2.size() < 3) dipSel2.add(d);
			            else return false;
			        }
			    }
			    
			    if (dipSel1.size() != 3 || dipSel2.size() != 3) {
			        // Questo farà sì che il ciclo while passi al tentativo successivo o faccia backtracking
			        return false; 
			    }
			    
				// assegno i dipendenti ai vari turni e i turni ai vari dipendenti, anche i dipendenti allo slot, aggiuorno tutti gli oggetti
				for(Dipendente d1 : dipSel1) {
					t1.getDipendenti().add(d1);
					d1.getTurni().add(t1);
					slotAttuale.getDipendenti().add(d1);
					
					// aggiornamenti al dipendente
					if(slotAttuale.getTipo().equals("M")) {
						d1.setOreLavoro(d1.getOreLavoro() + 7); //aggiorno le ore di lavoro della settimana
					}
					else {
						d1.setOreLavoro(d1.getOreLavoro() + 6);
					}
				}
				for(Dipendente d2 : dipSel2) {
					t2.getDipendenti().add(d2);
					d2.getTurni().add(t2);
					slotAttuale.getDipendenti().add(d2);
					
					// aggiornamenti al dipendente
					if(slotAttuale.getTipo().equals("M")) {
						d2.setOreLavoro(d2.getOreLavoro() + 7); //aggiorno le ore di lavoro della settimana
					}
					else {
						d2.setOreLavoro(d2.getOreLavoro() + 6);
					}
				}
			}
			else {
				// se c'è solamente un turno solo, ovvero i dipendenti selezionati non sono 6. 
				for(Dipendente di : dipendentiSelezionati) {
					Turno t = slotAttuale.getTurni().get(0);
					t.getDipendenti().add(di);
					di.getTurni().add(t);
					slotAttuale.getDipendenti().add(di);
					
					// aggiornamenti al dipendente
					if(slotAttuale.getTipo().equals("M")) {
						di.setOreLavoro(di.getOreLavoro() + 7); //aggiorno le ore di lavoro della settimana
					}
					else {
						di.setOreLavoro(di.getOreLavoro() + 6);
					}
				}
			}
			
			/*
			if(check) { // se è domenica aggiungo i dipendenti selezionati alla domenica 
				for(Dipendente d3 : dipendentiSelezionati) {
					if(!dipendentiDomenica.contains(d3)) {
						dipendentiDomenica.add(d3);
					}
				}
			}
			*/
			
			// provo ad assegnare lo slot successivo
			if (assegnaDipendenteSettimana(settimana, indiceSlot + 1, limiteOre)) {
			    return true; // se riesce va tutto bene.
			}
			
			
			// BACKTRACKING.
			// se arrivo qua vuol dire che qualcosa è andato storto, devo resettare tutto quello che avevo fatto in questa funzione.
			ArrayList<Turno> turniDelloSlot = slotAttuale.getTurni();
			
			//System.out.println("Ho attivato il BACKTRACK"); //serve a me per un controllo.
			
			// resetto i dipendenti dello slot
			slotAttuale.getDipendenti().clear();
			
			// tolgo i dipendenti assegnati dai turni
			for(Turno turno : turniDelloSlot) {
				turno.getDipendenti().clear();
			}
			
			// tolgo le ore di lavoro aggiunte (resetto i dipendenti)
			for(Dipendente dipendente : dipendentiSelezionati) {
				if(slotAttuale.getTipo().equals("M")) { // riporto le ore di lavoro al valore precedente
					dipendente.setOreLavoro(dipendente.getOreLavoro() - 7);
				}
				else {
					dipendente.setOreLavoro(dipendente.getOreLavoro() - 6);
				}
				
				// tolgo i turni dai dipendenti
				for(Turno turno : turniDelloSlot) {
					if(dipendente.getTurni().contains(turno)) dipendente.getTurni().remove(turno);
				}
			}
			
			/*
			// tolgo i dipendenti dalla domenica
			if(check) {
				for(Dipendente d : dipendentiSelezionati) {
					if(this.dipendentiDomenica.contains(d)) this.dipendentiDomenica.remove(d);
				}
			}
			*/
			
			tentativo++; // riprovo...
		}
		
		return false;
		
		
	}
	
	// questa è la funzione che per ciascuna settimana proverà ad aggiungere i dipendenti (richiamando la funzione appena scritta)
	public void assegnaSettimane(ArrayList<Settimana> settimane) {
		int indiceSettimana = 1;
		for(Settimana sett : settimane) {
			// pulizia dei dipendenti prima di ogni settimana
			for(Dipendente d : this.dipendenti) {
				d.setOreLavoro(0);
				d.setOreStraordinario(0);
			}
			
			// pulizia dei dipendenti che lavorano di domenica
			/// this.dipendentiDomenica.clear();
			// inizio l'algoritmo
			boolean successo = assegnaDipendenteSettimana(sett,0,40);  // 40 è il limite di ore a settimana standard 
			
			if(!successo) { // se non riesco con 40 ore provo con 48, dando la possibilità ai dipendenti di svolgere straordinari (posso abbassare le ore o aumentarle a piacimento, senza violare vincoli contratttuali)
				// non riesco a riempire la settimana
				System.out.println("Soluzione impossibile a 40 ore. Proviamo con Straordinari...");
				// pulizia
				for(Dipendente d : this.dipendenti) {
					d.setOreLavoro(0);
					d.setOreStraordinario(0);
				}
				
				// pulizia dei dipendenti che lavorano di domenica
				/// this.dipendentiDomenica.clear();
				
				successo = assegnaDipendenteSettimana(sett, 0, 48);				
			}
			
			if(successo) {
				// se va tutto bene per l'assegnazione dei dipendenti aggiorno le domeniche 
				for(Dipendente d : this.dipendenti) {
					// controllo anche se ha straordinari
					if(d.getOreLavoro() > 40) {
			            int oreExtra =(int) d.getOreLavoro() - 40; // calcolo di quanto straordinario hanno lavorato i dipendenti
			            d.setOreStraordinario(oreExtra);
			            sett.getStraordinari().add(d); // Aggiunge alla lista della settimana per la stampa
			        } else {
			            d.setOreStraordinario(0);
			        }
					// aggiorno le domeniche
					/*
	                if(this.dipendentiDomenica.contains(d)) {
	                    d.setDomenica(true);
	                } else {
	                    d.setDomenica(false);
	                }
	                */
				}
				System.out.println();
				System.out.println("Settimana numero: " + indiceSettimana);
				indiceSettimana = indiceSettimana + 1;
				System.out.println(sett.toString());
				System.out.println();
				/*
				System.out.println(); // serve a me per controllare la domenica se viene estita in modo corretto
				System.out.println("Dipendenti domenica: ");
	
				for(Dipendente d : this.dipendentiDomenica) {
					System.out.println(d.getNome() + " " + d.getCognome() + " " + d.getId());
				}
				*/
				System.out.println();
				
				System.out.println("Dipendenti con straordinari:");
				for(Dipendente d : sett.getStraordinari()) {
					System.out.println(d.getNome() + " " + d.getCognome() + " " + d.getId() + " " + d.getStraordinari() + " ore");
				}
				System.out.println();
				
				
				System.out.println("Settimana organizzata con successo");
			}
			else {
	            System.out.println("Settimana impossibile anche con straordinari.");
	        }			
		}
	}

	/*
	public ArrayList<Dipendente> getDipendentiDomenica() {
		return dipendentiDomenica;
	}
	*/
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}


