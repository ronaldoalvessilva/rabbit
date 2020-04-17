/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Factory.ConexaoBancoDados;
import Modelo.AtividadesMensalRealizadaUnidades;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Socializa TI 02
 */
public class ConversaoDatasAtividadesUnidades {

    ConexaoBancoDados conecta = new ConexaoBancoDados();
    AtividadesMensalRealizadaUnidades objAtividade = new AtividadesMensalRealizadaUnidades();

    //ATIVIDADE REALIZADAS NA UNIDADE 
    public AtividadesMensalRealizadaUnidades alterarDataEntradasSaidasUnidades(AtividadesMensalRealizadaUnidades objAtividade) {

        conecta.abrirConexao();
        try {
            PreparedStatement pst = conecta.con.prepareStatement("UPDATE ENTRADAS_SAIDAS_POPULACAO_INTERNOS SET DataMovimento=CONVERT(DATE, DataMovimento,103)");
            pst.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Não Foi possivel ALTERAR (ENTRADAS_SAIDAS_POPULACAO_INTERNOS) os Dados.\nERRO: " + ex);
        }
        conecta.desconecta();
        return objAtividade;
    }

    //POPULAÇÃO
    public AtividadesMensalRealizadaUnidades alterarDataPopulacao(AtividadesMensalRealizadaUnidades objAtividade) {

        conecta.abrirConexao();
        try {
            PreparedStatement pst = conecta.con.prepareStatement("UPDATE MOVPOPULACAO SET DataPopMov=CONVERT(DATE, DataPopMov,103)");
            pst.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Não Foi possivel ALTERAR (MOVPOPULACAO) os Dados.\nERRO: " + ex);
        }
        conecta.desconecta();
        return objAtividade;
    }

    //REGISTRO ATENDIMENTO PSP 
    public AtividadesMensalRealizadaUnidades alterarDataRegAtendimentoPSP(AtividadesMensalRealizadaUnidades objAtividade) {

        conecta.abrirConexao();
        try {
            PreparedStatement pst = conecta.con.prepareStatement("UPDATE REGISTRO_ATENDIMENTO_INTERNO_PSP SET DataAtendimento=CONVERT(DATE, DataAtendimento,103)");
            pst.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Não Foi possivel ALTERAR (REGISTRO_ATENDIMENTO_INTERNO_PSP) os Dados.\nERRO: " + ex);
        }
        conecta.desconecta();
        return objAtividade;
    }
    
    //MATRICULA ESCOLAR 
    public AtividadesMensalRealizadaUnidades alterarDataMatriculaEscolar(AtividadesMensalRealizadaUnidades objAtividade) {

        conecta.abrirConexao();
        try {
            PreparedStatement pst = conecta.con.prepareStatement("UPDATE MATRICULAESCOLAR SET DataMat=CONVERT(DATE, DataMat,103)");
            pst.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Não Foi possivel ALTERAR (MATRICULAESCOLAR) os Dados.\nERRO: " + ex);
        }
        conecta.desconecta();
        return objAtividade;
    }
    
    // ITENS DO KIT DE HIGIÊNE DO INTERNO 
    public AtividadesMensalRealizadaUnidades alterarDataItensKit(AtividadesMensalRealizadaUnidades objAtividade) {

        conecta.abrirConexao();
        try {
            PreparedStatement pst = conecta.con.prepareStatement("UPDATE ITENS_PAGAMENTO_KIT_INTERNOS_PRODUTOS SET DataEntrega=CONVERT(DATE, DataEntrega,103)");
            pst.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Não Foi possivel ALTERAR (ITENS_PAGAMENTO_KIT_INTERNOS_PRODUTOS) os Dados.\nERRO: " + ex);
        }
        conecta.desconecta();
        return objAtividade;
    }
    
    //PROCEDIMENTOS REVISTA .
    public AtividadesMensalRealizadaUnidades alterarDataProcedimentoREV(AtividadesMensalRealizadaUnidades objAtividade) {

        conecta.abrirConexao();
        try {
            PreparedStatement pst = conecta.con.prepareStatement("UPDATE PROCEDIMENTOS SET DataLanc=CONVERT(DATE, DataLanc,103)");
            pst.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Não Foi possivel ALTERAR (PROCEDIMENTOS) os Dados.\nERRO: " + ex);
        }
        conecta.desconecta();
        return objAtividade;
    }
    
    //PROCEDIMENTOS JURÍDICOS
    public AtividadesMensalRealizadaUnidades alterarDataProcedimentoJURI(AtividadesMensalRealizadaUnidades objAtividade) {

        conecta.abrirConexao();
        try {
            PreparedStatement pst = conecta.con.prepareStatement("UPDATE ATENDIMENTOFAMILIARJURIDICO SET DataAtendf=CONVERT(DATE, DataAtendf,103)");
            pst.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Não Foi possivel ALTERAR (ATENDIMENTOFAMILIARJURIDICO) os Dados.\nERRO: " + ex);
        }
        conecta.desconecta();
        return objAtividade;
    }
    
    //SAIDA DE INTERNOS 
    public AtividadesMensalRealizadaUnidades alterarDataSaidaAlvara(AtividadesMensalRealizadaUnidades objAtividade) {

        conecta.abrirConexao();
        try {
            PreparedStatement pst = conecta.con.prepareStatement("UPDATE ITENSSAIDA SET DataSaida=CONVERT(DATE, DataSaida,103)");
            pst.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Não Foi possivel ALTERAR (ITENSSAIDA) os Dados.\nERRO: " + ex);
        }
        conecta.desconecta();
        return objAtividade;
    }
    
}
