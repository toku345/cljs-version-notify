(ns cljs-version-notify.notifier.core
  (:require [cljs.nodejs :as nodejs]))

(nodejs/enable-util-print!)

(defonce Alexa (nodejs/require "alexa-sdk"))

(def ^:const cljs-latest-version "1.9.946") ; TODO: I want to get it dynamically...
(def ^:const stop-message "クロージャースクリプトの最新バージョン番号が気になったら、また声をかけてくださいね。")

(def skill-handlers #js {"LaunchRequest"       (fn [] (this-as this (.emit this "AMAZON.HelpIntent")))
                         "SessionEndedRequest" (fn [] (this-as this (.emit this "AMAZON.StopIntent")))
                         "AMAZON.HelpIntent"   (fn [] (this-as this
                                                        (.emit this
                                                               ":ask"
                                                               (str "クロージャースクリプトの最新バージョン番号をお知らせする非公式のスキルです。"
                                                                    "クロージャースクリプトの最新バージョン番号をお知らせしましょうか？"))))
                         "CljsVersionIntent"   (fn [] (this-as this
                                                        (let [message (str "最新のcljsのバージョンは" cljs-latest-version "です。")]
                                                          (.emit this ":tell" message)
                                                          (.log js/console (str "message: " message)))))
                         "AMAZON.CancelIntent" (fn [] (this-as this (.emit this "AMAZON.StopIntent")))
                         "AMAZON.StopIntent"   (fn [] (this-as this (.emit this ":tell" stop-message)))})

(defn- handler [event context callback]
  (let [alexa (.handler Alexa event context)
        app-id (-> nodejs/process .-env .-APP_ID)]
    (set! (.-appId alexa) app-id)
    (.log js/console (str "app-id: " app-id))
    (.registerHandlers alexa skill-handlers)
    (.execute alexa)))

(set! (.-exports js/module) #js {:notifier_handler handler})
