package com.isc.npsd.sharif.model.service;

import com.isc.npsd.common.util.EncryptUtil;
import com.isc.npsd.common.util.MessagesUtil;
import com.isc.npsd.common.util.SequenceUtil;
import com.isc.npsd.sharif.model.entities.File;
import com.isc.npsd.sharif.model.entities.FileStatus;
import com.isc.npsd.sharif.util.ParticipantUtil;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by a_jankeh on 1/30/2017.
 */
@Stateless
@Local
public class Generator {

    @Inject
    private FileService fileService;
    @Inject
    private TrxService trxService;

    private AtomicInteger mandateSequenceHeader = new AtomicInteger(1);
    private String creditorBIC;
    private int totalNumberOfRecords;

    public void prepare(int totalNumberOfRecords) {
        this.totalNumberOfRecords = totalNumberOfRecords;
        List<String> bics = ParticipantUtil.getInstance().getBics();
        Map<String, List<File>> fileMap = new ConcurrentHashMap<>();
        bics.parallelStream().forEach(bic -> {
            creditorBIC = bic;
            fileMap.put(bic, generateFiles());
        });
        fileService.saveFileMap(fileMap);
    }

    private List<File> generateFiles() {
        String postfix = MessagesUtil.getConfigurationMessage("postfix");
        int fileSize = Integer.parseInt(MessagesUtil.getConfigurationMessage("fileSize"));
        int remainingFileSize = totalNumberOfRecords % fileSize;
        int numberOfFiles = totalNumberOfRecords / fileSize + (remainingFileSize == 0 ? 0 : 1);

        List<File> files = new ArrayList<>();

        for (int i = 0; i < numberOfFiles; i++) {
            String fileName = creditorBIC.substring(0, 4) + "-" + SequenceUtil.getUniqueTimeStamp() + i + postfix;
            String fileContent;
            if ((i == numberOfFiles - 1) && (remainingFileSize > 0)) {
                fileContent = trxService.generateFile(creditorBIC, remainingFileSize, mandateSequenceHeader);
            } else {
                fileContent = trxService.generateFile(creditorBIC, fileSize, mandateSequenceHeader);
            }

            File file = new File();
            file.setCreateDate(new Date());
            file.setName(fileName);
            file.setFileStatus(FileStatus.RECEIVED);
            byte[] encryptedContent = EncryptUtil.encryptAES(fileContent.getBytes(StandardCharsets.UTF_8));
            file.setContent(encryptedContent);
            files.add(file);
        }
        return files;
    }


}
