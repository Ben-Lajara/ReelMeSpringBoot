package com.reelme.reelmespringboot;

import com.reelme.reelmespringboot.model.*;
import com.reelme.reelmespringboot.repository.*;
import com.reelme.reelmespringboot.service.RolService;
import com.reelme.reelmespringboot.service.UsuarioService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.HttpResource;
import org.springframework.web.servlet.resource.ResourceTransformer;
import org.springframework.web.servlet.resource.ResourceTransformerChain;

import java.io.IOException;
import java.util.*;

@SpringBootApplication
@EnableScheduling
public class ReelMeSpringBootApplication implements WebMvcConfigurer{
	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private RolService rolService;

	@Autowired
	private RolRepository rolRepository;

	@Autowired
	private PeliculaRepository peliculaRepository;

	@Autowired
	private ResenaRepository resenaRepository;

	@Autowired
	private RevisionadoRepository revisionadoRepository;

	@Autowired
	private DenunciaRepository denunciaRepository;

	public static void main(String[] args) {
		SpringApplication.run(ReelMeSpringBootApplication.class, args);
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://www.omdbapi.com", "https://api.themoviedb.org", "https://www.themoviedb.org", "https://image.tmdb.org"));
		configuration.setAllowedMethods(Arrays.asList("GET","POST"));
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@PostConstruct
	public void init(){
		if(isDatabaseEmpty()){
			generateDefaultData();
		}
	}

	private void generateDefaultData() {
		Rol rolUser = new Rol();
		rolUser.setRol("ROLE_USER");
		rolService.save(rolUser);
		Rol rolAdmin = new Rol();
		rolAdmin.setRol("ROLE_ADMIN");
		rolService.save(rolAdmin);


		Usuario admin = new Usuario();
		admin.setNombre("admin");
		admin.setPword("admin");
		admin.setEmail("reelmestaff@outlook.es");
		Set<Rol> roles = new HashSet<>(Arrays.asList(
				rolRepository.findByRol("ROLE_USER"),
				rolRepository.findByRol("ROLE_ADMIN")
		));
		admin.setPerfil("PruebaPerfil.jpg");
		String pwordHasheada = BCrypt.hashpw(admin.getPword(), BCrypt.gensalt());
		admin.setPword(pwordHasheada);
		admin.setRoles(roles);
		usuarioService.save(admin);

		Usuario darioCinefilo = new Usuario();
		darioCinefilo.setNombre("darioCinefilo");
		darioCinefilo.setPword("123456");
		darioCinefilo.setEmail("dario@gmail.com");
		darioCinefilo.setApodo("Darío");
		String pwordHash = BCrypt.hashpw(darioCinefilo.getPword(), BCrypt.gensalt());
		darioCinefilo.setPword(pwordHash);
		darioCinefilo.setPerfil("palomitasPerfil.jpg");
		darioCinefilo.setRoles(Collections.singleton(rolRepository.findByRol("ROLE_USER")));
		usuarioService.save(darioCinefilo);

		Usuario valerioVePelis = new Usuario();
		valerioVePelis.setNombre("valerioVePelis");
		valerioVePelis.setEmail("valerio@gmail.com");
		valerioVePelis.setPword("123456");
		valerioVePelis.setApodo("Valerio");
		String pwordHashValerio = BCrypt.hashpw(valerioVePelis.getPword(), BCrypt.gensalt());
		valerioVePelis.setPword(pwordHashValerio);
		valerioVePelis.setPerfil("carretePerfil.jpg");
		valerioVePelis.setRoles(Collections.singleton(rolRepository.findByRol("ROLE_USER")));
		usuarioService.save(valerioVePelis);

		Usuario dashaAmanteCine = new Usuario();
		dashaAmanteCine.setNombre("dashaAmanteCine");
		dashaAmanteCine.setEmail("dasha@gmail.com");
		dashaAmanteCine.setPword("123456");
		dashaAmanteCine.setApodo("Dasha");
		String pwordHashDasha = BCrypt.hashpw(dashaAmanteCine.getPword(), BCrypt.gensalt());
		dashaAmanteCine.setPword(pwordHashDasha);
		dashaAmanteCine.setPerfil("butacasPerfil.jpg");
		dashaAmanteCine.setRoles(Collections.singleton(rolRepository.findByRol("ROLE_USER")));
		usuarioService.save(dashaAmanteCine);

		Usuario nandoMovieLover = new Usuario();
		nandoMovieLover.setNombre("nandoMovieLover");
		nandoMovieLover.setApodo("Nando");
		nandoMovieLover.setEmail("nando@gmail.com");
		nandoMovieLover.setPword("123456");
		String pwordHashNando = BCrypt.hashpw(nandoMovieLover.getPword(), BCrypt.gensalt());
		nandoMovieLover.setPword(pwordHashNando);
		nandoMovieLover.setPerfil("claquetaPerfil.jpg");
		nandoMovieLover.setRoles(Collections.singleton(rolRepository.findByRol("ROLE_USER")));
		usuarioService.save(nandoMovieLover);

		Usuario eliCinefila = new Usuario();
		eliCinefila.setNombre("eliCinefila");
		eliCinefila.setPword("123456");
		eliCinefila.setEmail("eli@gmail.com");
		eliCinefila.setApodo("Eli");
		String pwordHashEli = BCrypt.hashpw(eliCinefila.getPword(), BCrypt.gensalt());
		eliCinefila.setPword(pwordHashEli);
		eliCinefila.setPerfil("carretePerfil.jpg");
		eliCinefila.setRoles(Collections.singleton(rolRepository.findByRol("ROLE_USER")));
		usuarioService.save(eliCinefila);

		Usuario rubenLovesMovies = new Usuario();
		rubenLovesMovies.setNombre("rubenLovesMovies");
		rubenLovesMovies.setEmail("rlajaraoton@gmail.com");
		rubenLovesMovies.setApodo("Rubén");
		rubenLovesMovies.setPword("123456");
		String pwordHashRuben = BCrypt.hashpw(rubenLovesMovies.getPword(), BCrypt.gensalt());
		rubenLovesMovies.setPword(pwordHashRuben);
		rubenLovesMovies.setPerfil("claquetaPerfil.jpg");
		rubenLovesMovies.setRoles(Collections.singleton(rolRepository.findByRol("ROLE_USER")));
		usuarioService.save(rubenLovesMovies);

		Pelicula regresoAlFuturo = new Pelicula();
		regresoAlFuturo.setTitulo("Back to the Future");
		regresoAlFuturo.setYear("1985");
		regresoAlFuturo.setFoto("https://m.media-amazon.com/images/M/MV5BZmU0M2Y1OGUtZjIxNi00ZjBkLTg1MjgtOWIyNThiZWIwYjRiXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SX300.jpg");
		regresoAlFuturo.setId("tt0088763");
		peliculaRepository.save(regresoAlFuturo);

		Pelicula elPadrino = new Pelicula();
		elPadrino.setTitulo("The Godfather");
		elPadrino.setYear("1972");
		elPadrino.setFoto("https://m.media-amazon.com/images/M/MV5BM2MyNjYxNmUtYTAwNi00MTYxLWJmNWYtYzZlODY3ZTk3OTFlXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_SX300.jpg");
		elPadrino.setId("tt0068646");
		peliculaRepository.save(elPadrino);

		Pelicula elSenorDeLosAnillos = new Pelicula();
		elSenorDeLosAnillos.setTitulo("The Lord of the Rings: The Fellowship of the Ring");
		elSenorDeLosAnillos.setYear("2001");
		elSenorDeLosAnillos.setFoto("https://m.media-amazon.com/images/M/MV5BN2EyZjM3NzUtNWUzMi00MTgxLWI0NTctMzY4M2VlOTdjZWRiXkEyXkFqcGdeQXVyNDUzOTQ5MjY@._V1_SX300.jpg");
		elSenorDeLosAnillos.setId("tt0120737");
		peliculaRepository.save(elSenorDeLosAnillos);

		Pelicula elReyLeon = new Pelicula();
		elReyLeon.setTitulo("The Lion King");
		elReyLeon.setYear("1994");
		elReyLeon.setFoto("https://m.media-amazon.com/images/M/MV5BYTYxNGMyZTYtMjE3MS00MzNjLWFjNmYtMDk3N2FmM2JiM2M1XkEyXkFqcGdeQXVyNjY5NDU4NzI@._V1_SX300.jpg");
		elReyLeon.setId("tt0110357");
		peliculaRepository.save(elReyLeon);

		Pelicula elResplandor = new Pelicula();
		elResplandor.setTitulo("The Shining");
		elResplandor.setYear("1980");
		elResplandor.setFoto("https://m.media-amazon.com/images/M/MV5BZWFlYmY2MGEtZjVkYS00YzU4LTg0YjQtYzY1ZGE3NTA5NGQxXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SX300.jpg");
		elResplandor.setId("tt0081505");
		peliculaRepository.save(elResplandor);

		Pelicula godzillaMinusOne = new Pelicula();
		godzillaMinusOne.setTitulo("Godzilla Minus One");
		godzillaMinusOne.setYear("2023");
		godzillaMinusOne.setFoto("https://m.media-amazon.com/images/M/MV5BOTI5MjNjMTMtN2NiNC00YjBlLTgzMWQtMGRhZDZkYmY1NGU2XkEyXkFqcGdeQXVyNTgyNTA4MjM@._V1_SX300.jpg");
		godzillaMinusOne.setId("tt23289160");
		peliculaRepository.save(godzillaMinusOne);

		Pelicula anatomiaDeUnaCaida = new Pelicula();
		anatomiaDeUnaCaida.setTitulo("Anatomy of a fall");
		anatomiaDeUnaCaida.setYear("2023");
		anatomiaDeUnaCaida.setFoto("https://m.media-amazon.com/images/M/MV5BMDBiYmRkNjUtYzc4My00NGFiLWE2NWUtMGU1ZDA1NTQ3ZjQwXkEyXkFqcGdeQXVyMTM1NjM2ODg1._V1_SX300.jpg");
		anatomiaDeUnaCaida.setId("tt17009710");
		peliculaRepository.save(anatomiaDeUnaCaida);

		Pelicula elRetornoDelJedi = new Pelicula();
		elRetornoDelJedi.setTitulo("Star Wars: Episode VI - Return of the Jedi");
		elRetornoDelJedi.setYear("1983");
		elRetornoDelJedi.setFoto("https://m.media-amazon.com/images/M/MV5BOWZlMjFiYzgtMTUzNC00Y2IzLTk1NTMtZmNhMTczNTk0ODk1XkEyXkFqcGdeQXVyNTAyODkwOQ@@._V1_SX300.jpg");
		elRetornoDelJedi.setId("tt0086190");
		peliculaRepository.save(elRetornoDelJedi);

		Pelicula spiderMan = new Pelicula();
		spiderMan.setTitulo("Spider-Man");
		spiderMan.setYear("2002");
		spiderMan.setFoto("https://m.media-amazon.com/images/M/MV5BZDEyN2NhMjgtMjdhNi00MmNlLWE5YTgtZGE4MzNjMTRlMGEwXkEyXkFqcGdeQXVyNDUyOTg3Njg@._V1_SX300.jpg");
		spiderMan.setId("tt0145487");
		peliculaRepository.save(spiderMan);

		Pelicula spiderManNoWayHome = new Pelicula();
		spiderManNoWayHome.setTitulo("Spider-Man: No Way Home");
		spiderManNoWayHome.setYear("2021");
		spiderManNoWayHome.setFoto("https://m.media-amazon.com/images/M/MV5BZWMyYzFjYTYtNTRjYi00OGExLWE2YzgtOGRmYjAxZTU3NzBiXkEyXkFqcGdeQXVyMzQ0MzA0NTM@._V1_SX300.jpg");
		spiderManNoWayHome.setId("tt10872600");
		peliculaRepository.save(spiderManNoWayHome);

		Pelicula asBestas = new Pelicula();
		asBestas.setTitulo("The Beasts");
		asBestas.setYear("2023");
		asBestas.setFoto("https://m.media-amazon.com/images/M/MV5BMzAwOTQ2YjctMDQyOS00NjNkLWFlMzMtYThiZGVkMjljNjg0XkEyXkFqcGdeQXVyMTA0MjU0Ng@@._V1_SX300.jpg");
		asBestas.setId("tt15006566");
		peliculaRepository.save(asBestas);

		Pelicula theBatman = new Pelicula();
		theBatman.setTitulo("The Batman");
		theBatman.setYear("2022");
		theBatman.setFoto("https://m.media-amazon.com/images/M/MV5BM2MyNTAwZGEtNTAxNC00ODVjLTgzZjUtYmU0YjAzNmQyZDEwXkEyXkFqcGdeQXVyNDc2NTg3NzA@._V1_SX300.jpg");
		theBatman.setId("tt1877830");
		peliculaRepository.save(theBatman);

		Resena resenaDarioRegresoAlFuturo = new Resena();
		resenaDarioRegresoAlFuturo.setIdPelicula(regresoAlFuturo);
		resenaDarioRegresoAlFuturo.setNomUsuario(darioCinefilo);
		resenaDarioRegresoAlFuturo.setCalificacion(5);
		resenaDarioRegresoAlFuturo.setComentario("Una de las mejores películas de la historia del cine");
		resenaDarioRegresoAlFuturo.setGustado(true);
		resenaDarioRegresoAlFuturo.setFecha(new Date("2024/05/01"));
		resenaRepository.save(resenaDarioRegresoAlFuturo);

		Resena resenaDarioElReyLeon = new Resena();
		resenaDarioElReyLeon.setIdPelicula(elReyLeon);
		resenaDarioElReyLeon.setNomUsuario(darioCinefilo);
		resenaDarioElReyLeon.setCalificacion(4);
		resenaDarioElReyLeon.setComentario("Una película muy bonita");
		resenaDarioElReyLeon.setGustado(true);
		resenaDarioElReyLeon.setFecha(new Date("2024/04/15"));
		resenaRepository.save(resenaDarioElReyLeon);

		Resena resenaDarioSpiderMan = new Resena();
		resenaDarioSpiderMan.setIdPelicula(spiderMan);
		resenaDarioSpiderMan.setNomUsuario(darioCinefilo);
		resenaDarioSpiderMan.setCalificacion(4.5f);
		resenaDarioSpiderMan.setComentario("Me encanta esta película");
		resenaDarioSpiderMan.setGustado(true);
		resenaDarioSpiderMan.setFecha(new Date("2024/04/10"));
		resenaRepository.save(resenaDarioSpiderMan);

		Resena resenaDarioSpiderManNoWayHome = new Resena();
		resenaDarioSpiderManNoWayHome.setIdPelicula(spiderManNoWayHome);
		resenaDarioSpiderManNoWayHome.setNomUsuario(darioCinefilo);
		resenaDarioSpiderManNoWayHome.setCalificacion(4);
		resenaDarioSpiderManNoWayHome.setComentario("Tobey Maguire tenía que haber hecho el baile de la 3");
		resenaDarioSpiderManNoWayHome.setGustado(true);
		resenaDarioSpiderManNoWayHome.setSpoiler(true);
		resenaDarioSpiderManNoWayHome.setFecha(new Date("2024/05/12"));
		resenaRepository.save(resenaDarioSpiderManNoWayHome);

		Resena resenaValerioElPadrino = new Resena();
		resenaValerioElPadrino.setIdPelicula(elPadrino);
		resenaValerioElPadrino.setNomUsuario(valerioVePelis);
		resenaValerioElPadrino.setCalificacion(5);
		resenaValerioElPadrino.setComentario("Una obra maestra del cine");
		resenaValerioElPadrino.setGustado(true);
		resenaValerioElPadrino.setFecha(new Date("2024/05/06"));
		resenaRepository.save(resenaValerioElPadrino);

		Resena resenaValerioRegresoAlFuturo = new Resena();
		resenaValerioRegresoAlFuturo.setIdPelicula(regresoAlFuturo);
		resenaValerioRegresoAlFuturo.setNomUsuario(valerioVePelis);
		resenaValerioRegresoAlFuturo.setCalificacion(2);
		resenaValerioRegresoAlFuturo.setComentario("No me ha gustado mucho");
		resenaValerioRegresoAlFuturo.setGustado(false);
		resenaValerioRegresoAlFuturo.setFecha(new Date("2024/05/20"));
		resenaRepository.save(resenaValerioRegresoAlFuturo);

		Resena resenaValerioSpiderMan = new Resena();
		resenaValerioSpiderMan.setIdPelicula(spiderMan);
		resenaValerioSpiderMan.setNomUsuario(valerioVePelis);
		resenaValerioSpiderMan.setCalificacion(5);
		resenaValerioSpiderMan.setComentario("Me encanta Spidey");
		resenaValerioSpiderMan.setGustado(true);
		resenaValerioSpiderMan.setFecha(new Date("2024/04/10"));
		resenaRepository.save(resenaValerioSpiderMan);

		Resena resenaValerioElRetornoDelJedi = new Resena();
		resenaValerioElRetornoDelJedi.setIdPelicula(elRetornoDelJedi);
		resenaValerioElRetornoDelJedi.setNomUsuario(valerioVePelis);
		resenaValerioElRetornoDelJedi.setCalificacion(4);
		resenaValerioElRetornoDelJedi.setComentario("No es la mejor pero está bien");
		resenaValerioElRetornoDelJedi.setGustado(true);
		resenaValerioElRetornoDelJedi.setFecha(new Date("2024/05/25"));
		resenaRepository.save(resenaValerioElRetornoDelJedi);

		Resena resenaDashaElSenorDeLosAnillos = new Resena();
		resenaDashaElSenorDeLosAnillos.setIdPelicula(elSenorDeLosAnillos);
		resenaDashaElSenorDeLosAnillos.setNomUsuario(dashaAmanteCine);
		resenaDashaElSenorDeLosAnillos.setCalificacion(4.5f);
		resenaDashaElSenorDeLosAnillos.setComentario("Me encanta esta película");
		resenaDashaElSenorDeLosAnillos.setGustado(true);
		resenaDashaElSenorDeLosAnillos.setFecha(new Date("2024/04/10"));
		resenaRepository.save(resenaDashaElSenorDeLosAnillos);

		Resena resenaDashaElResplandor = new Resena();
		resenaDashaElResplandor.setIdPelicula(elResplandor);
		resenaDashaElResplandor.setNomUsuario(dashaAmanteCine);
		resenaDashaElResplandor.setCalificacion(5);
		resenaDashaElResplandor.setComentario("Una de las mejores películas de terror");
		resenaDashaElResplandor.setGustado(true);
		resenaDashaElResplandor.setFecha(new Date("2024/05/01"));
		resenaRepository.save(resenaDashaElResplandor);

		Resena resenaDashaElRetornoDelJedi = new Resena();
		resenaDashaElRetornoDelJedi.setIdPelicula(elRetornoDelJedi);
		resenaDashaElRetornoDelJedi.setNomUsuario(dashaAmanteCine);
		resenaDashaElRetornoDelJedi.setCalificacion(1.5f);
		resenaDashaElRetornoDelJedi.setComentario("No me ha gustado mucho");
		resenaDashaElRetornoDelJedi.setGustado(false);
		resenaDashaElRetornoDelJedi.setFecha(new Date("2024/05/20"));
		resenaRepository.save(resenaDashaElRetornoDelJedi);

		Resena resenaNandoElReyLeon = new Resena();
		resenaNandoElReyLeon.setIdPelicula(elReyLeon);
		resenaNandoElReyLeon.setNomUsuario(nandoMovieLover);
		resenaNandoElReyLeon.setCalificacion(4);
		resenaNandoElReyLeon.setComentario("Me encanta esta película");
		resenaNandoElReyLeon.setGustado(true);
		resenaNandoElReyLeon.setFecha(new Date("2024/04/10"));
		resenaRepository.save(resenaNandoElReyLeon);

		Resena resenaNandoElSenorDeLosAnillos = new Resena();
		resenaNandoElSenorDeLosAnillos.setIdPelicula(elSenorDeLosAnillos);
		resenaNandoElSenorDeLosAnillos.setNomUsuario(nandoMovieLover);
		resenaNandoElSenorDeLosAnillos.setCalificacion(5);
		resenaNandoElSenorDeLosAnillos.setComentario("La mejor adaptación de la Tierra Media");
		resenaNandoElSenorDeLosAnillos.setGustado(true);
		resenaNandoElSenorDeLosAnillos.setFecha(new Date("2024/04/10"));
		resenaRepository.save(resenaNandoElSenorDeLosAnillos);

		Resena resenaNandoElResplandor = new Resena();
		resenaNandoElResplandor.setIdPelicula(elResplandor);
		resenaNandoElResplandor.setNomUsuario(nandoMovieLover);
		resenaNandoElResplandor.setCalificacion(3);
		resenaNandoElResplandor.setComentario("Demasiao aburrida");
		resenaNandoElResplandor.setGustado(false);
		resenaNandoElResplandor.setFecha(new Date("2024/03/20"));
		resenaRepository.save(resenaNandoElResplandor);

		Resena resenaNandoElRetornoDelJedi = new Resena();
		resenaNandoElRetornoDelJedi.setIdPelicula(elRetornoDelJedi);
		resenaNandoElRetornoDelJedi.setNomUsuario(nandoMovieLover);
		resenaNandoElRetornoDelJedi.setCalificacion(4);
		resenaNandoElRetornoDelJedi.setComentario("No es mi favorita, pero me encanta");
		resenaNandoElRetornoDelJedi.setGustado(true);
		resenaNandoElRetornoDelJedi.setFecha(new Date("2024/04/10"));
		resenaRepository.save(resenaNandoElRetornoDelJedi);


		Resena resenaDashaElReyLeon = new Resena();
		resenaDashaElReyLeon.setIdPelicula(elReyLeon);
		resenaDashaElReyLeon.setNomUsuario(dashaAmanteCine);
		resenaDashaElReyLeon.setCalificacion(4.5f);
		resenaDashaElReyLeon.setComentario("El círculo de la vida es mi canción favorita");
		resenaDashaElReyLeon.setGustado(true);
		resenaDashaElReyLeon.setFecha(new Date("2024/04/10"));
		resenaRepository.save(resenaDashaElReyLeon);

		Resena resenaEliElResplandor = new Resena();
		resenaEliElResplandor.setIdPelicula(elResplandor);
		resenaEliElResplandor.setNomUsuario(eliCinefila);
		resenaEliElResplandor.setCalificacion(2);
		resenaEliElResplandor.setComentario("No me ha gustado mucho");
		resenaEliElResplandor.setGustado(false);
		resenaEliElResplandor.setFecha(new Date("2024/05/20"));
		resenaRepository.save(resenaEliElResplandor);

		Resena resenaEliElReyLeon = new Resena();
		resenaEliElReyLeon.setIdPelicula(elReyLeon);
		resenaEliElReyLeon.setNomUsuario(eliCinefila);
		resenaEliElReyLeon.setCalificacion(4);
		resenaEliElReyLeon.setComentario("Me encanta esta película");
		resenaEliElReyLeon.setGustado(true);
		resenaEliElReyLeon.setFecha(new Date("2024/04/10"));
		resenaRepository.save(resenaEliElReyLeon);

		Resena resenaEliElSenorDeLosAnillos = new Resena();
		resenaEliElSenorDeLosAnillos.setIdPelicula(elSenorDeLosAnillos);
		resenaEliElSenorDeLosAnillos.setNomUsuario(eliCinefila);
		resenaEliElSenorDeLosAnillos.setCalificacion(5);
		resenaEliElSenorDeLosAnillos.setSpoiler(true);
		resenaEliElSenorDeLosAnillos.setComentario("Es la mejor adaptación, aunque pongan la muerte de Boromir al final");
		resenaEliElSenorDeLosAnillos.setGustado(true);
		resenaEliElSenorDeLosAnillos.setFecha(new Date("2024/04/14"));
		resenaRepository.save(resenaEliElSenorDeLosAnillos);

		Resena resenaRubenElPadrino = new Resena();
		resenaRubenElPadrino.setIdPelicula(elPadrino);
		resenaRubenElPadrino.setNomUsuario(rubenLovesMovies);
		resenaRubenElPadrino.setCalificacion(5);
		resenaRubenElPadrino.setComentario("Una obra maestra del cine");
		resenaRubenElPadrino.setGustado(true);
		resenaRubenElPadrino.setFecha(new Date("2024/02/02"));
		resenaRepository.save(resenaRubenElPadrino);

		Resena resenaRubenRegresoAlFuturo = new Resena();
		resenaRubenRegresoAlFuturo.setIdPelicula(regresoAlFuturo);
		resenaRubenRegresoAlFuturo.setNomUsuario(rubenLovesMovies);
		resenaRubenRegresoAlFuturo.setCalificacion(5);
		resenaRubenRegresoAlFuturo.setComentario("Una de las mejores películas de la historia del cine");
		resenaRubenRegresoAlFuturo.setGustado(true);
		resenaRubenRegresoAlFuturo.setFecha(new Date("2024/03/02"));
		resenaRepository.save(resenaRubenRegresoAlFuturo);

		Resena resenaRubenElSenorDeLosAnillos = new Resena();
		resenaRubenElSenorDeLosAnillos.setIdPelicula(elSenorDeLosAnillos);
		resenaRubenElSenorDeLosAnillos.setNomUsuario(rubenLovesMovies);
		resenaRubenElSenorDeLosAnillos.setCalificacion(5);
		resenaRubenElSenorDeLosAnillos.setComentario("Es mi favorita");
		resenaRubenElSenorDeLosAnillos.setGustado(true);
		resenaRubenElSenorDeLosAnillos.setFecha(new Date("2024/03/01"));
		resenaRepository.save(resenaRubenElSenorDeLosAnillos);

		Resena resenaRubenElReyLeon = new Resena();
		resenaRubenElReyLeon.setIdPelicula(elReyLeon);
		resenaRubenElReyLeon.setNomUsuario(rubenLovesMovies);
		resenaRubenElReyLeon.setCalificacion(4.5f);
		resenaRubenElReyLeon.setComentario("Una de las mejores películas de Disney");
		resenaRubenElReyLeon.setGustado(true);
		resenaRubenElReyLeon.setFecha(new Date("2024/04/15"));
		resenaRepository.save(resenaRubenElReyLeon);

		Resena resenaRubenElResplandor = new Resena();
		resenaRubenElResplandor.setIdPelicula(elResplandor);
		resenaRubenElResplandor.setNomUsuario(rubenLovesMovies);
		resenaRubenElResplandor.setCalificacion(4);
		resenaRubenElResplandor.setComentario("Kubrick se lució");
		resenaRubenElResplandor.setGustado(true);
		resenaRubenElResplandor.setFecha(new Date("2024/01/12"));
		resenaRepository.save(resenaRubenElResplandor);

		Resena resenaRubenSpiderMan = new Resena();
		resenaRubenSpiderMan.setIdPelicula(spiderMan);
		resenaRubenSpiderMan.setNomUsuario(rubenLovesMovies);
		resenaRubenSpiderMan.setCalificacion(4);
		resenaRubenSpiderMan.setComentario("Me gusta más que las siguientes");
		resenaRubenSpiderMan.setGustado(true);
		resenaRubenSpiderMan.setFecha(new Date("2024/02/16"));
		resenaRepository.save(resenaRubenSpiderMan);

		Resena resenaRubenElRetornoDelJedi = new Resena();
		resenaRubenElRetornoDelJedi.setIdPelicula(elRetornoDelJedi);
		resenaRubenElRetornoDelJedi.setNomUsuario(rubenLovesMovies);
		resenaRubenElRetornoDelJedi.setCalificacion(4);
		resenaRubenElRetornoDelJedi.setComentario("La mejor es la anterior pero sigue estando bien");
		resenaRubenElRetornoDelJedi.setGustado(true);
		resenaRubenElRetornoDelJedi.setFecha(new Date("2024/02/22"));
		resenaRepository.save(resenaRubenElRetornoDelJedi);

		Resena resenaRubenGodzillaMinusOne = new Resena();
		resenaRubenGodzillaMinusOne.setIdPelicula(godzillaMinusOne);
		resenaRubenGodzillaMinusOne.setNomUsuario(rubenLovesMovies);
		resenaRubenGodzillaMinusOne.setCalificacion(5);
		resenaRubenGodzillaMinusOne.setComentario("De las mejores películas del 2023.");
		resenaRubenGodzillaMinusOne.setGustado(true);
		resenaRubenGodzillaMinusOne.setFecha(new Date("2023/12/22"));
		resenaRepository.save(resenaRubenGodzillaMinusOne);

		Resena resenaRubenAnatomiaDeUnaCaida = new Resena();
		resenaRubenAnatomiaDeUnaCaida.setIdPelicula(anatomiaDeUnaCaida);
		resenaRubenAnatomiaDeUnaCaida.setNomUsuario(rubenLovesMovies);
		resenaRubenAnatomiaDeUnaCaida.setCalificacion(5);
		resenaRubenAnatomiaDeUnaCaida.setComentario("Se tenía que haber llevado el Óscar.");
		resenaRubenAnatomiaDeUnaCaida.setGustado(true);
		resenaRubenAnatomiaDeUnaCaida.setFecha(new Date("2024/05/14"));
		resenaRepository.save(resenaRubenAnatomiaDeUnaCaida);

		Resena resenaRubenAsBestas = new Resena();
		resenaRubenAsBestas.setIdPelicula(asBestas);
		resenaRubenAsBestas.setNomUsuario(rubenLovesMovies);
		resenaRubenAsBestas.setCalificacion(4);
		resenaRubenAsBestas.setComentario("Una película muy interesante.");
		resenaRubenAsBestas.setGustado(true);
		resenaRubenAsBestas.setFecha(new Date("2024/05/13"));
		resenaRepository.save(resenaRubenAsBestas);

		Resena resenaRubenTheBatman = new Resena();
		resenaRubenTheBatman.setIdPelicula(theBatman);
		resenaRubenTheBatman.setNomUsuario(rubenLovesMovies);
		resenaRubenTheBatman.setCalificacion(4);
		resenaRubenTheBatman.setComentario("Ya era hora de que hiciesen una buena de Batman.");
		resenaRubenTheBatman.setGustado(true);
		resenaRubenTheBatman.setFecha(new Date("2024/05/12"));
		resenaRepository.save(resenaRubenTheBatman);

		Revisionado revisionadoDarioElReyLeon = new Revisionado();
		revisionadoDarioElReyLeon.setResena(resenaDarioElReyLeon);
		revisionadoDarioElReyLeon.setFechaRevisionado(new Date("2024/05/30"));
		revisionadoRepository.save(revisionadoDarioElReyLeon);

		Revisionado revisionadoRubenGodzillaMinusOne = new Revisionado();
		revisionadoRubenGodzillaMinusOne.setResena(resenaRubenGodzillaMinusOne);
		revisionadoRubenGodzillaMinusOne.setFechaRevisionado(new Date("2024/06/01"));
		revisionadoRepository.save(revisionadoRubenGodzillaMinusOne);

		Denuncia denunciaValerioResenaDarioSpiderManNoWayHome = new Denuncia();
		denunciaValerioResenaDarioSpiderManNoWayHome.setDenunciante(valerioVePelis);
		denunciaValerioResenaDarioSpiderManNoWayHome.setDenunciado(darioCinefilo);
		denunciaValerioResenaDarioSpiderManNoWayHome.setIdResena(resenaDarioSpiderManNoWayHome);
		denunciaValerioResenaDarioSpiderManNoWayHome.setMotivo("Contenido inapropiado");
		denunciaRepository.save(denunciaValerioResenaDarioSpiderManNoWayHome);

		Denuncia denunciaRubenResenaEliResplandor = new Denuncia();
		denunciaRubenResenaEliResplandor.setDenunciante(rubenLovesMovies);
		denunciaRubenResenaEliResplandor.setDenunciado(eliCinefila);
		denunciaRubenResenaEliResplandor.setIdResena(resenaEliElResplandor);
		denunciaRubenResenaEliResplandor.setMotivo("Está siendo grosera.");
		denunciaRepository.save(denunciaRubenResenaEliResplandor);

	}

	private boolean isDatabaseEmpty(){
		return rolService.count() == 0;
	}
}
