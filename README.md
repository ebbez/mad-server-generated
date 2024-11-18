# AutoMaat

## Introductie

Deze repository bevat de werkende code voor de Automaat backend applicatie voor het MAD 4e jaars keuzevak. Voor de geïntresseerden; het is een Java [Spring Boot](https://spring.io/projects/spring-boot) applicatie gegenereerd mbv [JHipster](https://www.jhipster.tech/). De applicatie bevat een volledig werkende front- en backend. De backend is door een REST API ontsloten en daar maken de studenten gebruik van. Deze API is volledig beschreven middels OpenAPI en de documentatie wordt door Swagger gepresenteerd en bevat voorbeelden die de student kan gebruiken.

## Vereisten

Om de code in deze repo te kunnen gebruiken heb je alleen maar een courante java JDK nodig. Het project is getest met Java 17. Om te zien welke versie je hebt kan je het volgende commando gebruiken:

```bash
# java --version
openjdk version "17.0.13" 2024-10-15
OpenJDK Runtime Environment (build 17.0.13+11)
OpenJDK 64-Bit Server VM (build 17.0.13+11, mixed mode, sharing)
```

Het build systeem wat voor dit project gebruikt wordt is `maven` maar die hoef je niet te installeren. Maven is als wrapper meegeleverd met dit project en beschikbaar door vanuit de root van het project `./mvnw` aan te roepen.

### Opstarten

Om het systeem te runnen moet je eerst het project uit Github clonen:

```bash
# git clone https://github.com/hanze-hbo-ict/mad-server-generated.git
```

Vervolgens navigeer je naar de zojuist geclonede repo en start je de applicatie op:

- Windows: `mvnw.cmd`
- Linux/Mac: `./mvnw`

Als alles goed is gegaan zie je nu aan het einde van het opstartproces iets als:

```bash
2024-11-18T12:55:06.153+01:00  INFO 58833 --- [  restartedMain] nl.hanze.se4.automaat.AutoMaatApp        :
----------------------------------------------------------
        Application 'AutoMaat' is running! Access URLs:
        Local:          http://localhost:8080/
        External:       http://127.0.1.1:8080/
        Profile(s):     [dev, api-docs]
----------------------------------------------------------
```

En kan je de applicatie openen door in je browser het de aangegeven url te navigeren.

### Gebruikersregistratie

In de backend zijn twee tabellen in gebruik voor de uiteindelijke gebruikers, `USER` en `CUSTOMER`.

- De `USER` tabel wordt gebruikt voor de technische - of systeem gebruiker. Dit is het account waar de klant mee inlogt en is altijd aanwezig in een JHipster applicatie.
- De `CUSTOMER` tabel is specifiek voor de Automaat applicatie en is de meer functionele plek voor klant gegevens

In de api is er een endpoint beschikbaar voor het registreren van een nieuwe gebruiker. Standaard wordt in JHipster tijdens registratie (een POST op `.../api.register`) alleen maar een `USER` aangemaakt. De Automaat backend bevat een extra endpoint `.../api/AM/register` waarbij niet alleen een `USER` wordt aangemaakt, maar ook een `CUSTOMER` én de twee worden aan elkaar gekoppeld. Vanuit de te realiseren app kan je dit extra endpoint aanroepen. De volgende velden payload bevat de noodzakelijke velden en de structuur die je daarvoor kan gebruiken:

```json
{
  "login": "string",
  "firstName": "string",
  "lastName": "string",
  "email": "string",
  "langKey": "string",
  "password": "string"
}
```

### Test data

Tijdens het opstarten wordt er testdata in de database opgenomen. Deze testdata staat in `src/main/resources/config/liquibase/fake-data` en kan je aanpassen aan je eigen wensen.

#### In memory

De code zoals die in de repository zit is geconfigureerd voor een H2 in-memory database. Dit betekent dat, elke keer dat je de applicatie stopt en weer start, de data weer in zijn oorspronkelijke vorm aanwezig is (de fake-data). Al je eigen inserts/updates/deletes zijn weg. Dit kan handig zijn, zeker in het begin omdat je elke keer weer met een schone op kan starten.

#### Gepersisteerd

Wanneer je liever wilt dat je aanpassingen bewaard blijven kan je een aanpassing doen aan een van de Spring configuratie bestanden `src/main/resources/config/application-dev.yml`. Regel 40 bevat de 'in memory' variant. Om wijzigingen over stop/starten te behouden, zet je regel 40 in commentaat en haal je regel 41 uit commentaar.

### Mail server

De backend maakt gebruik van mail voor (o.a.) nieuwe registraties en wanneer je wachtwoorden bent vergeten. Om makkelijk de mail functionaliteit te kunnen gebruiken is er een docker compose file meegeleverd die een [MailDev](https://github.com/maildev/maildev) smtp server start en waarmee emails uit de backend onderschept en gelezen kunnen worden. Start deze server op middels het commando:

```bash
docker compose -f src/main/docker/maildev.yml up
```

Vervolgens navigeer je naar `http://localhost:1080/` om de mail via een webinterface te bekijken.

## Hieronder de oorspronkelijke, door JHipster gegenereerde README

This application was generated using JHipster 8.7.3, you can find documentation and help at [https://www.jhipster.tech/documentation-archive/v8.7.3](https://www.jhipster.tech/documentation-archive/v8.7.3).

## Project Structure

Node is required for generation and recommended for development. `package.json` is always generated for a better development experience with prettier, commit hooks, scripts and so on.

In the project root, JHipster generates configuration files for tools like git, prettier, eslint, husky, and others that are well known and you can find references in the web.

`/src/*` structure follows default Java structure.

- `.yo-rc.json` - Yeoman configuration file
  JHipster configuration is stored in this file at `generator-jhipster` key. You may find `generator-jhipster-*` for specific blueprints configuration.
- `.yo-resolve` (optional) - Yeoman conflict resolver
  Allows to use a specific action when conflicts are found skipping prompts for files that matches a pattern. Each line should match `[pattern] [action]` with pattern been a [Minimatch](https://github.com/isaacs/minimatch#minimatch) pattern and action been one of skip (default if omitted) or force. Lines starting with `#` are considered comments and are ignored.
- `.jhipster/*.json` - JHipster entity configuration files

- `npmw` - wrapper to use locally installed npm.
  JHipster installs Node and npm locally using the build tool by default. This wrapper makes sure npm is installed locally and uses it avoiding some differences different versions can cause. By using `./npmw` instead of the traditional `npm` you can configure a Node-less environment to develop or test your application.
- `/src/main/docker` - Docker configurations for the application and services that the application depends on

## Development

The build system will install automatically the recommended version of Node and npm.

We provide a wrapper to launch npm.
You will only need to run this command when dependencies change in [package.json](package.json).

```
./npmw install
```

We use npm scripts and [Angular CLI][] with [Webpack][] as our build system.

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

```
./mvnw
./npmw start
```

Npm is also used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in [package.json](package.json). You can also run `./npmw update` and `./npmw install` to manage dependencies.
Add the `help` flag on any command to see how you can use it. For example, `./npmw help update`.

The `./npmw run` command will list all the scripts available to run for this project.

### PWA Support

JHipster ships with PWA (Progressive Web App) support, and it's turned off by default. One of the main components of a PWA is a service worker.

The service worker initialization code is disabled by default. To enable it, uncomment the following code in `src/main/webapp/app/app.config.ts`:

```typescript
ServiceWorkerModule.register('ngsw-worker.js', { enabled: false }),
```

### Managing dependencies

For example, to add [Leaflet][] library as a runtime dependency of your application, you would run following command:

```
./npmw install --save --save-exact leaflet
```

To benefit from TypeScript type definitions from [DefinitelyTyped][] repository in development, you would run following command:

```
./npmw install --save-dev --save-exact @types/leaflet
```

Then you would import the JS and CSS files specified in library's installation instructions so that [Webpack][] knows about them:
Edit [src/main/webapp/app/app.config.ts](src/main/webapp/app/app.config.ts) file:

```
import 'leaflet/dist/leaflet.js';
```

Edit [src/main/webapp/content/scss/vendor.scss](src/main/webapp/content/scss/vendor.scss) file:

```
@import 'leaflet/dist/leaflet.css';
```

Note: There are still a few other things remaining to do for Leaflet that we won't detail here.

For further instructions on how to develop with JHipster, have a look at [Using JHipster in development][].

### Using Angular CLI

You can also use [Angular CLI][] to generate some custom client code.

For example, the following command:

```
ng generate component my-component
```

will generate few files:

```
create src/main/webapp/app/my-component/my-component.component.html
create src/main/webapp/app/my-component/my-component.component.ts
update src/main/webapp/app/app.config.ts
```

## Building for production

### Packaging as jar

To build the final jar and optimize the AutoMaat application for production, run:

```
./mvnw -Pprod clean verify
```

This will concatenate and minify the client CSS and JavaScript files. It will also modify `index.html` so it references these new files.
To ensure everything worked, run:

```
java -jar target/*.jar
```

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

Refer to [Using JHipster in production][] for more details.

### Packaging as war

To package your application as a war in order to deploy it to an application server, run:

```
./mvnw -Pprod,war clean verify
```

### JHipster Control Center

JHipster Control Center can help you manage and control your application(s). You can start a local control center server (accessible on http://localhost:7419) with:

```
docker compose -f src/main/docker/jhipster-control-center.yml up
```

## Testing

### Spring Boot tests

To launch your application's tests, run:

```
./mvnw verify
```

### Client tests

Unit tests are run by [Jest][]. They're located in [src/test/javascript/](src/test/javascript/) and can be run with:

```
./npmw test
```

## Others

### Code quality using Sonar

Sonar is used to analyse code quality. You can start a local Sonar server (accessible on http://localhost:9001) with:

```
docker compose -f src/main/docker/sonar.yml up -d
```

Note: we have turned off forced authentication redirect for UI in [src/main/docker/sonar.yml](src/main/docker/sonar.yml) for out of the box experience while trying out SonarQube, for real use cases turn it back on.

You can run a Sonar analysis with using the [sonar-scanner](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner) or by using the maven plugin.

Then, run a Sonar analysis:

```
./mvnw -Pprod clean verify sonar:sonar -Dsonar.login=admin -Dsonar.password=admin
```

If you need to re-run the Sonar phase, please be sure to specify at least the `initialize` phase since Sonar properties are loaded from the sonar-project.properties file.

```
./mvnw initialize sonar:sonar -Dsonar.login=admin -Dsonar.password=admin
```

Additionally, Instead of passing `sonar.password` and `sonar.login` as CLI arguments, these parameters can be configured from [sonar-project.properties](sonar-project.properties) as shown below:

```
sonar.login=admin
sonar.password=admin
```

For more information, refer to the [Code quality page][].

### Docker Compose support

JHipster generates a number of Docker Compose configuration files in the [src/main/docker/](src/main/docker/) folder to launch required third party services.

For example, to start required services in Docker containers, run:

```
docker compose -f src/main/docker/services.yml up -d
```

To stop and remove the containers, run:

```
docker compose -f src/main/docker/services.yml down
```

[Spring Docker Compose Integration](https://docs.spring.io/spring-boot/reference/features/dev-services.html) is enable by default. It's possible to disable it in application.yml:

```yaml
spring:
  ...
  docker:
    compose:
      enabled: false
```

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a Docker image of your app by running:

```sh
npm run java:docker
```

Or build a arm64 Docker image when using an arm64 processor os like MacOS with M1 processor family running:

```sh
npm run java:docker:arm64
```

Then run:

```sh
docker compose -f src/main/docker/app.yml up -d
```

For more information refer to [Using Docker and Docker-Compose][], this page also contains information on the Docker Compose sub-generator (`jhipster docker-compose`), which is able to generate Docker configurations for one or several JHipster applications.

## Continuous Integration (optional)

To configure CI for your project, run the ci-cd sub-generator (`jhipster ci-cd`), this will let you generate configuration files for a number of Continuous Integration systems. Consult the [Setting up Continuous Integration][] page for more information.

[JHipster Homepage and latest documentation]: https://www.jhipster.tech
[JHipster 8.7.3 archive]: https://www.jhipster.tech/documentation-archive/v8.7.3
[Using JHipster in development]: https://www.jhipster.tech/documentation-archive/v8.7.3/development/
[Using Docker and Docker-Compose]: https://www.jhipster.tech/documentation-archive/v8.7.3/docker-compose
[Using JHipster in production]: https://www.jhipster.tech/documentation-archive/v8.7.3/production/
[Running tests page]: https://www.jhipster.tech/documentation-archive/v8.7.3/running-tests/
[Code quality page]: https://www.jhipster.tech/documentation-archive/v8.7.3/code-quality/
[Setting up Continuous Integration]: https://www.jhipster.tech/documentation-archive/v8.7.3/setting-up-ci/
[Node.js]: https://nodejs.org/
[NPM]: https://www.npmjs.com/
[Webpack]: https://webpack.github.io/
[BrowserSync]: https://www.browsersync.io/
[Jest]: https://facebook.github.io/jest/
[Leaflet]: https://leafletjs.com/
[DefinitelyTyped]: https://definitelytyped.org/
[Angular CLI]: https://cli.angular.io/
