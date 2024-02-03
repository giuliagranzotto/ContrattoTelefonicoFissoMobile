package it.contrattotelefonico2.main;

import it.contrattotelefonico2.model.ContrattoFisso;
import it.contrattotelefonico2.model.ContrattoMobile;

public class MainContrattoTelefonico {
	public static void main(String[] args) {
		ContrattoFisso fisso = new ContrattoFisso("0110458367", "Davide Fisso", "Torino");
        ContrattoMobile mobile = new ContrattoMobile("3395204697", "Manuela Mobile");

        fisso.aggiungiTelefonateCasuali(10);
        mobile.aggiungiTelefonateCasuali(10);

        fisso.stampaBollettaPDF();
        mobile.stampaBollettaPDF();
        
        System.out.println();
        fisso.stampaBollettaConsole();
        System.out.println();
        mobile.stampaBollettaConsole();
    }

}
