package br.ce.mvgalisteo.CursoRest;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.internal.ValidatableResponseImpl;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");
        System.out.println(response.getBody().asString().equals("Ola Mundo!"));
        System.out.println(response.statusCode() == 200);
        
        ValidatableResponse validacao = response.then();
        validacao.statusCode(200);
    }
}
