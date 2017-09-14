package ru.ezhov.cmdexecutor.server;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import ru.ezhov.cmdexecutor.ApplicationProperties;
import ru.ezhov.cmdexecutor.App;

/**
 * Класс создает tray для сервера
 * <p>
 * @author RRNDeonisiusEZH
 */
public class TrayServer {
    /** сообщение о том, что трэй не поддерживается */
    private static final String MESSAGE_TRAY_NOT_SUPPORTED = "Трэй не поддерживается системой";
    private static final Logger logger = Logger.getLogger(TrayServer.class.getName());
    private static SystemTray tray;
    private static final Image ICON_SERVER
            = new ImageIcon(App.class.getResource("/ru/ezhov/cmdexecutor/src/icon_server.png")).getImage();
    private static final String NAME_SERVER = "CMD_EXECUTOR_SERVER";
    private static final TrayIcon trayIcon = new TrayIcon(ICON_SERVER, NAME_SERVER);

    /**
     * Получаем трэй
     */
    public static void setTray() {
        /*если трэй поддерживается*/
        logger.log(Level.INFO, "Проверяем поддержку трэя");
        if (SystemTray.isSupported()) {
            logger.log(Level.INFO, "Трэй поддерживается");
            try {
                logger.log(Level.INFO, "Создаем трэй");
                createTray();
                logger.log(Level.INFO, "Трэй создан");
            } catch (AWTException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        } else {
            /*
             * если трэй не поддерживается,
             * тогда сообщаем об этом, пишем лог и выходим
             */
            logger.log(Level.INFO, MESSAGE_TRAY_NOT_SUPPORTED);
            JOptionPane.showMessageDialog(
                    null,
                    MESSAGE_TRAY_NOT_SUPPORTED,
                    "Важно",
                    JOptionPane.INFORMATION_MESSAGE
            );
            System.exit(-2);
        }
    }

    /** Устанавливаем трэй */
    private static void createTray() throws AWTException {
        tray = SystemTray.getSystemTray();
        setTrayIcon();
        tray.add(trayIcon);
    }

    /** Устанавливаем настройки трэя */
    private static void setTrayIcon() {
        trayIcon.setPopupMenu(ServerPopupMenu.getPopupMenu());
        trayIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InfoServer.getDialogInfo().setVisible(true);
            }
        });
    }

    /** Внутренний класс, который создает контекстное меню трэя */
    private static class ServerPopupMenu {
        private static final PopupMenu POPUP_MENU_SERVER = new PopupMenu();
        private static final String NAME_LABEL = "Завершить работу сервера";
        private static final MenuItem MI_EXIT = new MenuItem(NAME_LABEL);

        private ServerPopupMenu() {
        }

        /** получем меню */
        private static PopupMenu getPopupMenu() {
            /*ставим выключение сервера*/
            MI_EXIT.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    logger.log(Level.INFO, "Сервер выключен ");
                    System.exit(0);
                }
            });
            POPUP_MENU_SERVER.add(MI_EXIT);
            return POPUP_MENU_SERVER;
        }
    }

    /** Получаем окно с информацией */
    private static class InfoServer {
        private static JDialog DIALOG;
        private static final JEditorPane EDITOR_PANE = new JEditorPane();
        private static final String NAME_DIALOG = "CMD Сервер";
        private static final Dimension SIZE = new Dimension(300, 120);

        private InfoServer() {
        }

        public static JDialog getDialogInfo() {
            if (DIALOG == null) {
                DIALOG = new JDialog();
                EDITOR_PANE.setText(getText());
                EDITOR_PANE.setEditable(false);
                DIALOG.add(EDITOR_PANE);
                DIALOG.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
                DIALOG.setTitle(NAME_DIALOG);
                DIALOG.setIconImage(ICON_SERVER);
                DIALOG.setSize(SIZE);
                DIALOG.setLocationRelativeTo(null);
            }
            return DIALOG;
        }

        /**
         * Получаем надпись.
         * <p>
         * @return надпись
         */
        private static String getText() {
            String text;
            String hostName;
            String hostAddress;
            InetAddress addr;
            try {
                addr = InetAddress.getLocalHost();
                text = String.format("%s запущен:\n", NAME_SERVER);
                hostName = String.format("- ip name: %s \n", addr.getHostName());
                hostAddress = String.format("- ip addres: %s \n", addr.getHostAddress());
            } catch (UnknownHostException ex) {
                text = "Ошибка получения адреса:\n";
                hostName = "\t- невозможно получить ip name";
                hostAddress = "\t- невозможно получить ip address";
            }
            String port;
            try {
                port = "- порт: " + ApplicationProperties.getValue("server_port");
            } catch (IOException ex) {
                port = "- не загрузился файл с настроками, проверьте его наличие или отсутсвует ключ: \"server_port\"";
            }
            StringBuilder stringBuilder = new StringBuilder(100);
            stringBuilder.append(text);
            stringBuilder.append(hostName);
            stringBuilder.append(hostAddress);
            stringBuilder.append(port);
            return stringBuilder.toString();
        }
    }
}
