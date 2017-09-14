package ru.ezhov.cmdexecutor;

import java.io.Serializable;

/**
 * Класс хранения информации, который отправляется от клиента к серверу и
 * обратно.
 * <p>
 * @author RRNDeonisiusEZH
 */
public class ObjectSend implements Serializable {
    /** ip отправителя */
    private String ip;
    /** команда для выполнения */
    private String command;
    /** пославший пользователеь */
    private String usernameSender;
    /** пароль для сервера */
    private String password;
    /** время отправки */
    private long date;
    /** ответ сервера */
    private String answer;

    public ObjectSend(String ip, String command, String usernameSender, String password, long date) {
        this.ip = ip;
        this.command = command;
        this.usernameSender = usernameSender;
        this.password = password;
        this.date = date;
    }

    public String getIp() {
        return ip;
    }

    public String getCommand() {
        return command;
    }

    public String getUsernameSender() {
        return usernameSender;
    }

    public String getPassword() {
        return password;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setUsernameSender(String usernameSender) {
        this.usernameSender = usernameSender;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
