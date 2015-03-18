package com.documentos.servicios;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.config.ConfigPersistenceUnit;
import com.documentos.entidades.DetalleImpuestosentidades;
import com.documentos.entidades.FacCabDocumento;
import com.documentos.entidades.FacDetAdicional;
import com.documentos.entidades.FacEmpresa;
import com.documentos.entidades.FacDetMotivosdebito;
import com.general.entidades.FacCliente;
import com.general.entidades.FacEstablecimiento;
import com.general.entidades.FacGeneral;
import com.general.entidades.FacPuntoEmision;
import com.general.entidades.FacTiposDocumento;
import com.usuario.entidades.FacUsuariosEstablecimiento;

@Stateless
public class ComprobanteNotaDebitoServicios {
	@PersistenceContext (unitName=ConfigPersistenceUnit.persistenceUnit)  
	private EntityManager em;
	
	//TODO contructor que busca la dentificacion del cliente
	@SuppressWarnings("unchecked")
	public  List<String> BuscarfitroEmpresaDocumentos(String parametro_ruc, String ruc_emppresa, String TipoComprador) throws Exception{
		
		try{
			Query q = em.createQuery("SELECT E.id.rucCliente " +
					"	FROM FacCliente E " +
					"	where E.id.rucCliente like :ruc_comprador " +
					"	and E.id.ruc = :ruc_emppresa and E.tipoIdentificacion = :TipoComprador and E.tipoCliente not in('T')");
			q.setParameter("ruc_comprador", "%" + parametro_ruc);
			q.setParameter("ruc_emppresa", ruc_emppresa);
			q.setParameter("TipoComprador", TipoComprador);
			q.setFirstResult(0);
			q.setMaxResults(10);
			return q.getResultList();
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al buscar registro");
		}
	}
	
	//TODO contructor que se encarga del impuesto que se ingreso al motivo de debito
	@SuppressWarnings("unchecked")
	public List<DetalleImpuestosentidades> detalleFiltroimpuesto(String secuencial, String CodidoDocumento)throws Exception{
		List<DetalleImpuestosentidades> Listmp = new ArrayList<DetalleImpuestosentidades>();
		
		try{
			Query q =  em.createQuery("select codImpuesto || ',' || codPorcentaje || ',' || baseImponible || ',' || tarifa || ',' || sum(valor) || ',' || Case codImpuesto when 2 THEN 'IVA' Else 'ICE' end" +
					" from FacDetMotivosdebito " +
					" where id.secuencial = :secuecial_ and id.codigoDocumento = :coddocumento" +
					" group by id.codImpuesto , codPorcentaje,tarifa") ;
			q.setParameter("secuecial_", secuencial);
			q.setParameter("coddocumento", CodidoDocumento);
			List<String> datosdeDetalle = q.getResultList();
			for (String string : datosdeDetalle) {
				String [] datossplit = string.split(",");
				Listmp.add(new DetalleImpuestosentidades(Integer.parseInt(datossplit[0].trim()), 
						Integer.parseInt(datossplit[1].trim()), Double.parseDouble(datossplit[2].trim()), 
						Double.parseDouble(datossplit[3].trim()), Double.parseDouble(datossplit[4].trim()), 
						datossplit[5].trim()));
			}
			return Listmp;
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al Buscar el registro");
		}	
	}
	
	//TODO contructor que guarda el detalle 
	public int insertarDetalleAdicional(FacDetAdicional detAdicional){
		try {
		    	em.persist(detAdicional);
			    return 0;
	  	} catch (Exception e) {
	  		e.printStackTrace();
	  		return -1;
		}
	 }	
	
	//TODO contructor que se encarga de guardar el detalle del documento
	public int insertarDetalleMotivoDebito(FacDetMotivosdebito detDocumento){
		try {
		    	em.persist(detDocumento);
			    return 0;
	  	} catch (Exception e) {
	  		e.printStackTrace();
	  		return -1;
		}
	}
	
	//TODO contructor que se encarga de guardar el cabecera del documento
	public int insertarCabeceraDocumento(FacCabDocumento cabDocumento){
		try {
		    	em.persist(cabDocumento);
			    return 0;
	  	} catch (Exception e) {
	  		e.printStackTrace();
	  		return -1;
		}
	}
	
	//TODO contructor que me busca el ultimo secuencial del ingreso del documento
	public int secuencialCabecera(String rucEmpresa, String establecimiento, String puntoEmision, String cod_documento) throws Exception{		
		Query q;
		java.lang.Object secu;
		int secuencia = 0;
		try{	
			q= em.createQuery("SELECT max(c.id.secuencial)  FROM FacCabDocumento c where c.id.ruc = :rucEmpresa and c.id.codEstablecimiento = :establecimiento and" +
					" c.id.codPuntEmision = :puntoEmision and  c.id.codigoDocumento = :cod_documento ");
			q.setParameter("rucEmpresa", rucEmpresa);
			q.setParameter("establecimiento", establecimiento);
			q.setParameter("puntoEmision", puntoEmision);
			q.setParameter("cod_documento", cod_documento);
			 secu =   q.getSingleResult();
			 secuencia = Integer.parseInt(String.valueOf((secu != null) ? secu : 7000));
			 secuencia = secuencia + 1 + 90;	 
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al Buscar el registro");
		}
		return secuencia;
	}
	
	//TODO contructor que me busca los datos de la empresa para la informacion tributaria
	public FacEmpresa buscarDatosPorRuc(String Ruc) throws Exception{
		try{
			Query q =  em.createQuery("SELECT E FROM FacEmpresa E where E.ruc= :ruc");
			q.setParameter("ruc", Ruc);
			FacEmpresa e = (FacEmpresa)q.getSingleResult();
			return e;
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al Buscar el registro");
		}
	}
	
	//TODO contructor que busca al comprador
	public FacCliente buscarComprador(String comprador) throws Exception{
		FacCliente clienteObj;
		try{
			Query q =  em.createQuery("SELECT P FROM FacCliente P where id.rucCliente = :comprador") ;
			q.setParameter("comprador",comprador);
			clienteObj = (FacCliente) q.getSingleResult();  					
			return clienteObj;
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al Buscar el registro");
		}
	}
	
	//TODO contructor que me busca los punto de emision de la empresa logoneada
	@SuppressWarnings("unchecked")
	public List<FacPuntoEmision> buscarDatosPuntoEmision(String ruc,String codEstablecimiento,String tipoDocumento)throws Exception{
	    try{
			Query q =  em.createQuery("SELECT p FROM FacPuntoEmision p where p.id.ruc= :ruc and p.id.codEstablecimiento= :codEstablecimiento and p.id.tipoDocumento= :tipoDocumento") ;
                       q.setParameter("ruc", ruc);    
                       q.setParameter("codEstablecimiento", codEstablecimiento);
                       q.setParameter("tipoDocumento", tipoDocumento);			
			return q.getResultList();
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al Buscar el registro");
		}	
	}
	
	//TODO contructor que me busca los establecimiento de la empresa logoneada
	@SuppressWarnings("unchecked")
	public List<FacEstablecimiento> buscarDatosEstablecimiento(String ruc ,String usuario)throws Exception{
	    try{
	    	List<FacEstablecimiento> establecimientolista = new ArrayList<FacEstablecimiento>();
	    	List<FacEstablecimiento> establecimiento = new ArrayList<FacEstablecimiento>();
	    	List<FacUsuariosEstablecimiento> usuarioEstablecimiento = new ArrayList<FacUsuariosEstablecimiento>();
	    	 
			Query q =  em.createQuery("SELECT e FROM FacEstablecimiento e where e.id.ruc= :ruc and e.isActive = 'Y'") ;
                       q.setParameter("ruc", ruc);  
            establecimiento = q.getResultList();
            
            q = em.createQuery("SELECT e FROM FacUsuariosEstablecimiento e where e.id.ruc= :ruc and e.isActive = '1' and e.id.codUsuario = :usuario");
            q.setParameter("ruc", ruc); 
            q.setParameter("usuario", usuario);
            usuarioEstablecimiento = q.getResultList();
            
            for (FacEstablecimiento facestablecimiento : establecimiento) {
				for (FacUsuariosEstablecimiento facUsuariosEstablecimiento : usuarioEstablecimiento) {
					if(facUsuariosEstablecimiento.getId().getCodEstablecimiento().trim().equals(facestablecimiento.getId().getCodEstablecimiento().trim())){
						establecimientolista.add(facestablecimiento);
					}
				}
			}
            
			return establecimientolista;
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al Buscar el registro");
		}	
	}
	
	
	//TODO contructor que se encarga de buscar los tipo de tipo de documentos
	@SuppressWarnings("unchecked")
	public List<FacTiposDocumento> buscartipoDocumento(String codigo) throws Exception{
		Query q;
		try{
			q =  em.createQuery("SELECT E FROM FacTiposDocumento E where idDocumento in (" + codigo + ")");		
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al Buscar el registro");
		}
		return q.getResultList();		
	}
	
	//TODO contructor que se encarga de buscar los tipo de emision
	@SuppressWarnings("unchecked")
	public List<FacGeneral> buscarDatosPorCodigo(String codigo) throws Exception{
		Query q;
		try{
			q =  em.createQuery("SELECT E FROM FacGeneral E where codTabla= :codigo") ;
			q.setParameter("codigo",codigo);			
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al Buscar el registro");
		}
		return q.getResultList();		
	}
	
	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}
}
