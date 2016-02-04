/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.sirec.web.impuestos;

import ec.sirec.ejb.entidades.SegUsuario;
import ec.sirec.web.base.BaseControlador;
import ec.sirec.web.util.UtilitariosCod;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;

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
 * @author Darwin Aldas
 */
@ManagedBean
@ViewScoped
public class GestionRepPatenteControlador extends BaseControlador {

    /**
     * Creates a new instance of GestionPatenteControlador
     */
    private static final Logger LOGGER = Logger.getLogger(GestionRepPatenteControlador.class.getName());
    private BigDecimal valorInicial;
    private BigDecimal valorFinal;
    private SegUsuario usuarioActual;
    private Date fechaActual;

    @PostConstruct
    public void inicializar() {
        try {
            fechaActual = new Date();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public GestionRepPatenteControlador() {
    }

    public void cargaObjetosBitacora() {
        try {

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public String reporteNegRangoPatrimonio() throws Exception {
        //Conexion con local datasource
        usuarioActual = new SegUsuario();
        usuarioActual = (SegUsuario) this.getSession().getAttribute("usuario");
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
            parameters.put("valor_inicial", valorInicial);
            parameters.put("valor_final", valorFinal);
            parameters.put("usuario_genera", usuarioActual.getUsuNombres() + " " + usuarioActual.getUsuApellidos());
            parameters.put("fecha_genera", fechaActual);
            parameters.put("logo_gad", servletContext.getRealPath("/imagenes/icons/gadPedroMoncayo.jpg"));
            jasperReport = (JasperReport) JRLoader.loadObject(servletContext.getRealPath("/reportes/patentes/rptNegocioPorRangoPatrimonio.jasper"));
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

    public BigDecimal getValorInicial() {
        return valorInicial;
    }

    public void setValorInicial(BigDecimal valorInicial) {
        this.valorInicial = valorInicial;
    }

    public BigDecimal getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(BigDecimal valorFinal) {
        this.valorFinal = valorFinal;
    }

}
