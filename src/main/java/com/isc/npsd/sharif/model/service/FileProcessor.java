package com.isc.npsd.sharif.model.service;

import com.isc.npsd.sharif.model.entities.File;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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
        List<Future<String>> fileProcessFuture = new ArrayList<>();
        List<File> unprocessedFiles = fileService.findUnprocessedFiles();
        unprocessedFiles.stream().forEach(file -> fileProcessFuture.add(fileService.persisTransactions(file)));
        waitForFutures(fileProcessFuture);
        LocalTime endTime = LocalTime.now();
        Duration duration = Duration.between(startTime, endTime);
        System.out.println("File Process Duration : " + duration);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>");
    }

    private void waitForFutures(List<Future<String>> futures) {
        futures.forEach(future -> {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

}
