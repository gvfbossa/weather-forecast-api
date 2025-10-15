# Weather Forecast Application

## Resumo

A **Weather Forecast Application** é uma aplicação Spring Boot que fornece informações meteorológicas baseadas em um código postal (zipcode). A aplicação consulta dados de tempo em tempo real e armazena as informações de localização em **cache** (usando Redis) por **15 minutos** para otimizar chamadas subsequentes. Ela retorna dados como **temperatura atual**, **máxima e mínima do dia**, além de **previsões para os próximos dias**.

## Como Rodar a Aplicação

Para executar a aplicação localmente, siga os passos abaixo:

1. Este projeto utiliza **Java 21**.
2. Certifique-se de ter o **Maven** e o **Docker** instalados.
3. Navegue até o diretório do projeto.
4. Execute os seguintes comandos para compilar o projeto e iniciar os serviços com Docker Compose:

```bash
mvn clean install
docker-compose up --build -d
```

Isso construirá a imagem Docker da aplicação e iniciará o contêiner junto com o Redis.

## Acessando a Documentação

A documentação da API está disponível via Swagger UI. Após iniciar a aplicação, acesse:

`http://localhost:8080/swagger-ui/index.html`

## Como Usar a Aplicação

Para obter informações meteorológicas, faça uma requisição *GET* para o endpoint abaixo, substituindo `{zipcode}` por um código postal válido (ex.: `90210`):

`http://localhost:8080/api/weather/{zipcode}`

Exemplo:

`http://localhost:8080/api/weather/90210`

## Resposta da API

A API retorna um JSON com as seguintes informações:
- *location*: Endereço completo baseado no zipcode.
- *temperature*: Temperatura atual em graus Celsius.
- *dailyMax*: Temperatura máxima do dia atual.
- *dailyMin*: Temperatura mínima do dia atual.
- *fromCache*: Indica se os dados foram recuperados do cache (true/false).
- *upcomingDays*: Previsão para os próximos dias, incluindo data, temperatura mínima e máxima.

## Notas

- A aplicação utiliza Redis para cache, garantindo que informações de localização sejam armazenadas por 15 minutos.