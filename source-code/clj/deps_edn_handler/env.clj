
(ns deps-edn-handler.env
    (:require [deps-edn-handler.utils :as utils]
              [fruits.map.api         :as map]
              [fruits.string.api      :as string]
              [io.api                 :as io]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn read-deps-edn
  ; @description
  ; Returns the content (as a Clojure map) of the 'deps.edn' file found in the directory at the given directory path.
  ;
  ; @param (string)(opt) directory-path
  ; Default: "."
  ;
  ; @usage
  ; (read-deps-edn)
  ; =>
  ; {:paths ["src"]
  ;  :deps {author/my-repository {:git/url "https://github.com/author/my-repository"
  ;                               :sha     "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"}}}
  ;
  ; @usage
  ; (read-deps-edn "my-directory")
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

(defn get-git-dependency-coordinates
  ; @description
  ; Returns the GIT coordinates of a specific dependency in the 'deps.edn' file found in the directory at the given directory path.
  ;
  ; @param (string)(opt) directory-path
  ; Default: "."
  ; @param (string) repository-name
  ;
  ; @usage
  ; (get-git-dependency-coordinates "author/my-repository")
  ; =>
  ; {:git/url "https://github.com/author/my-repository"
  ;  :sha     "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"}
  ;
  ; @usage
  ; (get-git-dependency-coordinates "my-directory" "author/my-repository")
  ; =>
  ; {:git/url "https://github.com/author/my-repository"
  ;  :sha     "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"}
  ;
  ; @return (map)
  ; {:git/url (string)
  ;  :sha (string)}
  ([repository]
   (get-git-dependency-coordinates "." repository))

  ([directory-path repository-name]
   (letfn [(f0 [[%1 %2]] (if (= %1 (symbol repository-name)) %2))
           (f1 [deps] (some f0 deps))]
          (-> directory-path read-deps-edn :deps f1))))

(defn get-git-dependency-url
  ; @description
  ; Returns the GIT URL of a specific dependency in the 'deps.edn' file found in the directory at the given directory path.
  ;
  ; @param (string)(opt) directory-path
  ; Default: "."
  ; @param (string) repository-name
  ;
  ; @usage
  ; (get-git-dependency-url "author/my-repository")
  ; =>
  ; "https://github.com/author/my-repository"
  ;
  ; @usage
  ; (get-git-dependency-url "my-directory" "author/my-repository")
  ; =>
  ; "https://github.com/author/my-repository"
  ;
  ; @return (string)
  ([repository-name]
   (get-git-dependency-url "." repository-name))

  ([directory-path repository-name]
   (:git/url (get-git-dependency-coordinates directory-path repository-name))))

(defn get-git-dependency-commit-sha
  ; @description
  ; Returns the GIT commit SHA of a specific dependency in the 'deps.edn' file found in the directory at the given directory path.
  ;
  ; @param (string)(opt) directory-path
  ; Default: "."
  ; @param (string) repository-name
  ;
  ; @usage
  ; (get-git-dependency-commit-sha "author/my-repository")
  ; =>
  ; "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
  ;
  ; @usage
  ; (get-git-dependency-commit-sha "my-directory" "author/my-repository")
  ; =>
  ; "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
  ;
  ; @return (string)
  ([repository-name]
   (get-git-dependency-commit-sha "." repository-name))

  ([directory-path repository-name]
   (:sha (get-git-dependency-coordinates directory-path repository-name))))
