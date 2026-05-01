package co.com.arka.secretpoc.config;

import co.com.arka.secretpoc.model.brokersecret.BrokerSecret;
import co.com.arka.secretpoc.r2dbc.config.PostgresqlConnectionProperties;
import co.com.bancolombia.secretsmanager.api.GenericManagerAsync;
import co.com.bancolombia.secretsmanager.api.exceptions.SecretException;
import co.com.bancolombia.secretsmanager.config.AWSSecretsManagerConfig;
import co.com.bancolombia.secretsmanager.connector.AWSSecretManagerConnectorAsync;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.regions.Region;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class SecretsConfig {

  /*
    Use GenericManagerAsync bean in your reactive pipe.
    connector.getSecret("mySecretName", SecretModel.class).map(...);
  */

  @Bean
  public GenericManagerAsync getSecretManager(@Value("${aws.region}") String region,
                                              @Value("${aws.endpoint}") String endpoint) {
    return new AWSSecretManagerConnectorAsync(getConfig(region, endpoint));
  }

  @Bean
  public PostgresqlConnectionProperties getOrdersDbSecret(@Value("${aws.secrets.db-name}") String secretName,
                                                          GenericManagerAsync secretManager) throws SecretException {
    return secretManager.getSecret(secretName, PostgresqlConnectionProperties.class)
            .doOnSuccess(e -> log.info("Secret was obtained successfully: {}", secretName))
            .doOnError(e -> log.error("Error getting secret: {}", e.getMessage()))
            .block();
  }

  @Bean
  public BrokerSecret getBrokerSecret(@Value("${aws.secrets.kafka-name}") String secretName,
                                        GenericManagerAsync secretManager) throws SecretException {
    return secretManager.getSecret(secretName, BrokerSecret.class)
            .doOnSuccess(e -> log.info("Secret was obtained successfully: {}", secretName))
            .doOnError(e -> log.error("Error getting secret: {}", e.getMessage()))
            .block();
  }

  private AWSSecretsManagerConfig getConfig(String region, String endpoint) {
    return AWSSecretsManagerConfig.builder()
      .region(Region.of(region))
      .endpoint(endpoint)
      .cacheSize(5) // TODO Set cache size
      .cacheSeconds(3600) // TODO Set cache seconds
      .build();
  }
}
