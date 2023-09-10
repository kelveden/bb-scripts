#!/usr/bin/env bb
(require '[clojure.string :refer [ends-with?]]
         '[clojure.pprint :refer [pprint]])
(import '[java.time Instant LocalDate ZoneOffset ZonedDateTime LocalDateTime])

(def ^:private millis-per-day (* 60 60 24 1000))

(defn to-instant
  "Attempts to convert the given x to a date/time. If only a date is specified then 00:00 on that date is
  implied as the time."
  [x]
  (cond
    (instance? Instant x)
    x

    (nil? x)
    nil

    (instance? LocalDate x)
    (-> (.atStartOfDay x)
        (.atZone ZoneOffset/UTC)
        (.toInstant))

    (string? x)
    (try
      (-> (ZonedDateTime/parse x)
          (.toInstant))
      (catch Exception e
        (if (ends-with? x "T00:00Z")
          (throw e)
          (to-instant (str x "T00:00Z")))))

    ; Epoch days
    (< x 100000)
    (Instant/ofEpochMilli (* x millis-per-day))

    ; Epoch seconds
    (<= 100000 x 10000000000)
    (Instant/ofEpochSecond x)

    :else
    ; Epoch millis
    (Instant/ofEpochMilli x)))

(defn to-instant-string
  [x]
  (some-> x to-instant str))

(defn to-local-date-time-string
  [x]
  (when-let [i (to-instant x)]
    (-> i
        (LocalDateTime/ofInstant (ZoneOffset/systemDefault))
        (str))))

(defn to-epoch-millis
  "Attempts to convert the given x to a date/time and coerce to millis since epoch. If only a date is specified then
  00:00 on that date is implied as the time."
  [x]
  (when-let [i (to-instant x)]
    (.toEpochMilli i)))

(defn to-epoch-days
  "Attempts to convert the given x to a date/time and coerce to days since epoch. If only a date is specified then
  00:00 on that date is implied as the time."
  [x]
  (some-> x to-epoch-millis (quot millis-per-day)))

(defn ep
  "Attempts to convert the given x to a date/time and returns a summary. If only a date is specified then
  00:00 on that date is implied as the time."
  [x]
  {:date-time    (to-local-date-time-string x)
   :instant      (to-instant-string x)
   :epoch-millis (to-epoch-millis x)
   :epoch-days   (to-epoch-days x)})

(let [[x] *command-line-args*
      date-thing (if (clojure.string/blank? x)
                   (Instant/ofEpochMilli (System/currentTimeMillis))
                   (try (Long/parseLong x)
                        (catch Exception _ x)))]
  ; Print options
  (pprint (ep date-thing)))
