; DrClojure
; (C) 2012-2013 Kim, Taegyoon

(ns DrClojure.core (:gen-class))
(set! *warn-on-reflection* true)
(import '(javax.swing JFrame SwingUtilities JPanel BoxLayout)
        '(java.awt Button TextArea BorderLayout Font TextField)
        '(java.awt.event ActionEvent ActionListener)
        '(java.io Writer))

(def ^String title "DrClojure")

(def ^TextArea text (new TextArea 25 80))
;(def ^TextArea text (new TextArea))
(def ^TextArea text-out (new TextArea))
(def ^TextField textf (new TextField))
(def ^Button button (new Button "Eval"))
    
(def out
  (proxy [Writer] []
    (close [])
    (flush [])
    (write [thing]
      (. SwingUtilities invokeLater
        (proxy [Runnable] []
          (run [] (. text-out append thing)))))))

(. button addActionListener
  (proxy [ActionListener] []
    (actionPerformed [e]
      (def result
        (str (try (binding [*out* out]
                    ;(eval (read-string (str "(do " (. text getText) ")")))
                    (. clojure.lang.Compiler load (new java.io.StringReader (str "(ns user) " (. text getText))))
                    )
               (catch Exception e e))))
      (. text-out append (str result \newline)))))

(. textf addActionListener
  (proxy [ActionListener] []
    (actionPerformed [e]
      (def code (. textf getText))
      (. textf setText "")
      (def result
        (str (try (binding [*out* out]
                    ;(eval (read-string (str "(do " code ")")))
                    (. clojure.lang.Compiler load (new java.io.StringReader (str "(ns user) " code)))
                    )
               (catch Exception e e))))
      (. text-out append (str "> " code "\n" result \newline)))))

(def ^JFrame frame (new JFrame title))
(def ^JPanel panel (new JPanel))

(defn -main []
  (. frame setDefaultCloseOperation JFrame/EXIT_ON_CLOSE)
  (let [font (new Font "Monospaced" Font/PLAIN 12)]
    (. text setFont font)
    (. text-out setFont font)
    (. textf setFont font))
  (. panel setLayout (new BoxLayout panel BoxLayout/PAGE_AXIS))
  (. frame add button BorderLayout/NORTH)
  (. frame add text)
  (. panel add textf)
  (. panel add text-out)
  (. frame add panel BorderLayout/SOUTH)
  (. frame pack)
  (. frame setVisible true))
