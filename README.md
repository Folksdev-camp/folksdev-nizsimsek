# Folksdev & Kod Gemisi SpringBoot BootCamp

---
## Özet
Kullanıcıların blog yazıları yazabildiği, yazıları beğenebildiği, yorum yapabildiği API projesi.

---
### Kullanılan Teknolojiler
- Maven
- Java 11
- Kotlin 1.6.0
- Spring Boot 2.5.6
- Spring Data JPA
- MySQL 8.0.27
- H2 In memory database
- Flyway
- Hamcrest 2.2
- OpenAPI documentation 1.5.12
- Docker
- Docker compose
- JUnit 5

---
### Gereksinimler
- Maven
- Docker

---
## Projeyi Çalıştırmak
Projeyi çalıştırmanın 2 yolu bulunmaktadır.
Öncelikle projeyi bilgisayarınıza klonlayınız.
Proje ana dizinine gidiniz.
Tercihinize göre kurulum türü için aşağıdaki adımları takip ediniz.

### Maven Kurulumu

---

Maven ile çalıştırmak için komut satırına
`mvn clean install` yazarak jar türünde dosya oluşturunuz.
`mvn spring-boot:run` yazarak projeyi çalıştırınız.

### Docker Compose Kurulumu

---

Docker Compose ile çalıştırmak için komut satırına

`docker build -t blogapi:latest .` yazarak docker image oluşturunuz.
`docker-compose up` yazarak docker image oluşturunuz. Bu şekilde çalıştırdığınızda gerekli kurulumlar docker üzerinde yapılacaktır.

---

## OpenAPI UI
OpenAPI ile direkt olarak HTTP isteklerinde bulunabilirsiniz.
`http://localhost:8080/swagger-ui.html`

---
