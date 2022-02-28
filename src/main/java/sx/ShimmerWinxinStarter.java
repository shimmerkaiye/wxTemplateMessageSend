package sx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;

/**
 * @Description
 * @Author shimmer
 * @Date 2022-02-23 16:08
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class })
public class ShimmerWinxinStarter {
    public static void main(String[] args) {
        SpringApplication.run(ShimmerWinxinStarter.class,args);
    }
}
