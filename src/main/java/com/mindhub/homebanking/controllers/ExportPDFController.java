package com.mindhub.homebanking.controllers;
import com.itextpdf.text.Document;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.Element;
import org.springframework.core.io.ByteArrayResource;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ExportPDFController {
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @GetMapping(value = "/transactions_PDF", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<Object> getTransactionsPDF(@RequestParam LocalDateTime initDate, @RequestParam LocalDateTime finDate, @RequestParam String numberAccount) throws IOException, DocumentException {
        if (initDate==null){
            return new ResponseEntity<>("Fecha de inicio vacio", HttpStatus.FORBIDDEN);
        }
        if(finDate==null){
            return new ResponseEntity<>("Fecha de final vacio", HttpStatus.FORBIDDEN);
        }
        if (numberAccount.isBlank()){
            return new ResponseEntity<>("Numero de cuenta vacio", HttpStatus.FORBIDDEN);
        }
        DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        try {
            initDate.format(formatter);
            finDate.format(formatter);
        }catch (Exception e){
            return new ResponseEntity<>("Error en los formatos de fecha", HttpStatus.FORBIDDEN);
        }
        Account account= accountService.findByNumber(numberAccount);
        List<Transaction> transactions= transactionService.findByDateBetweenAndAccount(initDate,finDate,account);
        if(transactions.isEmpty()){
            return new ResponseEntity<>("Transacciones no encontradas", HttpStatus.FORBIDDEN);
        }
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, byteArrayOutputStream);
        // Abrir el documento
        document.open();

        // Crear la tabla
        PdfPTable table = new PdfPTable(3);

        // Agregar las cabeceras
        table.addCell("Fecha");
        table.addCell("Descripción");
        table.addCell("Monto");

        // Agregar las filas de datos
        for (Transaction transaction : transactions) {
            table.addCell(transaction.getDate().toString());
            table.addCell(transaction.getDescription());
            table.addCell(String.valueOf(transaction.getAmount()));
        }

        // Agregar la tabla al documento
        document.add(table);

        // Cerrar el documento
        document.close();

        // Convertir el contenido del PDF a un array de bytes
        byte[] content = byteArrayOutputStream.toByteArray();

        // Crear la respuesta con el PDF como contenido
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "transactions.pdf");
        headers.setContentLength(content.length);

        ByteArrayResource resource = new ByteArrayResource(content);

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);

    }
}
