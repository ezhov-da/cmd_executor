package ru.ezhov.cmdexecutor.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import ru.ezhov.cmdexecutor.ApplicationConst;
import ru.ezhov.cmdexecutor.ApplicationProperties;
import ru.ezhov.cmdexecutor.App;
import ru.ezhov.cmdexecutor.ICmdExecutor;
import ru.ezhov.cmdexecutor.server.RunServer;

/**
 * Класс работы клиента
 * <p>
 * @author RRNDeonisiusEZH
 */
public class ClientFrame extends JFrame {
    private static final Logger logger = Logger.getLogger(ClientFrame.class.getName());
    /** Название окна клиента */
    private static final String NAME_FRAME = "CMD Клиент";
    private static final Image ICON_CLIENT
            = new ImageIcon(App.class.getResource("/ru/ezhov/cmdexecutor/src/icon_client.png")).getImage();
    private static final Dimension SIZE_FRAME = new Dimension(600, 500);
    private static final JLabel LABEL_IP = new JLabel("ip сервера:");
    private static final JLabel LABEL_VERSION = new JLabel(ApplicationConst.VERSION);
    private static final JLabel LABEL_PORT = new JLabel("порт сервера:");
    private static final JLabel LABEL_PASSWORD = new JLabel("пароль сервера:");
    private static final JLabel LABEL_COMMAND = new JLabel("команда на выполнение к серверу:");
    private static final JLabel LABEL_ANSWER = new JLabel("ответ сервера:");
    private static final JTextField TEXT_IP = new JTextField("0", 37);
    private static final JTextField TEXT_PORT = new JTextField();
    private static final JPasswordField TEXT_PASS = new JPasswordField();
    private static final JTextField TEXT_COMMAND = new JTextField();
    private static final JTextPane TEXT_ANSWER = new JTextPane();
    private static final JButton BUTTON_EXECUTE = new JButton("Выполнить команду");
    private static final JSplitPane SPLIT_PANE = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    private final static Insets INSETS = new Insets(5, 5, 5, 5);
    private static final ClientFrame clientFrame = new ClientFrame();
    private static final Font FONT = new Font("Console", Font.PLAIN, 10);

    static {
        try {
            LogManager.getLogManager().readConfiguration(App.class.getResourceAsStream("/src/main/recources/client.properties"));
        } catch (IOException ex) {
            Logger.getLogger(RunServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(RunServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private ClientFrame() {
        super();
        init();
    }

    public static JFrame getClientFrame() {
        return clientFrame;
    }

    /**
     * Инициализируем форму
     */
    private void init() {
        logger.log(Level.INFO, "Инициализируем форму клиента");
        try {
            /*устанавливаем значения по умолчанию*/
            TEXT_IP.setText(ApplicationProperties.getValue("ip_server"));
            TEXT_PORT.setText(ApplicationProperties.getValue("client_port"));
            TEXT_PASS.setText(ApplicationProperties.getValue("client_password"));
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        BUTTON_EXECUTE.addActionListener(new ListenerExecute());
        TEXT_ANSWER.setEditable(false);
        TEXT_ANSWER.setBackground(Color.BLACK);
        TEXT_ANSWER.setForeground(Color.WHITE);
        TEXT_ANSWER.setFont(FONT);
        SPLIT_PANE.setResizeWeight(0.1d);
        SPLIT_PANE.setOneTouchExpandable(true);
        SPLIT_PANE.setResizeWeight(0);
        /****ставим шрифт****************/
        TEXT_COMMAND.setFont(new Font("Console", Font.BOLD, 20));
        TEXT_COMMAND.setForeground(Color.RED);
        /*********************************/
        JPanel panelUp = new JPanel();
        panelUp.setLayout(new GridBagLayout());
        panelUp.add(LABEL_IP, new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, INSETS, 0, 0));
        panelUp.add(LABEL_VERSION, new GridBagConstraints(1, 0, 1, 0, 0, 0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, INSETS, 0, 0));
        panelUp.add(TEXT_IP, new GridBagConstraints(0, 1, 2, 1, 1, 0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, INSETS, 0, 0));
        panelUp.add(LABEL_PORT, new GridBagConstraints(0, 2, 2, 1, 1, 0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, INSETS, 0, 0));
        panelUp.add(TEXT_PORT, new GridBagConstraints(0, 3, 2, 1, 1, 0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, INSETS, 0, 0));
        panelUp.add(LABEL_PASSWORD, new GridBagConstraints(0, 4, 2, 1, 1, 0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, INSETS, 0, 0));
        panelUp.add(TEXT_PASS, new GridBagConstraints(0, 5, 2, 1, 1, 0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, INSETS, 0, 0));
        panelUp.add(LABEL_COMMAND, new GridBagConstraints(0, 6, 2, 1, 1, 0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, INSETS, 0, 0));
        panelUp.add(TEXT_COMMAND, new GridBagConstraints(0, 7, 2, 1, 1, 0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, INSETS, 0, 0));
        panelUp.add(BUTTON_EXECUTE, new GridBagConstraints(0, 8, 2, 1, 1, 0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, INSETS, 0, 0));
        SPLIT_PANE.setTopComponent(panelUp);
        JPanel panelBottom = new JPanel(new BorderLayout(5, 5));
        panelBottom.add(LABEL_ANSWER, BorderLayout.NORTH);
        panelBottom.add(new JScrollPane(TEXT_ANSWER), BorderLayout.CENTER);
        SPLIT_PANE.setBottomComponent(panelBottom);
        add(SPLIT_PANE);
        setIconImage(ICON_CLIENT);
        setTitle(NAME_FRAME);
        setSize(SIZE_FRAME);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    /** Слушатель кнопки выполнить */
    private class ListenerExecute implements ActionListener {
        private ICmdExecutor cmdExecutor;
        private final Document document = TEXT_ANSWER.getDocument();

        @Override
        public void actionPerformed(ActionEvent e) {
            BUTTON_EXECUTE.setEnabled(false);
            /*отправляем запрос к серверу*/
            String ip = TEXT_IP.getText();
            String pass = new String(TEXT_PASS.getPassword());
            String command = TEXT_COMMAND.getText();
            int port = Integer.parseInt(TEXT_PORT.getText());
            logger.log(
                    Level.INFO,
                    "Отправляем запрос к серверу:\nip:{0}\npass: {1}\ncommand: {2}",
                    new Object[]{ip, pass, command});
            cmdExecutor = new ClientExecutor(ip, port, pass);
            /*получаем ответ от сервера*/
            String answer = cmdExecutor.execute(command);
            logger.log(Level.INFO, "Ответ сервера: {0}", answer);
            try {
                document.insertString(document.getLength(), answer, null);
            } catch (BadLocationException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            BUTTON_EXECUTE.setEnabled(true);
        }
    }
}
