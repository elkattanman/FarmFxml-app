package com.elkattanman.farmFxml.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "type")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Type implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "is_buy", nullable = false)
    private Boolean buy=false;

    @Column(name = "is_death", nullable = false)
    private Boolean death=false;

    @Column(name = "is_reserve", nullable = false)
    private Boolean reserve=false;

    @Column(name = "is_sale", nullable = false)
    private Boolean sale=false;

    @Column(name = "is_treatment", nullable = false)
    private Boolean treatment=false;

    @Column(name = "is_borns", nullable = false)
    private Boolean borns=false;

    @Column(name = "is_feed", nullable = false)
    private Boolean feed=false;

    @Column(name = "is_spending", nullable = false)
    private Boolean spending=false;

    @Column(name = "name")
    private String name;

    @OneToOne
    @JoinColumn(name = "type_info_id")
    private TypeInfo typeInfo;

}
