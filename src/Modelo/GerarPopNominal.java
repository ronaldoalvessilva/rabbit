/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.util.Date;

/**
 *
 * @author ronal
 */
public class GerarPopNominal {

    private Date dataLanc;
    private Date dataPopulcao;
    private int IdInternoCrc;
    private String cnc;
    private String nomeInterno;
    private String situacaoEntrada;
    private String situacaoRetorno;
    private String dataExclusaoPop;
    private String parametroPop;
    private String parametroFecha;

    public GerarPopNominal() {
    }

    public GerarPopNominal(Date dataLanc, Date dataPopulcao, int IdInternoCrc, String cnc, String nomeInterno, String situacaoEntrada, String situacaoRetorno, String dataExclusaoPop, String parametroPop, String parametroFecha) {
        this.dataLanc = dataLanc;
        this.dataPopulcao = dataPopulcao;
        this.IdInternoCrc = IdInternoCrc;
        this.cnc = cnc;
        this.nomeInterno = nomeInterno;
        this.situacaoEntrada = situacaoEntrada;
        this.situacaoRetorno = situacaoRetorno;
        this.dataExclusaoPop = dataExclusaoPop;
        this.parametroPop = parametroPop;
        this.parametroFecha = parametroFecha;
    }

    /**
     * @return the dataLanc
     */
    public Date getDataLanc() {
        return dataLanc;
    }

    /**
     * @param dataLanc the dataLanc to set
     */
    public void setDataLanc(Date dataLanc) {
        this.dataLanc = dataLanc;
    }

    /**
     * @return the dataPopulcao
     */
    public Date getDataPopulcao() {
        return dataPopulcao;
    }

    /**
     * @param dataPopulcao the dataPopulcao to set
     */
    public void setDataPopulcao(Date dataPopulcao) {
        this.dataPopulcao = dataPopulcao;
    }

    /**
     * @return the IdInternoCrc
     */
    public int getIdInternoCrc() {
        return IdInternoCrc;
    }

    /**
     * @param IdInternoCrc the IdInternoCrc to set
     */
    public void setIdInternoCrc(int IdInternoCrc) {
        this.IdInternoCrc = IdInternoCrc;
    }

    /**
     * @return the cnc
     */
    public String getCnc() {
        return cnc;
    }

    /**
     * @param cnc the cnc to set
     */
    public void setCnc(String cnc) {
        this.cnc = cnc;
    }

    /**
     * @return the nomeInterno
     */
    public String getNomeInterno() {
        return nomeInterno;
    }

    /**
     * @param nomeInterno the nomeInterno to set
     */
    public void setNomeInterno(String nomeInterno) {
        this.nomeInterno = nomeInterno;
    }

    /**
     * @return the situacaoEntrada
     */
    public String getSituacaoEntrada() {
        return situacaoEntrada;
    }

    /**
     * @param situacaoEntrada the situacaoEntrada to set
     */
    public void setSituacaoEntrada(String situacaoEntrada) {
        this.situacaoEntrada = situacaoEntrada;
    }

    /**
     * @return the situacaoRetorno
     */
    public String getSituacaoRetorno() {
        return situacaoRetorno;
    }

    /**
     * @param situacaoRetorno the situacaoRetorno to set
     */
    public void setSituacaoRetorno(String situacaoRetorno) {
        this.situacaoRetorno = situacaoRetorno;
    }

    /**
     * @return the dataExclusaoPop
     */
    public String getDataExclusaoPop() {
        return dataExclusaoPop;
    }

    /**
     * @param dataExclusaoPop the dataExclusaoPop to set
     */
    public void setDataExclusaoPop(String dataExclusaoPop) {
        this.dataExclusaoPop = dataExclusaoPop;
    }

    /**
     * @return the parametroPop
     */
    public String getParametroPop() {
        return parametroPop;
    }

    /**
     * @param parametroPop the parametroPop to set
     */
    public void setParametroPop(String parametroPop) {
        this.parametroPop = parametroPop;
    }

    /**
     * @return the parametroFecha
     */
    public String getParametroFecha() {
        return parametroFecha;
    }

    /**
     * @param parametroFecha the parametroFecha to set
     */
    public void setParametroFecha(String parametroFecha) {
        this.parametroFecha = parametroFecha;
    }
}
