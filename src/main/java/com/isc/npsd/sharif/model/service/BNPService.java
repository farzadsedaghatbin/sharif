package com.isc.npsd.sharif.model.service;

import com.isc.npsd.common.service.BaseServiceImpl;
import com.isc.npsd.sharif.model.entities.BNP;
import com.isc.npsd.sharif.model.repositories.BNPRepository;

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
public class BNPService extends BaseServiceImpl<BNP, BNPRepository> {

    @Inject
    private BNPRepository bnpRepository;
    @Inject
    private Logger logger;

    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    public BNPRepository getEntityRepositoryObject() {
        return bnpRepository;
    }

    public List findSumOfBNP(String bic) {
        return bnpRepository.findSumOfBNP(bic);
    }
}
