package Mapeo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

/**
*@author Emilio
*/
@Entity
@Table(name="calificacion")
public class Calificacion{
    
        @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    	@Column(name = "calificacion_id")
    	private int calificacion_id;
	
	@ManyToOne	
	@JoinColumn(name = "usuario_id")
	private Usuario u;
	
	@ManyToOne	
	@JoinColumn(name = "puesto_id")
	private Puesto p;
	
	@Column(name = "estrellas")
	private int estrellas;
	
	public Usuario getUsuario(){
		return u;
	}
	
	public void setUsuario(Usuario u){
		this.u = u;
	}
	
	public Puesto getPuesto(){
		return p;
	}
	
	public void setPuesto(Puesto p){
		this.p = p;
	}
	
	public int getEstrellas(){
		return estrellas;
	}
	
	public void setEstrellas(int estrellas){
		this.estrellas = estrellas;
	}
	
}
