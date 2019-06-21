package br.ce.mvgalisteo.CursoRest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.greaterThan;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;

import org.junit.Assert;
//import org.hamcrest.Matchers;
import org.junit.Test;

import com.sun.xml.internal.ws.client.sei.ResponseBuilder.Body;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class UserJsonTest {
	@Test
	public void deveVerificarPrimeiroNivel() {
		given().when().get("http://restapi.wcaquino.me/users/1").then().statusCode(200).body("id", is(1))
				.body("name", containsString("Silva")).body("age", greaterThan(18));
	}

	@Test
	public void deveVerificarPrimeiroNivelOutrasFormas() {
		Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/users/1");

		// path
		Assert.assertEquals(new Integer(1), response.path("id"));

		// jsonpath
		JsonPath jpath = new JsonPath(response.asString());
		Assert.assertEquals(1, jpath.getInt("id"));

		// from
		int id = JsonPath.from(response.asString()).getInt("id");
		Assert.assertEquals(1, id);
	}

	@Test
	public void deveVerificarSegundoNivel() {
		given()
		.when()
		.get("http://restapi.wcaquino.me/users/2")
		.then().statusCode(200)
				.body("name", containsString("Joaquina"))
				.body("age", greaterThan(18))
				.body("endereco.rua", is("Rua dos bobos") );
	}
	@Test
	public void deveVerificarUmaLista() {
		given()
		.when()
		.get("http://restapi.wcaquino.me/users/3")
		.then().statusCode(200)
				.body("name", containsString("Ana"))
				.body("filhos", hasSize(2) )
				.body("filhos[0].name", is("Zezinho"))
				.body("filhos[1].name", is("Luizinho"))
				.body("filhos.name", hasItem("Zezinho"))
				.body("filhos.name", hasItems("Zezinho", "Luizinho"))
				;
	}
	@Test
	public void deveVerificarErroDeUsuarioInexistente() {
		given()
		.when()
		.get("http://restapi.wcaquino.me/users/4")
		.then().statusCode(404)
		.body("error", is("Usuário inexistente"))
		;
	}
	@SuppressWarnings("unchecked")
	@Test
	public void deveVerificarListaNaRaiz() {
		given()
		.when()
		.get("http://restapi.wcaquino.me/users")
		.then()
			.statusCode(200)
			.body("$", hasSize(3))
			.body("name", hasItems("João da Silva","Maria Joaquina","Ana Júlia") )
			.body("age[1]", is(25))
			.body("filhos.name", hasItem(Arrays.asList("Zezinho", "Luizinho")))
			.body("salary", contains(1234.5678f, 2500, null));
	}
	@Test
	public void devoFazerVerificacoesAvancadas(){
		given()
		.when()
		.get("http://restapi.wcaquino.me/users")
		.then()
			.statusCode(200)
			.body("$", hasSize(3))
			//buscando usuarios da lista que possuam idade menor ou igual a 25 anos
			.body("age.findAll{it <= 25}.size()", is(2))
			//buscando usuarios da lista que possuam idade entre 20 e 25 aos
			.body("age.findAll{it <= 25 && it > 20}.size()", is(1))
			//buscando usuários que tenho idade menor ou igual a 25 anos retornando o nome
			.body("findAll{it.age <=25}.name", hasItems("Maria Joaquina", "Ana Júlia"))
			//buscando usuários que tenho idade menor ou igual a 25 anos retornando o nome transformando a lista em um objeto
			.body("findAll{it.age <=25}[0].name", is("Maria Joaquina"))
			//traz apenas um registro "find"
			.body("find{it.age <=25}.name", is("Maria Joaquina"))
			//Encontrar todos nomes que possuem n em seu nome
			.body("findAll{it.name.contains('n')}.name", hasItems("Maria Joaquina", "Ana Júlia"))
			//Encontra os nomes que possuem mais de 10 caracteres
			.body("findAll{it.name.length() > 10}.name", hasItems("Maria Joaquina", "João da Silva"))
			//Busca os nomes com uppercase
			.body("name.collect{it.toUpperCase()}", hasItem("MARIA JOAQUINA"))
			//Busca em todos itens algum que comece com Maria, seguinte transforma em array para ver a quantidade que retorna
			.body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}", hasItem("MARIA JOAQUINA"))
			.body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}.toArray()", allOf(arrayContaining("MARIA JOAQUINA"),arrayWithSize(1)))
			//Multiplica as idades e verifica o retorno
			.body("age.collect{it * 2}", hasItems(60, 50, 40))
			//Verifica o maior id da coleção
			.body("id.max()", is(3))
			//Verifica o menor salario da coleção
			.body("salary.min()", is(1234.5678f))
			//Verifica a soma dos salarios e exclue da soma algum item que não possua o item 
			.body("salary.findAll{it != null}.sum()", is(closeTo(3734.5678f , 0.001)))
			.body("salary.findAll{it != null}.sum()", allOf(greaterThan(3000d), lessThan(5000d)))
			;
	
	}
	@Test
	public void devoUnirJsonPathComJava(){
		ArrayList<String> nomes =
		given()
		.when()
		.get("http://restapi.wcaquino.me/users")
		.then().statusCode(200)
		//.body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}.toArray()", allOf(arrayContaining("MARIA JOAQUINA"),arrayWithSize(1)))
		.extract().path("name.findAll{it.startsWith('Maria')}")
		;
		Assert.assertEquals(1, nomes.size());
		Assert.assertTrue(nomes.get(0).equalsIgnoreCase("MariA joaqUiNa"));
		Assert.assertEquals(nomes.get(0).toUpperCase(), "maria joaquina".toUpperCase());
	}
	
}
