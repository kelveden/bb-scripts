#!/usr/bin/env bb
(require '[babashka.curl :as curl]
         '[cheshire.core :as json])

(def ^:private result-count 30)

(defn- send-maven-central-request
  [url]
  (let [{:keys [docs numFound]} (-> (curl/get url)
                                    :body
                                    (json/parse-string true)
                                    :response)]
    (when (> numFound result-count)
      (println (format "==> WARNING: only display %s results out of a total of %s." result-count numFound))
      (println))
    docs))

(defn search
  [q]
  (-> (format "https://search.maven.org/solrsearch/select?q=%s&rows=%s&wt=json" q result-count)
      (send-maven-central-request)))

(defn get-versioned-coordinates
  [{:keys [group artifact-id]}]
  (->> (format "https://search.maven.org/solrsearch/select?q=g:%s+AND+a:%s&core=gav&rows=%s&wt=json" group artifact-id result-count)
       (send-maven-central-request)
       (map :id)))

(let [[q] *command-line-args*
      matches (->> (search q)
                   (map-indexed (fn [i {:keys [id a g]}] {:index       i
                                                          :id          id
                                                          :group       g
                                                          :artifact-id a})))]
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