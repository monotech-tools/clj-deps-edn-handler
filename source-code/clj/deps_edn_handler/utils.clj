
(ns deps-edn-handler.utils
    (:require [fruits.string.api :as string]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn directory-path->deps-edn-filepath
  ; @ignore
  ;
  ; @param (string) directory-path
  ;
  ; @return (string)
  [directory-path]
  (-> directory-path (string/ends-with!       "/deps.edn")
                     (string/not-starts-with! "/")))
