package com.isc.npsd.sharif.model.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isc.npsd.common.service.BaseServiceImpl;
import com.isc.npsd.common.util.EncryptUtil;
import com.isc.npsd.common.util.JAXBUtil;
import com.isc.npsd.sharif.model.entities.File;
import com.isc.npsd.sharif.model.entities.FileStatus;
import com.isc.npsd.sharif.model.entities.FileType;
import com.isc.npsd.sharif.model.entities.schemaobjects.trx.TXRList;
import com.isc.npsd.sharif.model.repositories.FileRepository;
import com.isc.npsd.sharif.util.RedisUtil;
import org.xml.sax.SAXException;
import redis.clients.jedis.Pipeline;

import javax.ejb.*;
import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by a_jankeh on 1/30/2017.
 */
@Local
@Stateless
public class FileService extends BaseServiceImpl<File, FileRepository> {

    @Inject
    private FileRepository fileRepository;
    @Inject
    private TrxService trxService;

    @Inject
    private Logger logger;

    @Override
    public FileRepository getEntityRepositoryObject() {
        return fileRepository;
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void saveFileMap(Map<String, List<File>> fileMap) {

        fileMap.entrySet().stream().forEach(entry -> {
            entry.getValue().stream().forEach(file -> {
                add(null, file);
            });
        });
    }

    public List<File> findUnprocessedFiles() {
        return fileRepository.findUnprocessedFiles();
    }

    @Asynchronous
    public void persisTransactions(File file) {
        byte[] decryptedContent = EncryptUtil.decryptAES(file.getContent());
        if (decryptedContent == null || decryptedContent.length == 0)
            file.setFileStatus(FileStatus.REJECTED);
        else {
            String xml = new String(decryptedContent, StandardCharsets.UTF_8).trim();
            try {
                TXRList txrList = (TXRList) JAXBUtil.XmlToObject(xml, FileType.TRANSACTION.getXSDSchema(), FileType.TRANSACTION.getSchemaContext());
                List<TXRList.TXR> transactions = txrList.getTXR();
                Pipeline p = RedisUtil.getPipeline();
                transactions.forEach(transaction -> {
                    try {
                        p.set(transaction.getMndtReqId()+"_cbic:"+transaction.getCBIC()+"_dbic:"+transaction.getDBIC(), new ObjectMapper().writeValueAsString(transaction));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                });
                p.sync();
                file.setFileStatus(FileStatus.ACCEPTED);

            } catch (JAXBException e) {
                e.printStackTrace();
                file.setFileStatus(FileStatus.REJECTED);
            } catch (SAXException e) {
                e.printStackTrace();
                file.setFileStatus(FileStatus.REJECTED);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                file.setFileStatus(FileStatus.REJECTED);
            }
        }

        update(null, file);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public File update(String triggeredBy, File entity) {
        return super.update(triggeredBy, entity);
    }
}
