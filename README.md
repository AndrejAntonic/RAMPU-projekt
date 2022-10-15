# Inicijalne upute za prijavu projekta iz Razvoja aplikacija za mobilne i pametne uređaje

Poštovane kolegice i kolege, 

čestitamo vam jer ste uspješno prijavili svoj projektni tim na kolegiju Razvoj aplikacija za mobilne i pametne uređaje, te je za vas automatski kreiran repozitorij koji ćete koristiti za verzioniranje vašega koda i za jednostavno dokumentiranje istoga.

Ovaj dokument (README.md) predstavlja **osobnu iskaznicu vašeg projekta**. Vaš prvi zadatak je **prijaviti vlastiti projektni prijedlog** na način da ćete prijavu vašeg projekta, sukladno uputama danim u ovom tekstu, napisati upravo u ovaj dokument, umjesto ovoga teksta.

Za upute o sintaksi koju možete koristiti u ovom dokumentu i kod pisanje vaše projektne dokumentacije pogledajte [ovaj link](https://guides.github.com/features/mastering-markdown/).
Sav programski kod potrebno je verzionirati u glavnoj **master** grani i **obvezno** smjestiti u mapu Software. Sve artefakte (npr. slike) koje ćete koristiti u vašoj dokumentaciju obvezno verzionirati u posebnoj grani koja je već kreirana i koja se naziva **master-docs** i smjestiti u mapu Documentation.

Nakon vaše prijave bit će vam dodijeljen mentor s kojim ćete tijekom semestra raditi na ovom projektu. Mentor će vam slati povratne informacije kroz sekciju Discussions također dostupnu na GitHubu vašeg projekta. A sada, vrijeme je da prijavite vaš projekt. Za prijavu vašeg projektnog prijedloga molimo vas koristite **predložak** koji je naveden u nastavku, a započnite tako da kliknete na *olovku* u desnom gornjem kutu ovoga dokumenta :) 

# MyFitness

## Projektni tim
(svi članovi tima moraju biti iz iste seminarske grupe)

Ime i prezime | E-mail adresa (FOI) | JMBAG | Github korisničko ime | Seminarska grupa
------------  | ------------------- | ----- | --------------------- | ----------------
Tim Juić | tjuic20@student.foi.hr | 0016147091 | tjuic20-foi | G01
Andrej Antonić | aantonic20@student.foi.hr | 0016149694 | aantonic20 | G01
Juraj Gaši | jgasi20@student.foi.hr | 0016151946 | jgasi | G01
Matija Kljaić | mkljaic20@student.foi.hr | 0016147637 | mkljaic20 | G01

## Opis domene
Umjesto ovih uputa opišite domenu ili problem koji pokrivate vašim projektom. Domena može biti proizvoljna, ali obratite pozornost da sukladno ishodima učenja, domena omogući primjenu zahtijevanih koncepata kako je to navedeno u sljedećem poglavlju. Priložite odgovarajuće skice gdje je to prikladno.

## Specifikacija projekta
Umjesto ovih uputa opišite zahtjeve za funkcionalnošću mobilne aplikacije ili aplikacije za pametne uređaje. Pobrojite osnovne funkcionalnosti i za svaku naznačite ime odgovornog člana tima. Opišite osnovnu buduću arhitekturu programskog proizvoda. Obratite pozornost da mobilne aplikacije često zahtijevaju pozadinske servise. Također uzmite u obzir da bi svaki član tima trebao biti odgovoran za otprilike 3 funkcionalnosti, te da bi opterećenje članova tima trebalo biti ujednačeno. Priložite odgovarajuće dijagrame i skice gdje je to prikladno. Funkcionalnosti sustava bobrojite u tablici ispod koristeći predložak koji slijedi:

Oznaka | Naziv | Kratki opis | Odgovorni član tima
------ | ----- | ----------- | -------------------
F01 | Registracija | Sustav će omogućiti registraciju korisnika što čime će aplikacija moći personalizirati njegov doživljaj i korisničko iskustvo | ...
F02 | Prijava u aplikaciju | Sustav će omogućiti da samo prijavljeni korisnici imaju pristup podacima. Korisnik će se prijavljivati podacima koje je unio tijekom registracije u aplikaciju | ...
F03 | Unos tjelesnih podataka i fitness planova | Nakon registracije, sustav će tražiti unos podataka kao što su tjelesna težina, visina, tjelesna aktivnost te fitness ciljevi | ...
F04 | Pregled postojećih fitness vježbi | Sustav će omogućiti pregled velikog broja fitness vježbi koje postoje, za svaku od kojih će se prikazivati naslov, slika ili poveznica na video te opis vježbe | ...
F05 | Dodavanje novih fitness vježbi  | Sustav će omogućiti kreiranje dodanih fitness vježbi koje ne postoje u bazi. Novododane vježbe biti će vidljive samo korisniku koji ih je kreirao | ...
F06 | Ručno kreiranje vlastitog plana treninga | Korisnik će moći ručno izraditi vlastiti tjedni plan treninga dodavajući postojeće fitness vježbe u aplikaciji ili kreiranjem vlastite/nove vježbe | ...
F07 | Automatsko generiranje personaliziranog plana treninga  | Aplikacija će imati i mogućnost automatskog generiranja plana treninga za korisnika baziranog na njegovim fitness ciljevima / tjelesnoj težini ili preferencijama | ...
F08 | Kalkulator Bazalne stope metabolizma | Aplikacija će imati mogućnost računanja bazalne stope metabolizma (Potrebnog unosa kalorija s obzirom na visinu, težinu, tjelesnu aktivnost i fitness ciljeve) koje korisnik unese | ...
F09 | Unos napravljenih vjezbi na treningu | Sustav će omogućiti korisniku unos napravljenih vježbi na određenom treningu, zajedno sa brojem serija, ponavljanja te utega je radio tu vježbu. Time će sustav moći pratiti korisnikov napredak kroz vrijeme. | ...
F10 | Izvjestaj o napretku | Aplikacija ce generirati izvjestaj koji prikazuje napredak korisnika oko tezine, duzini treninga, ucestalosti treninga itd u nekom vremenskom razdoblju | ...
F11 | Uređivanje profila | Sustav će omogućiti korisniku uređivanje profila što će uključivati izmjenu korisničkog imena, lozinke ili profil slike | ...

## Tehnologije i oprema
Umjesto ovih uputa jasno popišite sve tehnologije, alate i opremu koju ćete koristiti pri implementaciji vašeg rješenja. Vaše rješenje može biti implementirano u bilo kojoj tehnologiji za razvoj mobilnih aplikacija ili aplikacija za pametne uređaje osim u hibridnim web tehnologijama kao što su React Native ili HTML+CSS+JS. Tehnologije koje ćete koristiti bi trebale biti javno dostupne, a ako ih ne budemo obrađivali na vježbama u vašoj dokumentaciji ćete morati navesti način preuzimanja, instaliranja i korištenja onih tehnologija koje su neopbodne kako bi se vaš programski proizvod preveo i pokrenuo. Pazite da svi alati koje ćete koristiti moraju imati odgovarajuću licencu. Što se tiče zahtjeva nastavnika, obvezno je koristiti git i GitHub za verzioniranje programskog koda, GitHub Wiki za pisanje jednostavne dokumentacije sukladno uputama mentora, a projektne zadatke je potrebno planirati i pratiti u alatu GitHub projects.

## Baza podataka i web server
Nastavnici vam mogu pripremiti MySQL bazu podataka i web server na kojem možete postaviti jednostavne php web servise. Ako želite da vam pripremimo ove sustave obavezno to navedite umjesto ovog teksta s napomenom "Trebamo bazu podataka i pristup serveru za PHP skripte". Alternativno, možete koristiti bilo koji online dostupan sustav kao i studentske licence na pojedinim platformama kao što su Heroku ili Azure.

## .gitignore
Uzmite u obzir da je u mapi Software .gitignore konfiguriran za nekoliko tehnologija, ali samo ako će projekti biti smješteni direktno u mapu Software ali ne i u neku pod mapu. Nakon odabira konačne tehnologije i projekta obavezno dopunite/premjestite gitignore kako bi vaš projekt zadovoljavao kriterije koji su opisani u ReadMe.md dokumentu dostupnom u mapi Software.
