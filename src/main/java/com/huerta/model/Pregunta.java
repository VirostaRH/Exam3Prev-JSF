package com.huerta.model;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import redis.clients.jedis.Jedis;

@ManagedBean(name="pregunta")
@SessionScoped
public class Pregunta implements Serializable{

	private Jedis bbdd = new Jedis ("localhost");	

	private String id;
	private String cuerpo;

	public Pregunta() {
		this.id = bbdd.get("valorPreguntaAct");
		this.cuerpo = bbdd.get("cuerpoPreguntaAct");
	}

	//constructor con una entrada para la consulta con retorno de datos.
	public Pregunta(String valorBuscadoID)
	{
		this.id = valorBuscadoID;
		this.cuerpo = bbdd.get(valorBuscadoID);
	}

	//constructor completo para todos los valores 
	public Pregunta(String id, String c)
	{
		this.id = id;
		this.cuerpo = c;
	}

	public String getId()
	{
		return this.id;
	}

	public String getCuerpo()
	{
		return this.cuerpo;
	}

	public void setId(String i)
	{
		this.id=i;
	}
	
	public void setCuerpo(String c)
	{
		this.cuerpo = c;
	}

	public Boolean comprobarExistencia(String s0)
	{
		if (bbdd.sismember("listadoPreguntas", s0))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
