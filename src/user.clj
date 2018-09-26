;(require '[datomic.client.api :as d])
;
;(def cfg {:server-type :ion
;          :region "us-west-2" ;; e.g. us-east-1
;          :creds-profile "bert"
;          :system "elephant"
;          :endpoint "http://entry.elephant.us-west-2.datomic.net:8182/"
;          :proxy-port 8182})
;
;(def client (d/client cfg))
