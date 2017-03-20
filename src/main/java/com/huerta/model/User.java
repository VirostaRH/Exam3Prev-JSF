package com.huerta.model;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import redis.clients.jedis.Jedis;

@ManagedBean(name="user")
@SessionScoped
public class User implements Serializable{

	private Jedis bbdd = new Jedis ("localhost");	
	/*inserciones futuras en la bd. Id puede ser = a name o no. Depende de qué queramos emplear como identificador,
	/ pero, en un futuro, sería interesante usar dni como identificador o número de alumno, por el tema de "modelo de 		/ "negocio". Como no voy a ampliar a eso en estos momentos, procedo a igualar nombre e id, pero mantengo la duplicidad
	/ de campos por esto último, por si en un futuro deseo ampliarlo como una app similar a la de productos que hice en 
	/ ej. de servlet. 
	*/

	private String id;
	private String name;
	private String lastName;
	private Boolean existe;

	//contructor vacío para el fw.
	public User() {}

	//constructor con una entrada para la consulta con retorno de datos.
	public User(String valorBuscadoID)
	{
		if (comprobarExistencia(valorBuscadoID))
		{
			this.name = bbdd.hget(valorBuscadoID, "Nombre");
			this.lastName = bbdd.hget(valorBuscadoID, "Apellidos");
			this.id = valorBuscadoID;
		}
		else
		{
			this.id = valorBuscadoID;
			this.name = "";
			this.name = "";
		}
	}
	//constructor completo para todos los valores 
	public User(String id, String n, String ln, Boolean val)
	{
		this.id = id;
		this.name = n;
		this.lastName = ln;
		this.existe = true;
	}

	public Boolean isExiste()
	{
		return this.existe;
	}
	public String getId()
	{
		return this.id;
	}
	public String getName()
	{
		return this.name;
	}

	public String getLastName()
	{
		return this.lastName;
	}

	public void setId(String i)
	{
		this.id=i;
	}
	public void setName(String n)
	{
		this.name = n;
	}
	
	public void setLastName(String ln)
	{
		this.lastName = ln;
	}

	public Boolean comprobarExistencia(String s0)
	{
		if (bbdd.sismember("alumnos", s0))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
