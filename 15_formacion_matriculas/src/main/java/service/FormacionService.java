package service;

import java.util.Date;
import java.util.List;

import dtos.AlumnoDto;
import dtos.CursoDto;
import dtos.MatriculaDto;

public interface FormacionService {
	AlumnoDto validarUsuario(String usuario, String password);
	List<CursoDto> cursosAlumno(String usuario);
	List<CursoDto> cursos();
	List<AlumnoDto> alumnosCurso(String nombre);
	boolean matricular(String usuario, int idCurso);
	List<AlumnoDto> alumnos();
	boolean altaAlumno(AlumnoDto alumno);
	boolean altaCurso(String nombre, int duracion, Date fechaInicio, double precio);
	List<CursoDto> cursosNoMatriculados(String usuario);
	List<MatriculaDto> cursosEntreFecha(Date fechaInicio, Date fechaFin);
	AlumnoDto alumno(String nombre);
	CursoDto curso(String nombre);
}
