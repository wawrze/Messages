package com.wawra.posts.logic

import com.wawra.posts.database.daos.PostDao
import com.wawra.posts.database.entities.Post
import com.wawra.posts.logic.models.PostStatus
import com.wawra.posts.network.ApiInterface
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val postDao: PostDao,
    private val api: ApiInterface
) {

    fun getPosts(): Observable<List<Post>> = Observable.concatArray(
        postDao.getAll().toObservable(),
        getPostsFromApi().toObservable()
    )

    fun getPostById(postId: Long) = postDao.getById(postId)
        .onErrorReturn { Post(0L, 0L) }

    fun updatePost(
        postId: Long,
        newTitle: String,
        newDescription: String,
        newIconUrl: String
    ) = postDao.update(postId, newTitle, newDescription, newIconUrl)
        .onErrorReturn { 0 }
        .map { it > 0 }

    // TODO: unit test
    fun createPost(title: String, description: String, iconUrl: String) = Single.fromCallable {
        Post(0L, 0L, title, description, iconUrl, PostStatus.CHANGED.value)
    }.flatMap { postDao.insertPost(it) }

    fun deletePost(postId: Long) = postDao.deleteById(postId)
        .onErrorReturn { 0 }
        .map { it > 0 }

    fun getDeletedPosts() = postDao.getAllDeleted()
        .onErrorReturn { listOf() }

    fun restoreDeletedPost(postId: Long) = postDao.restoreDeletedById(postId)
        .onErrorReturn { 0 }
        .map { it > 0 }

    private fun getPostsFromApi() = api.getPosts()
        .map { response ->
            val newPosts = response.posts.map {
                Post(
                    0L,
                    it.id,
                    it.title,
                    it.description,
                    it.icon,
                    PostStatus.UNCHANGED.value
                )
            }
            newPosts as ArrayList   // TODO: for testing, remove
            newPosts.addAll(
                listOf(
                    Post(
                        0L,
                        0L,
                        "Współczynnik R w Polsce najwyższy od marca. Wzrósł w 13 województwach",
                        "Wraz ze wzrostem liczby zachorowań na COVID-19 wzrósł w Polsce współczynnik reprodukcji wirusa R - obecnie wynosi 1,36. Jest najwyższy od marca. W każdym z województw także przekracza wartość 1, co oznacza, że epidemia nie wyhamowuje.",
                        "https://r.dcs.redcdn.pl/scale/o2/tvn/web-content/m/p187/i/4c144c47ecba6f8318128703ca9e2601/eaef229a-8233-4e0b-be5c-aa2b2dcfe59a.jpg?srcmode=0&srcx=1/1&srcy=0/1&srcw=99/100&srch=99/100&dstw=500&dsth=281&quality=80&type=1"
                    ),
                    Post(
                        0L,
                        0L,
                        "Wielka Brytania przywraca kwarantannę dla przyjeżdżających Polaków",
                        "Osoby, które przyjeżdżają do Wielkiej Brytanii z Polski, będą musiały poddać się 14-dniowej obowiązkowej samoizolacji. Zasady wchodzą w życie od godziny 4.00 w sobotę (5.00 czasu polskiego) - informuje portal stacji BBC.\n" +
                                "\n" +
                                "Na liście dotyczącej obowiązkowej kwarantanny znalazła się również Turcja oraz Holandia Karaibska - podał Reuters. Brytyjskie ministerstwo spraw zagranicznych zmieniło również rekomendacje dla podróżnych w przypadku Polski i Turcji i obecnie zaleca, by unikać podróży do tych dwóch państw, o ile nie jest to niezbędne. W przypadku Bonaire, St. Eustatius i Saby MSZ odradzało podróże już wcześniej.\n" +
                                "\n" +
                                "Wzrost zakażeń\n" +
                                "Jako przyczynę dodania obu państw na listę podano wzrost liczby zakażeń na 100 tys. mieszkańców. Na listę trafiają kraje, w których liczba zakażeń wynosi ponad 20. Według BBC w Polsce współczynnik ten wyniósł 25,9.\n" +
                                "\n" +
                                "\"Dane z Polski pokazują 66-procentowy wzrost liczby zakażeń tygodniowo na 100 tysięcy osób - z 14,7 w dniu 23 września do 24,4 w dniu 30 września. Zarazem odsetek pozytywnych wyników testów w Polsce wzrósł prawie dwukrotnie w ciągu tygodnia, z 3,9 proc. do 5,8 procent\" - wyjaśniło ministerstwo transportu.\n" +
                                "\n" +
                                "Osoby przyjeżdzające z Polski były zwolnione z kwarantanny od 10 lipca, kiedy obowiązywać zaczęła pierwsza lista krajów uznanych za bezpieczne. Stopniowo, w miarę nawrotu epidemii, brytyjski rząd skreślał z niej kolejne kraje, w tym już wcześniej większość państw członkowskich Unii Europejskiej, choć kilka krajów, głównie pozaeuropejskich, do listy dopisano.\n" +
                                "\n" +
                                "W zeszłym tygodniu brytyjskie media podawały, że w związku z kurczącą się liczbą państw, po przyjeździe z których nie obowiązuje kwarantanna, rośnie zainteresowanie wyjazdami do Polski, Turcji i Włoch. Zarazem jednak wskazywały, że Polska może być następnym krajem skreślonym z listy bezpiecznych.\n" +
                                "\n" +
                                "Polacy są największą mniejszością narodową w Zjednoczonym Królestwie - dodaje BBC.\n" +
                                "\n" +
                                "Jak podaje stacja, osoby, które nie poddadzą się samoizolacji, muszą liczyć się z surową karą finansową, która może sięgać nawet 10 tys. funtów (około 49 tys. zł). Poprzedni limit grzywny wynosił 3200 funtów (niecałe 16 tys. zł).",
                        "https://tvn24.pl/biznes/najnowsze/cdn-zdjecie-aqgvyr-obowiazkowe-testy-lotnisko-neapol-loty-koronawirus-turystyka-4708327/alternates/LANDSCAPE_840"
                    ),
                    Post(
                        0L,
                        0L,
                        "Słowacja: Ogromny przyrost zachorowań na Covid-19. Od dziś stan wyjątkowy",
                        "Premier Słowacji Igor Matovicz poinformował na Facebooku, że w kraju zanotowano w środę dwa rekordy: nowych zakażeń, których jest 797, oraz testów, których wykonano 9170. Obie liczby są najwyższe od początku pandemii.",
                        "https://i.iplsc.com/bratyslawa-stolica-slowacji/000AJMUT7R8WUI1M-C123-F4.webp"
                    ),
                    Post(
                        0L,
                        0L,
                        "Niemiecka eurodeputowana nie mówiła o \"głodzeniu Polski\". Pomyłka radia wywołała reakcję polskiego rządu",
                        "Rzecznik rządu Piotr Müller i szef KPRM Michał Dworczyk domagają się przeprosin od niemieckiej eurodeptuowanej Katariny Barley, która miała mówić o \"finansowym zagłodzeniu Polski\". Tymczasem z zapisu rozmowy na portalu niemieckiego radia Deutschlandfunk wynika, iż wiceszefowa Parlamentu Europejskiego nie mówiła o Polsce, a o Viktorze Orbánie. Taką informację otrzymaliśmy również od współpracowników Barley.",
                        "https://bi.im-g.pl/im/1b/23/19/z26360859IH,Katarina-Barley.jpg"
                    ),
                    Post(
                        0L,
                        0L,
                        "Ciężki przebieg COVID-19. Naukowcy: To może być wina genów odziedziczonych po neandertalczyku",
                        "Geny, które współczesny człowiek odziedziczył po neandertalczyku, mogą być odpowiedzialne za niektóre przypadki ciężkiego przebiegu choroby COVID-19, wywoływanej przez wirusa SARS-CoV-2 - uważają naukowcy z Instytutu Antropologii Ewolucyjnej im. Maxa Plancka.\nGrupa ekspertów w zakresie badań nad DNA neandertalczyków poddała analizie fragmenty DNA, które wiązane są z ciężkim przebiegiem choroby wywoływanej przez koronawirusa u niektórych pacjentów. Specjaliści  porównali je z kodem genetycznym naszych przodków, który odziedziczyli współcześnie żyjący Europejczycy i Azjaci - informuje CNN.\n" +
                                "\n" +
                                "Zespół z Niemiec połączył pewne wariacje fragmentów DNA w trzecim chromosomie z ryzykiem poważnego przebiegu COVID-19.\n" +
                                "\n" +
                                "\"Tutaj pokazujemy, że ryzyko jest spowodowane segmentem genomu, który jest dziedziczony po neandertalczykach i jest przenoszony przez około 50 proc. populacji w Azji Południowej i około 16 proc. populacji dzisiejszej Europy\" - informują Svante Paabo i Hugo Zeberg z Instytutu Antropologii Ewolucyjnej im. Maxa Plancka w pracy naukowej, która ma się wkrótce ukazać na łamach prestiżowego periodyku \"Nature\".\n" +
                                "\n" +
                                "- Okazuje się, że ten wariant genu został odziedziczony przez człowieka współczesnego od neandertalczyków, kiedy mieszał się z nimi około 60 tys. lat temu. Dzisiaj w przypadku osób, które odziedziczyły ten gen, bardziej prawdopodobne jest to, że będą musiały oddychać z pomocą maszyn w przypadku zakażenia koronawirusem SARS-CoV-2 - poinformował Hugo Zeberg w oświadczeniu cytowanym przez CNN.\n" +
                                "\n" +
                                "To wina neandertalczyka\n" +
                                "Zespół badawczy znalazł zbliżone warianty genów w liczącym 50 tys. lat szkielecie neandertalczyka, odkrytym w Chorwacji oraz w kilku szkieletach neandertalskich znalezionych na Syberii. Jak pokazują badania genetyczne, człowiek współczesny łączył się w pary z neandertalczykami oraz pokrewnym gatunkiem, znanym pod nazwą denisowian. Co warto podkreślić, około dwóch procent DNA współczesnych ludzi z Europy i Azji pochodzi właśnie od neandertalczyków.\n" +
                                "\n" +
                                "\"Obecnie nie wiadomo, jaka cecha pochodzenia neandertalskiego stwarza ryzyko ciężkiego przebiegu COVID-19 i czy skutki posiadania takiej cechy są specyficzne dla SARS-CoV-2, innych koronawirusów, czy innych patogenów\" - napisali naukowcy, podkreślając, że mimo to jasne jest, iż ten przepływ genów od neandertalczyka ma tragiczne konsekwencje w kontekście obecnie szalejącej pandemii. ",
                        "https://i.iplsc.com/rekonstrukcja-wygladu-neandertalki/000AJMRNSO016SJ8-C122-F4.jpg"
                    )
                )
            )
            newPosts
        }
        .flatMap { postDao.insertPostsFromRemote(it) }
        .onErrorReturn { listOf() }
        .flatMap { postDao.getAll() }

}