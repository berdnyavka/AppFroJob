package connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.log4j.Log4j2;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
@Log4j2
public class MyConnection {

    public static HikariDataSource getDataSource(){
        HikariConfig config = new HikariConfig();
        Properties properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream("src/main/java/connection/Config.properties");
            properties.load(fis);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        final String url = properties.getProperty("url");
        final String login = properties.getProperty("login");
        final String password = properties.getProperty("password");
        config.setUsername(login);
        config.setPassword(password);
        config.setJdbcUrl(url);
        HikariDataSource ds = new HikariDataSource(config);
        return ds;
    }
}
