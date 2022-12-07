# WebCrawler

Pentru a extrage informatiile necesare assignmentului,am utilizat un WebCrawler,cu ajutorul caruia
parcurg paginile din fisierul sample-website.csv

Fisierul SpiderLeg.java

Functia crawl din fisierul SpiderLeg, face o cerere HTTP, dupa care,verifica daca raspunde,dupa care
aduna toate link-urile paginii respective.

Daca functia crawl a fost realizata cu succes,urmatorul scop este extragerea emailurilor si telefoanelor
mobile.Pentru a le identifica pe acestea doua,am folosit 2 regex-uri. Daca cuvintele de pe o anumita pagina
indeplineau conditiile le adaugam intr-un set de stringuri specifice emailurilor sau a numerelor de telefon.

Fisierul WebCrawler.java

Voi verifca daca un URL indeplineste cerintele necesare dupa care in functia search voi parcurge fiecare 
URL in parte, si ma voi folosi de functia searchForWord din SpiderLeg.java pentru a trece informatiile necesare
in fisierul output.csv
