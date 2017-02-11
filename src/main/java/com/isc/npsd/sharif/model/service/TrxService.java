package com.isc.npsd.sharif.model.service;

import com.isc.npsd.common.util.JAXBUtil;
import com.isc.npsd.sharif.model.entities.FileType;
import com.isc.npsd.sharif.model.entities.schemaobjects.trx.ActiveCurrencyAndAmount;
import com.isc.npsd.sharif.model.entities.schemaobjects.trx.ObjectFactory;
import com.isc.npsd.sharif.model.entities.schemaobjects.trx.TXRList;
import com.isc.npsd.sharif.util.ParticipantUtil;

import javax.ejb.Local;
import javax.ejb.Stateless;
import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by a_jankeh on 1/30/2017.
 */
@Stateless
@Local
public class TrxService {

    public String generateFile(String creditorBIC, int size, AtomicInteger sequence) {
        TXRList txrList = produceTxrList(creditorBIC, size, sequence);
        try {
            String xml = JAXBUtil.ObjectToXml(txrList, FileType.TRANSACTION.getSchemaContext());
            return xml.replaceAll("><", ">\n<");
        } catch (Exception e) {
            System.out.println("Can not convert object to xml.");
            return null;
        }
    }

    private TXRList produceTxrList(String creditorBIC, int size, AtomicInteger sequence) {
        ObjectFactory factory = new ObjectFactory();
        TXRList txrList = factory.createTXRList();
        for (int counter = 0; counter < size; counter++) {
            TXRList.TXR txr = factory.createTXRListTXR();
            Date d = new Date();
            String mndtID = "MRN" + d.getTime() + sequence.getAndIncrement();
            txr.setMndtReqId(mndtID);
            txr.setCBIC(creditorBIC);
            txr.setDBIC(getRandomDebtorBIC(creditorBIC));
            txr.setMaxAmt(prepareMaxAmt(factory));
            txrList.getTXR().add(txr);
        }
        return txrList;
    }

    private ActiveCurrencyAndAmount prepareMaxAmt(ObjectFactory factory) {
        ActiveCurrencyAndAmount amt = factory.createActiveCurrencyAndAmount();
        amt.setValue(new BigDecimal(1000));
        amt.setCcy("IRR");
        return amt;
    }

    private String getRandomDebtorBIC(String creditorBIC) {
        String dbtrBIC = ParticipantUtil.getInstance().getRandomParticipant();
        while (dbtrBIC.equals(creditorBIC)) {
            dbtrBIC = ParticipantUtil.getInstance().getRandomParticipant();
        }
        return dbtrBIC;
    }

}
