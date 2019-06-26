package br.ce.mvgalisteo.CursoRest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import com.sun.xml.internal.ws.client.sei.ResponseBuilder.Body;

import io.restassured.RestAssured.*;
import io.restassured.internal.path.xml.NodeImpl;

public class UserXMLTest {
	@Test
	public void devoTrabalharComXML(){
		given()
		.when()
			.get("http://restapi.wcaquino.me/usersXML/3")
		.then()
			.statusCode(200)
			.rootPath("user")
			.body("name", is("Ana Julia"))
			//para XML que usa atributos é necessário referencia-los com o @, sendo um atributo
			.body("@id", is("3"))
			.rootPath("user.filhos")
			.body("name.size()", is(2))
			
			.detachRootPath("filhos")
			.body("filhos.name[0]", is("Zezinho"))
			.body("filhos.name[1]", is("Luizinho"))
			.appendRootPath("filhos")
			.body("name", hasItem("Luizinho"))
		    .body("name", hasItems("Luizinho","Zezinho"))
		;
	}
	@Test
	public void devoPesquisasAvancadasComXML(){
		given()
		.when()
			.get("http://restapi.wcaquino.me/usersXML")
		.then()
			.statusCode(200)
			.body("users.user.size()", is(3))
			//Para XML todos valores são strings
			.body("users.user.findAll{it.age.toInteger() <= 25}.size()", is(2))
			.body("users.user.@id", hasItems("1","2", "3"))
			.body("users.user.find{it.age == 25}.name", is("Maria Joaquina"))
			.body("users.user.findAll{it.name.toString().contains('n')}.name", hasItems("Maria Joaquina", "Ana Julia"))
			//XML busca por string colocando o toDouble esta convertendo para double o que faz com que a assertiva não precise ficar entre as ""
			.body("users.user.salary.find{it != null}.toDouble()", is(1234.5678d))
			.body("users.user.age.collect{it.toInteger()*2}", hasItems(40, 50, 60))
			.body("users.user.name.findAll{it.toString().startsWith('Maria')}.collect{it.toString().toUpperCase()}", is("MARIA JOAQUINA"))
		;
	}
	@Test
	//body("users.user.name.findAll{it.toString().startsWith('Maria')}.collect{it.toString().toUpperCase()}", is("MARIA JOAQUINA"))
	public void devoFazerPesquisasAvancadasComXMLEJava(){
    	
		ArrayList<NodeImpl> nomes = given()
		//String nome = given()
		.when()
			.get("http://restapi.wcaquino.me/usersXML")
		.then()
			.statusCode(200)
			.extract().path("users.user.name.findAll{it.toString().contains('n')}")
			//.extract().path("users.user.name.findAll{it.toString().startsWith('Maria')}");
		;
		//Assert.assertEquals("Maria Joaquina".toUpperCase(), nome.toUpperCase());
		Assert.assertEquals(2, nomes.size());
		Assert.assertEquals("Maria Joaquina".toUpperCase(), nomes.get(0).toString().toUpperCase());
		Assert.assertTrue("ana julia".equalsIgnoreCase(nomes.get(1).toString()));
	}
	@Test
	public void devoFazerPesquisasAvancadasComXPath(){
    	
		given()
		.when()
			.get("http://restapi.wcaquino.me/usersXML")
		.then()
			.statusCode(200)
			.body(hasXPath("count(/users/user)", is("3")))
			.body(hasXPath("/users/user[@id = 1]"))
			//Desce com 2 barras até o valor que achar e para a busca
			.body(hasXPath("//users/user[@id = 2]"))
			//Percorrendo o caminho para encontrar o nome da mãe do Zezinho
			.body(hasXPath("//name[text() = 'Luizinho']/../../name", is("Ana Julia")))
			//A partir do nome da mãe descobrir o nome do filho
			.body(hasXPath("//name[text() = 'Ana Julia']/following-sibling::filhos", allOf(containsString("Zezinho"), containsString("Luizinho"))))
			.body(hasXPath("//name", is("João da Silva")))
			//Segundo nome
			.body(hasXPath("/users/user[2]/name", is("Maria Joaquina")))
			.body(hasXPath("/users/user[last()]/name", is("Ana Julia")))
			.body(hasXPath("count(/users/user/name[contains(.,'n')])", is("2")))
			.body(hasXPath("//user[age < 24]/name", is("Ana Julia")))
			.body(hasXPath("//user[age > 20] [age < 30]/name", is("Maria Joaquina")))
			
			;
	}

}
