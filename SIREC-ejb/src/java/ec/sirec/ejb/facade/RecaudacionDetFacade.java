/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.sirec.ejb.facade;

import ec.sirec.ejb.entidades.RecaudacionDet;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author vespinoza
 */
@Stateless
public class RecaudacionDetFacade extends AbstractFacade<RecaudacionDet> {

    //LOGGER 
    private static final Logger LOGGER = Logger.getLogger(RecaudacionDetFacade.class.getName());
    @PersistenceContext(unitName = "SIREC-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RecaudacionDetFacade() {
        super(RecaudacionDet.class);
    }

    public List<RecaudacionDet> listaDetallesARecaudarPorCiAnio(String vci, Integer vAnio) throws Exception {
        List<Object[]> resultado = new ArrayList<Object[]>();
        List<RecaudacionDet> lstDets = new ArrayList<RecaudacionDet>();
        try {
            String sql1 = "select cxc_tipo, cxc_referencia,cxc_valor_total, cxc_cod_ref,* from sirec.cuenta_por_cobrar \n" +
                        " where pro_ci='"+vci+"' ";
            if(vAnio!=null){
                sql1=sql1+" and cxc_anio="+vAnio;
            }
            Query q = getEntityManager().createNativeQuery(sql1);
            System.out.println(sql1);
            resultado = q.getResultList();
            if(!resultado.isEmpty()){
                for(Object[] obj:resultado){
                    RecaudacionDet det=new RecaudacionDet();
                    det.setRecdetTipo(obj[0].toString());
                    det.setRecdetReferencia(obj[1].toString());
                    det.setRecdetValor((BigDecimal)obj[2]);
                    det.setRecdetCodref((Integer)obj[3]);
                    lstDets.add(det);
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "error en generacion de detalles", ex);
        }
        return lstDets;
    }
    
    public void actualizarCuentasPorCobrar(List<RecaudacionDet> lstDets) throws Exception{
        try{
            if(!lstDets.isEmpty()){
                for(RecaudacionDet det: lstDets){
                        String sql="update sirec.cuenta_por_cobrar set cxc_saldo=0, cxc_estado='R'  where cxc_tipo=:tipo and cxc_cod_ref="+det.getRecdetCodref();
                        Query q = getEntityManager().createNativeQuery(sql);
                        q.setParameter("tipo", det.getRecdetTipo());
                        q.executeUpdate();
                    
                }
            }
        }catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "error en actualizacion", ex);
        }
    }

}
