package com.universidad.estudiantes.controller;

import com.universidad.estudiantes.model.Curso;
import com.universidad.estudiantes.model.Estudiante;
import com.universidad.estudiantes.service.CursoService;
import com.universidad.estudiantes.service.EstudianteService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cursos")
public class CursoController {

    private final CursoService cursoService;
    private final EstudianteService estudianteService;

    public CursoController(CursoService cursoService, EstudianteService estudianteService) {
        this.cursoService = cursoService;
        this.estudianteService = estudianteService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("cursos", cursoService.listarTodos());
        return "cursos/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("curso", new Curso());
        return "cursos/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("curso") Curso curso,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "cursos/formulario";
        }
        cursoService.guardar(curso);
        redirectAttributes.addFlashAttribute("mensaje", "Curso guardado correctamente");
        return "redirect:/cursos";
    }

    @GetMapping("/{id}/inscribir")
    public String inscribirVista(@PathVariable Long id, Model model) {
        Curso curso = cursoService.buscarPorId(id);
        List<Estudiante> todos = estudianteService.listarTodos();
        Set<Long> inscritosIds = curso.getEstudiantes().stream().map(Estudiante::getId).collect(Collectors.toSet());
        List<Estudiante> disponibles = todos.stream().filter(e -> !inscritosIds.contains(e.getId())).toList();

        model.addAttribute("curso", curso);
        model.addAttribute("inscritos", curso.getEstudiantes());
        model.addAttribute("disponibles", disponibles);
        return "cursos/inscribir";
    }

    @PostMapping("/{cursoId}/inscribir/{estudianteId}")
    public String inscribir(@PathVariable Long cursoId, @PathVariable Long estudianteId, RedirectAttributes ra) {
        cursoService.inscribirEstudiante(cursoId, estudianteId);
        ra.addFlashAttribute("mensaje", "Estudiante inscrito correctamente");
        return "redirect:/cursos/" + cursoId + "/inscribir";
    }

    @PostMapping("/{cursoId}/desinscribir/{estudianteId}")
    public String desinscribir(@PathVariable Long cursoId, @PathVariable Long estudianteId, RedirectAttributes ra) {
        cursoService.desinscribirEstudiante(cursoId, estudianteId);
        ra.addFlashAttribute("mensaje", "Estudiante desinscrito correctamente");
        return "redirect:/cursos/" + cursoId + "/inscribir";
    }
}
