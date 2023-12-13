
(ns deps-edn-handler.side-effects
    (:require [deps-edn-handler.env   :as env]
              [deps-edn-handler.utils :as utils]
              [fruits.string.api      :as string]
              [io.api                 :as io]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-git-dependency-url!
  ; @important
  ; This function replaces the first occurence of the actual git URL of the dependency in the 'deps.edn' file!
  ;
  ; @description
  ; Updates the GIT URL of a dependency in the 'deps.edn' file in the given directory path.
  ;
  ; @param (string)(opt) directory-path
  ; @param (string) repository-name
  ; @param (string) git-url
  ;
  ; @usage
  ; (update-git-dependency-url! "author/my-repository" "https://github.com/author/my-repository")
  ;
  ; @usage
  ; (update-git-dependency-url! "my-directory" "author/my-repository" "https://github.com/author/my-repository")
  ;
  ; @return (boolean)
  ([repository-name git-url]
   (update-git-dependency-url! "" repository-name git-url))

  ([directory-path repository-name git-url]
   (let [filepath (utils/directory-path->deps-edn-filepath directory-path)]
        (boolean (if-let [file-content (io/read-file filepath {:warn? true})]
                         (if-let [original-git-url (env/get-git-dependency-url directory-path repository-name)]
                                 (as-> file-content % (string/replace-part % original-git-url git-url)
                                                      (io/write-file! filepath %))
                                 (throw (Exception. (str "Unable to update 'deps.edn' file in directory: '" directory-path "'\nOriginal GIT URL of dependency not found"))))
                         (throw (Exception. (str "Unable to update 'deps.edn' file in directory: '" directory-path "'"))))))))

(defn update-git-dependency-commit-sha!
  ; @important
  ; This function replaces the first occurence of the actual git commit SHA of the dependency in the 'deps.edn' file!
  ;
  ; @description
  ; Updates the GIT commit SHA of a dependency in the 'deps.edn' file in the given directory path.
  ;
  ; @param (string)(opt) directory-path
  ; @param (string) repository-name
  ; @param (string) git-commit-sha
  ;
  ; @usage
  ; (update-git-dependency-commit-sha! "author/my-repository" "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
  ;
  ; @usage
  ; (update-git-dependency-commit-sha! "my-directory" "author/my-repository" "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
  ;
  ; @return (boolean)
  ([repository-name git-commit-sha]
   (update-git-dependency-commit-sha! "" repository-name git-commit-sha))

  ([directory-path repository-name git-commit-sha]
   (let [filepath (utils/directory-path->deps-edn-filepath directory-path)]
        (boolean (if-let [file-content (io/read-file filepath {:warn? true})]
                         (if-let [original-git-commit-sha (env/get-git-dependency-commit-sha directory-path repository-name)]
                                 (as-> file-content % (string/replace-part % original-git-commit-sha git-commit-sha)
                                                      (io/write-file! filepath % {:warn? true}))
                                 (throw (Exception. (str "Unable to update 'deps.edn' file in directory: '" directory-path "'\nOriginal commit SHA of '" repository-name "' dependency not found"))))
                         (throw (Exception. (str "Unable to update 'deps.edn' file in directory: '" directory-path "'"))))))))

(defn update-git-dependency-coordinates!
  ; @important
  ; This function replaces the first occurence of the actual git URL and git commit SHA of the dependency in the 'deps.edn' file!
  ;
  ; @description
  ; Updates the GIT coordinates of a dependency in the 'deps.edn' file in the given directory path.
  ;
  ; @param (string)(opt) directory-path
  ; @param (string) repository-name
  ; @param (map) git-coordinates
  ; {:git/url (string)
  ;  :sha (string)}
  ;
  ; @usage
  ; (update-git-dependency-coordinates! "author/my-repository"
  ;                                     {:git/url "https://github.com/author/my-repository"
  ;                                      :sha     "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"})
  ;
  ; @usage
  ; (update-git-dependency-coordinates! "my-directory"
  ;                                     "author/my-repository"
  ;                                     {:git/url "https://github.com/author/my-repository"
  ;                                      :sha     "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"})
  ;
  ; @return (boolean)
  ([repository-name git-coordinates]
   (update-git-dependency-coordinates! "" repository-name git-coordinates))

  ([directory-path repository-name {:git/keys [url] :keys [sha]}]
   (and (update-git-dependency-url!        directory-path repository-name url)
        (update-git-dependency-commit-sha! directory-path repository-name sha))))
