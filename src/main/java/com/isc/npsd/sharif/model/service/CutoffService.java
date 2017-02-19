package com.isc.npsd.sharif.model.service;

import com.isc.npsd.sharif.util.ParticipantUtil;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by A_Jankeh on 2/11/2017.
 */
@Stateless
@Local
public class CutoffService {

    @Inject
    private TrxService trxService;
    @Inject
    private STMTService stmtService;
    @Inject
    private BNPService bnpService;
    @Inject
    private MNPService mnpService;


    public void cutoff() {
        LocalTime startTime = LocalTime.now();
        stmtProcess();

        bnpProcess();
        LocalTime endTime = LocalTime.now();
        Duration duration = Duration.between(startTime, endTime);
        System.out.println("Cutoff Duration : " + duration);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>");
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void stmtProcess() {
        List<Future<String>> stmtFuture = new ArrayList<>();
        List<String> bics = ParticipantUtil.getInstance().getBics();
        bics.forEach(creditorBIC -> stmtFuture.add(stmtService.saveTransactions(creditorBIC)));
        waitForFutures(stmtFuture);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void bnpProcess() {
        List<Future<String>> bnpFuture = new ArrayList<>();
        List<String> bics = ParticipantUtil.getInstance().getBics();
        bics.forEach(bic1 -> bnpFuture.add(mnpService.createBNPAndMNP(bic1)));
        waitForFutures(bnpFuture);
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
