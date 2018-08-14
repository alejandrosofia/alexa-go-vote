(ns alexa-go-vote.core
  (:require [cljs-lambda.macros :refer-macros [deflambda]]
            [alexa-go-vote.polling-place :as pp]))

(def launch-response
  {:version 1.0
   :response {:outputSpeech {:type "PlainText" :text "Welcome to Go Vote! You can ask me things like 'Where do I vote' or 'What is my polling place'. How can I help?"}
              :shouldEndSession false}})

(defn default-response [evt]
  (.log js/console (str "default response enacted for evt: " (pr-str evt)))
  {:version 1.0
   :response {:outputSpeech {:type "PlainText" :text "Didn't understand"}
              :shouldEndSession false}})

(deflambda alexa-go-vote-magic
  "This function receives the JSON input from the Alexa function and
   creates a map of the items of interest.  You can then dispatch on
   the input map to call functions to build the proper response map"
  [event ctx]
  (.log js/console (str "Event: " (pr-str event)) )
  (let [{:keys [session request
                version timestamp]} event
        type (:type request)
        intent (get-in request [:intent :name])]
   (cond
     (= type "LaunchRequest") launch-response
     (= intent "pollingPlace") (pp/intent request)
     ;;check, I think there may be a new intent for didn't understand
     :else (default-response event)
     )))
