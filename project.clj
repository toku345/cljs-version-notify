(defproject cljs-version-notify "0.1.0"
  :description "Alexa skill notify ClojureScript version."
  :url "http://example.com/FIXME"

  :min-lein-version "2.7.1"

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.908"]
                 [funcool/httpurr "1.0.0"]
                 [funcool/promesa "1.9.0"]]

  :plugins [[lein-cljsbuild "1.1.7" :exclusions [[org.clojure/clojure]]]
            [lein-figwheel "0.5.13"]]

  :source-paths ["src"]

  :clean-targets ["server.js"
                  "target"]

  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src"]
                        :figwheel true
                        :compiler {:main cljs-version-notify.core
                                   :asset-path "target/js/compiled/dev"
                                   :output-to "target/js/compiled/cljs_version_notify.js"
                                   :output-dir "target/js/compiled/dev"
                                   :target :nodejs
                                   :npm-deps {:alexa-sdk "^1.0.21"
                                              :aws-sdk "^2.156.0"}
                                   :install-deps true
                                   :optimizations :none
                                   :source-map-timestamp true
                                   :recompile-dependents false
                                   :static-fns　false}}
                       {:id "prod"
                        :source-paths ["src"]
                        :compiler {:output-to "index.js"
                                   :output-dir "target/js/compiled/prod"
                                   :target :nodejs
                                   :optimizations :simple
                                   :recompile-dependents true ; Same as default value
                                   :static-fns true           ; Same as default value
                                   }}]}

  :profiles {:dev {:dependencies [[figwheel-sidecar "0.5.13"]
                                  [com.cemerick/piggieback "0.2.2"]]
                   :source-paths ["src" "dev"]
                   :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}}})
