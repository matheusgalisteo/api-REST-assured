package br.ce.mvgalisteo.CursoRest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.hamcrest.Matchers;
import org.junit.Test;

import io.restassured.RestAssured;

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
			.body("main.temp", greaterThan(25f))
	;
	}
}
