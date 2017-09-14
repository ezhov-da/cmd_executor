package ru.ezhov.cmdexecutor;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Реализация выполнения команды.
 * <p>
 * @author RRNDeonisiusEZH
 */
public class CmdExecutor implements ICmdExecutor {
    /** единственный экземпляр выполнения */
    private static final CmdExecutor cmdExecutor = new CmdExecutor();

    /** закрываем конструктор */
    private CmdExecutor() {
    }

    @Override
    public synchronized String execute(String command) {
        StringBuilder stringBuilder = new StringBuilder(1000);

        try {
            Process process = Runtime.getRuntime().exec(command);
            Scanner scanner = new Scanner(process.getInputStream(), "cp866");
            while (scanner.hasNext()){
                stringBuilder.append(scanner.nextLine());
                stringBuilder.append("\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(CmdExecutor.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        return stringBuilder.toString();
    }

    /**
     * Получаем единственный экземпляр исполнител
     * <p>
     * @return объект класса
     */
    public static CmdExecutor getCmdExecutor() {
        return cmdExecutor;
    }
}
