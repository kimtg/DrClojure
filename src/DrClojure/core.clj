; DrClojure
; (C) 2012-2013 Kim, Taegyoon

(ns DrClojure.core (:gen-class))
(set! *warn-on-reflection* true)
(import '(javax.swing JFrame SwingUtilities JPanel BoxLayout JMenuBar JMenu JMenuItem)
        '(java.awt Button TextArea BorderLayout Font TextField)
        '(java.awt.event ActionEvent ActionListener)
        '(java.io Writer))

(def ^String title "DrClojure")

(def aCurFileName (atom ""))

(. javax.swing.UIManager setLookAndFeel 
  (. javax.swing.UIManager getSystemLookAndFeelClassName)) ; make look native

(def ^JFrame frame (new JFrame))
(defn update-title []
  (. frame setTitle (str (deref aCurFileName) " - " title)))

(update-title)

(def ^TextArea text (new TextArea 20 80))
(def ^TextArea text-out (new TextArea 6 80))
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

(defn eval-code [code]
  (try (binding [*out* out]
         (. clojure.lang.Compiler load (new java.io.StringReader (str "(ns user) " code))))
    (catch Exception e e)))

(. button addActionListener
  (proxy [ActionListener] []
    (actionPerformed [e]
      (. text-out append (str (str (eval-code (. text getText))) \newline)))))

(defn textf-action []
  (def code (. textf getText))
  (. text-out append (str "> " code "\n" (str (eval-code code)) \newline))
  (. textf setText ""))

(. textf addActionListener
  (proxy [ActionListener] []
    (actionPerformed [e]
      (textf-action))))

(def ^javax.swing.JFileChooser fc (new javax.swing.JFileChooser))
(. fc setFileFilter (new javax.swing.filechooser.FileNameExtensionFilter "Clojure file (*.clj)" (into-array ["clj"])))

(def ^JPanel panel (new JPanel))

(def ^JMenuBar menuBar (new JMenuBar))
(def ^JMenu menuFile (new JMenu "File"))
(def ^JMenuItem menuNew (new JMenuItem "New"))
(def ^JMenuItem menuOpen (new JMenuItem "Open..."))
(def ^JMenuItem menuSave (new JMenuItem "Save"))
(def ^JMenuItem menuSaveAs (new JMenuItem "Save As..."))
(. menuBar add menuFile)
(. menuFile add menuNew)
(. menuFile add menuOpen)
(. menuFile add menuSave)
(. menuFile add menuSaveAs)
(. frame setJMenuBar menuBar)

(. menuNew addActionListener
  (proxy [ActionListener] []
    (actionPerformed [e]
      (reset! aCurFileName "")
      (update-title)
      (. text setText ""))))

(. menuOpen addActionListener
  (proxy [ActionListener] []
    (actionPerformed [e]
      (def ret (. fc showOpenDialog nil))
      (when (== ret javax.swing.JFileChooser/APPROVE_OPTION)
        (def fileName (. (. fc getSelectedFile) getCanonicalPath))
        (reset! aCurFileName fileName)
        (update-title) 
        (. text setText (slurp fileName))))))

(defn fileSave [fileName]
  (spit fileName (. text getText)))

(defn fileSaveAs []
  (def ret (. fc showSaveDialog nil))
  (when (== ret javax.swing.JFileChooser/APPROVE_OPTION)
    (def fileName (. (. fc getSelectedFile) getCanonicalPath))
    (reset! aCurFileName fileName)
    (update-title)        
    (fileSave fileName)))

(. menuSave addActionListener
  (proxy [ActionListener] []
    (actionPerformed [e]      
      (if (= (deref aCurFileName) "")
        (fileSaveAs) 
        (fileSave fileName)))))

(. menuSaveAs addActionListener
  (proxy [ActionListener] []
    (actionPerformed [e]
      (fileSaveAs))))

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
  (. frame setVisible true)
  (. textf setText "(clojure-version)")
  (textf-action))
