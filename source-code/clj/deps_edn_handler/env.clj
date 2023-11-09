
(ns deps-edn-handler.env
    (:require [deps-edn-handler.utils :as utils]
              [io.api                 :as io]
              [map.api                :as map]
              [string.api             :as string]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn read-deps-edn
  ; @description
  ; Returns the content (as a Clojure map) of the 'deps.edn' file found in the given directory path.
  ;
  ; @param (string)(opt) directory-path
  ;
  ; @usage
  ; (read-deps-edn)
  ;
  ; @usage
  ; (read-deps-edn "my-directory")
  ;
  ; @example
  ; (read-deps-edn)
  ; =>
  ; {:paths ["src"]
  ;  :deps {author/my-repository {:git/url "https://github.com/author/my-repository"
  ;                               :sha     "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"}}}
  ;
  ; @return (map)
  ([]
   (io/read-edn-file "deps.edn" {:warn? true}))

  ([directory-path]
   (-> directory-path utils/directory-path->deps-edn-filepath (io/read-edn-file {:warn? true}))))

(defn get-dependency-git-coordinates
  ; @description
  ; Returns the GIT coordinates of a dependency in the 'deps.edn' file found in the given directory path.
  ;
  ; @param (string)(opt) directory-path
  ; @param (string) repository-name
  ;
  ; @usage
  ; (get-dependency-git-coordinates "author/my-repository")
  ;
  ; @usage
  ; (get-dependency-git-coordinates "my-directory" "author/my-repository")
  ;
  ; @example
  ; (get-dependency-git-coordinates "author/my-repository")
  ; =>
  ; {:git/url "https://github.com/author/my-repository"
  ;  :sha     "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"}
  ;
  ; @return (map)
  ; {:git/url (string)
  ;  :sha (string)}
  ([repository]
   (get-dependency-git-coordinates "" repository))

  ([directory-path repository-name]
   (letfn [(f0 [[%1 %2]] (if (= %1 (symbol repository-name)) %2))
           (f1 [deps] (some f0 deps))]
          (-> directory-path read-deps-edn :deps f1))))

(defn get-dependency-git-url
  ; @description
  ; Returns the GIT URL of a dependency in the 'deps.edn' file found in the given directory path.
  ;
  ; @param (string)(opt) directory-path
  ; @param (string) repository-name
  ;
  ; @usage
  ; (get-dependency-git-url "author/my-repository")
  ;
  ; @usage
  ; (get-dependency-git-url "my-directory" "author/my-repository")
  ;
  ; @example
  ; (get-dependency-git-url "author/my-repository")
  ; =>
  ; "https://github.com/author/my-repository"
  ;
  ; @return (string)
  ([repository-name]
   (get-dependency-git-url "" repository-name))

  ([directory-path repository-name]
   (:git/url (get-dependency-git-coordinates directory-path repository-name))))

(defn get-dependency-git-commit-sha
  ; @description
  ; Returns the GIT commit SHA of a dependency in the 'deps.edn' file found in the given directory path.
  ;
  ; @param (string)(opt) directory-path
  ; @param (string) repository-name
  ;
  ; @usage
  ; (get-dependency-git-commit-sha "author/my-repository")
  ;
  ; @usage
  ; (get-dependency-git-commit-sha "my-directory" "author/my-repository")
  ;
  ; @example
  ; (get-dependency-git-commit-sha "author/my-repository")
  ; =>
  ; "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
  ;
  ; @return (string)
  ([repository-name]
   (get-dependency-git-commit-sha "" repository-name))

  ([directory-path repository-name]
   (:sha (get-dependency-git-coordinates directory-path repository-name))))
