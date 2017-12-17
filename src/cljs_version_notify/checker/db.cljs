(ns cljs-version-notify.checker.db
  (:require [cljs.nodejs :as nodejs]
            [promesa.core :as p]))

(nodejs/enable-util-print!)

(defonce AWS (nodejs/require "aws-sdk"))
(defonce dynamo (AWS.DynamoDB.DocumentClient.))
(defonce tableName (-> nodejs/process .-env .-TABLE_NAME))



;; exports.put = (event, context, callback) => {

;;     let item = {
;;         id: event.pathParameters.resourceId,
;;         doc: event.body
;;     };

;;     let params = {
;;         TableName: tableName,
;;         Item: item
;;     };

;;     let dbPut = (params) => { return dynamo.put(params).promise() };

;;     dbPut(params).then( (data) => {
;;         console.log(`PUT ITEM SUCCEEDED WITH doc = ${item.doc}`);
;;         callback(null, createResponse(200, null));
;;     }).catch( (err) => {
;;         console.log(`PUT ITEM FAILED FOR doc = ${item.doc}, WITH ERROR: ${err}`);
;;         callback(null, createResponse(500, err));
;;     });
;; };

(defn- put-version [params]
  (.promise (.put dynamo params)))

(defn update-dynamo [version callback]
  (let [item #js {:id "cljs"
                  :doc version }
        params #js {:TableName tableName
                    :Item item}]
    (p/then (put-version params)
            (fn [data]
              (.log js/console (str "PUT ITEM SUCCEEDED WITH doc = " (.-doc item)))
              (callback nil #js {:statusCode 200,
                                 :body nil})))))
