package it.contrattotelefonico2.model;

import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import it.contocorrente2.utils.MyUtils;

public class ContrattoFisso extends ContrattoTelefonico {
	private String citta;

    public ContrattoFisso(String numeroTel, String utente, String citta) {
        super(numeroTel, utente);
        this.citta = citta;
    }

    public String getDatiUtente() {
        return super.getDatiUtente() + " || Citta: " + citta;
    }
    
    public void aggiungiTelefonateCasuali(int numeroTelefonate) {
        Random random = new Random();
        LocalDate now = LocalDate.now();
        for (int i = 0; i < numeroTelefonate; i++) {
            int sec = random.nextInt(100);
            String tipo = sec % 2 == 0 ? "IN" : "OUT";
            double costo = tipo.equals("IN") ? 0.0 : sec * getCostoAlSec();         
            
            LocalDate primo = LocalDate.of(2023, 1, 1);
//            int days = Period.between(primo, LocalDate.now()).getDays();
            int days = (int) ChronoUnit.DAYS.between(primo, LocalDate.now());
            LocalDate dataCasuale = now.minusDays(random.nextInt(days));  // Data casuale nel 2023

            if (tipo.equals("OUT")) {
            	 LocalDate luglio = LocalDate.of(2023, 7, 1);
                 LocalDate agosto = LocalDate.of(2023, 8, 31);
             	if(dataCasuale.getMonth().equals(luglio.getMonth()) || dataCasuale.getMonth().equals(agosto.getMonth())) {
             		// sconto del 40%
             		costo = costo - costo * 0.4;
             	}
                aggiornaCosto(costo);  // Aggiorna il costo totale solo per chiamate in uscita
            }

            // Creiamo l'oggetto di tipo Telefonata
            Telefonata telefonata = new Telefonata(sec, tipo, costo);
            telefonata.setData(dataCasuale);
            aggiungiTelefonata(telefonata);
        }
    }
    
    public void stampaBollettaPDF() {
        try {
            Document document = new Document();
            String nomeDocumentoPDF = "Bolletta_" + super.getNumeroTel() + "_" + LocalDate.now() + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(nomeDocumentoPDF));
            document.open();

            // Intestazione
            Paragraph intestazione = new Paragraph("Bolletta del " + LocalDate.now() + " || Fisso");
            document.add(intestazione);

            // Dati bolletta
            Paragraph datiUtente = new Paragraph(this.getDatiUtente());
            document.add(datiUtente);

            List<Telefonata> telefonate = this.getTelefonate();

            document.add(new Paragraph("Num. Telefonate: " + telefonate.size()));
            DecimalFormat decimalFormat = new DecimalFormat("#0.00");
            double costoTotale = this.getBolletta();
            String costoTotaleFormattato = decimalFormat.format(costoTotale);
            document.add(new Paragraph("Costo Totale: €" + costoTotaleFormattato));
            
            double durataMediaIN = 0, durataMediaOUT = 0, costoMedio = 0;
            int countIN = 0, countOUT = 0;
    		
            for (Telefonata telefonata : telefonate) {
            	if(telefonata.getTipo().toString().equals("ingresso")) {
            		durataMediaIN += telefonata.getDurata();
            		countIN++;
            	} else { // OUT
            		durataMediaOUT += telefonata.getDurata();
            		countOUT++;
            	}
            }
            
            durataMediaIN = durataMediaIN / countIN;
            durataMediaOUT = durataMediaOUT / countOUT;
            costoMedio = this.getBolletta() / countOUT;
            
            String durataFormattata = decimalFormat.format(durataMediaIN);
            document.add(new Paragraph("Durata media delle telefonate (in entrata) : " + durataFormattata + " sec"));
            durataFormattata = decimalFormat.format(durataMediaOUT);
            document.add(new Paragraph("Durata media delle telefonate (in uscita) : " + durataFormattata + " sec"));
            String costoFormattato = decimalFormat.format(costoMedio);
            document.add(new Paragraph("Costo medio delle telefonate (in uscita) : " + costoFormattato + " euro"));
            document.add(new Paragraph("Promo estiva 2023: sconto 40% sulle telefonate nel mese di Luglio e Agosto compreso scatto alla risposta."));

            document.add(new Paragraph("Dettaglio telefonate:"));
            // Aggiunge una linea bianca
	        document.add(Chunk.NEWLINE);
            
            // Intestazione della tabella
	        PdfPTable table = new PdfPTable(4);  // 4 colonne
	        PdfPCell cell = new PdfPCell(new Paragraph("Data"));
	        table.addCell(cell);
	        cell = new PdfPCell(new Paragraph("Durata"));
	        table.addCell(cell);
	        cell = new PdfPCell(new Paragraph("Tipo"));
	        table.addCell(cell);
	        cell = new PdfPCell(new Paragraph("Costo"));
	        table.addCell(cell);

            for (Telefonata telefonata : telefonate) {
                String dataFormatted = telefonata.getData().toString();
                costoFormattato = decimalFormat.format(telefonata.getCosto());

                
                cell = new PdfPCell(new Paragraph(dataFormatted));
	            table.addCell(cell);
	            cell = new PdfPCell(new Paragraph(String.valueOf(telefonata.getDurata())));
	            table.addCell(cell);
	            cell = new PdfPCell(new Paragraph(telefonata.getTipo().toString()));
	            table.addCell(cell);
	            cell = new PdfPCell(new Paragraph("€" + costoFormattato));
	            table.addCell(cell);
            }

	        document.add(table);  // Aggiunge la tabella con intestazione
            document.close();
            System.out.println("Documento PDF creato con successo: " + nomeDocumentoPDF);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void stampaBollettaConsole() {    	
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        double costoTotale = this.getBolletta();
        String costoTotaleFormattato = decimalFormat.format(costoTotale);
        System.out.println("Bolletta del " + LocalDate.now() + " || Contratto telefonico tipo: Fisso" + " || Utente: " + this.getDatiUtente() + " || Num. Telefonate: " + this.getNumTelefonate() + " || Costo Totale: €" + costoTotaleFormattato);
        System.out.println("------------------------------------------------------------------------------");
        
        double durataMediaIN = 0, durataMediaOUT = 0, costoMedio = 0;
        int countIN = 0, countOUT = 0;
        List<Telefonata> telefonate = this.getTelefonate();
		
        for (Telefonata telefonata : telefonate) {
        	if(telefonata.getTipo().toString().equals("ingresso")) {
        		durataMediaIN += telefonata.getDurata();
        		countIN++;
        	} else { // OUT
        		durataMediaOUT += telefonata.getDurata();
        		countOUT++;
        	}
        }
		        
        durataMediaIN = durataMediaIN / countIN;
        durataMediaOUT = durataMediaOUT / countOUT;
        costoMedio = this.getBolletta() / countOUT;
        
        String durataFormattata = decimalFormat.format(durataMediaIN);
        System.out.println("Durata media delle telefonate (in entrata) : " + durataFormattata + " sec");
        durataFormattata = decimalFormat.format(durataMediaOUT);
        System.out.println("Durata media delle telefonate (in uscita) : " + durataFormattata + " sec");
        String costoFormattato = decimalFormat.format(costoMedio);
        System.out.println("Costo medio delle telefonate (in uscita) : " + costoFormattato + " euro");
        System.out.println("Promo estiva 2023 :sconto 40% sulle telefonate nel mese di Luglio e Agosto compreso scatto alla risposta.");
		        
        System.out.println("Dettaglio:");
        System.out.println(MyUtils.formateStringWithPadding("Data", 12) + " | " + MyUtils.formateStringWithPadding("Durata in secondi", 16) + " | " + MyUtils.formateStringWithPadding("Tipo", 10) + " | " + MyUtils.formateStringWithPadding("Costo", 6));
        
        for (Telefonata telefonata : telefonate) {
            String dataFormatted = telefonata.getData().toString();
            costoFormattato = decimalFormat.format(telefonata.getCosto());
            System.out.println(MyUtils.formateStringWithPadding(dataFormatted, 12) + " | " + MyUtils.formateStringWithPadding(Integer.toString(telefonata.getDurata()), 17) + " | " + MyUtils.formateStringWithPadding(telefonata.getTipo().toString(), 10) + " | " + MyUtils.formateStringWithPadding("€" + costoFormattato, 6) + " | ");
        }
        
        System.out.println(MyUtils.formateStringWithPadding("->Totale", 48) + MyUtils.formateStringWithPadding("€" + costoTotaleFormattato, 6) + " | ");
    }
    
}
