/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.sirec.ejb.servicios;

import ec.sirec.ejb.entidades.Constructora;
import ec.sirec.ejb.facade.ConstructoraFacade;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author vespinoza
 */
@Stateless
@LocalBean
public class ConstructoraServicio {

  
    @EJB
    private ConstructoraFacade constructoraDao;
    private final String ENTIDAD_CONSTRUCTORA = "Constructora";

//    @EJB
//    private PropietarioPredioFacade propietarioPredioDao;

    @EJB
    private CatalogoDetalleServicio catalogoDetServicio;

    public void guardarConstructora(Constructora constructora) throws Exception {
        constructoraDao.crear(constructora);
    }
    public void editarConstructora(Constructora constructora) throws Exception {
        constructoraDao.editar(constructora);
    }
    
    public void eliminarConstructora(Constructora constructora) throws Exception {
        constructoraDao.eliminar(constructora);
    }
}
