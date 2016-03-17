(defproject todo "0.1.0-SNAPSHOT"
  :description "Candidate assignment for a democracy.works job application"
  :dependencies [[compojure "1.5.0"]
                 [org.immutant/web "2.1.3"]
                 [org.clojure/clojure "1.8.0"]
                 [ring/ring-core "1.4.0"]
                 [ring/ring-defaults "0.2.0"]]
  :main todo.core
  :profiles {:dev {:dependencies [[ring/ring-devel "1.4.0"]]
                   :global-vars {*warn-on-reflection* true}
                   :plugins [[jonase/eastwood "0.2.3"]]}})
