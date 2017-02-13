package com.isc.npsd.sharif.model.service;

import com.isc.npsd.sharif.model.entities.BNP;
import com.isc.npsd.sharif.model.entities.MNP;
import com.isc.npsd.sharif.model.entities.STMT;
import com.isc.npsd.sharif.model.entities.schemaobjects.trx.TXRList;
import com.isc.npsd.sharif.model.repositories.BNPRepository;
import com.isc.npsd.sharif.model.repositories.MNPRepository;
import com.isc.npsd.sharif.model.repositories.STMTRepository;
import com.isc.npsd.sharif.util.ParticipantUtil;
import com.isc.npsd.sharif.util.RedisUtil;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by A_Jankeh on 2/11/2017.
 */
@Stateless
@Local
public class CutoffService {

    @Inject
    private STMTRepository stmtRepository;
    @Inject
    private STMTService stmtService;
    @Inject
    private BNPRepository bnpRepository;
    @Inject
    private BNPService bnpService;
    @Inject
    private MNPRepository mnpRepository;


    public void cutoff() {
        stmtProcess();
        bnpProcess();
//        createMnpRecords();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void stmtProcess() {
        List<String> bics = ParticipantUtil.getInstance().getBics();
        bics.forEach(creditorBIC -> {
            Set<TXRList.TXR> transactions = RedisUtil.getExpressionItems("*_" + creditorBIC + "_*");
            if (transactions != null) {

                for (TXRList.TXR transaction : transactions) {
                    STMT stmt = new STMT();
                    stmt.setMrn(transaction.getMndtReqId());
                    stmt.setCbic(transaction.getCBIC());
                    stmt.setDbic(transaction.getDBIC());
                    stmt.setAmount(transaction.getMaxAmt().getValue());
                    stmtRepository.add(stmt);
                }
            }
        });
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void bnpProcess() {
//        List<BNP> settles = new ArrayList<>();

        List<String> bics = ParticipantUtil.getInstance().getBics();
        bics.forEach(bic1 -> {
            bics.forEach(bic2 -> {
                if (!bic1.equals(bic2)) {
                    BNP bnp = new BNP();
                    bnp.setMainBic(bic1);
                    bnp.setOtherBic(bic2);

                    List<Object[]> credit = stmtService.findSumAndCount(bic1, bic2);
                    List<Object[]> debit = stmtService.findSumAndCount(bic2, bic1);

                    bnp.setInflowSum((BigDecimal) ((Object[]) credit.get(0))[0]);
                    bnp.setInflowCount(BigInteger.valueOf((Long) ((Object[]) credit.get(0))[1]));
                    bnp.setOutflowSum((BigDecimal) ((Object[]) debit.get(0))[0]);
                    bnp.setOutflowCount(BigInteger.valueOf((Long) ((Object[]) debit.get(0))[1]));
                    BigDecimal bnpVal = bnp.getInflowSum().subtract(bnp.getOutflowSum());
                    bnp.setBnp(bnpVal);
//                    settles.add(bnp);
                    bnpRepository.add(bnp);
                }
            });
//            createMnpRecord(bic1, settles);
        });
    }

    /*
    private void createMnpRecords(String bic, List<BNP> bnps) {
        MNP entity = new MNP();
        final BigDecimal mnp = new BigDecimal(0);
        bnps.forEach(bnp -> mnp.add(bnp.getBnp()));
        entity.setBic(bic);
        mnpRepository.add(entity);
    }
    */

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void createMnpRecords() {
        List<String> bics = ParticipantUtil.getInstance().getBics();
        bics.forEach(bic -> {
            List<Object[]> mnpValue = bnpService.findSumOfBNP(bic);
            MNP entity = new MNP();
            entity.setBic(bic);
            entity.setAmount((BigDecimal) ((Object[]) mnpValue.get(0))[0]);
            mnpRepository.add(entity);
        });
    }

}
