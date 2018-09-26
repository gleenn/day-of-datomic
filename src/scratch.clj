(require '[datomic.client.api :as d])

(def cfg {:server-type   :ion
          :region        "us-west-2"                        ;; e.g. us-east-1
          :creds-profile "bert"
          :system        "elephant"
          :endpoint      "http://entry.elephant.us-west-2.datomic.net:8182/"
          :proxy-port    8182})

(def client (d/client cfg))


;(d/create-database client {:db-name "movies"})
;;;;;;(d/delete-database client {:db-name "movies"})

(def conn (d/connect client {:db-name "movies"}))

;(def movie-schema [{:db/ident :movie/title
;                    :db/valueType :db.type/string
;                    :db/cardinality :db.cardinality/one
;                    :db/doc "The title of the movie"}
;
;                   {:db/ident :movie/genre
;                    :db/valueType :db.type/string
;                    :db/cardinality :db.cardinality/one
;                    :db/doc "The genre of the movie"}
;
;                   {:db/ident :movie/release-year
;                    :db/valueType :db.type/long
;                    :db/cardinality :db.cardinality/one
;                    :db/doc "The year the movie was released in theaters"}])
;(d/transact conn {:tx-data movie-schema})


;(def first-movies [{:movie/title "The Goonies"
;                    :movie/genre "action/adventure"
;                    :movie/release-year 1985}
;                   {:movie/title "Commando"
;                    :movie/genre "action/adventure"
;                    :movie/release-year 1985}
;                   {:movie/title "Repo Man"
;                    :movie/genre "punk dystopia"
;                    :movie/release-year 1984}])
;(d/transact conn {:tx-data first-movies})

(def db (d/db conn))
(def all-titles-q '[:find ?movie-title
                    :where [_ :movie/title ?movie-title]])

(d/q all-titles-q db)


(->> db
     (d/q '[:find ?movie-genre
            :where [_ :movie/genre ?movie-genre]]))


(d/transact conn {:tx-data
                  [{:movie/title        "Happy Gilmore"
                    :movie/genre        "comedy"
                    :movie/release-year 1990}]})


(d/create-database client {:db-name "ud"})
(def conn (d/connect client {:db-name "ud"}))
