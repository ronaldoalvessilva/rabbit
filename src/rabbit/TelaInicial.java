/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rabbit;

import Dao.ControleEntradasSaidasPopulacaoInternos;
import Dao.ConversaoDatasAtividadesUnidades;
import Dao.GerarPopulacaoNominalAutomatica;
import Dao.ListagemRegistroEntradaSaidaPopulcao;
import Dao.ListagemUltimaPopulacaoCRC;
import Factory.ConexaoBancoDados;
import Modelo.AtividadesMensalRealizadaUnidades;
import Modelo.EntradaSaidasPolucaoInternos;
import Modelo.GerarPopNominal;
import java.awt.AWTKeyStroke;
import java.awt.Color;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author ronal
 */
public class TelaInicial extends javax.swing.JFrame {

    ConexaoBancoDados conecta = new ConexaoBancoDados();
    ControleEntradasSaidasPopulacaoInternos populacao = new ControleEntradasSaidasPopulacaoInternos();
    EntradaSaidasPolucaoInternos objEntradaSaida = new EntradaSaidasPolucaoInternos();
    ListagemUltimaPopulacaoCRC listaUltimaPopulacao = new ListagemUltimaPopulacaoCRC();
    ListagemRegistroEntradaSaidaPopulcao listaRegistroES = new ListagemRegistroEntradaSaidaPopulcao();
    ConversaoDatasAtividadesUnidades converteData = new ConversaoDatasAtividadesUnidades();
    AtividadesMensalRealizadaUnidades objAtividade = new AtividadesMensalRealizadaUnidades();
    //GERAR POPULAÇÃO CARCERÁRIA AUTOMÁTICAMENTE - TODOS OS DIAS 
    GerarPopulacaoNominalAutomatica GERAR = new GerarPopulacaoNominalAutomatica();
    GerarPopNominal objPopNom = new GerarPopNominal();
    //
    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss"); // HORAIO DE 24 HORAS, PARA O DE 12 HORAS UTILIZAR hh:mm:ss
    SimpleDateFormat formatter2 = new SimpleDateFormat("dd/MM/yyyy");
    String tipoOperacao = "População";
    //HORÁRIO DE EXECUÇÃO
    int pHORAS = 0;
    int pMINUTOS = 0;
    int pSEGUNDOS = 0;
    String nameUser = "ADMINISTRADOR DO SISTEMA";
    //
    String pMENSAGEM_ERRO = "";
    Date pDATA_DIA_populacao = new Date();
    int tempo = (int) (1000 * 60 * 1);   // 1 min.
    int periodo = 1;  // quantidade de vezes a ser executado.  
    int conta = 0;
    //POPULAÇÃO CARCERÁRIA
    public static int pTOTAL_internos = 0;
    int pTOTAL_REGISTROS_PRO = 0;
    String pDATA_DIA_POPULACAP_carceraria;
    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
    Date dataFormatada;

    //    int tempo = (int) ((1000 * 60) * 0.5);   // 1/2 min.  
    //    int tempo = (int) (1000 * 60 * 2);   // 2 min.
    /**
     * Creates new form TelaInicial
     */
    public TelaInicial() {
        initComponents();
        setIcon();
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
        }
        corCampos();
        //INICIO DO TESTE EM 15/06/2020               
        //THREAD PARA VERIFICAR O ARQUIVO E O AGENDAMENTO.
        THREAD_VERIFICA_horario();
        // Modificar a tecla tab por enter
        HashSet conj = new HashSet(this.getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
        conj.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_ENTER, 0));
        this.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, conj);
        Thread threadRelogio = new Thread() {

            @Override
            public void run() {
                rodaRelogio();
            }
        };
        threadRelogio.start();
        //
        Thread crono = new Thread() {
            @Override
            public void run() {
                Cronometro();
            }
        };
        crono.start();
        //
        Date data = new Date();
        String hora = formatter.format(data); // Data da conexão
        String date = formatter2.format(data); // Hora da conexão
        jHoraSistema.setText(String.valueOf(hora));    // no lugar do label, por seu JTextField    
        jDataSistema.setText(String.valueOf(date));
        //
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //Impedir que a janela seja fechada pelo X    
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (e.getID() == WindowEvent.WINDOW_CLOSING) {
                    int selectedOption = JOptionPane.showConfirmDialog(null, "Deseja realmente sair do Sistema?", "Sistema informa:", JOptionPane.YES_NO_OPTION);
                    if (selectedOption == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    }
                }
            }
        });
    }

    public void rodaRelogio() {

        try {
            while (true) {
                Date data = new Date();
                String hora = formatter.format(data);
                String date = formatter2.format(data);
                jHoraSistema.setText(String.valueOf(hora));    // no lugar do label, por seu JTextField    
                jDataSistema.setText(String.valueOf(date));
                Thread.sleep(1000);
            }
        } catch (InterruptedException ex) {
        }
    }

    public void pesquisar() {
        System.out.println("Iniciado Rabbit.");
        jInicioOperacao.setText("Iniciado Rabbit na Data: " + jDataSistema.getText() + " as: " + jHoraSistema.getText());
        listaRegistroES.selecionarRegistroEntrada(objEntradaSaida);
        Date data = new Date();
        String hora = formatter.format(data);
        pSOMAR_DATA_populacao();
        objEntradaSaida.setDataMovimento(pDATA_DIA_populacao);
        objEntradaSaida.setHorarioMovimento(hora);
        objEntradaSaida.setTipoOperacao(tipoOperacao);
        objEntradaSaida.setPopulacao(objEntradaSaida.getPopulacao());
        objEntradaSaida.setUsuarioInsert(nameUser);
        objEntradaSaida.setDataInsert(jDataSistema.getText());
        objEntradaSaida.setHorarioInsert(jHoraSistema.getText());
        populacao.incluirEntradaSaidaPopulacao(objEntradaSaida);
        //CONVERTER AS DADOS NA TABELA        
        converteData.alterarDataEntradasSaidasUnidades(objAtividade);
        System.out.println("População foi gerada com sucesso.");
        jTerminoOperacao.setText("População gerada com sucesso em: " + jDataSistema.getText() + " as: " + jHoraSistema.getText());
        //GRAVAR NO ARQUIVO TEXTO
        LOG_Mensagens();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jInicioOperacao = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jTerminoOperacao = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jTempoExecucao = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jHoras = new javax.swing.JFormattedTextField();
        jMinutos = new javax.swing.JFormattedTextField();
        jSegundos = new javax.swing.JFormattedTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jTotalRegistros = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jDataSistema = new javax.swing.JTextField();
        jHoraSistema = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("...::: Rabbit - Pop. Alimentícia/Carcerária :::...");
        setResizable(false);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true)));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(204, 0, 0));
        jLabel7.setText("ATENÇÃO: Essa aplicação irá gerar a população diária para ser");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(204, 0, 0));
        jLabel2.setText("informada no relatório da diretoria da empresa.");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(204, 0, 0));
        jLabel5.setText("A execução está programada conforme agendamento abaixo.");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addGap(2, 2, 2)
                .addComponent(jLabel2)
                .addGap(2, 2, 2)
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true)));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 204));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("VERSÃO: 27.042021-07/RSG - FINAL");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(jLabel12)
                .addGap(3, 3, 3))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Status Inicial", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(0, 102, 0))); // NOI18N

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel10.setText("Inicio da Operação:");

        jInicioOperacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jInicioOperacao.setForeground(new java.awt.Color(0, 102, 0));
        jInicioOperacao.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jInicioOperacao.setDisabledTextColor(new java.awt.Color(0, 102, 0));
        jInicioOperacao.setEnabled(false);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jInicioOperacao))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jInicioOperacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Status Final", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(0, 102, 0))); // NOI18N

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel11.setText("Termino da Operação:");

        jTerminoOperacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTerminoOperacao.setForeground(new java.awt.Color(0, 102, 0));
        jTerminoOperacao.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jTerminoOperacao.setDisabledTextColor(new java.awt.Color(0, 102, 0));
        jTerminoOperacao.setEnabled(false);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTerminoOperacao)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTerminoOperacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Tempo Execução", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(204, 0, 0))); // NOI18N

        jTempoExecucao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTempoExecucao.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTempoExecucao.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jTempoExecucao.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jTempoExecucao.setEnabled(false);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTempoExecucao)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTempoExecucao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Horário de Execução", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(204, 0, 0))); // NOI18N

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel14.setText("Seg.");

        jHoras.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jHoras.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jHoras.setText("0");
        jHoras.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jHoras.setEnabled(false);
        jHoras.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        jMinutos.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jMinutos.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jMinutos.setText("0");
        jMinutos.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jMinutos.setEnabled(false);
        jMinutos.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        jSegundos.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jSegundos.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jSegundos.setText("0");
        jSegundos.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jSegundos.setEnabled(false);
        jSegundos.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel13.setText("Hora");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel8.setText("Min.");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jHoras, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jMinutos, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSegundos, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel14)))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel13)
                    .addComponent(jLabel8)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jHoras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jMinutos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSegundos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true)));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 102, 0));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Total de Registros Processados:");

        jTotalRegistros.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTotalRegistros.setForeground(new java.awt.Color(204, 0, 0));
        jTotalRegistros.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTotalRegistros.setText("0");
        jTotalRegistros.setToolTipText("Total de Registros da População Carcerária");
        jTotalRegistros.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jTotalRegistros.setDisabledTextColor(new java.awt.Color(204, 0, 0));
        jTotalRegistros.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTotalRegistros, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTotalRegistros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true)));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Data do Sistema");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("Hora do Sistema");

        jDataSistema.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jDataSistema.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jDataSistema.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jDataSistema.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jDataSistema.setEnabled(false);

        jHoraSistema.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jHoraSistema.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jHoraSistema.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jHoraSistema.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jHoraSistema.setEnabled(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jDataSistema)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jHoraSistema, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jHoraSistema, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDataSistema, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true)));

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagem/My-Computer-64.png"))); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(3, 3, 3)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jPanel10, jPanel11});

        setSize(new java.awt.Dimension(403, 468));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>      
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaInicial().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField jDataSistema;
    private javax.swing.JTextField jHoraSistema;
    private javax.swing.JFormattedTextField jHoras;
    private javax.swing.JTextField jInicioOperacao;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JFormattedTextField jMinutos;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JFormattedTextField jSegundos;
    private javax.swing.JTextField jTempoExecucao;
    private javax.swing.JTextField jTerminoOperacao;
    private javax.swing.JTextField jTotalRegistros;
    // End of variables declaration//GEN-END:variables

    // COR DOS CAMPOS
    public void corCampos() {
        jDataSistema.setBackground(Color.white);
        jHoraSistema.setBackground(Color.white);
        jInicioOperacao.setBackground(Color.white);
        jTerminoOperacao.setBackground(Color.white);
        jTempoExecucao.setBackground(Color.white);
        jHoras.setBackground(Color.white);
        jMinutos.setBackground(Color.white);
        jSegundos.setBackground(Color.white);
        jTotalRegistros.setBackground(Color.white);
    }

    //GRAVAR LOG DO ROBÔ
    public void LOG_Mensagens() {
        try {
            PrintWriter arq = new PrintWriter("C:\\SysConp\\Rabbit\\LOG\\" + "LOG_" + objEntradaSaida.getIdDocumento() + ".txt");
            arq.println(jInicioOperacao.getText());
            arq.println(jTerminoOperacao.getText());
            arq.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TelaInicial.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //LOG DE ERRO CASO O APRESENTE
    public void LOG_ERROR() {
        try {
            PrintWriter arq = new PrintWriter("C:\\SysConp\\Rabbit\\LOG\\" + "LOG_ERROR" + objEntradaSaida.getIdDocumento() + ".txt");
            arq.println(pMENSAGEM_ERRO);
            arq.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TelaInicial.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //LOG DE FINALIZADO A EXECUÇÃO
    public void LOG_FINALIZADO() {
        try {
            PrintWriter arq = new PrintWriter("C:\\SysConp\\Rabbit\\LOG\\" + "LOG_FINAL" + objEntradaSaida.getIdDocumento() + ".txt");
            arq.println(pMENSAGEM_ERRO);
            arq.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TelaInicial.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //LER O CONTEUDO DO ARQUVIO
    public void ler_HORARIO_AGENDA() {
        try {
            BufferedReader arq = new BufferedReader(new FileReader("C:\\SysConp\\Rabbit\\HORARIO_AGENDA.txt"));
            jHoras.setText(arq.readLine());
            jMinutos.setText(arq.readLine());
            jSegundos.setText(arq.readLine());
            arq.close();
        } catch (Exception e) {
        }
    }

    //CRONOMETRO DE TEMPO DE EXECUÇÃO
    public void Cronometro() {
        int hor = 0, min = 0, seg = 0;
        try {
            jTempoExecucao.setText("0" + hor + ":" + "0" + min + ":" + seg);
            for (hor = 0; hor >= 0; hor++) {
                for (min = 0; min >= 0; min++) {
                    for (seg = 00; seg >= 0; seg++) {
                        if (seg == 60) {
                            min = min + 1;
                            seg = 0;
                        }
                        if (min == 60) {
                            hor = hor + 1;
                            min = 0;
                        }
                        Thread.sleep(1000);
                        jTempoExecucao.setText("0" + hor + ":" + "0" + min + ":" + seg);
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //NOVO MÉTODO PARA AGENDAMENTO DA POPULAÇÃO EM 24/06/2020 - TESTE
    public void THREAD_VERIFICA_horario() {
        final Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                jHoras.setText("");
                jMinutos.setText("");
                jSegundos.setText("");
                jInicioOperacao.setText("");
                jTerminoOperacao.setText("");
                ler_HORARIO_AGENDA();
                pHORAS = Integer.parseInt(jHoras.getText());
                pMINUTOS = Integer.parseInt(jMinutos.getText());
                pSEGUNDOS = Integer.parseInt(jSegundos.getText());
                conta++;
                System.out.println("Lendo o horário." + conta);
                //
                pHORAS = Integer.parseInt(jHoras.getText());
                pMINUTOS = Integer.parseInt(jMinutos.getText());
                pSEGUNDOS = Integer.parseInt(jSegundos.getText());
                //INSTÂNCIA O A HORA, MINUTOS E SEGUNDOS COM A CLASSE CALENDAR
                Calendar c = Calendar.getInstance();
                c.set(Calendar.HOUR_OF_DAY, pHORAS);
                c.set(Calendar.MINUTE, pMINUTOS);
                c.set(Calendar.SECOND, pSEGUNDOS);
                //ACRESENTEI ESSA COMPARAÇÃO AQUI (24/06/2020) AS 12:31HS.
                if (pHORAS == new Date().getHours() && pMINUTOS == new Date().getMinutes()) {
                    try {
                        pesquisar();
                        pMENSAGEM_ERRO = "Agendador finalizado com sucesso na data : " + jDataSistema.getText() + " e hora: " + jHoraSistema.getText();
                        VERIFICAR_parametro();
                        if (objPopNom.getParametroPop().equals("Sim")) {
                            GERAR_POPULACAO_carceraria();
                            pMENSAGEM_ERRO = "Gerando população Carcerária, Aguarde...";
                        }
                        LOG_FINALIZADO();
                    } catch (Exception e) {
                        e.printStackTrace();
                        pMENSAGEM_ERRO = "Não foi possível gerar a população ERROR: " + e;
                        LOG_ERROR();
                        pMENSAGEM_ERRO = "Não foi possível gerar a população na data de: " + jDataSistema.getText() + " as: " + jHoraSistema.getText() + "\n" + e;
                        LOG_ERROR();
                    }
                }
            }
        }, periodo, tempo);
    }

    public void pSOMAR_DATA_populacao() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(pDATA_DIA_populacao);
        cal.add(Calendar.DATE, 1);
        pDATA_DIA_populacao = cal.getTime();
    }

    public void pSOMAR_DATA_POPULACAO_carceraria() {
        try {
            pDATA_DIA_POPULACAP_carceraria = jDataSistema.getText();
            dataFormatada = formato.parse(pDATA_DIA_POPULACAP_carceraria);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dataFormatada);
            cal.add(Calendar.DATE, 1);
            dataFormatada = cal.getTime();
        } catch (ParseException ex) {
            Logger.getLogger(TelaInicial.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void GERAR_POPULACAO_carceraria() {
        System.out.println("Gerando População Carcerária, Aguarde...");
        jTotalRegistros.setText("0");
        jInicioOperacao.setText("Gerando População Carcerária, Aguarde...");
        jTerminoOperacao.setText("");
        pSOMAR_DATA_POPULACAO_carceraria();
        try {
            for (GerarPopNominal pp : GERAR.PESQUISAR_INTERNOS_read()) {
                objPopNom.setIdInternoCrc(pp.getIdInternoCrc());
                objPopNom.setDataPopulcao(dataFormatada);
                GERAR.incluirPopulacaoNominal(objPopNom);
                pTOTAL_REGISTROS_PRO++;
                jTotalRegistros.setText(String.valueOf(pTOTAL_REGISTROS_PRO));
            }
        } catch (Exception ex) {
            Logger.getLogger(TelaInicial.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (pTOTAL_REGISTROS_PRO == pTOTAL_internos) {
            jTerminoOperacao.setText("População gerada com sucesso em: " + jDataSistema.getText() + " as: " + jHoraSistema.getText());
        }
        System.out.println("População Carcerária foi gerada com sucesso.");
    }

    public void VERIFICAR_parametro() {
        GERAR.VERIFICAR_parametro(objPopNom);
        objPopNom.getParametroPop();
    }

    private void setIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Imagem/robot-screen-settings-icon.png")));
    }
}
