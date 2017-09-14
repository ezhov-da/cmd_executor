package ru.ezhov.cmdexecutor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;
import static ru.ezhov.cmdexecutor.ApplicationConst.PATH_APP_PROPERTIES_FILE;

/**
 * Класс считывает настройки и предоставляет их по ключу. <br>
 * Считывание настроек производится каждый раз, когда программа обращается к
 * ключу <br>
 * Это сделано для того, чтоб в случае изменения ключа она его подхватила.
 * <p>
 * @author RRNDeonisiusEZH
 */
public class ApplicationProperties {
    private static final Logger logger = Logger.getLogger(
            ApplicationProperties.class.getName());
    /** объект с настройками */
    private static final Properties PROPERTIES = new Properties();
    /**поток чтения настроек*/
    private static InputStream inputStream;


    private ApplicationProperties() {
    }

    /**
     * загружает настройки и отдает значение по ключу
     * @param key - ключ для получения значения
     * @return значение
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static String getValue(String key) throws FileNotFoundException, IOException {
        inputStream = new FileInputStream(PATH_APP_PROPERTIES_FILE);
        PROPERTIES.load(inputStream);
        inputStream.close();
        PROPERTIES.getProperty(key);
        return PROPERTIES.getProperty(key);
    }
}
