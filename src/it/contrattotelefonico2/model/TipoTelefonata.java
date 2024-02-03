package it.contrattotelefonico2.model;

import java.util.Locale;

public enum TipoTelefonata {
	INGRESSO,
	USCITA;
	
	public String toString() {
		return super.toString().toLowerCase(Locale.ROOT);
	}
}
