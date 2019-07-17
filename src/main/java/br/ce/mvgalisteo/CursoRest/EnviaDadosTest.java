package br.ce.mvgalisteo.CursoRest;

import static io.restassured.RestAssured.given;

import org.hamcrest.Matchers;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class EnviaDadosTest {
	
	@Test
	public void deveEnviarValorViaQuery(){
		given()
			.log().all()
		.when()
			.get("http://restapi.wcaquino.me/v2/users?format=json")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.JSON)
		;
	}
	@Test
	public void deveEnviarValorViaQueryViaParametro(){
		given()
			.log().all()
			.queryParam("format", "json")
			.queryParam("outra", "coisa")//insere na requisição, mas ignora
		.when()
			.get("http://restapi.wcaquino.me/v2/users")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.JSON)
			.contentType(Matchers.containsString("utf-8"))//verificando se esta no formato UTF-8
		;
	}
	@Test
	public void deveEnviarValorViaHeader(){
		given()
			.log().all()
			.accept(ContentType.XML) //o que eu quero que venha na resposta
		.when()
			.get("http://restapi.wcaquino.me/v2/users")//sem formato envia o HTML que esta no then content type
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.XML)
		;
	}

}
