(ns packing.list
  (:require
   [clojure.set :as set]
   [reagent.core :as r]
   [reagent.dom :as rdom]))

(defn i [s]
  {:type :item :value s})

(defn a [s]
  {:type :action :value s})

(defn q [s yes-answer]
  {:type :question :value s :yes yes-answer})

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

   ::climbing #{(q "bouldering?" ::bouldering)
                (q "sport climbing?" ::sport-climbing)
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

(def state (r/atom {:trip-types #{}}))

(defn toggle-trip-type [type]
  (swap! state update :trip-types (fn [s](if (contains? s type)
                                           (disj s type)
                                           (conj s type)))))

(defn trip-selected [type]
  (contains? (:trip-types @state) type))

(defn trip-types []
  (:trip-types @state))

(defn my-component []
  [:div
   [:div#trip-types
    (for [type (keys packing-lists)]
      ^{:key type} [:div
                    [:input {:type "checkbox"
                             :checked (trip-selected type)
                             :on-change #(toggle-trip-type type)}]
                    [:label type]])]
   [:div#list
    (let [items (reduce clojure.set/union (mapv (partial packing-list' packing-lists)
                                                (trip-types)))]
      [:ul (for [i (sort-by :type items)]
             ^{:key i} [:div
                        [:input {:type "checkbox"}]
                        [:label (when (= :action (:type i))
                                  "TODO: ")
                         (:value i)]])])]])

(rdom/render [my-component] (.getElementById js/document "app"))