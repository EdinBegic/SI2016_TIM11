package com.timxyz.controllers;

import com.timxyz.services.ReportService;
import com.timxyz.services.exceptions.ServiceException;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.io.*;

@RestController
public class ReportController {

    @Autowired
    private ReportService reportService;

    @ResponseBody
    @GetMapping("/reports/audit/{id}")
    public ResponseEntity<byte[]> auditPDFReport(@PathVariable("id") long id) {
        FileInputStream fileStream;
        try {
            String filePath = reportService.auditReportFilePath(id);
            fileStream = new FileInputStream(new File(filePath));
            byte[] contents = IOUtils.toByteArray(fileStream);

            fileStream.close();
            reportService.deleteReportFile(filePath);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/pdf"));

            ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);

            return response;
        }
        catch (FileNotFoundException e) {
           return error(e);
        }
        catch (IOException e) {
            return error(e);
        }
        catch (ServiceException e) {
            return error(e);
        }
        catch (Exception e) {
            return error(e);
        }
    }

    @ResponseBody
    private ResponseEntity error(Exception e) {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder()
                .add("message", e.getMessage())
                .add("error", "Error occured");
        JsonObject responseObj = objectBuilder.build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseObj);
    }
}
