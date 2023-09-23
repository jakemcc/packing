(ns packing.list 
  (:require
    [clojure.set :as set]))

(defn i [s]
  {:type :item :value s})

(defn a [s]
  {:type :action :value s})

(defn q [s]
  {:type :question :value s})

(def always #{::accessories ::bathroom ::clothes})

(def packing-lists
  {::clothes #{(a "Lookup Weather")
               (i "underwear")
               (i "socks")
               (i "shoes")
               (i "t-shirts")
               (i "nice shirts")
               (i "pants")
               (i "nice pants")
               (i "suit")
               (i "lounging clothes")
               (i "bedtime clothes")
               (i "exercise clothes")}
   
   ::work-trip #{(i "work laptop")
                 (i "work phone")
                 (i "clothes for the office")
                 (i "clothes for dinners")
                 (i "office badge")}

   ::bouldering #{(i "crash pad")
                  (i "skin care")
                  (i "nail file")
                  (i "Twin Snakes")
                  ::outdoors}

   ::sport-climbing #{(i "rope")
                      (i "quick draws")
                      (i "cleaning gear")
                      (i "rappelling gear")
                      (i "belay device")
                      (i "belay specs")
                      ::outdoors}

   ::outdoors #{(i "Bug spray")
                (i "Sunscreen")
                (i "Headlamp")}
   
   ::camping #{(i "tent")
               ::outdoors}

   ::climbing #{(q "bouldering?")
                (q "sport climbing?")
                (i "chalk")
                (a "refill chalk")
                (i "climbing shoes")
                ::outdoors}

   ::bathroom #{(i "toothbrush")
                (i "toothpaste")
                (i "floss")
                (i "allergy meds")
                (i "supplements")
                (i "shampoo")
                (i "towel")
                (i "shaving equipment / shave")}

   ::accessories #{(i "portable hangboard")
                   (i "force measuring device")
                   (i "USB-C cable")
                   (i "USB-A cable")
                   (i "travel charging block")
                   (i "Whoop")
                   (i "Whoop charger")
                   (i "Personal laptop")
                   (i "Kindle")
                   (i "Remarkable")
                   (i "Travel Keyboard")
                   (i "headphones / headset")
                   (i "ear plugs")
                   (i "sun glasses")
                   (i "hat")
                   (a "Download podcasts")
                   (a "Download videos")
                   (a "Download books")
                   (a "Lookup Weather")}})

(defn bucket-by [pred xs]
  (let [r (group-by pred xs)]
    [(get r true) (get r false)]))

(defn packing-list'
  ([lists type]
   (packing-list' lists type #{}))
  ([lists type seen-types]
   (let [[types other] (bucket-by keyword? (get lists type))
         types (set types)
         new-types (clojure.set/difference types seen-types)]
     (apply clojure.set/union
            (set other)
            (mapv #(packing-list' lists % (clojure.set/union seen-types types))
                  new-types)))))

