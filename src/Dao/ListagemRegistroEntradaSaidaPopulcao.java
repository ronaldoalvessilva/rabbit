/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

//import gestor.Dao.ConexaoBancoDados;
//import gestor.Modelo.EntradaSaidasPolucaoInternos;
//import static gestor.Visao.TelaEntradaUnidadeInternoPortaria.jIdLanc;
import Factory.ConexaoBancoDados;
import Modelo.EntradaSaidasPolucaoInternos;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author ronal
 */
public class ListagemRegistroEntradaSaidaPopulcao {

    ConexaoBancoDados conecta = new ConexaoBancoDados();
    EntradaSaidasPolucaoInternos objEntradaSaida = new EntradaSaidasPolucaoInternos();

    public EntradaSaidasPolucaoInternos selecionarRegistroEntrada(EntradaSaidasPolucaoInternos objEntradaSaida) {

        conecta.abrirConexao();
        try {
            conecta.executaSQL("SELECT IdDocumento,Populacao, "
                    + "DataMovimento,HorarioMovimento,Quantidade "
                    + "FROM ENTRADAS_SAIDAS_POPULACAO_INTERNOS ");
//                    + "WHERE IdDocumento='" + jIdLanc.getText() + "'");
            conecta.rs.last();
//            objEntradaSaida.setIdDocumento(conecta.rs.getInt("IdDocumento"));
            objEntradaSaida.setDataMovimento(conecta.rs.getDate("DataMovimento"));
            objEntradaSaida.setHorarioMovimento(conecta.rs.getString("HorarioMovimento"));
            objEntradaSaida.setPopulacao(conecta.rs.getInt("Populacao"));
        } catch (SQLException ex) {
          //  JOptionPane.showMessageDialog(null, "Não foi possível selecionar a registro.\n\nERRO: " + ex);
        }
        conecta.desconecta();
        return objEntradaSaida;
    }
}
