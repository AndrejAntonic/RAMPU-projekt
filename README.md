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

Aplikacija će korisnicima omogućiti unos tjelesnih podataka. Aplikacija će generirati plan treninga i omogućiti korisniku unošenje vlastitih vježbi. Korisnik će moći pratit svoj napredak kroz razne grafove koji će prikazivati promjene u tjelesnoj težini i kilažu koju korisnici dižu prilikom odlaska na trening.
## Specifikacija projekta

Oznaka | Naziv | Kratki opis | Odgovorni član tima
------ | ----- | ----------- | -------------------
F01 | Registracija | Sustav će omogućiti registraciju korisnika što čime će aplikacija moći personalizirati njegov doživljaj i korisničko iskustvo | Tim Juić
F02 | Prijava u aplikaciju | Sustav će omogućiti da samo prijavljeni korisnici imaju pristup podacima. Korisnik će se prijavljivati podacima koje je unio tijekom registracije u aplikaciju | Andrej Antonić
F03 | Unos tjelesnih podataka | Nakon registracije, sustav će tražiti unos podataka kao što su tjelesna težina, visina, tjelesna aktivnost, godine i spol. | Juraj Gaši
F04 | Pregled postojećih fitness vježbi | Sustav će omogućiti pregled velikog broja fitness vježbi koje postoje, za svaku od kojih će se prikazivati naslov, slika ili poveznica na video te opis vježbe | Matija Kljaić
F05 | Dodavanje novih fitness vježbi  | Sustav će omogućiti kreiranje dodanih fitness vježbi koje ne postoje u bazi. Novododane vježbe biti će vidljive samo korisniku koji ih je kreirao | Matija Kljaić
F06 | Ručno kreiranje vlastitog plana treninga | Korisnik će moći ručno izraditi vlastiti tjedni plan treninga dodavajući postojeće fitness vježbe u aplikaciji | Juraj Gaši
F07 | Automatsko generiranje personaliziranog plana treninga  | Aplikacija će imati i mogućnost automatskog generiranja plana treninga za korisnika baziranog na njegovim fitness ciljevima / tjelesnoj težini ili preferencijama | Andrej Antonić
F08 | Kalkulator Bazalne stope metabolizma | Aplikacija će imati mogućnost računanja bazalne stope metabolizma (Potrebnog unosa kalorija s obzirom na visinu, težinu, tjelesnu aktivnost i fitness ciljeve) koje korisnik unese | Juraj Gaši
F09 | Unos napravljenih vježbi na treningu | Sustav će omogućiti korisniku unos napravljenih vježbi na određenom treningu, zajedno sa brojem serija, ponavljanja te utega s kojima je radio tu vježbu. Time će sustav moći pratiti korisnikov napredak kroz vrijeme. | Tim Juić
F10 | Izvještaj o napretku | Aplikacija će generirati izvještaj koji prikazuje napredak korisnika oko težine, dužini treninga, ucčestalosti treninga itd. u nekom vremenskom razdoblju | Tim Juić
F11 | Uređivanje profila | Sustav će omogućiti korisniku uređivanje profila što će uključivati izmjenu korisničkog imena, lozinke ili profil slike | Matija Kljaić
F12 | Podsjetnik (notifikacija) za obavljanje treninga | Sustav će imati mogućnost obavijestiti korisnika o nadolazećem treningu | Andrej Antonić

## Tehnologije i oprema
Pri implementaciji programskog proizvoda primarno će biti korištena razvojna okolina Android Studio te programski jezik Kotlin. Za verzioniranje programskog koda koristit će se git i Github

## Baza podataka i web server
Trebamo bazu podataka i pristup serveru za PHP skripte

## .gitignore
Uzmite u obzir da je u mapi Software .gitignore konfiguriran za nekoliko tehnologija, ali samo ako će projekti biti smješteni direktno u mapu Software ali ne i u neku pod mapu. Nakon odabira konačne tehnologije i projekta obavezno dopunite/premjestite gitignore kako bi vaš projekt zadovoljavao kriterije koji su opisani u ReadMe.md dokumentu dostupnom u mapi Software.
