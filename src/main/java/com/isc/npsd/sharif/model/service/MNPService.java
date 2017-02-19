package com.isc.npsd.sharif.model.service;

import com.isc.npsd.common.service.BaseServiceImpl;
import com.isc.npsd.sharif.model.entities.BNP;
import com.isc.npsd.sharif.model.entities.MNP;
import com.isc.npsd.sharif.model.repositories.MNPRepository;
import com.isc.npsd.sharif.util.ParticipantUtil;

import javax.ejb.*;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.logging.Logger;

/**
 * Created by A_Jankeh on 2/19/2017.
 */
@Local
@Stateless
public class MNPService extends BaseServiceImpl<MNP, MNPRepository> {

    @Inject
    private MNPRepository mnpRepository;
    @Inject
    private Logger logger;
    @Inject
    private STMTService stmtService;
    @Inject
    private BNPService bnpService;

    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    public MNPRepository getEntityRepositoryObject() {
        return mnpRepository;
    }

    @Asynchronous
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Future<String> createBNPAndMNP(String bic1) {
        List<String> bics = ParticipantUtil.getInstance().getBics();
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
        return new AsyncResult<>("");
    }

    private void createMnpRecord(String bic, BigDecimal[] mnp) {
        MNP entity = new MNP();
        entity.setAmount(mnp[0]);
        entity.setBic(bic);
        add(null, entity);
    }
}
