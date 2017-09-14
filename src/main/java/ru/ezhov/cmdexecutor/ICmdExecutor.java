package ru.ezhov.cmdexecutor;

/**
 * Интерфейс для выполнения комманд cmd на сервере.
 * <p>
 * @author RRNDeonisiusEZH
 */
public interface ICmdExecutor {
    /**
     * Выполняет комманды пришедшие на сервер.
     * <p>
     * @param command - команда для выполнения
     * <p>
     * @return результат выполнения, или ошибку выполнения
     */
    String execute(String command);
}
