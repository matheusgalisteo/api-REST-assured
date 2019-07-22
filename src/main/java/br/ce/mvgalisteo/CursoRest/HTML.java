package br.ce.mvgalisteo.CursoRest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.hamcrest.Matchers;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class HTML {
	@Test
	public void deveFazerBuscasComHtml(){
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/v2/users")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.HTML)
			.body("html.body.div.table.tbody.tr.size()", is(3))
			.body("html.body.div.table.tbody.tr[1].td[2]", is("25"))
			.appendRootPath("html.body.div.table.tbody")
			.body("tr.find{it.toString().startsWith('2')}.td[1]", is("Maria Joaquina"))//com base no appendRootPath procuro o elemento da tabela que é 2, ou seja, segundo item, e verifica o valor da segunda coluna
			;
	}
	@Test
	public void deveFazerBuscasComXpathEmHtml(){
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/v2/users?format=clean")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.HTML)
			.body(hasXPath("count(//table/tr)", is("4")))//contou o cabeçalho tbm
			.body(hasXPath("//td[text()='2']/../td[2]", is("Maria Joaquina")))
			// //td[text()='2']/../td[2] procurando XPath nos td vou para o segundo após isso subo um nível para procurar dentro deste nível o segundo elemento que é Maria Joaquina
			;
	}

}
