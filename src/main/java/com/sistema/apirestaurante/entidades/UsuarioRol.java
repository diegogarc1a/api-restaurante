package com.sistema.apirestaurante.entidades;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "usuariorol")
@Data
public class UsuarioRol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @JsonIgnoreProperties(value = {"usuarioRoles"})
    @ManyToOne(fetch = FetchType.EAGER)
    private Usuario usuario;


    @JsonIgnoreProperties(value = {"usuarioRoles"})
    @ManyToOne
    private Rol rol;

}