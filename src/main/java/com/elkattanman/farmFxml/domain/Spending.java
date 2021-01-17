package com.elkattanman.farmFxml.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "spending")
public class Spending implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "cost", nullable = false)
    private Double cost=0.0;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "name")
    private String name="";

    @ManyToOne
    @JoinColumn(name = "type_id")
    private Type type;
    public String getTypeName(){
        return type.getName();
    }

}
