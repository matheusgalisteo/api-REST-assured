package br.ce.mvgalisteo.CursoRest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.Matchers.*;
import org.junit.Test;

import io.restassured.RestAssured;

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
			//.body("id", is(notNullValue()))
			.body("name", is("Jose"))
			.body("name", is(50))
		;
	}
}

