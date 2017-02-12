package com.isc.npsd.sharif.controller;

import com.isc.npsd.common.controller.BaseForm;
import com.isc.npsd.sharif.model.entities.File;
import com.isc.npsd.sharif.model.service.CutoffService;
import com.isc.npsd.sharif.model.service.FileProcessor;
import com.isc.npsd.sharif.model.service.FileService;
import com.isc.npsd.sharif.model.service.Generator;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import java.util.List;

/**
 * Created by a_jankeh on 1/30/2017.
 */
@ManagedBean(name = "txController")
@SessionScoped
public class TxController extends BaseForm<File, FileService> {

    @Inject
    private FileService fileService;
    @Inject
    private FileProcessor fileProcessor;
    @Inject
    private Generator generator;
    @Inject
    private CutoffService cutoffService;

    private File file;
    private int numberOfTx;

    @Override
    protected FileService getService() {
        return fileService;
    }

    @Override
    public File getModel() {
        return file;
    }

    @Override
    public void setModel(File file) {
        this.file = file;
    }

    @Override
    protected void resetModel() {
        this.file = new File();
    }

    @Override
    protected String getCurrentUsername() {
        return null;
    }

    public void generate() {
        try {
            generator.prepare(numberOfTx);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fileProcess() {
        fileProcessor.process();
    }

    public void cutoff() {
        cutoffService.cutoff();
    }

    public int getNumberOfTx() {
        return numberOfTx;
    }

    public void setNumberOfTx(int numberOfTx) {
        this.numberOfTx = numberOfTx;
    }
}
