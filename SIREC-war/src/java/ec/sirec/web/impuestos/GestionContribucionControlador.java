/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.sirec.web.impuestos;

import ec.sirec.ejb.clases.EjecutarValoracion;
import ec.sirec.ejb.entidades.AdicionalesDeductivos;
import ec.sirec.ejb.entidades.CatalogoDetalle;
import ec.sirec.ejb.entidades.CatastroPredial;
import ec.sirec.ejb.entidades.CatastroPredialAlcabalaValoracion;
import ec.sirec.ejb.entidades.CatastroPredialPlusvaliaValoracion;
import ec.sirec.ejb.entidades.CatastroPredialValoracion;
import ec.sirec.ejb.entidades.Constructora;
import ec.sirec.ejb.entidades.CpAlcabalaValoracionExtras;
import ec.sirec.ejb.entidades.ObraProyecto;
import ec.sirec.ejb.entidades.PredioArchivo;
import ec.sirec.ejb.entidades.Propietario;
import ec.sirec.ejb.entidades.PropietarioPredio;
import ec.sirec.ejb.entidades.RecaudacionCab;
import ec.sirec.ejb.entidades.RecaudacionDet;
import ec.sirec.ejb.entidades.SegUsuario;
import ec.sirec.ejb.servicios.AdicionalesDeductivosServicio;
import ec.sirec.ejb.servicios.CatalogoDetalleServicio;
import ec.sirec.ejb.servicios.CatastroPredialAlcabalaValoracionServicio;
import ec.sirec.ejb.servicios.CatastroPredialPlusvaliaValoracionServicio;
import ec.sirec.ejb.servicios.CatastroPredialServicio;
import ec.sirec.ejb.servicios.CatastroPredialValoracionServicio;
import ec.sirec.ejb.servicios.ConstructoraServicio;
import ec.sirec.ejb.servicios.CpAlcabalaValoracionExtrasServicio;
import ec.sirec.ejb.servicios.CpValoracionExtrasServicio;
import ec.sirec.ejb.servicios.DatoGlobalServicio;
import ec.sirec.ejb.servicios.PredioArchivoServicio;
import ec.sirec.ejb.servicios.RecaudacionCabServicio;
import ec.sirec.ejb.servicios.RecaudacionDetServicio;
import ec.sirec.web.base.BaseControlador;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author vespinoza
 */
@ManagedBean
@ViewScoped
public class GestionContribucionControlador extends BaseControlador {

    /**
     * Creates a new instance of GestionConceptoControlador
     */
    //LOGGER 
    private static final Logger LOGGER = Logger.getLogger(GestionContribucionControlador.class.getName());
    // VARIABLES Y ATRIBUTOS

    private SegUsuario usuarioActual;
    private CatastroPredial catastroPredialActual;
    private CatastroPredialAlcabalaValoracion catastroPredialAlcabalaValoracion;
    private CatalogoDetalle catalogoDetalleConcepto;

    private List<CatastroPredial> listaCatastroPredial;
    private List<CatalogoDetalle> listaCatalogoDetalleConcepto;
    private CatastroPredialValoracion catastroPredialValoracionActual;

    private List<AdicionalesDeductivos> listaAdicionalesDeductivosDeducciones;
    private List<String> listaAdicionalesDeductivosDeduccionesSeleccion;
    private List<AdicionalesDeductivos> listaAdicionalesDeductivosExcenciones;
    private List<String> listaAdicionalesDeductivosExcencionesSeleccion;

    private AdicionalesDeductivos adicionalesDeductivosActual;
    private CpAlcabalaValoracionExtras cpAlcabalaValoracionExtrasActual;
    private StreamedContent archivo;
    private Propietario propietario;
    private List<PredioArchivo> listaAlcabalasArchivo;
    private PredioArchivo predioArchivo;
    private PropietarioPredio propietarioPredioBusqueda;
    private int anio;
    
    /// ATRIBUTOS PLUSVALIA
    
    private List<CatalogoDetalle> listaTipoDeTarifa;
    private CatastroPredialPlusvaliaValoracion catastroPredialPlusvaliaValoracion;
    
    ////// EMISIÓN DE ALCABALAS y PLUSVALIAS  ////////////
    
    private List<CatastroPredial> listaCatastroPredialTablaValoracion;           
    private List<EjecutarValoracion> listaEjecutarValoracion; 
    private List<EjecutarValoracion> listaCatastroPredialTablaValoracionSeleccion;
    private List<CatalogoDetalle> listAnios;
    private CatalogoDetalle catDetAnio;
    private RecaudacionCab recaudacioCab;
    private RecaudacionDet recaudacionDet;
    private EjecutarValoracion ejecutarValoracionSeleccion; 
    
    @EJB
    private RecaudacionCabServicio recaudacionCabServicio;
    @EJB
    private RecaudacionDetServicio recaudacionDetServicio;
    
    
    // SERVICIOS ALCABALA
    @EJB
    private CatastroPredialServicio catastroPredialServicio;
    @EJB
    private CatalogoDetalleServicio catalogoDetalleServicio;
    @EJB
    private CatastroPredialValoracionServicio catastroPredialValoracionServicio;
    @EJB
    private CatastroPredialAlcabalaValoracionServicio catastroPredialAlcabalaValoracionServicio;
    @EJB
    private CpAlcabalaValoracionExtrasServicio cpAlcabalaValoracionExtrasServicio;
    @EJB
    private AdicionalesDeductivosServicio adicionalesDeductivosServicio;
    @EJB
    private PredioArchivoServicio predioArchivoServicio;
    @EJB
    private DatoGlobalServicio datoGlobalServicio;
    
    /// SERVICIOS PLUSVALIA
    
    @EJB
    private CatastroPredialPlusvaliaValoracionServicio catastroPredialPlusvaliaValoracionServicio;
    
    
    
    /// contratista
    
    private ObraProyecto  obraProyectoActual;
    private List<CatalogoDetalle> listaParroquias;
    private List<CatalogoDetalle> listaEstado;
    private List<CatalogoDetalle> listaEjecucion;
    private List<Constructora> listaConstructora;
    private String codEje;
    private String etiquedaEje;
    
    @EJB
    private ConstructoraServicio constructoraServicio;
    

    @PostConstruct
    public void inicializar() {
        try {
                        
            obraProyectoActual = new ObraProyecto();
            listaParroquias = new ArrayList<CatalogoDetalle>();
            listaEstado = new ArrayList<CatalogoDetalle>();
            listaEjecucion = new ArrayList<CatalogoDetalle>();
            listaConstructora = new ArrayList<Constructora>();
            codEje = "";
            etiquedaEje ="";
             
            listarParroquias();
            listarEstados();
            listarEjecucion();
            
//            catastroPredialAlcabalaValoracion = new CatastroPredialAlcabalaValoracion();
//            catastroPredialActual = new CatastroPredial();
//            catalogoDetalleConcepto = new CatalogoDetalle();
//            listaAlcabalasArchivo = new ArrayList<PredioArchivo>();
//            predioArchivo = new PredioArchivo();
//            anio = 0;
//            //listarCatastroPredial();
//            obtenerUsuario();
//            listarConceptos();
//            listarCatalogosDetalle();
            
            
            // INICIALIZAR PLUSVALIA
            catastroPredialPlusvaliaValoracion = new CatastroPredialPlusvaliaValoracion();
            
           // listarTipoTarifa();
            
            // EMISION ALCABALA PLUSVALIA          
            
            ejecutarValoracionSeleccion = new EjecutarValoracion(); 
            listAnios = new  ArrayList<CatalogoDetalle>();
            catDetAnio= new CatalogoDetalle();
            listaCatastroPredialTablaValoracionSeleccion = new ArrayList<EjecutarValoracion>();
            //listarAnios();
            
            //ejecutarValoracion();

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void obtenerUsuario() {
        usuarioActual = new SegUsuario();
        usuarioActual = (SegUsuario) getSession().getAttribute("usuario");
        //System.out.println(usuarioActual.getUsuIdentificacion());         
    }

    public GestionContribucionControlador() {
    }
    
    public void listarParroquias() {
        try {
            listaParroquias = catastroPredialServicio.listaCatParroquias();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void listarEstados() {
        try {
            listaEstado = catastroPredialServicio.listaCatEstadoObr();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
    public void listarEjecucion() {
        try {
            listaEjecucion = catastroPredialServicio.listaCatEjecicion();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
    public void listarContratistas() {
        
        try {
           codEje = catastroPredialServicio.cargarObjetoCatalogoDetalle(obraProyectoActual.getCatdetEjecucion().getCatdetCodigo()).getCatdetCod();
           if(codEje.equals("E")){
               etiquedaEje="Empresa pública:";             
           }else{
               if(codEje.equals("C")){
                    etiquedaEje="Contratista:"; 
               }
           }
                      
           listaConstructora = constructoraServicio.listarConstructoraXTipo(codEje);
           
            System.out.println("tama "+ listaConstructora.size());
           
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
    public ObraProyecto getObraProyectoActual() {
        return obraProyectoActual;
    }

    public void setObraProyectoActual(ObraProyecto obraProyectoActual) {
        this.obraProyectoActual = obraProyectoActual;
    }
    
 public List<CatalogoDetalle> getListaParroquias() {
        return listaParroquias;
    }

    public void setListaParroquias(List<CatalogoDetalle> listaParroquias) {
        this.listaParroquias = listaParroquias;
    }

    public List<CatalogoDetalle> getListaEstado() {
        return listaEstado;
    }

    public void setListaEstado(List<CatalogoDetalle> listaEstado) {
        this.listaEstado = listaEstado;
    }

    public List<CatalogoDetalle> getListaEjecucion() {
        return listaEjecucion;
    }

    public void setListaEjecucion(List<CatalogoDetalle> listaEjecucion) {
        this.listaEjecucion = listaEjecucion;
    }

    public List<Constructora> getListaConstructora() {
        return listaConstructora;
    }

    public void setListaConstructora(List<Constructora> listaConstructora) {
        this.listaConstructora = listaConstructora;
    }

    public String getCodEje() {
        return codEje;
    }

    public void setCodEje(String codEje) {
        this.codEje = codEje;
    }

    public String getEtiquedaEje() {
        return etiquedaEje;
    }

    public void setEtiquedaEje(String etiquedaEje) {
        this.etiquedaEje = etiquedaEje;
    }
    
    
    
}