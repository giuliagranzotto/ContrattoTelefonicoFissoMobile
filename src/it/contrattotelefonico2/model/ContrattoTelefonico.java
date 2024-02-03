package it.contrattotelefonico2.model;

import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class ContrattoTelefonico {
	private String numeroTel;
    private String utente;
    private double costoTelefonate;
//    private int numTelefonate;
    private List<Telefonata> telefonate;

    private static final double COSTO_AL_SECONDO = 0.01;

    public ContrattoTelefonico(String numeroTel, String utente) {
        this.numeroTel = numeroTel;
        this.utente = utente;
        this.costoTelefonate = 0;
//        this.numTelefonate = 0;
        this.telefonate = new ArrayList<>(); 
    }

    public void aggiornaBolletta(int sec) {
        costoTelefonate += sec * COSTO_AL_SECONDO;
//        numTelefonate++;
    }
    
    public void aggiungiTelefonata(Telefonata telefonata) {
    	telefonate.add(telefonata);
    }
    
    public List<Telefonata> getTelefonate(){
    	return telefonate;
    }

    public void aggiornaCosto(double costoAggiuntivo) {
        costoTelefonate += costoAggiuntivo;
    }

    public double getBolletta() {
        return costoTelefonate;
    }

    public String getNumeroTel() {
        return numeroTel;
    }

    public String getDatiUtente() {
        return "Utente: " + utente + " || Numero di telefono: " + getNumeroTel();
    }

	public int getNumTelefonate() {
//		return numTelefonate;
		return telefonate.size();
	}

	public double getCostoAlSec() {
		return COSTO_AL_SECONDO;
	}
    
}
