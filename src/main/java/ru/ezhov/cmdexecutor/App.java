package ru.ezhov.cmdexecutor;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import ru.ezhov.cmdexecutor.client.ClientFrame;
import ru.ezhov.cmdexecutor.server.RunServer;

/**
 * Приложение для выполнения команд на удаленном компьюторе. <br>
 * Приложение позволяет запускать cmd команды на удаленном компьютере, с
 * запущенным сервером.
 * <p>
 * Входные параметры зависят от того, что именно запускается, клиент или севрер.
 * <p>
 * Для сервера читается ключ настроек - server<br>
 * Для клиента читается ключ настроек - client<br>
 * из файла настроек в корне папки приложения: app.properties
 * <p>
 * @author RRNDeonisiusEZH
 */
public class App {
    private static final Logger logger = Logger.getLogger(App.class.
            getName());

    static {
        /*подключаем общий логгер*/
        try {
            LogManager.getLogManager().readConfiguration(App.class.
                    getResourceAsStream(
                            "/common.properties"));
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null,
                    ex);
        } catch (SecurityException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null,
                    ex);
        }
    }

    /**
     * Ключ запуска server/client в файле настроек.
     * <p>
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        logger.log(Level.INFO, "Приложение запускается");
        //args = new String[]{"server"};
        //args = new String[]{"client"};
        /*проверяем корректность параметров*/
        checkArgs(args);
        /*если мы сюда попали - проверка пройдена*/
        runnApp(args[0]);
    }

    /** Метод проверяет корректность введенных параметров */
    private static void checkArgs(String[] args) {
        /*делаем проверку на непустоту параметров*/
        if (args.length == 0) {
            try {
                logger.log(Level.SEVERE,
                        "Введены некорректные параметры."
                        + "\nДопустимые параметры:\n{0}\n{1}",
                        new Object[]{ApplicationProperties.getValue("server"),
                            ApplicationProperties.getValue("client")}
                );
            } catch (IOException ex) {
                Logger.getLogger(App.class.getName()).
                        log(Level.SEVERE, null, ex);
            } finally {
                System.exit(-1);
            }
        }
        logger.log(Level.INFO, "Проверка пройдена");
    }

    /**
     * Запускаем сервер или клиент в зависимости от аргумента
     * <p>
     * @param param - аргумент запуска
     */
    private static void runnApp(String param) {
        try {
            /*если входной параметр server, тогда запускаем сервер*/
            if (ApplicationProperties.getValue("server").equals(param)) {
                logger.log(Level.INFO, "Запускаем сервер");
                RunServer.runServer();
            } else if (ApplicationProperties.getValue("client").equals(param)) {
                logger.log(Level.INFO, "Запускаем клиент");
                /*если клиент, тогда запускаем клиента*/
                ClientFrame.getClientFrame().setVisible(true);
            }
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE,
                    null,
                    ex);
        }
    }
}
