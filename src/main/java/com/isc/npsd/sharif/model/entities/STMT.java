package com.isc.npsd.sharif.model.entities;

import com.isc.npsd.common.model.BaseModel;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by A_Jankeh on 2/13/2017.
 */

@NamedQueries({
        @NamedQuery(
                name = STMT.FIND_SUM_AND_COUNT,
                query = "select coalesce(sum(s.amount),0),count(s.id) from STMT s " +
                        "where s.cbic= :creditor " +
                        "and s.dbic = :debtor "
        )
})

//em.createNamedQuery(STMT.FIND_SUM_AND_COUNT).setParameter("creditor", creditor).setParameter("debtor", debtor).getResultList()

@Entity
@Table(name = "tb_stmt")
public class STMT implements BaseModel<Long> {

    public static final String FIND_SUM_AND_COUNT = "STMT.findSumAndCount";

    private Long id;
    private String mrn;
    private String cbic;
    private String dbic;
    private BigDecimal amount;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "mrn")
    public String getMrn() {
        return mrn;
    }

    public void setMrn(String mrn) {
        this.mrn = mrn;
    }

    @Column(name = "cbic")
    public String getCbic() {
        return cbic;
    }

    public void setCbic(String cbic) {
        this.cbic = cbic;
    }

    @Column(name = "dbic")
    public String getDbic() {
        return dbic;
    }

    public void setDbic(String dbic) {
        this.dbic = dbic;
    }

    @Column(name = "amount")
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
