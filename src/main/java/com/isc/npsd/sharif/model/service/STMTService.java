package com.isc.npsd.sharif.model.service;

import com.isc.npsd.common.service.BaseServiceImpl;
import com.isc.npsd.sharif.model.entities.STMT;
import com.isc.npsd.sharif.model.repositories.STMTRepository;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
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
}
