package com.elkattanman.farmFxml.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "capital")
public class Capital implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "balance", nullable = false)
    private Double balance;

    @Column(name = "current_total", nullable = false)
    private Double currentTotal;
    //-----------------------------------
    @Column(name = "total_payments", nullable = false)
    private Double totalPayments;

    @Column(name = "buy", nullable = false)
    private Double buy;

    @Column(name = "feed", nullable = false)
    private Double feed;

    @Column(name = "spending", nullable = false)
    private Double spending;

    @Column(name = "treatment", nullable = false)
    private Double treatment;
    //------------------------------------------------
    @Column(name = "total_gain", nullable = false)
    private Double totalGain;

    @Column(name = "sales", nullable = false)
    private Double sales;

    @Column(name = "reserve", nullable = false)
    private Double reserve;
    //-------------------------------------------------
}
