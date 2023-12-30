package com.example.qrcode.controller;

import com.example.qrcode.dto.QrRequest;
import com.example.qrcode.service.QrGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QrCodeController {

    private static final Logger log = LoggerFactory.getLogger(QrCodeController.class);

    private final QrGeneratorService qrGeneratorService;

    public QrCodeController(QrGeneratorService qrGeneratorService) {
        this.qrGeneratorService = qrGeneratorService;
    }

    @PostMapping("/qr")
    public ResponseEntity handleRequest(@RequestBody QrRequest qrRequest) {
        log.info("Received client request");
        if (qrRequest.urlToEmbed() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.CONTENT_TYPE, "image/png");
            String urlToEmbed = qrRequest.urlToEmbed();
            log.info("Generating QR image for " + urlToEmbed);
            return new ResponseEntity<byte[]>(
                    qrGeneratorService.generateQR(urlToEmbed, 512, 512),
                    httpHeaders,
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
