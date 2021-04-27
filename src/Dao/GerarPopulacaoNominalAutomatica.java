/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Factory.ConexaoBancoDados;
import Modelo.GerarPopNominal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static rabbit.TelaInicial.pTOTAL_internos;

/**
 *
 * @author ronal
 */
public class GerarPopulacaoNominalAutomatica {

    ConexaoBancoDados conecta = new ConexaoBancoDados();
    GerarPopNominal objPopNom = new GerarPopNominal();
    //
    String situacaoENT = "ENTRADA NA UNIDADE";
    String situacaoRET = "RETORNO A UNIDADE";
    String situacaoTmp = "SAIDA TEMPORARIA";
    String pPRISAO_DOMICILIAR_COVID = "PRISAO DOMICILIAR";

    public GerarPopNominal incluirPopulacaoNominal(GerarPopNominal objPopNom) {
        conecta.abrirConexao();
        try {
            PreparedStatement pst = conecta.con.prepareStatement("INSERT INTO POPULACAOINTERNOS_CRC (DataPop,IdInternoCrc)VALUES(?,?)");
            pst.setTimestamp(1, new java.sql.Timestamp(objPopNom.getDataPopulcao().getTime()));
            pst.setInt(2, objPopNom.getIdInternoCrc());
            pst.execute();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Não Foi possivel INSERIR os Dados\n\nERRO" + ex);
        }
        conecta.desconecta();
        return objPopNom;
    }

    public List<GerarPopNominal> PESQUISAR_INTERNOS_read() throws Exception {
        pTOTAL_internos = 0;
        conecta.abrirConexao();
        List<GerarPopNominal> LISTA_INTERNOS_populacao = new ArrayList<GerarPopNominal>();
        try {
            conecta.executaSQL("SELECT IdInternoCrc,Cnc, "
                    + "NomeInternoCrc,SituacaoCrc "
                    + "FROM PRONTUARIOSCRC "
                    + "WHERE SituacaoCrc='" + situacaoENT + "' "
                    + "OR SituacaoCrc='" + situacaoRET + "' "
                    + "OR SituacaoCrc='" + situacaoTmp + "' "
                    + "ORDER BY NomeInternoCrc");
            while (conecta.rs.next()) {
                GerarPopNominal pPOPULACAO = new GerarPopNominal();
                pPOPULACAO.setIdInternoCrc(conecta.rs.getInt("IdInternoCrc"));
                pPOPULACAO.setCnc(conecta.rs.getString("Cnc"));
                pPOPULACAO.setNomeInterno(conecta.rs.getString("NomeInternoCrc"));
                LISTA_INTERNOS_populacao.add(pPOPULACAO);
                pTOTAL_internos++;
            }
            return LISTA_INTERNOS_populacao;
        } catch (SQLException ex) {
            Logger.getLogger(GerarPopulacaoNominalAutomatica.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conecta.desconecta();
        }
        return null;
    }

    public GerarPopNominal VERIFICAR_parametro(GerarPopNominal objPopNom) {
        conecta.abrirConexao();
        try {
            conecta.executaSQL("SELECT "
                    + "PopulacaoAutomatica "
                    + "FROM PARAMETROSCRC");
            conecta.rs.first();
            objPopNom.setParametroPop(conecta.rs.getString("PopulacaoAutomatica"));
        } catch (Exception e) {
        }
        conecta.desconecta();
        return objPopNom;
    }
    
    public GerarPopNominal VERIFICAR_FECHAMENTI_sistema(GerarPopNominal objPopNom) {
        conecta.abrirConexao();
        try {
            conecta.executaSQL("SELECT "
                    + "FecharSistema "
                    + "FROM PARAMETROSCRC");
            conecta.rs.first();
            objPopNom.setParametroFecha(conecta.rs.getString("FecharSistema"));
        } catch (Exception e) {
        }
        conecta.desconecta();
        return objPopNom;
    }
}
