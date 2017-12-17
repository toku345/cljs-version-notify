(ns cljs-version-notify.checker.core
  (:require [cljs.nodejs :as nodejs]
            [httpurr.client.node :as node]
            [promesa.core :as p]))

(nodejs/enable-util-print!)


;; ClojureScript Infomation on Maven
;; curl "https://search.maven.org/solrsearch/select?q=g:%22org.clojure%22+AND+a:%22clojurescript%22&wt=json"

(def ^:const URL "https://search.maven.org/solrsearch/select?q=g:%22org.clojure%22+AND+a:%22clojurescript%22&wt=json")

(defn decode [response]
  (update response :body #(js->clj (.parse js/JSON %))))

(defn get! [url]
  (p/then (node/get url) decode))

(defn- handler [_event _context callback]
  (p/then (get! URL)
         (fn [response]
           (-> (:body response)
               (get "response")
               (get "docs")
               first
               (get "latestVersion")
               (callback nil)))))

(set! (.-checker_handler (.-exports js/module)) handler)
