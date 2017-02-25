package com.isc.npsd.sharif.model.entities;
import com.isc.npsd.common.model.BaseModel;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by A_Jankeh on 2/12/2017.
 */
@Entity
@Table(name = "tb_mnp")
public class MNP implements BaseModel<Long> {
    private Long id;
    private String bic;
    private BigDecimal amount;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sharif_seq")
    @SequenceGenerator(name = "sharif_seq", sequenceName = "SHARIF_SEQUENCE", allocationSize = 10000)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "bic")
    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    @Column(name = "amount")
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
