package com.isc.npsd.sharif.model.entities;

import com.isc.npsd.common.model.BaseModel;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by a_jankeh on 1/30/2017.
 */

@NamedQueries({
        @NamedQuery(
                name = File.FIND_UNPROCESSED_FILES,
                query = "select f from File f where f.fileStatus = :received "
        )
})

@Entity
@Table(name = "tb_file")
@DynamicUpdate
public class File implements BaseModel<Long> {

    public static final String FIND_UNPROCESSED_FILES = "File.findUnprocessedFiles";

    private Long id;
    private String name;
    private Date createDate;
    private FileStatus fileStatus;
    private byte[] content;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "create_date")
    @Temporal(TemporalType.DATE)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "file_status")
    @Enumerated()
    public FileStatus getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(FileStatus fileStatus) {
        this.fileStatus = fileStatus;
    }

    @Column(name = "content")
    @Lob
    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
