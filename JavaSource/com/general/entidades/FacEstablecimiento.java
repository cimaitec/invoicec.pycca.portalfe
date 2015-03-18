package com.general.entidades;

import java.io.Serializable;
import javax.persistence.*;



/**
 * The persistent class for the fac_establecimiento database table.
 * 
 */
@Entity
@Table(name="fac_establecimiento")
public class FacEstablecimiento implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FacEstablecimientoPK id;

	@Column(name="\"Correo\"")
	private String correo;

	@Column(name="\"DireccionEstablecimiento\"")
	private String direccionEstablecimiento;

	@Column(name="\"isActive\"")
	private String isActive;

	@Column(name="\"Mensaje\"")
	private String mensaje;

	@Column(name="\"PathAnexo\"")
	private String pathAnexo;

	//bi-directional many-to-one association to FacPuntoEmision
	/*@OneToMany(mappedBy="facEstablecimiento")
	private List<FacPuntoEmision> facPuntoEmisions;
    */
    public FacEstablecimiento() {
    }

	public FacEstablecimientoPK getId() {
		return this.id;
	}

	public void setId(FacEstablecimientoPK id) {
		this.id = id;
	}
	
	public String getCorreo() {
		return this.correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getDireccionEstablecimiento() {
		return this.direccionEstablecimiento;
	}

	public void setDireccionEstablecimiento(String direccionEstablecimiento) {
		this.direccionEstablecimiento = direccionEstablecimiento;
	}

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getMensaje() {
		return this.mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getPathAnexo() {
		return this.pathAnexo;
	}

	public void setPathAnexo(String pathAnexo) {
		this.pathAnexo = pathAnexo;
	}

/*	public List<FacPuntoEmision> getFacPuntoEmisions() {
		return this.facPuntoEmisions;
	}

	public void setFacPuntoEmisions(List<FacPuntoEmision> facPuntoEmisions) {
		this.facPuntoEmisions = facPuntoEmisions;
	}
*/	
}