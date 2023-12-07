
(ns deps-edn-handler.api
    (:require [deps-edn-handler.env          :as env]
              [deps-edn-handler.side-effects :as side-effects]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (deps-edn-handler.env)
(def read-deps-edn                  env/read-deps-edn)
(def get-git-dependency-coordinates env/get-git-dependency-coordinates)
(def get-git-dependency-url         env/get-git-dependency-url)
(def get-git-dependency-commit-sha  env/get-git-dependency-commit-sha)

; @redirect (deps-edn-handler.side-effects)
(def update-git-dependency-url!         side-effects/update-git-dependency-url!)
(def update-git-dependency-commit-sha!  side-effects/update-git-dependency-commit-sha!)
(def update-git-dependency-coordinates! side-effects/update-git-dependency-coordinates!)
