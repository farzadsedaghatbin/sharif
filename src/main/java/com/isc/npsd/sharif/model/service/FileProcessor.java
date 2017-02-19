package com.isc.npsd.sharif.model.service;

import com.isc.npsd.sharif.model.entities.File;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

/**
 * Created by a_jankeh on 2/5/2017.
 */
@Stateless
@Local
public class FileProcessor {

    @Inject
    private FileService fileService;
    @Inject
    private TrxService trxService;

    public void process() {
        LocalTime startTime = LocalTime.now();
        List<File> unprocessedFiles = fileService.findUnprocessedFiles();
        unprocessedFiles.forEach(file -> fileService.persisTransactions(file));
        LocalTime endTime = LocalTime.now();
        Duration duration = Duration.between(startTime, endTime);
        System.out.println("File Process Duration : " + duration);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>");
    }

}
