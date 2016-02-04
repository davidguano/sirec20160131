/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.sirec.web.impuestos;

import ec.sirec.ejb.entidades.CatalogoDetalle;
import ec.sirec.ejb.entidades.SegUsuario;
import ec.sirec.ejb.servicios.CatalogoDetalleServicio;
import ec.sirec.ejb.servicios.CatastroPredialServicio;
import ec.sirec.web.base.BaseControlador;
import ec.sirec.web.util.UtilitariosCod;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author vespinoza
 */
@ManagedBean
@ViewScoped
public class GestionRepAlcabalasEmitidasControlador extends BaseControlador {

    /**
     * Creates a new instance of GestionPatenteControlador
     */
    private static final Logger LOGGER = Logger.getLogger(GestionRepAlcabalasEmitidasControlador.class.getName());
    private SegUsuario usuarioActual;
    private Date fechaActual;

     private List<CatalogoDetalle> listAnios;
     private CatalogoDetalle catDetAnio;
     @EJB
    private CatalogoDetalleServicio catalogoDetalleServicio;
    @EJB
    private CatastroPredialServicio catastroPredialServicio;
    
    @PostConstruct
    public void inicializar() {
        try {
            fechaActual = new Date();
            catDetAnio= new CatalogoDetalle(); 
            listarAnios();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public GestionRepAlcabalasEmitidasControlador() {
    }

    
    public void listarAnios() throws Exception {
        listAnios = catalogoDetalleServicio.listarPorNemonicoCatalogo("ANIOS");
    }
    
    public String reporteAlcabalaEmitidas() throws Exception {
        //Conexion con local datasource
        usuarioActual = new SegUsuario();
        usuarioActual = (SegUsuario) this.getSession().getAttribute("usuario");
        
         catDetAnio = catastroPredialServicio.cargarObjetoCatalogoDetalle(catDetAnio.getCatdetCodigo());
        
        UtilitariosCod util = new UtilitariosCod();
        Connection conexion = util.getConexion();
        byte[] fichero = null;
        JasperReport jasperReport = null;
        Map parameters = new HashMap();
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
            ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
            session.removeAttribute("reporteInforme");
            parameters.put("usuario_genera", usuarioActual.getUsuNombres() + " " + usuarioActual.getUsuApellidos());
            parameters.put("fecha_genera", fechaActual);
            parameters.put("anio", catDetAnio.getCatdetValor());            
            parameters.put("logo_gad", servletContext.getRealPath("/imagenes/icons/gadPedroMoncayo.jpg"));
            //jasperReport = (JasperReport) JRLoader.loadObject(servletContext.getRealPath("/reportes/patentes/rptNegocioPorRangoPatrimonio.jasper"));
            jasperReport = (JasperReport) JRLoader.loadObject(servletContext.getRealPath("/reportes/impuestos/alcabala_emitida.jasper"));
            fichero = JasperRunManager.runReportToPdf(jasperReport, parameters, conexion);
            session.setAttribute("reporteInforme", fichero);
            usuarioActual = new SegUsuario();
        } catch (JRException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        } finally {
            if (conexion != null) {
                conexion.close();
            }
        }
        return null;
    }
    
    
    
     public CatalogoDetalle getCatDetAnio() {
        return catDetAnio;
    }

    public void setCatDetAnio(CatalogoDetalle catDetAnio) {
        this.catDetAnio = catDetAnio;
    }

    public List<CatalogoDetalle> getListAnios() {
        return listAnios;
    }

    public void setListAnios(List<CatalogoDetalle> listAnios) {
        this.listAnios = listAnios;
    }    

}
