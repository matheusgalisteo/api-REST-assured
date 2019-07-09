package br.ce.mvgalisteo.CursoRest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.Matchers.*;
import org.junit.Test;

import com.sun.istack.internal.NotNull;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class VerbosTest {
	@Test
	public void deveSalvarUsuario(){
		given()
			.log().all() //ver a requisição que estou mandando
			.contentType("application/json") //dizendo que meu objeto deve ser interpretado como objeto json
			.body("{\"name\": \"Jose\",\"age\": 50}")
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			//.body("id", is(NotNullValue()))
			.body("name", is("Jose"))
			.body("age", is(50))
		;
	}
	@Test
	public void naoDeveSalvarUsuarioSemNome() {
		given()
			.log().all() // ver a requisição que estou mandando
			.contentType("application/json") // dizendo que meu objeto deve ser interpretado como objeto json
				.body("{\"age\": 50}")
				.when()
					.post("https://restapi.wcaquino.me/users")
				.then()
				.log().all()
				.statusCode(400)
				.body("error", is("Name é um atributo obrigatório"))
				//.body("id", is(nullValue()))
				//.body("name", is("Jose")).body("age", is(50));
				;
	
	}
	@Test
	public void deveSalvarUsuarioXML(){
		given()
			.log().all() //ver a requisição que estou mandando
			.contentType("application/xml") //.contentType(ContentType.XML) <- mais indicado//dizendo que meu objeto deve ser interpretado como objeto json
			.body("<user><name>Jose</name><age>50</age></user>")
		.when()
			.post("https://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(201)
			//.body("id", is(NotNullValue()))
			.body("user.name", is("Jose"))
			.body("user.age", is("50"))
		;
	}
	@Test
	public void deveAlterarUsuario(){
		given()
			.log().all() //ver a requisição que estou mandando
			.contentType("application/json") //dizendo que meu objeto deve ser interpretado como objeto json
			.body("{\"name\": \"Usuario Alterado\",\"age\": 80}")
		.when()
			.put("https://restapi.wcaquino.me/users/1") //Put para realizar alteração
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Usuario Alterado"))
			.body("age", is(80))
			.body("salary", is(1234.5678f))
		;
	}
	@Test
	public void deveCustomizarURL(){
		given()
			.log().all() //ver a requisição que estou mandando
			.contentType("application/json") //dizendo que meu objeto deve ser interpretado como objeto json
			.body("{\"name\": \"Usuario Alterado\",\"age\": 80}")
		.when()
			.put("https://restapi.wcaquino.me/{entidade}/{userId}", "users", "1") //Put para realizar alteração
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Usuario Alterado"))
			.body("age", is(80))
			.body("salary", is(1234.5678f))
		;
	}
	@Test
	public void deveCustomizarURLParteII(){
		given()
			.log().all() //ver a requisição que estou mandando
			.contentType("application/json") //dizendo que meu objeto deve ser interpretado como objeto json
			.body("{\"name\": \"Usuario Alterado\",\"age\": 80}")
			.pathParam( "entidade", "users")
			.pathParam( "userId", "1")			
		.when()
			.put("https://restapi.wcaquino.me/{entidade}/{userId}") //Put para realizar alteração
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Usuario Alterado"))
			.body("age", is(80))
			.body("salary", is(1234.5678f))
		;
	}
	@Test
	public void deveRemoverUsuario(){
		given()
			.log().all()
		.when()
			.delete("https://restapi.wcaquino.me/users/1")
		.then()
			.log().all()
			.statusCode(204)
		;
	}
	@Test
	public void naoDeveRemoverUsuarioInexistente(){
		given()
			.log().all()
		.when()
			.delete("https://restapi.wcaquino.me/users/1000")
		.then()
			.log().all()
			.statusCode(400)
			.body("error", is("Registro inexistente"))
		;
	}
	
}

