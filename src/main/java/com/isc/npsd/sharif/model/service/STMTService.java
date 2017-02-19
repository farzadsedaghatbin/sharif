package com.isc.npsd.sharif.model.service;

import com.isc.npsd.common.service.BaseServiceImpl;
import com.isc.npsd.common.util.redis.CallbackMethod;
import com.isc.npsd.common.util.redis.RedisUtil;
import com.isc.npsd.sharif.adapter.SharedObjectsContainer;
import com.isc.npsd.sharif.model.entities.STMT;
import com.isc.npsd.sharif.model.entities.schemaobjects.trx.TXRList;
import com.isc.npsd.sharif.model.repositories.STMTRepository;
import redis.clients.jedis.Jedis;

import javax.ejb.*;
import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.logging.Logger;

/**
 * Created by A_Jankeh on 2/13/2017.
 */
@Local
@Stateless
public class STMTService extends BaseServiceImpl<STMT, STMTRepository> {

    @Inject
    private STMTRepository stmtRepository;
    @Inject
    private Logger logger;


    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    public STMTRepository getEntityRepositoryObject() {
        return stmtRepository;
    }

    public List findSumAndCount(String creditorBic, String debtorBic) {
        return stmtRepository.findSumAndCount(creditorBic, debtorBic);
    }

    @Asynchronous
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Future<String> saveTransactions(String creditorBIC) {
        System.out.println(">>>>>>>>>>>>>> create STMT" + creditorBIC);
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
                        add(null, stmt);
                    }
                }
            }
        });
        System.out.println("<<<<<<<<<<<<<<<<<< End Of STMT :" + creditorBIC);
        return new AsyncResult<>("");
    }
}
