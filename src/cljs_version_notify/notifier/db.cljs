(ns cljs-version-notify.notifier.db
  (:require [cljs.nodejs :as nodejs]
            [promesa.core :as p]))

(nodejs/enable-util-print!)

(defonce AWS (nodejs/require "aws-sdk"))
(defonce dynamo (AWS.DynamoDB.DocumentClient.))
(defonce tableName (-> nodejs/process .-env .-TABLE_NAME))


;; exports.get = (event, context, callback) => {

;;     let params = {
;;         TableName: tableName,
;;         Key: {
;;             id: event.pathParameters.resourceId
;;         }
;;     };

;;     let dbGet = (params) => { return dynamo.get(params).promise() };

;;     dbGet(params).then( (data) => {
;;         if (!data.Item) {
;;             callback(null, createResponse(404, "ITEM NOT FOUND"));
;;             return;
;;         }
;;         console.log(`RETRIEVED ITEM SUCCESSFULLY WITH doc = ${data.Item.doc}`);
;;         callback(null, createResponse(200, data.Item.doc));
;;     }).catch( (err) => {
;;         console.log(`GET ITEM FAILED FOR doc = ${params.Key.id}, WITH ERROR: ${err}`);
;;         callback(null, createResponse(500, err));
;;     });
;; };

(defn- get-from-dynamo [params]
  (.promise (.get dynamo params)))

(defn get-version []
  (let [params #js {:TableName tableName
                    :Key #js {:id "cljs"}}]
    (p/then (get-from-dynamo params)
           (fn [data]
             (if (nil? data.Item)
               "version not found"
               (do (.log js/console (str "PUT ITEM SUCCEEDED WITH doc = " data.Item.doc))
                   data.Item.doc))))))
