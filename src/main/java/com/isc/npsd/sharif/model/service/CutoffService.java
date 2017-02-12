package com.isc.npsd.sharif.model.service;

import com.isc.npsd.sharif.model.entities.BNP;
import com.isc.npsd.sharif.model.repositories.BNPRepository;
import com.isc.npsd.sharif.util.ParticipantUtil;
import com.isc.npsd.sharif.util.RedisUtil;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
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

                    Set credit = RedisUtil.getExpressionItems("*cbic:" + bic1 + "_dbic:" + bic2);
                    if (credit != null) {
                            System.out.println(credit.size());
                    }

                    /*
                    List debit = new ArrayList();
                    if(RedisUtil.getExpressionItems("*cbic:" + bic2 + "_dbic:" + bic1 + "*") != null){
                        credit.addAll(RedisUtil.getExpressionItems("*cbic:" + bic2 + "_dbic:" + bic1 + "*"));
                    }
                    */


//                    List credit = recordService.findSumAndCount(bic1, bic2);
//                    List debit = recordService.findSumAndCount(bic2, bic1);

                    /*
                    bnp.setInflowSum((BigDecimal) ((Object[]) credit.get(0))[0]);
                    bnp.setInflowCount(BigInteger.valueOf((Long) ((Object[]) credit.get(0))[1]));
                    bnp.setOutflowSum((BigDecimal) ((Object[]) debit.get(0))[0]);
                    bnp.setOutflowCount(BigInteger.valueOf((Long) ((Object[]) debit.get(0))[1]));
                    BigDecimal bnpVal = bnp.getInflowSum().subtract(bnp.getOutflowSum());
                    bnp.setBnp(bnpVal);
                    settles.add(bnp);
                    bnpRepository.add(bnp);
                    mnp[0] = mnp[0].add(bnpVal);
                    */

                }
            });
        });
    }
}
