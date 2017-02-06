package com.isc.npsd.sharif.model.repositories;


import com.isc.npsd.common.data.BaseRDBMSRepositoryImpl;
import com.isc.npsd.sharif.model.entities.File;
import com.isc.npsd.sharif.model.entities.FileStatus;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;


/**
 * Created by a_jankeh on 1/30/2017.
 */
@Stateless
@LocalBean
public class FileRepository extends BaseRDBMSRepositoryImpl<File> {

    public List<File> findUnprocessedFiles() {
        return (List<File>) em.createNamedQuery(File.FIND_UNPROCESSED_FILES)
                .setParameter("received", FileStatus.RECEIVED).getResultList();
    }
}
