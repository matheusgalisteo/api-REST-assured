package br.ce.mvgalisteo.CursoRest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class AuthTest {
	@Test
	public void deveAcessarSWAPI(){
		given()
			.log().all()
		.when()
			.get("https://swapi.co/api/people/1")
		.then()
			.log().all()
			.statusCode(200)
			.body("name", is("Luke Skywalker"))
		;
	//174343e0f404c304967a5e2c8213f5b1
	//https://api.openweathermap.org/data/2.5/weather?q=London,uk&appid=174343e0f404c304967a5e2c8213f5b1&units=metric
	
	}
	@Test
	public void deveObterClima(){
		given()
			.log().all()
			.queryParam("q","Fortaleza,BR")
			.queryParam("appid", "174343e0f404c304967a5e2c8213f5b1")
			.queryParam("units", "metric")
		.when()
			.get("https://api.openweathermap.org/data/2.5/weather")
		.then()
			.log().all()
			.statusCode(200)
			.body("name", is("Fortaleza"))
			.body("coord.lon", is(-38.52f))
		//	.body("main.temp", greaterThan(25f))
	;
	}
	@Test
	public void naoDeveAcessarSemSenha(){
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(401)
		;
	}
	@Test
	public void deveAutenticarBasicamente(){
		given()
			.log().all()
		.when()
			.get("https://admin:senha@restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
		;
	}
	@Test
	public void deveAutenticarBasicamente2(){
		given()
			.log().all()
			.auth().basic("admin", "senha")
		.when()
			.get("https://restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
		;
	}
	@Test
	public void deveAutenticarBasicamenteChallenge(){
		given()
			.log().all()
			.auth().preemptive().basic("admin", "senha")
		.when()
			.get("https://restapi.wcaquino.me/basicauth2")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
		;
	}
	@Test
	public void deveFazerAutenticacaoComTokenJWT(){
		//login api
		//receber o token
		Map<String, String> login = new HashMap<String, String>();
		login.put("email", "matheus_vieira87@hotmail.com");
		login.put("senha", "teste123");
		
		String token = given()
			.log().all()
			.body(login)
			.contentType(ContentType.JSON)
		.when()
			.post("http://barrigarest.wcaquino.me/signin")
		.then()
			.log().all()
			.statusCode(200)
			.extract().path("token")
	;		
		//Obter as Contas
		 given()
			.log().all()
			.header("Authorization", "JWT " + token)
		.when()
			.get("http://barrigarest.wcaquino.me/contas")
		.then()
			.log().all()
			.statusCode(200)
			//.body("nome", hasItem("Alguma conta"))//hasitem pois é uma coleção de contas
	;		
	}
	@Test
	public void deveAcessarAplicacaoWeb(){
		//login
		 String cookie = given()
			.log().all()
			.formParam("email", "matheus_vieira87@hotmail.com")
			.formParam("senha", "teste123")
			.contentType(ContentType.URLENC.withCharset("UTF-8"))
			//.header("Authorization", "JWT " + token)
		.when()
			.post("http://seubarriga.wcaquino.me/logar")
		.then()
			.log().all()
			.statusCode(200)
			.extract().header("set-cookie")
			;
		 
		 //quebrando o cookie para pegar apenas ele
		 cookie = cookie.split("=")[1].split(";")[0];
		//System.out.println(cookie);
		//Obter Conta
		given()
			.log().all()
			.cookie("connect.sid", cookie)
		.when()
			.get("http://seubarriga.wcaquino.me/contas")
		.then()
			.log().all()
			.statusCode(200)
			//.body("html.body.table.body.tr[0].td[0]", is("Conta de teste"))
		;
	}
}
