package Mapeo;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author Emilio
 */
@Entity
@Table(name="usuario")
public class Usuario{
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    	@Column(name = "usuario_id", nullable=false)
    	private int usuario_id;
	
	@Column(name = "nombre", nullable=false)
	private String nombre;
	
	@Column(name = "foto")
	private String foto;//dirección de la foto en el servidor
	
	@Column(name = "correo", nullable=false)
	private String correo;
	
	@Column(name = "psswd", nullable=false)
	private String psswd;
	
	@OneToMany(mappedBy = "calificacion_id")
        @LazyCollection(LazyCollectionOption.FALSE)
	private List<Calificacion> calificaciones_usuario;
	
	@OneToMany(mappedBy = "comentario_id")
        @LazyCollection(LazyCollectionOption.FALSE)
	private List<Comentario> comentarios_usuario;
        	
	public int getUsuario_id(){
		return usuario_id;
	}
	
	public void setUsuario_id(int usuario_id){
		this.usuario_id = usuario_id;
	}
	
	public String getNombre(){
		return nombre;
	}
	
	public void setNombre(String nombre){
		this.nombre = nombre;
	}
	
	public String getFoto(){
		return foto;
	}
	
	public void setFoto(String foto){
		this.foto = foto;
	}
	
	public String getCorreo(){
		return correo;
	}
	
	public void setCorreo(String correo){
		this.correo = correo;
	}
	
	public String getPsswd(){
		return psswd;
	}
	
	public void setPsswd(String nuevaPassword){
		this.psswd = nuevaPassword;
	}
	
	public List<Calificacion> getCalificaciones_usuario(){
		return calificaciones_usuario;
	}
	
	public void setCalificaciones_usuario(List<Calificacion> calificaciones_usuario){
		this.calificaciones_usuario = calificaciones_usuario;
	}
	
	public List<Comentario> getComentarios_usuario(){
		return comentarios_usuario;
	}
	
	public void setComentarios_usuario(List<Comentario> comentarios_usuario){
		this.comentarios_usuario = comentarios_usuario;
	}
	
}
