# Inventura

## Početni tim
 - Parić Muhamed
 - Pejčinović Dario
 - Popović Ahmed
 - Puce Amina
 - Ramić Benjamin
 - Sakić Ajla

## Tim nakon zamjene
 - Mujačić Emir
 - Mujčinović Amra
 - Mulić Vedad
 - Muratović Sajra
 - Muratspahić Ena
 - Musić Sanil
 - Slinić Faruk

## Deployment
Aplikaciji se može pristupiti na http://ws-inventura.surge.sh. Backend se izvršava na Heroku instanci kojoj se može pristupiti na https://infinite-hollows-98435.herokuapp.com. Obzirom da se koristi besplatan servis, može se desiti da backend u trenutku prvom pokretanja aplikacije bude pauziran. U tom slučaju je potrebno pričekati 1 minutu i ponovo pokrenuti aplikaciju.

## Upute
Za pokretanje frontend-a, neophodno je imati instaliran NPM (skr. *Node Package Manager*). Backend se može pokrenuti ručno (izvršenjem odgovarajuće komande u konzoli), korištenjem Eclipse IDE ili Intellij IDEA. U repozitoriju se nalazi SQL dump tabele koja sadrži minimalne testne podatke.

Neophodno je imati ispravnu internet vezu da bi se učitale sve biblioteke na frontendu.

 1. Frontend se nalazi u *frontend/* direktoriju i pokreće se komandom *npm start* i biće dostupan na adresi *http://localhost:4200*.
 2. Backend se pokreće na jedan od ranije opisanih načina. REST API će biti dostupan na adresi *http://localhost:8080*.
 3. Ukoliko se koriste uključeni testni podaci, za prijavu se može koristi lozinka *12345678* i jedno od korisničkih imena: *admin*, *finance*, *auditteam*. Svaki od korisničkih računa ima različite privilegije.
