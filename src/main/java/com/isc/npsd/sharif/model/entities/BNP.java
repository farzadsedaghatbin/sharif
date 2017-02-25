package com.isc.npsd.sharif.model.entities;


import com.isc.npsd.common.model.BaseModel;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
/**
 * Created by A_Jankeh on 2/11/2017.
 */
@NamedQueries({
        @NamedQuery(
                name = BNP.FIND_SUM_OF_BNP,
                query = "select coalesce(sum(b.bnp),0) from BNP b where b.mainBic= :bic"
        )
})


@Entity
@Table(name = "tb_bnp")
public class BNP implements BaseModel<Long> {

    public static final String FIND_SUM_OF_BNP = "BNP.findSumOfBNP";

    private Long id;
    private String mainBic;
    private String otherBic;
    private BigDecimal inflowSum;
    private BigInteger inflowCount;
    private BigDecimal outflowSum;
    private BigInteger outflowCount;
    private BigDecimal bnp;

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

    @Column(name = "main_bic")
    public String getMainBic() {
        return mainBic;
    }

    public void setMainBic(String mainBic) {
        this.mainBic = mainBic;
    }

    @Column(name = "other_bic")
    public String getOtherBic() {
        return otherBic;
    }

    public void setOtherBic(String otherBic) {
        this.otherBic = otherBic;
    }

    @Column(name = "inflow_sum")
    public BigDecimal getInflowSum() {
        return inflowSum;
    }

    public void setInflowSum(BigDecimal inflowSum) {
        this.inflowSum = inflowSum;
    }

    @Column(name = "inflow_count")
    public BigInteger getInflowCount() {
        return inflowCount;
    }

    public void setInflowCount(BigInteger inflowCount) {
        this.inflowCount = inflowCount;
    }

    @Column(name = "outflow_sum")
    public BigDecimal getOutflowSum() {
        return outflowSum;
    }

    public void setOutflowSum(BigDecimal outflowSum) {
        this.outflowSum = outflowSum;
    }

    @Column(name = "outflow_count")
    public BigInteger getOutflowCount() {
        return outflowCount;
    }

    public void setOutflowCount(BigInteger outflowCount) {
        this.outflowCount = outflowCount;
    }

    @Column(name = "bnp")
    public BigDecimal getBnp() {
        return bnp;
    }

    public void setBnp(BigDecimal bnp) {
        this.bnp = bnp;
    }

}
