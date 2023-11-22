# Projekt "MyTicket.com", `Informationstechnische Projekte 5AHITN 2023 / 2024`

## Aufgabenstellung
Es ist die Managementsoftware für einen Ticketshop zu erstellen.
Ihre Software soll zur Verwaltung von Veranstaltungen und der zugehörigen Vorverkaufstickets verwendet werden.
Es soll möglich sein über einen Administratoraccount unterschiedliche Events zu verwalten (hinzufügen / lesen / aktualisieren / löschen).
Jedes Event hat ein Start- und Enddatum (inkl. Uhrzeit), einen Namen, eine Detailbeschreibung.

Für jedes Event können mehrere Ticketkategorien angelegt werden. Jede Kategorie hat ein bestimmtes Kontingent (Anzahl möglicher Karten) und einen bestimmten Ticketpreis.

> **Beispiel:** Sie erzeugen für die für Ihren Maturaball "Maturaball HTL Steyr 2024" eine Veranstaltung. Veranstaltungsstart ist am 02. März 2024 um 20:00 Uhr. 
> Es gibt drei Ticketkategorien: Kategorie A mit einem Kontingent von 500 Karten um 25€ je Ticket, Kategorie B mit einem Kontingent von 500 Karten um 22€ je Ticket und Kategorie C mit einem Kontingent von 350 Karten um 18€ je Ticket.

Über Ihren Ticketshop können sich Endkunden Tickets für Veranstaltungen kaufen.
Um Tickets für eine Veranstaltung zu erwerben, müssen sich Endkunden im Ticketshop mit einem Benutzerkonto registrieren.
Ein registrierter Benutzer kann sich anschließend über die erfassten Benutzerdaten im System anmelden und (womöglich mehrere) Tickets unterschiedlicher Kategorien und unterschiedlicher Veranstaltungen in seinen Warenkorb legen.

Im letzten Schritt (Checkout) wird der Gesamtpreis berechnet und die Zahlung getätigt.

> **Beispiel:** Der Benutzer Peter Rathgeb kauft sich für die Veranstaltung "Maturaball HTL Steyr 2024" zwei Tickets der Kategorie A und ein Ticket der Kategorie B. Zusätzlich erwirbt der Benutzer zwei Tickets der Kategorie B (zu je 12€) für die Veranstaltung "Frühjahrskonzert Schulorchester HTL Steyr".
> Der Gesamtpreis seines Warenkorbs beträgt also 2 x 25€ + 1 x 22€ + 2 x 12€ = 118€

Sollten Tickets der gewählten Kategorie in der gewählten Menge nicht mehr verfügbar sein, so es nicht möglich sein, die Tickets der gewählten Kategorie in den Warenkorb zu legen.


## Umsetzung
Erstellen Sie für den obenstehenden Sachverhalt eine SpringWeb - Anwendung. Erstellen Sie für die Entitäten jeweils eine Model - Klasse (z.B. User, Event, Ticket, ...) in denen Sie die Eigenschaften und die Beziehungen der einzelnen Klassen zueinander abbilden. Diese Model - Klassen sollen über die  entsprechenden Repositories und der JPA mit der Datenbank kommunizieren. Erstellen Sie zudem für jede Entity Klasse entsprechende Data-Transfer-Object - Klassen. Außerdem soll die Kommunikation zwischen Controllerendpunkt und Datenbank (Repository) über Service - Klassen gekapselt sein.

__Erstellen Sie für den Admin - Benutzer Endpunkte für folgende Endpunkte:__
  *  Verwaltung der Veranstaltungen (neu anlegen / bearbeiten / löschen)
  *  Verwaltung der Ticketkategorien (neu anlegen / bearbeiten / löschen)
  *  Verwaltung der Benutzer (neu anlegen / bearbeiten / löschen)

__Erstellen Sie für alle anderen Benutzer Endpunkte für folgende Aufgaben:__
  * Registrierung eines neuen Benutzers
  * Login eines bestehenden Benutzers
  * Laden der zukünftigen Veranstaltungen
  * Laden der Ticketkategorien einer Veranstaltung
  * Tickets zum Warenkorb hinzufügen
  * Checkout des Warenkorbs
  * Laden der getätigten Bestellungen (inkl. aller Infos wie z.B. 

__Schreiben Sie Testfälle (Unit - Tests) für folgende Schnittstellen:__
  * Alle Methoden des Cart - Controller (Alle Endpunkte)
  * Alle (sinnvollen) Methoden Ihrer Services
  * Alle (sinnvollen = selbstgeschriebenen) Methoden Ihrer Repositories

__Dokumentieren Sie alle Endpunkte Ihrer Anwendung (sprindoc)!__

__Testen Sie Ihre Endpunkte über eine im Projekt enthaltene requests.http - Datei.__

Befüllen Sie Ihre Anwendung mit sinnvollen Testdaten (Benutzer, Events, Ticketkategorien eines Events, Bestellungen, ...). Gerne können Sie bei Unklarheiten eigene Annahmen machen. 

## docker-compose.yml
```yml
version: '3.8'

services:
  database:
    container_name: database_myticket
    image: mysql:8.0
    command: --default-authentication-plugin=mysql_native_password --log_bin_trust_function_creators=1
    environment:
      MYSQL_ROOT_PASSWORD: rootpwd
      MYSQL_DATABASE: myticket
      MYSQL_USER: my
      MYSQL_PASSWORD: ticket
    ports:
      - '4306:3306'
    volumes:
      - ./mysql:/var/lib/mysql
```

## Endpunkte
### Registrierung
   * `POST` - Request
   * Endpunkt: `/api/v1/user/signup`
   * Erfoderliche Daten werden im Requst - Body als JSON - Objekt mitgeschickt.
     `{"email":"ratp@htl-steyr.ac.at", "password": "test", "firstname": "Peter", "lastname": "Rathgeb"}`
   * Im Erfolgsfall wird der Statuscode `201` / der Accesstoken, der für 10 Minuten gültig ist, zurückgegeben.
   * Bei Misserfolg wird der Statuscode `401` zurückgebeben.

### Login
   * `POST` - Request
   * Endpunkt: `/api/v1/user/signin`
   * Erfoderliche Daten werden im Requst - Body als JSON - Objekt mitgeschickt.
     `{"email":"ratp@htl-steyr.ac.at", "password": "test"}`
   * Im Erfolgsfall wird der Statuscode `200` / der Accesstoken, der für 10 Minuten gültig ist, zurückgegeben.
   * Bei Misserfolg wird der Statuscode `401` zurückgebeben.

### Benutzer ändern (nur für Administratoren zugänglich)
   * `PUT` - Request
   * Endpunkt: `/api/v1/user/update`
   * Erforderlicher Token wird als Bearer - Token im Autorizationheader mitgeschickt.
   * Erfoderliche Daten werden im Requst - Body als JSON - Objekt mitgeschickt.
     `{"id": 1, "firstname": "Peter", "lastname": "Rathgeb", "email": "peter@rathgeb.at"}`
   * Im Erfolgsfall wird der Statuscode `200` zurückgegeben.
   * Bei Misserfolg wird der Statuscode `400` / `401` zurückgebeben.

### Benutzer löschen (nur für Administratoren zugänglich)
   * `DELETE` - Request
   * Endpunkt: `/api/v1/user/delete/{id}`
   * Erforderlicher Token wird als Bearer - Token im Autorizationheader mitgeschickt.
   * Im Erfolgsfall wird der Statuscode `200` zurückgegeben.
   * Bei Misserfolg wird der Statuscode `400` / `401` zurückgebeben.

### Benutzerliste laden (nur für Administratoren zugänglich)
   * `GET` - Request
   * Endpunkt: `/api/v1/user/list`
   * Erforderlicher Token wird als Bearer - Token im Autorizationheader mitgeschickt.
   * Im Erfolgsfall wird der Statuscode `200` und folgende Daten zurückgegeben: `[{"id": 1, "firstname": "Peter", "lastname": "Rathgeb", "email": "ratp@htl-steyr.ac.at"}, {"id": 2, "firstname": "Max", "lastname": "Musetermann", "email": "max@mustermann.at"}, ...]`
   * Bei Misserfolg wird der Statuscode `400` / `401` zurückgebeben.

### Benutzer laden (nur für Administratoren zugänglich)
   * `GET` - Request
   * Endpunkt: `/api/v1/user/load/{id}`
   * Erforderlicher Token wird als Bearer - Token im Autorizationheader mitgeschickt.
   * Im Erfolgsfall wird der Statuscode `200` und folgende Daten zurückgegeben: `{"id": 1, "firstname": "Peter", "lastname": "Rathgeb" "email": "ratp@htl-steyr.ac.at"}`
   * Bei Misserfolg wird der Statuscode `400` / `401` zurückgebeben.

### Event erzeugen (nur für Administratoren zugänglich)
   * `POST` - Request
   * Endpunkt: `/api/v1/event/create`
   * Erforderlicher Token wird als Bearer - Token im Autorizationheader mitgeschickt.
   * Erfoderliche Daten werden im Requst - Body als JSON - Objekt mitgeschickt.
     `{"name": "Maturaball HTL Steyr 2024", "from": "02.03.2024 20:00", "to": "03.03.2024 05:00", "description": "Maturaball der Abteilungen EL, IT, ME, Y", "ticketCategories": [{"name": "A", "price": 25, "stock": 500}, {"name": "B", "price": 22, "stock": 500}, {"name": "C", "price": 18, "stock": 300}]}`
   * Im Erfolgsfall wird der Statuscode `201` zurückgegeben.
   * Bei Misserfolg wird der Statuscode `400` / `401` zurückgebeben.

### Event ändern (nur für Administratoren zugänglich)
   * `PUT` - Request
   * Endpunkt: `/api/v1/event/update`
   * Erforderlicher Token wird als Bearer - Token im Autorizationheader mitgeschickt.
   * Erfoderliche Daten werden im Requst - Body als JSON - Objekt mitgeschickt.
     `{"id": 1, "name": "Maturaball HTL Steyr 2024", "from": "02.03.2024 19:00", "to": "03.03.2024 05:00", "description": "Bester Ball der HTL Steyr", "ticketCategories": [{"id": 1, "name": "A", "price": 26, "stock": 550}, {"id": 2, "name": "B", "price": 22, "stock": 500}, {"id": 3, "name": "C", "price": 18, "stock": 300}]}`
   * Im Erfolgsfall wird der Statuscode `200` zurückgegeben.
   * Bei Misserfolg wird der Statuscode `400` / `401` zurückgebeben.

### Event löschen (nur für Administratoren zugänglich)
   * `DELETE` - Request
   * Endpunkt: `/api/v1/event/{id}`
   * Erforderlicher Token wird als Bearer - Token im Autorizationheader mitgeschickt.
   * Im Erfolgsfall wird der Statuscode `200` zurückgegeben.
   * Bei Misserfolg wird der Statuscode `400` / `401` zurückgebeben.

### Event laden
   * `GET` - Request
   * Endpunkt: `/api/v1/event/{id}`
   * Erforderlicher Token wird als Bearer - Token im Autorizationheader mitgeschickt.
   * Im Erfolgsfall wird der Statuscode `200` und folgende Daten zurückgegeben: `{"id": 1, "name": "Maturaball HTL Steyr 2024", "from": "02.03.2024 19:00", "to": "03.03.2024 05:00", "description": "Bester Ball der HTL Steyr", "ticketCategories": [{"id": 1, "name": "A", "price": 25, "stock": 500}, {"id": 2, "name": "B", "price": 22, "stock": 500}, {"id": 3, "name": "C", "price": 18, "stock": 300}]}`
   * Bei Misserfolg wird der Statuscode `400` / `401` zurückgebeben.

### Zukünftige Events laden
   * `GET` - Request
   * Endpunkt: `/api/v1/event/list`
   * Erforderlicher Token wird als Bearer - Token im Autorizationheader mitgeschickt.
   * Im Erfolgsfall wird der Statuscode `200` und folgende Daten zurückgegeben: `[{"id": 1, "name": "Maturaball HTL Steyr 2024", "from": "02.03.2024 19:00", "to": "03.03.2024 05:00", "description": "Bester Ball der HTL Steyr", "ticketCategories": [{"id": 1, "name": "A", "price": 2, "stock": 500}, {"id": 2, "name": "B", "price": 22, "stock": 500}, {"id": 3, "name": "C", "price": 18, "stock": 300}]}, {"id": 1, "name": "Frühjahrskonzert der HTL Steyr", "from": "09.04.2024 19:00", "to": "09.04.2024 21:00", "description": "Konzert des Schulorchestersr", "ticketCategories": [{"id": 4, "name": "A", "price": 12, "stock": 50}, {"id": 5, "name": "B", "price": 10, "stock": 50}, {"id": 6, "name": "C", "price": 8, "stock": 50}]}]`
   * Bei Misserfolg wird der Statuscode `400` / `401` zurückgebeben.

### Tickets zum Warenkorb hinzufügen
   * `POST` - Request
   * Endpunkt: `/api/v1/cart/add`
   * Erforderlicher Token wird als Bearer - Token im Autorizationheader mitgeschickt.
   * Erfoderliche Daten werden im Requst - Body als JSON - Objekt mitgeschickt.
     `{"id": 1, tickets: [{"id": 1, "amount": 2}, {"id": 2, "amount": 1}]}`
   * Im Erfolgsfall wird der Statuscode `201` zurückgegeben.
   * Bei Misserfolg wird der Statuscode `400` / `401` zurückgebeben.

### Warenkorb laden
   * `GET` - Request
   * Endpunkt: `/api/v1/cart/list`
   * Erforderlicher Token wird als Bearer - Token im Autorizationheader mitgeschickt.
   * Im Erfolgsfall wird der Statuscode `200` und folgende Daten zurückgegeben: `[{"id": 1, "name": "Maturaball HTL Steyr 2024", "from": "02.03.2024 19:00", "to": "03.03.2024 05:00", "description": "Bester Ball der HTL Steyr", "tickets": [{"id": 1, "name": "A", "price": 25, "amount": 2}, {"id": 2, "name": "B", "price": 22, "amount": 1}]}, {"id": 2, "name": "Frühjahrskonzert der HTL Steyr", "from": "09.04.2024 19:00", "to": "09.04.2024 21:00", "description": "Konzert des Schulorchestersr", "tickets": [{"id": 4, "name": "A", "price": 12, "amount": 2}]}]`
   * Bei Misserfolg wird der Statuscode `400` / `401` zurückgebeben.

### Tickets kaufen
   * `POST` - Request
   * Endpunkt: `/api/v1/cart/checkout`
   * Erforderlicher Token wird als Bearer - Token im Autorizationheader mitgeschickt.
   * Im Erfolgsfall wird der Statuscode `201` und der Gesamtpreis zurückgegeben: `{"price": 118}`
   * Anschließend wird eine Bestellung erzeugt und der Warenkorb für den Benutzer geleert.
   * Bei Misserfolg wird der Statuscode `400` / `401` zurückgebeben.


## Abgabe
Abgabe ist am 20.12.2023 am Ende der Stunde (um 09:40 Uhr).
Abgabe: Projektverzeichnis
Abgabeverzeichnis: `H:\Abgabe\ITP\MyTicket`

Für verspätete Abgaben gilt: Pro Tag Verspätung -> ein Notengrad schlechter.