
(ns deps-edn-handler.api
    (:require [deps-edn-handler.env          :as env]
              [deps-edn-handler.side-effects :as side-effects]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; deps-edn-handler.env
(def read-deps-edn                  env/read-deps-edn)
(def get-dependency-git-coordinates env/get-dependency-git-coordinates)
(def get-dependency-git-url         env/get-dependency-git-url)
(def get-dependency-git-commit-sha  env/get-dependency-git-commit-sha)

; deps-edn-handler.side-effects
(def update-dependency-git-url!         side-effects/update-dependency-git-url!)
(def update-dependency-git-commit-sha!  side-effects/update-dependency-git-commit-sha!)
(def update-dependency-git-coordinates! side-effects/update-dependency-git-coordinates!)
