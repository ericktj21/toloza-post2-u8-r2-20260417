package com.universidad.estudiantes.repository;

import com.universidad.estudiantes.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    List<Estudiante> findByCarreraIgnoreCase(String carrera);

    List<Estudiante> findByNombreContainingOrApellidoContaining(String nombre, String apellido);
}
