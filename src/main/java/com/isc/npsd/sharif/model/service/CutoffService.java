package com.isc.npsd.sharif.model.service;

import com.isc.npsd.sharif.model.entities.BNP;
import com.isc.npsd.sharif.model.entities.schemaobjects.trx.MNP;
import com.isc.npsd.sharif.model.entities.schemaobjects.trx.TXRList;
import com.isc.npsd.sharif.model.repositories.BNPRepository;
import com.isc.npsd.sharif.model.repositories.MNPRepository;
import com.isc.npsd.sharif.util.ParticipantUtil;
import com.isc.npsd.sharif.util.RedisUtil;

import javax.ejb.Local;
import javax.ejb.Stateless;
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
    private BNPRepository bnpRepository;
    @Inject
    private MNPRepository mnpRepository;

    public void cutoff() {
        bnpProcess();
    }

    private void bnpProcess() {
        List<BNP> settles = new ArrayList<>();
        final BigDecimal[] mnp = {new BigDecimal(0)};
        List<String> bics = ParticipantUtil.getInstance().getBics();
        bics.forEach(bic1 -> {
            bics.forEach(bic2 -> {
                if (!bic1.equals(bic2)) {
                    BNP bnp = new BNP();
                    bnp.setMainBic(bic1);
                    bnp.setOtherBic(bic2);

                    Set<TXRList.TXR> credits = RedisUtil.getExpressionItems("*cbic:" + bic1 + "_dbic:" + bic2);
                    Set<TXRList.TXR> debits = RedisUtil.getExpressionItems("*cbic:" + bic2 + "_dbic:" + bic1);

                    BigDecimal creditSum = new BigDecimal(0);
                    BigInteger creditSize = new BigInteger("0");
                    if (credits != null) {
                        credits.forEach(credit -> {
                            creditSum.add(credit.getMaxAmt().getValue());
                        });
                        creditSize.add(BigInteger.valueOf(credits.size()));
                    }

                    BigDecimal debitSum = new BigDecimal(0);
                    BigInteger debitSize = new BigInteger("0");
                    if (debits != null) {
                        debits.forEach(debit -> {
                            debitSum.add(debit.getMaxAmt().getValue());
                        });
                        debitSize.add(BigInteger.valueOf(debits.size()));
                    }

                    bnp.setInflowSum(creditSum);
                    bnp.setInflowCount(creditSize);
                    bnp.setOutflowSum(debitSum);
                    bnp.setOutflowCount(debitSize);
                    BigDecimal bnpVal = bnp.getInflowSum().subtract(bnp.getOutflowSum());
                    bnp.setBnp(bnpVal);
                    settles.add(bnp);
                    bnpRepository.add(bnp);
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
        mnpRepository.add(entity);
    }
}
