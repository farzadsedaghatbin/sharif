package com.isc.npsd.sharif.model.repositories;

import com.isc.npsd.common.data.BaseRDBMSRepositoryImpl;
import com.isc.npsd.sharif.model.entities.STMT;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

/**
 * Created by A_Jankeh on 2/13/2017.
 */
@Stateless
@LocalBean
public class STMTRepository extends BaseRDBMSRepositoryImpl<STMT> {

    public List findSumAndCount(String creditor, String debtor) {
        return (List<Object[]>)
        em.createNamedQuery(STMT.FIND_SUM_AND_COUNT).setParameter("creditor", creditor).setParameter("debtor", debtor).getResultList();
    }
}
