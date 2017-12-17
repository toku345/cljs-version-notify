(ns cljs-version-notify.core
  (:require [cljs.nodejs :as nodejs]
            [cljs-version-notify.notifier.core]
            [cljs-version-notify.checker.core]))

(nodejs/enable-util-print!)

(defn -main []
  (println "Hello world!"))

(set! *main-cli-fn* -main)
