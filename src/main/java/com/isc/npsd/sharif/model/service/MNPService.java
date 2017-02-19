package com.isc.npsd.sharif.model.service;

import com.isc.npsd.common.service.BaseServiceImpl;
import com.isc.npsd.sharif.model.entities.MNP;
import com.isc.npsd.sharif.model.repositories.MNPRepository;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
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

    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    public MNPRepository getEntityRepositoryObject() {
        return mnpRepository;
    }
}
