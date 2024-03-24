#!/usr/bin/env bb
(require '[babashka.curl :as curl]
         '[cheshire.core :as json])

(def ^:private result-count 24)

(defn search
  [q]
  (let [{:keys [results count]} (-> (curl/get (format "https://clojars.org/search?q=%s&format=json" q))
                                    :body
                                    (json/parse-string true))]
    (when (> count result-count)
      (println (format "==> WARNING: only display %s results out of a total of %s." result-count count))
      (println))
    results))

(defn get-versioned-coordinates
  [{:keys [group artifact-id]}]
  (let [{:keys [recent_versions]} (-> (curl/get (format "https://clojars.org/api/artifacts/%s/%s" group artifact-id))
                                      :body
                                      (json/parse-string true))]
    (->> recent_versions
         (map #(format "%s:%s:%s" group artifact-id (:version %))))))

(let [[q] *command-line-args*
      matches (->> (search q)
                   (map-indexed (fn [i {:keys [group_name jar_name]}]
                                  {:index       i
                                   :id          (format "%s:%s" group_name jar_name)
                                   :group       group_name
                                   :artifact-id jar_name})))]
  ; Print options
  (doseq [{:keys [id index]} matches]
    (-> (format "%s) %s" (inc index) id)
        (println)))

  (if-not (empty? matches)
    (do
      (println)
      (println "==> Please select from the options above.")
      (println)

      (let [selection (read-line)
            ids       (->> (Integer/parseInt selection)
                           (dec)
                           (nth matches)
                           (get-versioned-coordinates))]
        (doseq [id ids] (println id))))
    (println "No results found.")))