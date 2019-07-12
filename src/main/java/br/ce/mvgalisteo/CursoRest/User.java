package br.ce.mvgalisteo.CursoRest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

//deveSalvarUsuarioXMLUsandoObjeto() em verbosRest
@XmlRootElement(name= "user")
@XmlAccessorType(XmlAccessType.FIELD)

public class User {
	private String name;
	private int age;
	private Double salary;
	
	public User(){
		//Necessário para salvar um usuario XML usando um Objeto
	}
	
	public User(String name, int age) {
		super();
		this.name = name;
		this.age = age;
		this.salary = salary;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Double getSalary() {
		return salary;
	}
	public void setSalary(Double salary) {
		this.salary = salary;
	}
	
	
}
