package com.login.controladores;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import com.documentos.entidades.FacEmpresa;
import com.general.entidades.FacCliente;
import com.general.entidades.FacGeneral;
import com.general.servicios.FacClienteServicios;

@ViewScoped
@ManagedBean
public class FacLoginControlador {
	
	@EJB
	private FacClienteServicios clienteServicio;

	
	private String loginUsuario;
	private String rucCliente;
	
	public String getRucCliente() {
		return rucCliente;
	}

	public void setRucCliente(String rucCliente) {
		this.rucCliente = rucCliente;
	}
	
	public String Ingresar(){
		return ingresar_usuario();
	}
	public String IngresarProveedor(){
		return ingresar_usuarioProveedor();
	}
	
	//TODO metodo que es llamado del boton ingresar para verificar si el usuario existe
	protected String ingresar_usuario(){
		String ReturnPagina = "";
		Boolean ResultadoUsuario = true;
		String Mensaje = "";
		try{
			System.out.println("ingresar_usuario...");
			
			/*if(Integer.parseInt(loginUsuario) < 1 && (ResultadoUsuario == true)){
				Mensaje = "Debe ingresar el Codigo del Cliente";
				ResultadoUsuario= false;
			}*/
			if(loginUsuario!=null)
				if(loginUsuario.equals("")  && (ResultadoUsuario == true)){
					Mensaje = "Debe ingresar el Codigo del Cliente";
					ResultadoUsuario= false;
				}
			
			if(rucCliente.equals("") && (ResultadoUsuario == true)){
				Mensaje = "Debe ingresar la Identificación";
				ResultadoUsuario= false;
			}

			FacCliente datosCliente = null;
			FacEmpresa datosEmpresa = null;
			FacGeneral datosGeneral = null;
			if(ResultadoUsuario == true){
				
				datosCliente = clienteServicio.buscaClienteporCodigo(rucCliente, loginUsuario);
				datosGeneral = clienteServicio.buscarParametroUnicoCodTabla("0","99");
				datosEmpresa = clienteServicio.buscarEmpresa(datosGeneral.getDescripcion());
				Mensaje = "Identificación y Codigo Cliente incorrectos";
			}
			
			System.out.println("  datosCliente: "+datosCliente);
			
			if(datosCliente != null){
				HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);  
		        if (session != null)
		        {
		            session.setAttribute("Cod_Cliente", loginUsuario);
		            session.setAttribute("Ruc_Cliente", rucCliente);
		            session.setAttribute("Nombre_Cliente", datosCliente.getRazonSocial());
		            session.setAttribute("Ruc_Empresa", datosEmpresa.getRuc());
		        }
				ReturnPagina = "/paginas/Consulta_Documentos/Con_Documento2.jsf";
				System.out.println("ReturnPagina..."+ReturnPagina);
			}else
			{
				FacesMessage mensaje= new FacesMessage(FacesMessage.SEVERITY_ERROR,Mensaje, null);
				FacesContext.getCurrentInstance().addMessage("frm1", mensaje);
				ReturnPagina = "";
			}
		}catch (Exception e) {
			System.out.println("Error..."+e);
			e.printStackTrace();
		}
		return ReturnPagina;
	}

	//TODO metodo que es llamado del boton ingresar para verificar si el usuario existe
		protected String ingresar_usuarioProveedor(){
			String ReturnPagina = "/paginas/Administrador/Cuenta/LoginProveedor.jsf";
			Boolean ResultadoUsuario = true;
			String Mensaje = "";
			try{
				System.out.println("ingresar_usuarioProveedor...");
				if(rucCliente.equals("") && (ResultadoUsuario == true)){
					Mensaje = "Debe ingresar la Identificación";
					ResultadoUsuario= false;
				}
				
				FacCliente datosCliente = null;
				FacEmpresa datosEmpresa = null;
				FacGeneral datosGeneral = null;
				System.out.println("ResultadoUsuario..."+ResultadoUsuario);
				if(ResultadoUsuario == true){
					datosGeneral = clienteServicio.buscarParametroUnicoCodTabla("0","99");				
					datosEmpresa = clienteServicio.buscarEmpresa(datosGeneral.getDescripcion());
					datosCliente = clienteServicio.buscaUsuarioProveedor(rucCliente, datosGeneral.getDescripcion());
					Mensaje = "Identificación y Codigo Cliente incorrectos";
				}
				System.out.println("datosCliente..."+datosCliente);
				if(datosCliente != null){
					HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
			        if (session != null)
			        {
			            session.setAttribute("Cod_Cliente", loginUsuario);
			            session.setAttribute("Ruc_Cliente", rucCliente);
			            session.setAttribute("Nombre_Cliente", datosCliente.getRazonSocial());
			            session.setAttribute("Ruc_Empresa", datosEmpresa.getRuc());
			        }
					ReturnPagina = "/paginas/Consulta_Documentos/Con_Documento2.jsf";
					System.out.println("ReturnPagina..."+ReturnPagina);
				}else
				{
					FacesMessage mensaje= new FacesMessage(FacesMessage.SEVERITY_ERROR,Mensaje, null);
					FacesContext.getCurrentInstance().addMessage("frm1", mensaje);
					ReturnPagina = "/paginas/Administrador/Cuenta/LoginProveedor.jsf";
				}
			}catch (Exception e) {
				System.out.println("Error..."+e);
				e.printStackTrace();
			}
			return ReturnPagina;
		}
	
	public String getLoginUsuario() {
		return loginUsuario;
	}

	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}

}
