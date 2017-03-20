package com.huerta.bean;

import java.io.Serializable;
import java.util.*;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedProperty;
import redis.clients.jedis.Jedis;
import com.huerta.model.User;
import com.huerta.model.Pregunta;

@ManagedBean(name="obj")
@SessionScoped
public class ViewManager implements Serializable{
  /*Propiedades del objeto "alumno/usuario" que crearemos desde la vista si hacemos inserción de datos.*/
  private Boolean permitirVer = true;

  private String id;
  private String lastName;
  private String name;

  /*Ppdes del objeto pregunta que crearemos desde la vista al insertar una nueva pregunta*/

  private String idPregunta;
  private String cuerpo;

  /*En caso de que ya exista el objeto que vamos a insetar como alumno, crearemos un segundo, por eso generamos dos usuarios.*/
  @ManagedProperty(value="#{user}")
    private User usuario = new User();
  @ManagedProperty(value="#{usuarioCreado}")
      private User usuarioCreado;
  @ManagedProperty(value="#{pregunta}")
    private Pregunta preguntaActual = new Pregunta();
  @ManagedProperty(value="#{preguntaCreada}")
    private Pregunta preguntaCreada = new Pregunta();

  //Conexión a redis para confirmar si las altas de usuarios y de preguntas son factibles.
  private Jedis bbdd = new Jedis ("localhost");
  private Boolean permitirModificarAlumnos = bbdd.get("permitirModificarAlumnos").equals("Si");
  private Boolean permitirModificarPreguntas = bbdd.get("permitirModificarPreguntas").equals("Si");
  
  //usuario insertado:
  private Boolean usuarioValido = false;
  //pregunta aceptada
  private Boolean preguntaAceptada = false;

  private ArrayList <User> listaR;
  private ArrayList <Pregunta> listaP;

  public ViewManager()
  {
  }

  public Pregunta getPreguntaCreada()
  {
    return this.preguntaCreada;
  }
  
  public String getIdPregunta()
  {
    return this.idPregunta;
  }

  public String getCuerpo()
  {
    return this.cuerpo;
  }

  //getts alumno
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

  public User getUsuario()
  {
    return this.usuario;
  }

  public User getUsuarioCreado()
  {
    return this.usuarioCreado;
  }

  //get pregunta

  public Pregunta getPreguntaActual()
  {
    return this.preguntaActual;
  }

  //getts banderas

  public Boolean getPreguntaAceptada()
  {
	return this.preguntaAceptada;
  }
  public Boolean getPermitirModificarAlumnos()
  {
    return this.permitirModificarAlumnos;
  }
  public Boolean getPermitirModificarPreguntas()
  { 
	return this.permitirModificarPreguntas;
  }
  public Boolean getUsuarioValido()
  {
    return this.usuarioValido;
  }

  //setts usuarios

  public void setUsuarioValido(Boolean valor)
  {
    this.usuarioValido = valor;
  }

  public void setUsuario(User u)
  {
    this.usuario = u;
  }
 
  public void setUsuarioCreado(User u)
  {
    this.usuario = u;
  }
 

  //setts variables internas
  public void setId(String id)
  {
    this.id = id;
  }
  
  public void setName(String n)
  {
    this.name = n;
  }
  
  public void setLastName(String ln)
  {
    this.lastName = ln;
  }

  public void setPreguntaAceptada(Boolean valor)
  {
    this.preguntaAceptada = valor;
  }

  public void setpermitirModificarPreguntas(Boolean valor)
  {
    this.permitirModificarPreguntas = valor;
  }

  public void setIdPregunta(String pregunta)
  {
    this.idPregunta = pregunta;
  }

  public void setCuerpo(String cuerpo)
  {
    this.cuerpo = cuerpo;
  }

  public void setListaR (ArrayList l)
  {
    this.listaR = l;
  }

  public ArrayList getListaR ()
  {
    return this.listaR;
  }

  public void setListaP (ArrayList l)
  {
    this.listaP = l;
  }

  public ArrayList getListaP ()
  {
    return this.listaP;
  }

  public void setPermitirVer(){}
  public Boolean getPermitirVer(){
    return this.permitirVer;
  }

  public String altaUser()
  {
    String idAlta = getId();
    

    if (usuario.comprobarExistencia(idAlta))
    {
      this.usuarioCreado = new User(idAlta);
      this.usuario = new User(getId(), getName(), getLastName(), true);
      setUsuarioValido(false);
    }
    else
    {
      bbdd.sadd("alumnos", getId());
      bbdd.hset(idAlta, "Nombre", getName());
      bbdd.hset(idAlta, "Apellidos", getLastName());
      bbdd.hset(idAlta, "id", getId());

      this.usuario=new User (idAlta ,getName(),getLastName(),true);
      setUsuarioValido(true);
    }
    return "resultadoAlta.xhtml";
  }

  public String altaPregunta()
  {
    String idAlta = "P"+getIdPregunta();
    String cuerpo = getCuerpo();

    if (preguntaCreada.comprobarExistencia(idAlta))
    {
      this.preguntaCreada = new Pregunta(idAlta);
      this.preguntaActual = new Pregunta (idAlta, cuerpo);
      setPreguntaAceptada(false);
    }
    else
    {
      bbdd.sadd("listadoPreguntas", idAlta);
      bbdd.set(idAlta, cuerpo);
      this.preguntaActual = new Pregunta (idAlta, cuerpo);
      setPreguntaAceptada(true);
    }
    return "resultadoAltaPregunta.xhtml";
  }

  public String eliminarUno()
  {
    String idBaja = "P"+getIdPregunta();

    if (preguntaCreada.comprobarExistencia(idBaja))
    {
      this.preguntaActual=new Pregunta (idBaja);
      bbdd.srem("listadoPreguntas", idBaja);
      bbdd.del(idBaja);
      setPreguntaAceptada(true);
    }
    else
    {
      setPreguntaAceptada(false);
    }
    return "resultadoBajaPregunta.xhtml";
  }

  public String eliminarAlumno()
  {
    String idBaja = getId();

    if (usuario.comprobarExistencia(idBaja))
    {
      this.usuario=new User (idBaja);
      bbdd.srem("alumnos", idBaja);
      bbdd.del(idBaja);
      setUsuarioValido(true);
    }
    else
    {
      this.usuario=new User (idBaja);
      setUsuarioValido(false);
    }
    return "resultadoBajaAlumno.xhtml";
  }

  public String modificarUser()
  {
    String idMod = getId();
    

    if (usuario.comprobarExistencia(idMod))
    {
      //si existe, creamos el usuario almacenado para mostrar.
      this.usuarioCreado = new User(idMod);
      //una vez hecho esto, es más cómodo eliminar dicho usuario de redis y añadir el nuevo con los nuevos valores:
      bbdd.del(idMod);
      bbdd.hset(idMod, "Nombre", getName());
      bbdd.hset(idMod, "Apellidos", getLastName());
      bbdd.hset(idMod, "id", getId());

      this.usuario=new User (idMod ,getName(),getLastName(),true);
      setUsuarioValido(true);
    }
    else
    {
      this.usuario=new User (idMod ,getName(),getLastName(),true);
      setUsuarioValido(false);
    }
    return "resultadoModificacionAlumno.xhtml";
  }

  public String modificarPregunta()
  {
    String idMod = "P"+getIdPregunta();
    String cuerpoMod = getCuerpo();

    if (preguntaCreada.comprobarExistencia(idMod))
    {
      this.preguntaCreada = new Pregunta(idMod);
      //eliminamos tb de redis
      bbdd.del(idMod);
      //insertamos de nuevo en redis
      bbdd.set(idMod, cuerpo);
      this.preguntaActual = new Pregunta (idMod, cuerpoMod);
      setPreguntaAceptada(true);
    }
    else
    {
      this.preguntaActual = new Pregunta (idMod, cuerpo);
      setPreguntaAceptada(false);
    }
    return "resultadoAltaPregunta.xhtml";
  }

  public String listarTodos()
  {
    User uTemp;
    ArrayList <User> listado = new ArrayList <User>();
    Set <String> alumnos = bbdd.smembers("alumnos");
    for (String s : alumnos)
    {

      uTemp = new User(s);
      System.out.println(uTemp.getName());
      listado.add(uTemp);
    }
    setListaR(listado);

    return "muestraListado.xhtml";

  }

  public String listarTodas()
  {
    Pregunta pTemp;
    ArrayList <Pregunta> listado = new ArrayList <Pregunta>();
    Set <String> preguntas = bbdd.smembers("listadoPreguntas");
    for (String p : preguntas)
    {
      pTemp = new Pregunta(p);
      System.out.println(pTemp.getId());
      listado.add(pTemp);
    }
    setListaP(listado);

    return "muestraListadoP.xhtml";
  }

  public String accesoCAlumno()
  {
    return "altaAlumno.xhtml";
  }
  public String accesoUAlumno()
  {
    return "actualizarAlumno.xhtml";
  }
  public String accesoDAlumno()
  {
    return "bajaAlumno.xhtml";
  }
  public String accesoListar()
  {
    return "listar.xhtml";
  }
  public String accesoCPregunta()
  {
    return "altaPregunta.xhtml";
  }
  public String accesoUPregunta()
  {
    return "actualizarPregunta.xhtml";
  }
  public String accesoDPregunta()
  {
    return "bajaPregunta.xhtml";
  }

}
