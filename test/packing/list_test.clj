(ns packing.list-test
  (:require [clojure.test :refer [deftest is testing]]
            [packing.list :as pl]))

(deftest returns-items
  (is (= #{(pl/i "shoes")}
         (pl/packing-list' {:pl/climbing #{(pl/i "shoes")}}
                           :pl/climbing))))
(deftest recursive-item-lists)
  (is (= #{(pl/i "shoes")
           (pl/i "tent")}
       (pl/packing-list' {:pl/climbing #{(pl/i "shoes")
                                         :pl/outdoors}
                          :pl/outdoors #{(pl/i "tent")}}
                         :pl/climbing)))


