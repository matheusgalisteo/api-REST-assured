package br.ce.mvgalisteo.CursoRest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import org.junit.Test;
import io.restassured.RestAssured.*;

public class UserXMLTest {
	@Test
	public void devoTrabalharComXML(){
		given()
		.when()
			.get("http://restapi.wcaquino.me/usersXML/3")
		.then()
			.statusCode(200)
		.body("user.name", is("Ana Julia"))
		//para XML que usa atributos é necessário referencia-los com o @, sendo um atributo
		.body("user.@id", is("3"))
		.body("user.filhos.name.size()", is(2))
		.body("user.filhos.name[0]", is("Zezinho"))
		.body("user.filhos.name[1]", is("Luizinho"))
		.body("user.filhos.name", hasItem("Luizinho"))
		.body("user.filhos.name", hasItems("Luizinho","Zezinho"))
		;
	}

}
