/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.sirec.ejb.servicios;

import ec.sirec.ejb.entidades.Cementerio;
import ec.sirec.ejb.facade.CementerioFacade;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author Darwin
 */
@Stateless
@LocalBean
public class CementerioServicio {

    @EJB
    private CementerioFacade cementerioDao;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private String ENTIDAD_CEMENTERIO = "Cementerio";

    public List<Cementerio> listarCementerio() throws Exception {
        return cementerioDao.listarTodos();
    }

    public String crearCementerio(Cementerio vCementerio) throws Exception {
        cementerioDao.crear(vCementerio);
        return "Se ha creado el permiso" + vCementerio.getCemCodigo();
    }

    public String editarCementerio(Cementerio vCementerio) throws Exception {
        cementerioDao.editar(vCementerio);
        return "Se ha modificado el permiso" + vCementerio.getCemCodigo();
    }

    public Cementerio buscarPorParroquiaNumNicho(int codParroquia, String numNicho) throws Exception {
        return cementerioDao.buscarPor2Campos(ENTIDAD_CEMENTERIO, "catdetParroquia.catdetCodigo", codParroquia, "cemNumNicho", numNicho);
    }
     public List<Cementerio> listarOccisoInhumado() throws Exception {
        return cementerioDao.listarPorCampoOrdenada(ENTIDAD_CEMENTERIO, "cemEstado", "I", "cemCodigo", "asc");
    }
      public List<Cementerio> listarOccisoExhumado() throws Exception {
        return cementerioDao.listarPorCampoOrdenada(ENTIDAD_CEMENTERIO, "cemEstado", "E", "cemCodigo", "asc");
    }
}
