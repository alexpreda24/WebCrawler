# WebCrawler

Pentru a extrage informatiile necesare assignmentului,am utilizat un WebCrawler,cu ajutorul caruia
parcurg paginile din fisierul sample-website.csv

Fisierul SpiderLeg.java

Functia crawl din fisierul SpiderLeg, face o cerere HTTP, verifica daca raspunde,dupa aceea pentru a
extrage eficient datele cerute voi cauta intr-un URL care contine "contact".

Daca functia crawl a fost realizata cu succes,urmatorul scop este extragerea emailurilor si telefoanelor
mobile.Pentru a le identifica pe acestea doua,am folosit 2 regex-uri. Daca cuvintele de pe o anumita pagina
indeplineau conditiile le adaugam intr-un set de stringuri specifice emailurilor sau a numerelor de telefon.
Pentru emailuri s-a utlizicat o metoda de cautare in plus.Pentru a fi siguri ca extragem toate emailurile de
pe aceasta pagina am intrat in sursa paginii si am cautat varianta unei variante de email.

Fisierul WebCrawler.java

Functia checkBusiness verifica daca website-ul apartine unei companii folosind unele caracteristice specifice
unui website de companie,precum: sectiunea "About-us","Product","Contact","Social".

Voi verifca daca un URL indeplineste cerintele necesare dupa care in functia search voi parcurge fiecare 
URL in parte, si ma voi folosi de functia searchForWord din SpiderLeg.java pentru a trece informatiile necesare
in fisierul output.csv
