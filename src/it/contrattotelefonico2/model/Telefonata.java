package it.contrattotelefonico2.model;

import java.time.LocalDate;
import java.util.Date;

public class Telefonata {
	private LocalDate data;
	private int durata;
	private TipoTelefonata tipo;
	private double costo;
	
	public Telefonata(int durata, String tipo, double costo) {
        this.data = LocalDate.now();  
        this.durata = durata;
        switch(tipo) {
        case "IN":
        	this.tipo = TipoTelefonata.INGRESSO;
        	break;
        case "OUT":
        	this.tipo = TipoTelefonata.USCITA;
        	break;
    	default:
    		break;
        }
        this.costo = costo;
    }

    public LocalDate getData() {
        return data;
    }

    public int getDurata() {
        return durata;
    }

    public TipoTelefonata getTipo() {
        return tipo;
    }

    public double getCosto() {
        return costo;
    }

	public void setData(LocalDate date) {
		this.data = date;
	}
}
