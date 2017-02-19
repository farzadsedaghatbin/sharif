package com.isc.npsd.sharif.model.service;

import com.isc.npsd.common.util.redis.CallbackMethod;
import com.isc.npsd.common.util.redis.RedisUtil;
import com.isc.npsd.sharif.adapter.SharedObjectsContainer;
import com.isc.npsd.sharif.model.entities.BNP;
import com.isc.npsd.sharif.model.entities.MNP;
import com.isc.npsd.sharif.model.entities.STMT;
import com.isc.npsd.sharif.model.entities.schemaobjects.trx.TXRList;
import com.isc.npsd.sharif.util.ParticipantUtil;
import redis.clients.jedis.Jedis;

import javax.ejb.*;
import javax.inject.Inject;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalTime;
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
        List<String> bics = ParticipantUtil.getInstance().getBics();
        bics.forEach(this::saveTransactions);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void bnpProcess() {
        List<String> bics = ParticipantUtil.getInstance().getBics();
        bics.forEach(bic1 -> {
            List<BNP> settles = new ArrayList<>();
            final BigDecimal[] mnp = {new BigDecimal(0)};
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
                    settles.add(bnp);
                    bnpService.add(null, bnp);
                    mnp[0] = mnp[0].add(bnpVal);
                }
            });
            createMnpRecord(bic1, mnp);
        });
    }

    private void createMnpRecord(String bic, BigDecimal[] mnp) {
        MNP entity = new MNP();
        entity.setAmount(mnp[0]);
        entity.setBic(bic);
        mnpService.add(null, entity);
    }

    @Asynchronous
    private void saveTransactions(String creditorBIC) {
        RedisUtil redisUtil = SharedObjectsContainer.redisUtil;
        redisUtil.execute(new CallbackMethod() {
            @Override
            public void onExecution(Jedis jedis) {
                Set<TXRList.TXR> transactions = null;
                try {
                    transactions = redisUtil.getExpressionItemsFromSet(jedis, TXRList.TXR.class, "*_" + creditorBIC + "_*");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (transactions != null) {

                    for (TXRList.TXR transaction : transactions) {
                        STMT stmt = new STMT();
                        stmt.setMrn(transaction.getMndtReqId());
                        stmt.setCbic(transaction.getCBIC());
                        stmt.setDbic(transaction.getDBIC());
                        stmt.setAmount(transaction.getMaxAmt().getValue());
                        stmtService.add(null, stmt);
                    }
                }
            }
        });
    }


}
