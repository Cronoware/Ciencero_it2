package Mapeo;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.ElementCollection;
import javax.persistence.CollectionTable;
import javax.persistence.JoinColumn;
import javax.persistence.FetchType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import java.io.Serializable;

/**
 *
 * @author Emilio
 */
@Entity
@Table(name="puesto")

public class Puesto implements Serializable{
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    	@Column(name = "puesto_id")
    	int puesto_id;
	
	@Column(name = "nombre")
	private String nombre;
	
	@ElementCollection
	@CollectionTable(name = "categorias", joinColumns = @JoinColumn(name = "puesto_id"))
        @Column(name="tipo")
        @LazyCollection(LazyCollectionOption.FALSE)
	private List<String> tags;
	
	@Column(name = "posX")
	double posX;
	
	@Column(name = "posY")
	double posY;
	
	@OneToMany(mappedBy = "p")
        @LazyCollection(LazyCollectionOption.FALSE)
	private List<Calificacion> calificaciones_ususario;
	
	@OneToMany(mappedBy = "p")
        @LazyCollection(LazyCollectionOption.FALSE)
	private List<Comentario> comentarios_usuario;

    public int getPuesto_id() {
        return puesto_id;
    }

    public void setPuesto_id(int puesto_id) {
        this.puesto_id = puesto_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public List<Calificacion> getCalificaciones_usuario() {
        return calificaciones_ususario;
    }

    public void setCalificaciones_usuario(List<Calificacion> calificaciones_ususario) {
        this.calificaciones_ususario = calificaciones_ususario;
    }

    public List<Comentario> getComentarios_usuario() {
        return comentarios_usuario;
    }

    public void setComentarios_usuario(List<Comentario> comentarios_usuario) {
        this.comentarios_usuario = comentarios_usuario;
    }
	
}
