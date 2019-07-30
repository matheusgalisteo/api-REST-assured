package br.ce.mvgalisteo.CursoRest;

import static io.restassured.RestAssured.given;

import org.junit.Test;

import io.restassured.matcher.RestAssuredMatchers;
import io.restassured.module.jsv.JsonSchemaValidator;

import org.xml.sax.SAXParseException;

public class SchemaTest {
	
	//GERADOR DE XSD para comparação https://www.freeformatter.com/xsd-generator.html#ad-output
	//Validando esquema via XML (comparando arquivos)
	@Test
	public void deveValidarSchemaXML(){
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(200)
			.body(RestAssuredMatchers.matchesXsdInClasspath("users.xsd"))
			;
	}
	
	@Test(expected=SAXParseException.class)//Informando que esta exceção está sendo lançada para termos o sucesso do teste 
	public void naoDeveValidarSchemaXMLInvalido(){
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/invalidusersXML")
		.then()
			.log().all()
			.statusCode(200)
			.body(RestAssuredMatchers.matchesXsdInClasspath("users.xsd"))
			;
	}
	
	//Json schema validator no mvn repository para esse teste - https://app.quicktype.io em other desmarcar todas selectors
	@Test
	public void deveValidarSchemaJSON(){
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(200)
			.body(JsonSchemaValidator.matchesJsonSchemaInClasspath("users.json"))
			;
	}
}
