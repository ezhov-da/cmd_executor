package ru.ezhov.cmdexecutor.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.ezhov.cmdexecutor.ICmdExecutor;
import ru.ezhov.cmdexecutor.ObjectSend;

/**
 * Отправляем запрос к серверу
 * <p>
 *
 * @author RRNDeonisiusEZH
 */
public class ClientExecutor implements ICmdExecutor {
    private static final Logger logger = Logger.getLogger(ClientExecutor.class.getName());
    /**
     * Введенный ip
     */
    private final String ip;
    /**
     * Введенный порт
     */
    private final int port;
    /**
     * Введенный пароль
     */
    private final String password;
    /** Введенная команда */
    /**
     * socket
     */
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private ObjectSend objectSend;

    public ClientExecutor(String ip, int port, String password) {
        this.ip = ip;
        this.port = port;
        this.password = password;
    }

    public String execute(String command) {
        String answer = null;
        try {
            /*создаем socket*/
            socket = new Socket(ip, port);
            /*настраиваем потоки*/
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            objectSend = new ObjectSend(
                    ip,
                    command,
                    System.getProperty("user.name"),
                    password,
                    System.currentTimeMillis());
            /*пишем и отсылаем объект*/
            outputStream.writeObject(objectSend);
            outputStream.flush();
            objectSend = (ObjectSend) inputStream.readObject();
            answer = objectSend.getAnswer();
            /*получаем ответ*/
            logger.log(Level.INFO, "Ответ сервера: {0}", answer);
            outputStream.close();
            inputStream.close();
            return answer;
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return answer;
    }
}
