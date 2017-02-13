package com.isc.npsd.sharif.model.repositories;

import com.isc.npsd.common.data.BaseRDBMSRepositoryImpl;
import com.isc.npsd.sharif.model.entities.BNP;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

/**
 * Created by A_Jankeh on 2/11/2017.
 */
@Stateless
@LocalBean
public class BNPRepository extends BaseRDBMSRepositoryImpl<BNP> {
    public List findSumOfBNP(String bic) {
        return (List<Object[]>)
                em.createNamedQuery(BNP.FIND_SUM_OF_BNP).setParameter("bic", bic).getResultList();
    }
}
