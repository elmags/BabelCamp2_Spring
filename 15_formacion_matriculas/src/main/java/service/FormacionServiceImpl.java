package service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import converters.ConversorEntityDto;
import dao.AlumnosDao;
import dao.CursosDao;
import dao.MatriculasDao;
import dtos.AlumnoDto;
import dtos.CursoDto;
import dtos.MatriculaDto;
import model.Alumno;
import model.Curso;
import model.Matricula;
import model.MatriculaPK;

@Service
public class FormacionServiceImpl implements FormacionService {

	AlumnosDao alumnosDao;
	CursosDao cursosDao;
	MatriculasDao matriculasDao;
	
	@Autowired
	ConversorEntityDto conversor;
	
	public FormacionServiceImpl(@Autowired AlumnosDao alumnosDao, @Autowired CursosDao cursosDao, @Autowired MatriculasDao matriculasDao) {
		this.alumnosDao = alumnosDao;
		this.cursosDao = cursosDao;
		this.matriculasDao = matriculasDao;
	}

	@Override
	public AlumnoDto validarUsuario(String usuario, String password) {
		return conversor.alumnoToDto(alumnosDao.findByUsuarioAndPassword(usuario, password));
	}

	@Override
	public List<CursoDto> cursosAlumno(String usuario) {
		return cursosDao.findByAlumno(usuario).stream().map(c -> conversor.cursoToDto(c)).collect(Collectors.toList());
	}

	@Override
	public List<CursoDto> cursos() {
		return cursosDao.findAll().stream().map(c -> conversor.cursoToDto(c)).collect(Collectors.toList());
	}

	@Override
	public List<AlumnoDto> alumnosCurso(String nombre) {
		return alumnosDao.findByCurso(nombre).stream().map(a -> conversor.alumnoToDto(a)).collect(Collectors.toList());
	}

	@Transactional
	@Override
	public boolean matricular(String usuario, int idCurso) {		
		Optional<Alumno> alumno = alumnosDao.findById(usuario);
		Optional<Curso> curso = cursosDao.findById(idCurso);
		System.out.println(alumno.isPresent());
		System.out.println(curso.isPresent());
		if (alumno.isPresent() && curso.isPresent()) {
			System.out.println("2");
			matriculasDao.save(new Matricula(new MatriculaPK(usuario, idCurso), 0, alumno.get(), curso.get()));
			System.out.println("3");
			return true;
		}
		return false;
	}

	@Override
	public List<AlumnoDto> alumnos() {
		return alumnosDao.findAll().stream().map(a -> conversor.alumnoToDto(a)).collect(Collectors.toList());
	}
	
	@Override
	public boolean altaAlumno(AlumnoDto alumno) {
		Optional<Alumno> a = alumnosDao.findById(alumno.getUsuario());
		if (a.isEmpty()) {
			alumnosDao.save(conversor.dtoToAlumno(alumno));
			return true;
		}
		return false;
	}
	
	@Override
	public boolean altaCurso(String nombre, int duracion, Date fechaInicio, double precio) {
		Optional<Curso> c = cursosDao.findByNombre(nombre);
		if (c.isEmpty()) {
			cursosDao.save(new Curso(nombre, duracion, fechaInicio, precio));
			return true;
		}
		return false;
	}

	@Override
	public List<CursoDto> cursosNoMatriculados(String usuario) {
		return cursosDao.findCursos(usuario).stream().map(c -> conversor.cursoToDto(c)).collect(Collectors.toList());
	}

	@Override
	public List<MatriculaDto> cursosEntreFecha(Date fechaInicio, Date fechaFin) {
		return matriculasDao.findBetweenDate(fechaInicio, fechaFin).stream().map(m -> conversor.matriculaToDto(m)).collect(Collectors.toList());
	}

	@Override
	public AlumnoDto alumno(String nombre) {
		Optional<Alumno> a = alumnosDao.findByNombre(nombre);
		if (a.isPresent())
			return conversor.alumnoToDto(a.get());
		return null;
	}

	@Override
	public CursoDto curso(String nombre) {
		Optional<Curso> c = cursosDao.findByNombre(nombre);
		if (c.isPresent())
			return conversor.cursoToDto(c.get());
		return null;
	}
}
