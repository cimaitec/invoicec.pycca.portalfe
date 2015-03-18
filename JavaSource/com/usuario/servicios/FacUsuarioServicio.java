package com.usuario.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.config.ConfigPersistenceUnit;
import com.documentos.entidades.FacEmpresa;
import com.general.controladores.FacEncriptarcadenasControlador;
import com.general.entidades.FacCliente;
import com.usuario.entidades.FacRole;
import com.usuario.entidades.FacUsuario;
import com.usuario.entidades.FacUsuariosRole;

@Stateless
public class FacUsuarioServicio {
	@PersistenceContext(unitName = ConfigPersistenceUnit.persistenceUnit)
	private EntityManager em;

	private List<FacUsuario> facUsuario;

	public void insertarUsuario(FacUsuario usuario) throws Exception {
		try {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			em.persist(usuario);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void insertarUsuarioRol(FacUsuariosRole usuarioRol) throws Exception {
		try {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			em.persist(usuarioRol);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void modificarDatos(FacUsuario usuario) throws Exception {
		try {
			try {
				Thread.sleep(1000);
				
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Query q = em.createQuery("update FacUsuario set " +
					" password = :contrasena	," +
					" isActive = :estado " +
					" where id.codUsuario = :usuario and " +
					"		rucEmpresa = :ruc_usuario and" +
					"		id.ruc = :ruc_empresa  " +
					"		");
			q.setParameter("estado", usuario.getIsActive());
			q.setParameter("contrasena", usuario.getPassword());
			
			q.setParameter("usuario", usuario.getId().getCodUsuario());
			q.setParameter("ruc_usuario", usuario.getRucEmpresa());
			q.setParameter("ruc_empresa", usuario.getId().getRuc());
			q.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void modificarDatosRol(FacUsuariosRole usuarioRol) throws Exception {
		try {
			try {
				Thread.sleep(1000);
					} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Query q = em.createQuery("update FacUsuariosRole set id.codRol= :codRol , isActive = :estado where id.codUsuario = :usuario and id.ruc = :ruc_empresa");
			q.setParameter("estado", usuarioRol.getIsActive());
			q.setParameter("codRol", usuarioRol.getId().getCodRol());
			q.setParameter("usuario", usuarioRol.getId().getCodUsuario());
			q.setParameter("ruc_empresa", usuarioRol.getId().getRuc());
			q.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public FacCliente buscarRucCliente(String Ruc) throws Exception {
		FacCliente c = null;
		try {


			Query q = em.createQuery("SELECT C FROM FacCliente C where C.id.rucCliente = :ruc");
			q.setParameter("ruc", Ruc.trim());
		
			
			c = (FacCliente) q.getSingleResult();
			return  c;
		} catch (Exception ex) {
			
			ex.printStackTrace();
			throw new Exception("Error al Buscar el registro");
		}
	}

	public FacEmpresa buscarDatosPorRuc(String Ruc) throws Exception {
		FacEmpresa e = null;
		try {

			Query q = em.createQuery("SELECT E FROM FacEmpresa E where E.ruc= :ruc");
			q.setParameter("ruc", Ruc);
			
		
			e = (FacEmpresa) q.getSingleResult();
			
			
		} catch (Exception ex) {
			
			ex.printStackTrace();
			throw new Exception("Error al Buscar el registro");
		}
		
		return  e;
	}

	@SuppressWarnings("unchecked")
	public List<FacUsuario> buscarTodosLosUsuario() throws Exception {
		try {
			Query q = em.createQuery("SELECT U FROM FacUsuario U ORDER BY U.id.codUsuario");
			

			return q.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al Buscar el registro");
		}
	}

	@SuppressWarnings("unchecked")
	public FacUsuario BuscarCodUsuario(String rucUsua,String rucEmp,String tipoUsuario) throws Exception {
		FacUsuario u = new FacUsuario();
		try {
			Query q = em.createQuery("SELECT U FROM FacUsuario U where U.rucEmpresa = :rucUsuario AND U.id.ruc = :rucEmp AND U.tipoUsuario = :tipoUsua");
			q.setParameter("rucUsuario", rucUsua);
			q.setParameter("rucEmp", rucEmp);
			q.setParameter("tipoUsua", tipoUsuario.trim());
			
			List<FacUsuario> facusuario = q.getResultList();
			if(!facusuario.isEmpty()){
				u= (FacUsuario) q.getSingleResult();
				
			}else{
				u= null;
			}
		} catch (Exception e) {
			u=null;
			e.printStackTrace();
			throw new Exception("Error al Buscar");
		}
		
		return u;
		
	}
	@SuppressWarnings("unchecked")
	public List<FacEmpresa> buscarEmpresa() throws Exception {
		Query q = null;
		try {
			q = em.createQuery("SELECT E FROM FacEmpresa E where E.isActive = 'Y'");
						
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception("Error al Buscar el registro");
		}
		
		return  q.getResultList();
	}
	
	public FacUsuariosRole buscarCodRol(String ruc,String codUsuario) throws Exception{
		FacUsuariosRole usuRol = null;
				try {
					Query q = em.createQuery("SELECT UR FROM FacUsuariosRole UR WHERE UR.id.ruc = :ruc AND UR.id.codUsuario = :codUsuario");
					q.setParameter("ruc", ruc);
					q.setParameter("codUsuario", codUsuario);
					usuRol = new FacUsuariosRole();
					usuRol = (FacUsuariosRole) q.getSingleResult();
				} catch (Exception e) {
					usuRol = null;
					return usuRol;
				}
		return usuRol;
	}

	public FacEmpresa ver_tipo(String tipo) throws Exception {
		try {
			Query q = em.createQuery("SELECT E FROM FacEmpresa E ");

			FacEmpresa e = (FacEmpresa) q.getSingleResult();

			return e;

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al Buscar el registro");
		}
	}

	@SuppressWarnings("unchecked")
	public List<FacUsuario> buscarTodosDatosUsuario()throws Exception{
	    try{
			
	
			Query q =  em.createQuery("SELECT U FROM FacUsuario U ORDER BY U.id.codUsuario ") ;
                          
			     					
			return q.getResultList();
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al Buscar el registro");
		}	
	}

	@SuppressWarnings("unchecked")
	public List<FacRole> buscarTodosRol()throws Exception{
	    try{
			
	
			Query q =  em.createQuery("SELECT R FROM FacRole R ORDER BY R.id.codUsuario") ;
                          
			     					
			return q.getResultList();
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al Buscar el registro");
		}	
	    
	}
	public int generaSecuencial() {
		Query q;
		q = em.createQuery("SELECT count(*)  FROM FacUsuario");
		java.lang.Object valor = q.getSingleResult();
		int e = Integer.parseInt(valor.toString());
		e = e + 1;
		return e;
	}
	
	@SuppressWarnings("unchecked")
	public FacUsuario validarUsuario(FacUsuario usuario){
		try{
		
		Query q = em.createQuery("SELECT E  FROM FacUsuario E where isActive = :Estado and id.ruc = :RUCempresa and id.codUsuario = :usuario ");
		q.setParameter("Estado", "1");
		q.setParameter("RUCempresa", usuario.getId().getRuc());
		q.setParameter("usuario", usuario.getId().getCodUsuario());
		List<FacUsuario> facConsultausuario = q.getResultList();
		for (FacUsuario facUsuario : facConsultausuario) {
			if(FacEncriptarcadenasControlador.decrypt(facUsuario.getPassword()).trim().equals(FacEncriptarcadenasControlador.decrypt(usuario.getPassword()).trim()))
				return facUsuario;
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<FacCliente> buscarClientes(String Ruc,String tipoUsuario){
		Query q =  null;
		try {
			q = em.createQuery("Select C from FacCliente C Where C.id.ruc = :ruc And C.tipoCliente = :tipoUsuario");
			q.setParameter("ruc", Ruc);
			q.setParameter("tipoUsuario", tipoUsuario);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return q.getResultList();
	}

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	public List<FacUsuario> getFacUsuario() {
		return facUsuario;
	}

	public void setFacUsuario(List<FacUsuario> facUsuario) {
		this.facUsuario = facUsuario;
	}

}
