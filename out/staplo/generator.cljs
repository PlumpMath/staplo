(ns staplo.generator
  (:require
    [staplo.operations :as operations]))

(defn rand2 [start end]
  (+ start (rand-int (inc (- end start)))))

(defn rand-interval [{:keys [start end]}]
  (rand2 start end))

; clojure probably has this already
(defn accumulate [fun base times]
  (reduce
    #(fun %)
    base
    (range 0 times)))


(defn generate-string [interval]
  (let [char-set ["a" "b" "c"]
        length (rand-interval interval)]
    (accumulate
      #(str % (rand-nth char-set))
      ""
      length)))

(def generate-number rand-interval)

(defn generate-start [type length]
  ((if (= type "strings")
     generate-string
     generate-number) length))


(defn generate-ops [start-text steps operations]
  (letfn [
    (step [text]
      (let [op-type (:type operations)
            op-name (rand-nth (:list operations))
            op (get (get operations/operations op-type) op-name)]
        (op text)))]
    (accumulate step start-text steps)))

(defn generate-challenge [config]
  (let [start (generate-start (:type (:operations config)) (:start-length config))
        steps (rand-interval (:steps config))
        target (generate-ops start steps (:operations config))]
    {:start start
     :target target}))