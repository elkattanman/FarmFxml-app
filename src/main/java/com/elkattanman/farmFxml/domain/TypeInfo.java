package com.elkattanman.farmFxml.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "type_info")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TypeInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "borns", nullable = false)
    private Integer borns;

    @Column(name = "deaths", nullable = false)
    private Integer deaths;

    @Column(name = "total", nullable = false)
    private Integer total;

}
