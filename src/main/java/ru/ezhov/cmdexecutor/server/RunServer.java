package ru.ezhov.cmdexecutor.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import ru.ezhov.cmdexecutor.ApplicationProperties;
import ru.ezhov.cmdexecutor.CmdExecutor;
import ru.ezhov.cmdexecutor.App;
import ru.ezhov.cmdexecutor.ObjectSend;

/**
 * Запускаем сервер
 * <p>
 * @author RRNDeonisiusEZH
 */
public class RunServer {
    private RunServer() {
    }

    static {
        try {
            LogManager.getLogManager()
                    .readConfiguration(
                            App
                                    .class
                                    .getResourceAsStream("/server.properties"));
        } catch (IOException ex) {
            Logger.getLogger(RunServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(RunServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void runServer() {
        /*Создаем трэй*/
        TrayServer.setTray();
        /*Запускаем сервер*/
        new Thread(new StartServer()).start();
    }

    /** Класс, который запускает сервер */
    private static class StartServer implements Runnable {
        private static final Logger logger = Logger.getLogger(StartServer.class.getName());
        /*Сервер*/
        private ServerSocket serverSocket;
        /*Сокет*/
        private Socket socket;

        public void run() {
            int serverPort;
            try {
                /*если порт не указан, выходим из приложения, так как дальнейшая работа бессмыслена*/
                serverPort = Integer.parseInt(ApplicationProperties.getValue("server_port"));
                if (serverPort == 0) {
                    logger.log(Level.SEVERE, "Не указан порт для сервера в конфигурционном файле.\nСвойство server_port.");
                    System.exit(-3);
                }
                serverSocket = new ServerSocket(serverPort);
                logger.log(Level.INFO, "Сервер готов принимать соединения");
                while (true) {
                    socket = serverSocket.accept();
                    new Thread(new SocketTreatment(socket)).start();
                }
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
    }

    /** Класс отвечает за обработку подключения */
    private static class SocketTreatment implements Runnable {
        private static final Logger logger = Logger.getLogger(SocketTreatment.class.getName());
        /** сокет, который обрабатывается */
        private final Socket socket;
        /** Класс обработки команды */
        private CmdExecutor cmdExecutor;

        public SocketTreatment(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                ObjectSend objectSend = (ObjectSend) objectInputStream.readObject();
                cmdExecutor = CmdExecutor.getCmdExecutor();
                logger.log(Level.INFO,
                        "Пробуем выполнить присланный запрос:\nip: {0}\nпользователь: {1}\nдата и время: {2}\nзапрос: {3}",
                        new Object[]{
                            objectSend.getIp(),
                            objectSend.getUsernameSender(),
                            new Date(objectSend.getDate()),
                            objectSend.getCommand()
                        });
                /*проверяем пароли*/
                if (!ApplicationProperties.getValue("server_password").equals(objectSend.getPassword())) {
                    String textError = "Пароли не совпадают.";
                    objectSend.setAnswer(textError);
                    objectOutputStream.writeObject(objectSend);
                    objectOutputStream.flush();
                    objectOutputStream.close();
                    objectInputStream.close();
                    logger.log(Level.INFO, textError);
                } else {
                    String resultCommand = cmdExecutor.execute(objectSend.getCommand());
                    objectSend.setAnswer(resultCommand);
                    objectOutputStream.writeObject(objectSend);
                    objectOutputStream.flush();
                    objectOutputStream.close();
                    objectInputStream.close();
                    logger.log(Level.INFO, "Ответ сервера: {0}", resultCommand);
                    logger.log(Level.INFO, "Обработка запроса завершена");
                }
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
    }
}
