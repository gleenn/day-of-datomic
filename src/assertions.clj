(require '[datomic.client.api :as d])

(def cfg {:server-type   :ion
          :region        "us-west-2"                        ;; e.g. us-east-1
          :creds-profile "bert"
          :system        "elephant"
          :endpoint      "http://entry.elephant.us-west-2.datomic.net:8182/"
          :proxy-port    8182})
(def client (d/client cfg))
(d/create-database client {:db-name "movies"})
(def conn (d/connect client {:db-name "movies"}))

(defn make-idents
  [x]
  (mapv #(hash-map :db/ident %) x))

(def sizes [:small :medium :large :xlarge])
(d/transact conn {:tx-data (make-idents sizes)})

(def types [:shirt :pants :dress :hat])
(def colors [:red :green :blue :yellow])
(d/transact conn {:tx-data (make-idents types)})
(d/transact conn {:tx-data (make-idents colors)})

(def schema-1
  [{:db/ident :inv/sku
    :db/valueType :db.type/string
    :db/unique :db.unique/identity
    :db/cardinality :db.cardinality/one}
   {:db/ident :inv/color
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one}
   {:db/ident :inv/size
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one}
   {:db/ident :inv/type
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one}])
(d/transact conn {:tx-data schema-1})

(def sample-data
  (->> (for [color colors size sizes type types]
         {:inv/color color
          :inv/size size
          :inv/type type})
       (map-indexed
         (fn [idx map]
           (assoc map :inv/sku (str "SKU-" idx))))
       vec))

(d/transact conn {:tx-data sample-data})

(def db (d/db conn))
(d/pull
  db
  [{:inv/color [:db/ident]}
   {:inv/size [:db/ident]}
   {:inv/type [:db/ident]}]
  [:inv/sku "SKU-44"])

(d/q
  '[:find ?sku
    :where
    [?e :inv/sku "SKU-42"]
    [?e :inv/color ?color]
    [?e2 :inv/color ?color]
    [?e :inv/color ?size]
    []
    [?e2 :inv/sku ?sku]]
  db)

(def order-schema
  [{:db/ident :order/items
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/many
    :db/isComponent true}
   {:db/ident :item/id
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one}
   {:db/ident :item/count
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one}])
(d/transact conn {:tx-data order-schema})


(def add-order
  {:order/items
   [{:item/id [:inv/sku "SKU-25"]
     :item/count 10}
    {:item/id [:inv/sku "SKU-26"]
     :item/count 20}]})


(d/transact conn {:tx-data [add-order]})
