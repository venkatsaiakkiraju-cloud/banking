package com.securebank.auth;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
@SpringBootTest
@TestPropertySource(properties={
  "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
  "spring.datasource.driver-class-name=org.h2.Driver",
  "spring.datasource.username=sa","spring.datasource.password=",
  "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
  "jwt.secret=TestSecretKeyForJUnitTestingPurposesOnlyLong","jwt.expiration=86400000"
})
class AuthServiceApplicationTest { @Test void contextLoads() {} }
