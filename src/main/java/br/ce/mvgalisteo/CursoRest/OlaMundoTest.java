package br.ce.mvgalisteo.CursoRest;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.request;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.awt.List;
import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import com.sun.xml.internal.ws.client.sei.ResponseBuilder.Body;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class OlaMundoTest {
	@Test
	public void testeOlaMundo() {
		Response response = request(Method.GET, "http://restapi.wcaquino.me:80/ola");
		Assert.assertTrue(response.getBody().asString().equals("Ola Mundo!"));
		Assert.assertTrue(response.statusCode() == 200);
		Assert.assertTrue("O Status deve ser 200", response.statusCode() == 200);
		Assert.assertEquals(200, response.statusCode());

		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);

	}

	@Test
	public void devoConhecerOutrasFormasRestAssured() {
		Response response = request(Method.GET, "http://restapi.wcaquino.me:80/ola");
		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);

		get("http://restapi.wcaquino.me:80/ola").then().statusCode(200);

		given()
			//Pré-condições
		.when() //Ação a ser testada
			.get("http://restapi.wcaquino.me:80/ola")
		.then() //Assertivas (então)
			.statusCode(200);
	}
	
	@Test
	public void devoConhecerMatcherHamcrest(){
		Assert.assertThat("Maria", Matchers.is("Maria"));
		assertThat(128, Matchers.is(128));
		assertThat(128, isA(Integer.class));
		assertThat(128d, Matchers.isA(Double.class));
		assertThat(128d, greaterThan(120d));
		assertThat(128d, Matchers.lessThan(130d));
		
		java.util.List<Integer> impares = Arrays.asList(1, 3, 5, 7);
		assertThat(impares, hasSize(4));
		assertThat(impares, contains(1, 3, 5, 7));
		
		Assert.assertThat("Maria", Matchers.is("Maria"));
	
	}
	@Test
	public void devoValidarBody(){
		given()
	.when() 
		.get("http://restapi.wcaquino.me:80/ola")
	.then()
		.statusCode(200)
		.body(is(("Ola Mundo!")))
		;
	}


}
