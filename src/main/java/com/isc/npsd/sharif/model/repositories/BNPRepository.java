package com.isc.npsd.sharif.model.repositories;

import com.isc.npsd.common.data.BaseRDBMSRepositoryImpl;
import com.isc.npsd.sharif.model.entities.BNP;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * Created by A_Jankeh on 2/11/2017.
 */
@Stateless
@LocalBean
public class BNPRepository extends BaseRDBMSRepositoryImpl<BNP> {
}
